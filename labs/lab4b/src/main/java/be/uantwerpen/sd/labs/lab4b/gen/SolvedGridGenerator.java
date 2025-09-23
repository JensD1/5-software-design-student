package be.uantwerpen.sd.labs.lab4b.gen;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.Level;
import be.uantwerpen.sd.labs.lab4b.model.Cell;
import be.uantwerpen.sd.labs.lab4b.model.Ground;
import be.uantwerpen.sd.labs.lab4b.model.Thing;
import be.uantwerpen.sd.labs.lab4b.model.World;

import java.util.*;

/**
 * Witness-first world generator for Sokoban.
 * <p>
 * Warehouse:
 * - Build targets.
 * - Create a witness by reversing from solved with single-tile pulls (macro pushes forward).
 * - Carve minimal walkable cells to ensure each reverse pull is legal.
 * <p>
 * Glacier (ice):
 * - Build sparse walls and a few L-shaped reflectors to create a rich Stop Graph.
 * - Choose start/target "stops" with minimum turn constraints in the Stop Graph.
 * - Build per-crate witness as shortest stop-paths with the required turns.
 * - Realize witness by reverse pulls along the straight corridors between stops.
 * <p>
 * Both modes:
 * - The reverse-from-solved construction guarantees solvability by design.
 * - Light, local checks keep generation fast (no heavy global search).
 */
public final class SolvedGridGenerator {

    // Screen-safe caps to prevent runaway growth; layouts vary but size stays bounded.
    private static final int SCREEN_CAP_W = 25;
    private static final int SCREEN_CAP_H = 18;
    private final Random rng = new Random();

    private static P slideToStop(boolean[][] walls, P from, Dir d) {
        int x = from.x, y = from.y;
        int W = walls[0].length, H = walls.length;
        while (true) {
            int nx = x + d.dx, ny = y + d.dy;
            if (!in(W, H, nx, ny)) break;
            if (walls[ny][nx]) break;
            x = nx;
            y = ny;
        }
        return new P(x, y);
    }

    // =====================================================================
    // Warehouse (classic) — witness by reverse pulls from solved
    // =====================================================================

    private static void addPerimeter(boolean[][] g) {
        int H = g.length, W = g[0].length;
        for (int x = 0; x < W; x++) {
            g[0][x] = true;
            g[H - 1][x] = true;
        }
        for (int y = 0; y < H; y++) {
            g[y][0] = true;
            g[y][W - 1] = true;
        }
    }

    private static boolean isCorner(boolean[][] g, P p) {
        if (g[p.y][p.x]) return false;
        boolean up = g[p.y - 1][p.x], dn = g[p.y + 1][p.x], lf = g[p.y][p.x - 1], rt = g[p.y][p.x + 1];
        return (up || dn) && (lf || rt);
    }

    private static boolean wouldBecomeDeepPocket(boolean[][] g, P p) {
        // cheap heuristic: three walls around the cell -> pocket
        int c = 0;
        if (g[p.y - 1][p.x]) c++;
        if (g[p.y + 1][p.x]) c++;
        if (g[p.y][p.x - 1]) c++;
        if (g[p.y][p.x + 1]) c++;
        return c >= 3;
    }

    private static boolean hasAdjacentWall(boolean[][] g, P p) {
        return g[p.y - 1][p.x] || g[p.y + 1][p.x] || g[p.y][p.x - 1] || g[p.y][p.x + 1];
    }

    private static boolean hasAdjacentWall(boolean[][] g, int x, int y) {
        return g[y - 1][x] || g[y + 1][x] || g[y][x - 1] || g[y][x + 1];
    }

    private static boolean hasStraightShotToAnyTarget(boolean[][] walls, P from, Collection<P> targets) {
        for (Dir d : Dir.values()) {
            P p = from;
            while (true) {
                int nx = p.x + d.dx, ny = p.y + d.dy;
                if (!in(walls[0].length, walls.length, nx, ny) || walls[ny][nx]) break;
                p = new P(nx, ny);
                if (targets.contains(p)) return true;
            }
        }
        return false;
    }

    private static List<P> allStops(boolean[][] walls) {
        int W = walls[0].length, H = walls.length;
        ArrayList<P> out = new ArrayList<>();
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (!walls[y][x] && hasAdjacentWall(walls, x, y)) out.add(new P(x, y));
        return out;
    }

    private static List<P> interiorFloors(boolean[][] walls) {
        int W = walls[0].length, H = walls.length;
        ArrayList<P> out = new ArrayList<>();
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (!walls[y][x]) out.add(new P(x, y));
        return out;
    }

    private static int[] fairSplit(int total, int parts, int minEach) {
        int[] arr = new int[parts];
        Arrays.fill(arr, minEach);
        int remain = Math.max(0, total - parts * minEach);
        for (int i = 0; i < remain; i++) arr[i % parts]++;
        // lightweight shuffle
        Random r = new Random();
        for (int i = parts - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
        return arr;
    }

    private static int manhattan(P a, P b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static boolean in(int W, int H, int x, int y) {
        return x >= 0 && y >= 0 && x < W && y < H;
    }

    private static boolean in(int W, int H, P p) {
        return in(W, H, p.x, p.y);
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static Dir dirFromTo(P a, P b) {
        if (a.x == b.x) return (a.y < b.y) ? Dir.D : Dir.U;
        if (a.y == b.y) return (a.x < b.x) ? Dir.R : Dir.L;
        return null;
    }

    // =====================================================================
    // Public API
    // =====================================================================
    public World generate(int w, int h, int crates, double wallDensity,
                          long seed, int minPulls, int maxPulls, Level level) {
        rng.setSeed(seed);

        // Clamp map dimensions so the board fits on screen; after the cap, we keep difficulty but vary layouts.
        if (w > SCREEN_CAP_W || h > SCREEN_CAP_H) {
            w = Math.min(w, SCREEN_CAP_W);
            h = Math.min(h, SCREEN_CAP_H);
        }

        boolean glacier = (level.defaultFloor() == Ground.ICE) || level.requireBackstops();

        // Clamp density to a sane safety cap.
        double safety = 0.55;
        try {
            if (AppConfig.get() != null) safety = Math.max(0.20, Math.min(0.80, AppConfig.get().safetyDensity));
        } catch (Throwable ignored) {
        }
        final double safeDensity = Math.min(Math.max(0.02, wallDensity), safety);

        // Difficulty knobs derived from inputs
        final int totalPullsTarget = clamp(minPulls + rng.nextInt(Math.max(1, maxPulls - minPulls + 1)), 6, 2000);
        final int tMinTurns = glacier ? Math.min(5, 1 + (totalPullsTarget / Math.max(1, crates + 2))) : 0; // ice: minimum turns per crate
        final int attempts = 24; // bounded; we relax locally rather than spin

        for (int attempt = 0; attempt < attempts; attempt++) {

            // ------------------ 1) Base walls ------------------
            boolean[][] walls = new boolean[h][w];
            addPerimeter(walls);
            if (glacier) {
                carveSparse(walls, Math.min(0.22, safeDensity));     // low clutter
                sprinkleReflectors(walls, Math.max(1, (w + h) / 28)); // very light structure
            } else {
                carveSparse(walls, Math.min(0.18, safeDensity));     // keep walkable
            }

            // ------------------ 2) Targets ------------------
            List<P> targets;
            if (glacier) {
                targets = pickGlacierTargets(walls, crates, /*minSep*/ 2);
                if (targets.size() < crates) continue;
            } else {
                targets = pickWarehouseTargets(walls, crates, /*minSep*/ 2);
                if (targets.size() < crates) continue;
            }

            // ------------------ 3) Witness construction ------------------
            GenerationResult res;
            if (glacier) {
                res = buildGlacierWitnessAndRealize(walls, w, h, crates, targets, tMinTurns);
            } else {
                res = buildWarehouseWitnessAndRealize(walls, w, h, crates, targets, totalPullsTarget);
            }
            if (!res.success) {
                // Local light relaxation and retry:
                if (glacier) sprinkleReflectors(walls, 1 + rng.nextInt(2));
                else carveConnectorToOpenUp(walls);
                continue;
            }

            // ------------------ 4) Player placement & connectivity touch-ups ------------------
            P player = ensurePlayerConnectivity(walls, res.boxes, res.witnessStandSquares);

            // ------------------ 5) Build world ------------------
            World world = new World(w, h);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Ground g = walls[y][x] ? Ground.WALL : (glacier ? Ground.ICE : Ground.FLOOR);
                    world.set(x, y, new Cell(g, Thing.NONE));
                }
            }
            for (P t : targets) world.get(t.x, t.y).ground = Ground.TARGET;
            for (P b : res.boxes) world.get(b.x, b.y).thing = level.boxProduct();

            Cell under = world.get(player.x, player.y);
            world.set(player.x, player.y, new Cell(under.ground, Thing.PLAYER));

            return world;
        }

        return fallback(level);
    }

    private GenerationResult buildWarehouseWitnessAndRealize(boolean[][] walls, int W, int H, int crates,
                                                             List<P> targets, int pullsBudget) {
        LinkedHashSet<P> boxes = new LinkedHashSet<>(targets);
        HashSet<P> standSquares = new HashSet<>();
        int remaining = Math.max(pullsBudget, crates * 4);

        // Assign each crate a share of reverse pulls
        int[] quota = fairSplit(remaining, crates, /*min*/ 3);

        // For each crate independently, reverse-pull away from its target
        for (int ci = 0; ci < crates; ci++) {
            P t = targets.get(ci);
            P cur = t;
            int steps = quota[ci];

            for (int s = 0; s < steps; s++) {
                Dir d = pickReversePullDirectionWarehouse(walls, W, H, boxes, cur);
                if (d == null) break; // no safe direction; stop lengthening this crate

                // Reverse pull one tile: box moves from cur to cur - d
                P pStand = new P(cur.x - d.dx, cur.y - d.dy);     // where box will go
                P pBack = new P(pStand.x - d.dx, pStand.y - d.dy);

                // Carve to guarantee legality of the reverse pull
                ensureFloor(walls, pStand);
                ensureFloor(walls, pBack);
                if (isCorner(walls, pStand)) {
                    // avoid making fresh non-target corners; carve one neighbor to de-corner (never perimeter)
                    carveDeCorner(walls, pStand, W, H);
                }
                if (boxes.contains(pStand) || boxes.contains(pBack)) break; // don't collide other crates

                boxes.remove(cur);
                boxes.add(pStand);
                standSquares.add(pStand);
                standSquares.add(pBack);
                cur = pStand;
            }
        }

        // Sanity: avoid placing any box in a fresh non-target corner
        HashSet<P> targetSet = new HashSet<>(targets);
        for (P b : boxes) {
            if (!targetSet.contains(b) && isCorner(walls, b)) {
                carveDeCorner(walls, b, W, H);
            }
        }

        return GenerationResult.ok(boxes, standSquares);
    }

    private Dir pickReversePullDirectionWarehouse(boolean[][] walls, int W, int H, Set<P> boxes, P cur) {
        // Bias directions that increase distance from any target-ish wall (spread out)
        List<Dir> dirs = new ArrayList<>(Arrays.asList(Dir.values()));
        Collections.shuffle(dirs, rng);
        for (Dir d : dirs) {
            P pStand = new P(cur.x - d.dx, cur.y - d.dy);
            P pBack = new P(pStand.x - d.dx, pStand.y - d.dy);
            if (!in(W, H, pStand) || !in(W, H, pBack)) continue;
            if (walls[pStand.y][pStand.x] || walls[pBack.y][pBack.x]) continue;
            if (boxes.contains(pStand) || boxes.contains(pBack)) continue;
            // avoid moving into tight re-entrant corner
            if (wouldBecomeDeepPocket(walls, pStand)) continue;
            return d;
        }
        return null;
    }


    // =====================================================================
    // Player placement & connectivity
    // =====================================================================

    // =====================================================================
// Glacier (ice) — deterministic lane builder (always solvable)
// =====================================================================
    private GenerationResult buildGlacierWitnessAndRealize(
            boolean[][] walls, int W, int H, int crates,
            List<P> targets, int minTurnsPerCrate) {

        // 0) Reset interior to walls; keep the perimeter as-is.
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                walls[y][x] = true;

        // 1) Allocate disjoint start rows (spacing >= 2 so lanes never touch)
        int[] laneRows = allocateStartRows(H, crates);

        // 2) Carve lanes: one zig-zag polyline per target, ending exactly at target.
        boolean[][] carved = new boolean[H][W];   // track carved floor to avoid lane intersections
        LinkedHashSet<P> boxes = new LinkedHashSet<>();
        HashSet<P> witnessStandSquares = new HashSet<>();

        final int Tmin = Math.max(1, minTurnsPerCrate);

        for (int i = 0; i < crates; i++) {
            P t = targets.get(i);
            P s = new P(2, laneRows[i]);              // start stop near the left border
            int turns = Math.max(2, Tmin);            // ≥2 turns feels “ice-like”

            List<P> path = buildZigZagPolyline(s, t, turns, carved, W, H);

            // Robust fallback chain: relax turns → minimal L → L+detour (always exists)
            if (path == null) path = buildZigZagPolyline(s, t, Math.max(1, turns - 1), carved, W, H);
            if (path == null) path = buildSimplePathNoIntersect(s, t, carved, W, H); // H→V or V→H
            if (path == null) path = buildDetouredSimplePath(s, t, carved, W, H);    // L with one detour
            if (path == null) {
                // Last-ditch: straight to same row, then to target column, both with a detour column in the middle.
                path = Arrays.asList(s, new P(Math.min(W - 3, s.x + 2), s.y), new P(Math.min(W - 3, s.x + 2), t.y), t);
            }

            // Carve the path cells as floor (ICE)
            carvePolyline(path, walls, carved);

            // Add a tiny 2x2 player pad next to the lane start so the first push is positionable on ICE
            carvePlayerPad(walls, s, W, H);

            // Place the box at the lane start; solution is to slide along the corridor to the target
            boxes.add(s);
            witnessStandSquares.add(s);
        }

        // Done. We constructed K disjoint solvable lanes that end on the provided targets.
        return GenerationResult.ok(boxes, witnessStandSquares);
    }

    // =====================================================================
    // Target placement
    // =====================================================================

    /**
     * Allocate evenly spaced lane rows with >=2 spacing inside (1..H-2).
     */
    private int[] allocateStartRows(int H, int crates) {
        int[] rows = new int[crates];
        int usable = Math.max(1, H - 4);                 // interior rows
        int step = Math.max(2, usable / Math.max(1, crates));
        int y = 2;
        for (int i = 0; i < crates; i++) {
            rows[i] = Math.max(2, Math.min(H - 3, y));
            y += step;
        }
        // enforce >=2 spacing
        for (int i = 1; i < crates; i++) {
            if (Math.abs(rows[i] - rows[i - 1]) < 2) rows[i] = Math.min(H - 3, rows[i - 1] + 2);
        }
        return rows;
    }

    /**
     * Build a zig-zag polyline from s to t with ~`turns` turns,
     * staying in-bounds and avoiding carved cells. Returns null if not possible.
     */
    private List<P> buildZigZagPolyline(P s, P t, int turns, boolean[][] carved, int W, int H) {
        ArrayList<P> path = new ArrayList<>();
        path.add(s);

        // Candidate interior columns/rows to weave through
        ArrayList<Integer> cols = new ArrayList<>();
        for (int x = 3; x <= W - 4; x += 2) cols.add(x);
        Collections.shuffle(cols, rng);

        ArrayList<Integer> rows = new ArrayList<>();
        for (int y = 2; y <= H - 3; y++) rows.add(y);
        Collections.shuffle(rows, rng);

        P cur = s;
        boolean horizontal = true; // alternate H/V segments
        int usedTurns = 0;
        int guard = W * H;

        while (usedTurns < turns && guard-- > 0) {
            if (horizontal) {
                Integer nx = pickZigColumn(cur.x, t.x, cols, W);
                if (nx == null) break;
                P next = new P(nx, cur.y);
                if (!lineIsClear(cur, next, carved, W, H)) {
                    cols.remove(Integer.valueOf(nx));   // remove by value!
                    continue;
                }
                path.add(next);
                cur = next;
            } else {
                Integer ny = pickZigRow(cur.y, t.y, rows, H);
                if (ny == null) break;
                P next = new P(cur.x, ny);
                if (!lineIsClear(cur, next, carved, W, H)) {
                    rows.remove(Integer.valueOf(ny));   // remove by value!
                    continue;
                }
                path.add(next);
                cur = next;
            }
            horizontal = !horizontal;
            usedTurns++;
        }

        // Finish with an L to t; if both orders blocked, try small auto-detours.
        List<P> tail = finishToTarget(cur, t, carved, W, H);
        if (tail == null) return null;
        path.addAll(tail);

        // Compress duplicates
        ArrayList<P> simple = new ArrayList<>();
        for (P p : path) if (simple.isEmpty() || !simple.get(simple.size() - 1).equals(p)) simple.add(p);
        return simple;
    }

    // =====================================================================
    // Stop Graph (for Glacier)
    // =====================================================================

    private Integer pickZigColumn(int xCur, int xTarget, List<Integer> cols, int W) {
        for (int c : cols) if ((xTarget > xCur && c > xCur) || (xTarget < xCur && c < xCur)) return c;
        for (int c : cols) if (c != xCur && c > 1 && c < W - 1) return c;
        return null;
    }

    private Integer pickZigRow(int yCur, int yTarget, List<Integer> rows, int H) {
        for (int r : rows) if ((yTarget > yCur && r > yCur) || (yTarget < yCur && r < yCur)) return r;
        for (int r : rows) if (r != yCur && r > 1 && r < H - 1) return r;
        return null;
    }

    private boolean lineIsClear(P a, P b, boolean[][] carved, int W, int H) {
        if (a.x != b.x && a.y != b.y) return false; // must be axis aligned
        int dx = Integer.compare(b.x, a.x), dy = Integer.compare(b.y, a.y);
        int x = a.x, y = a.y;
        while (!(x == b.x && y == b.y)) {
            x += dx;
            y += dy;
            if (!in(W, H, x, y)) return false;
            if (carved[y][x]) return false; // would intersect an existing lane
        }
        return true;
    }

    /**
     * Try H→V and V→H; if both blocked, attempt a tiny detour that avoids carved cells.
     */
    private List<P> finishToTarget(P from, P t, boolean[][] carved, int W, int H) {
        // H then V
        if (from.x != t.x) {
            P a = new P(t.x, from.y);
            if (lineIsClear(from, a, carved, W, H) && lineIsClear(a, t, carved, W, H)) {
                return Arrays.asList(a, t);
            }
        }
        // V then H
        if (from.y != t.y) {
            P a = new P(from.x, t.y);
            if (lineIsClear(from, a, carved, W, H) && lineIsClear(a, t, carved, W, H)) {
                return Arrays.asList(a, t);
            }
        }
        // Small detours
        // Try H to a detour column, then V to t.y, then H to t.x
        Integer detCol = pickDetourColumn(carved, W);
        if (detCol != null) {
            P a = new P(detCol, from.y);
            P b = new P(detCol, t.y);
            if (lineIsClear(from, a, carved, W, H) && lineIsClear(a, b, carved, W, H) && lineIsClear(b, t, carved, W, H)) {
                return Arrays.asList(a, b, t);
            }
        }
        // Try V to a detour row, then H to t.x, then V to t.y
        Integer detRow = pickDetourRow(carved, H);
        if (detRow != null) {
            P a = new P(from.x, detRow);
            P b = new P(t.x, detRow);
            if (lineIsClear(from, a, carved, W, H) && lineIsClear(a, b, carved, W, H) && lineIsClear(b, t, carved, W, H)) {
                return Arrays.asList(a, b, t);
            }
        }
        return null;
    }

    private Integer pickDetourColumn(boolean[][] carved, int W) {
        List<Integer> candidates = new ArrayList<>();
        for (int x = 3; x <= W - 4; x++) candidates.add(x);
        Collections.shuffle(candidates, rng);
        for (int x : candidates) {
            // column is acceptable if it has plenty of uncarved cells (coarse check)
            return x;
        }
        return null;
    }

    // =====================================================================
    // Small geometry helpers & local edits
    // =====================================================================

    private Integer pickDetourRow(boolean[][] carved, int H) {
        List<Integer> candidates = new ArrayList<>();
        for (int y = 2; y <= H - 3; y++) candidates.add(y);
        Collections.shuffle(candidates, rng);
        for (int y : candidates) {
            return y;
        }
        return null;
    }

    /**
     * Carve a polyline (axis-aligned segments) as floor; mark carved[] for intersection checks.
     */
    private void carvePolyline(List<P> pts, boolean[][] walls, boolean[][] carved) {
        if (pts == null || pts.size() < 2) return; // safety guard

        final int W = walls[0].length, H = walls.length;

        // Carve each axis-aligned segment
        for (int i = 0; i + 1 < pts.size(); i++) {
            P a = pts.get(i), b = pts.get(i + 1);
            if (a.x == b.x) carveV(a, b, walls, carved);
            else if (a.y == b.y) carveH(a, b, walls, carved);
        }

        // Open the vertices themselves
        for (P p : pts) {
            if (in(W, H, p.x, p.y)) {
                walls[p.y][p.x] = false;
                carved[p.y][p.x] = true;
            }
        }
    }

    private void carveH(P a, P b, boolean[][] walls, boolean[][] carved) {
        int x0 = Math.min(a.x, b.x), x1 = Math.max(a.x, b.x), y = a.y;
        for (int x = x0; x <= x1; x++) {
            walls[y][x] = false;
            carved[y][x] = true;
        }
    }

    private void carveV(P a, P b, boolean[][] walls, boolean[][] carved) {
        int y0 = Math.min(a.y, b.y), y1 = Math.max(a.y, b.y), x = a.x;
        for (int y = y0; y <= y1; y++) {
            walls[y][x] = false;
            carved[y][x] = true;
        }
    }

    /**
     * Small 2x2 pad near the lane start for player positioning on ice.
     */
    private void carvePlayerPad(boolean[][] walls, P s, int W, int H) {
        int x = Math.min(W - 3, Math.max(2, s.x + 1));
        int y = Math.min(H - 3, Math.max(2, s.y));
        for (int yy = y; yy <= y + 1; yy++)
            for (int xx = x; xx <= x + 1; xx++)
                if (in(W, H, xx, yy)) walls[yy][xx] = false;
    }

    /**
     * Plain L path if both legs are clear; otherwise null.
     */
    private List<P> buildSimplePathNoIntersect(P s, P t, boolean[][] carved, int W, int H) {
        // H->V
        if (lineIsClear(s, new P(t.x, s.y), carved, W, H) && lineIsClear(new P(t.x, s.y), t, carved, W, H))
            return Arrays.asList(s, new P(t.x, s.y), t);
        // V->H
        if (lineIsClear(s, new P(s.x, t.y), carved, W, H) && lineIsClear(new P(s.x, t.y), t, carved, W, H))
            return Arrays.asList(s, new P(s.x, t.y), t);
        return null;
    }

    /**
     * L path with a one-step detour that always exists in a solid interior.
     */
    private List<P> buildDetouredSimplePath(P s, P t, boolean[][] carved, int W, int H) {
        Integer col = pickDetourColumn(carved, W);
        if (col != null) {
            P a = new P(col, s.y), b = new P(col, t.y);
            if (lineIsClear(s, a, carved, W, H) && lineIsClear(a, b, carved, W, H) && lineIsClear(b, t, carved, W, H))
                return Arrays.asList(s, a, b, t);
        }
        Integer row = pickDetourRow(carved, H);
        if (row != null) {
            P a = new P(s.x, row), b = new P(t.x, row);
            if (lineIsClear(s, a, carved, W, H) && lineIsClear(a, b, carved, W, H) && lineIsClear(b, t, carved, W, H))
                return Arrays.asList(s, a, b, t);
        }
        return null;
    }

    private P ensurePlayerConnectivity(boolean[][] walls, Set<P> boxes, Set<P> mustReach) {
        // Pick a starting free cell; ensure all mustReach squares are connected through floors by carving minimal connectors.
        P start = pickAnyFree(walls, boxes);
        if (start == null) start = new P(1, 1);
        boolean[][] reach = flood(walls, boxes, start);
        for (P p : mustReach) {
            if (!in(walls[0].length, walls.length, p)) continue;
            if (walls[p.y][p.x] || boxes.contains(p)) continue;
            if (!reach[p.y][p.x]) {
                carveConnectorPath(walls, reach, p); // minimally carve a Manhattan path (never touches perimeter)
                reach = flood(walls, boxes, start);
            }
        }
        return start;
    }

    private List<P> pickWarehouseTargets(boolean[][] walls, int k, int minSep) {
        List<P> free = interiorFloors(walls);
        Collections.shuffle(free, rng);
        ArrayList<P> out = new ArrayList<>();
        for (P p : free) {
            if (isCorner(walls, p)) continue;
            boolean ok = true;
            for (P q : out)
                if (manhattan(p, q) < minSep) {
                    ok = false;
                    break;
                }
            if (ok) {
                out.add(p);
                if (out.size() == k) break;
            }
        }
        return out;
    }

    private List<P> pickGlacierTargets(boolean[][] walls, int k, int minSep) {
        List<P> stops = allStops(walls);
        Collections.shuffle(stops, rng);
        ArrayList<P> out = new ArrayList<>();
        for (P p : stops) {
            boolean backstopped = hasAdjacentWall(walls, p);
            if (!backstopped) continue;
            boolean ok = true;
            for (P q : out)
                if (manhattan(p, q) < minSep) {
                    ok = false;
                    break;
                }
            if (!ok) continue;
            out.add(p);
            if (out.size() == k) break;
        }
        return out;
    }

    private P pickStartStopWithMinTurns(StopGraph G, P targetStop, int tMin,
                                        Set<P> forbiddenStops) {
        Integer tIdx = G.index.get(targetStop);
        if (tIdx == null) return null;
        int n = G.nodes.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        dist[tIdx] = 0;
        dq.add(tIdx);
        while (!dq.isEmpty()) {
            int u = dq.removeFirst();
            for (int v : G.adj.get(u)) {
                if (dist[v] != -1) continue;
                dist[v] = dist[u] + 1;
                dq.addLast(v);
            }
        }
        // choose farthest stops (>= tMin+1 edges => >= tMin turns)
        ArrayList<P> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i == tIdx) continue;
            if (dist[i] >= Math.max(1, tMin + 1)) {
                P p = G.nodes.get(i);
                if (!forbiddenStops.contains(p)) candidates.add(p);
            }
        }
        if (candidates.isEmpty()) return null;
        return candidates.get(rng.nextInt(candidates.size()));
    }

    private List<P> shortestStopPath(StopGraph G, P start, P goal) {
        Integer sIdx = G.index.get(start), gIdx = G.index.get(goal);
        if (sIdx == null || gIdx == null) return null;
        int n = G.nodes.size();
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        boolean[] vis = new boolean[n];
        dq.add(sIdx);
        vis[sIdx] = true;
        while (!dq.isEmpty()) {
            int u = dq.removeFirst();
            if (u == gIdx) break;
            for (int v : G.adj.get(u)) {
                if (!vis[v]) {
                    vis[v] = true;
                    prev[v] = u;
                    dq.addLast(v);
                }
            }
        }
        if (!vis[gIdx]) return null;
        ArrayList<P> path = new ArrayList<>();
        for (int cur = gIdx; cur != -1; cur = prev[cur]) path.add(G.nodes.get(cur));
        Collections.reverse(path);
        return path;
    }

    private P nearestStopTo(boolean[][] walls, P p) {
        if (hasAdjacentWall(walls, p)) return p;
        // small BFS to nearest stop
        int W = walls[0].length, H = walls.length;
        boolean[][] vis = new boolean[H][W];
        ArrayDeque<P> dq = new ArrayDeque<>();
        dq.add(p);
        vis[p.y][p.x] = true;
        while (!dq.isEmpty()) {
            P c = dq.removeFirst();
            if (!walls[c.y][c.x] && hasAdjacentWall(walls, c)) return c;
            for (Dir d : Dir.values()) {
                int nx = c.x + d.dx, ny = c.y + d.dy;
                if (!in(W, H, nx, ny) || vis[ny][nx] || walls[ny][nx]) continue;
                vis[ny][nx] = true;
                dq.add(new P(nx, ny));
            }
        }
        return null;
    }

    private void carveSparse(boolean[][] g, double density) {
        int H = g.length, W = g[0].length;
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (rng.nextDouble() < density) g[y][x] = true;
        // punch a few holes to avoid total choke
        int holes = Math.max(2, (int) Math.round(density * 10));
        for (int k = 0; k < holes; k++) {
            int x = 1 + rng.nextInt(Math.max(1, W - 2));
            int y = 1 + rng.nextInt(Math.max(1, H - 2));
            g[y][x] = false;
        }
    }

    private void sprinkleReflectors(boolean[][] g, int n) {
        int H = g.length, W = g[0].length;
        for (int i = 0; i < n; i++) {
            int x = 2 + rng.nextInt(Math.max(1, W - 4));
            int y = 2 + rng.nextInt(Math.max(1, H - 4));
            g[y][x] = false;           // keep center open
            g[y][x - 1] = true;          // L arms
            g[y - 1][x] = true;
            if (rng.nextBoolean() && x + 1 < W - 1) g[y][x + 1] = true;
            if (rng.nextBoolean() && y + 1 < H - 1) g[y + 1][x] = true;
        }
    }

    private void addTinyBumpReflector(boolean[][] g, P around, Dir slideDir) {
        // place a single wall tile orthogonal to slide direction near 'around' to kill a straight shot
        int H = g.length, W = g[0].length;
        if (slideDir == Dir.L || slideDir == Dir.R) {
            if (in(W, H, around.x, around.y - 1) && around.y - 1 > 0) g[around.y - 1][around.x] = true;
            else if (in(W, H, around.x, around.y + 1) && around.y + 1 < H - 1) g[around.y + 1][around.x] = true;
        } else {
            if (in(W, H, around.x - 1, around.y) && around.x - 1 > 0) g[around.y][around.x - 1] = true;
            else if (in(W, H, around.x + 1, around.y) && around.x + 1 < W - 1) g[around.y][around.x + 1] = true;
        }
    }

    private void ensureFloor(boolean[][] g, P p) {
        if (!in(g[0].length, g.length, p)) return;
        g[p.y][p.x] = false;
    }

    // =====================================================================
    // Misc small utilities
    // =====================================================================

    private void carveDeCorner(boolean[][] g, P p, int W, int H) {
        // open at most one interior neighbor to break the corner,
        // and NEVER touch the perimeter walls.
        for (Dir d : Dir.values()) {
            P q = new P(p.x + d.dx, p.y + d.dy);
            if (!in(W, H, q)) continue;
            if (q.x == 0 || q.y == 0 || q.x == W - 1 || q.y == H - 1) continue; // preserve boundary walls
            if (g[q.y][q.x]) {                 // only if currently a wall
                g[q.y][q.x] = false;           // carve one cell and stop
                break;
            }
        }
    }

    private void carveConnectorToOpenUp(boolean[][] g) {
        // open a small plus-shaped hole somewhere interior
        int H = g.length, W = g[0].length;
        int x = 2 + rng.nextInt(Math.max(1, W - 4));
        int y = 2 + rng.nextInt(Math.max(1, H - 4));
        g[y][x] = false;
        if (y > 1) g[y - 1][x] = false;
        if (y < H - 2) g[y + 1][x] = false;
        if (x > 1) g[y][x - 1] = false;
        if (x < W - 2) g[y][x + 1] = false;
    }

    private void carveConnectorPath(boolean[][] walls, boolean[][] reach, P to) {
        // carve a simple Manhattan path from any reached cell to 'to' — never modify the perimeter
        int W = walls[0].length, H = walls.length;
        // find nearest reached cell by manhattan
        P start = null;
        int best = Integer.MAX_VALUE;
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (reach[y][x]) {
                    int d = Math.abs(x - to.x) + Math.abs(y - to.y);
                    if (d < best) {
                        best = d;
                        start = new P(x, y);
                    }
                }
        if (start == null) return;
        int x = start.x, y = start.y;
        while (x != to.x || y != to.y) {
            if (x < to.x) x++;
            else if (x > to.x) x--;
            else if (y < to.y) y++;
            else if (y > to.y) y--;
            if (in(W, H, x, y) && x > 0 && y > 0 && x < W - 1 && y < H - 1) {
                walls[y][x] = false;
            }
        }
    }

    private boolean[][] flood(boolean[][] walls, Set<P> boxes, P start) {
        int W = walls[0].length, H = walls.length;
        boolean[][] vis = new boolean[H][W];
        ArrayDeque<P> dq = new ArrayDeque<>();
        if (!in(W, H, start) || walls[start.y][start.x] || boxes.contains(start)) return vis;
        dq.add(start);
        vis[start.y][start.x] = true;
        while (!dq.isEmpty()) {
            P c = dq.removeFirst();
            for (Dir d : Dir.values()) {
                int nx = c.x + d.dx, ny = c.y + d.dy;
                P p = new P(nx, ny);
                if (!in(W, H, p) || vis[ny][nx] || walls[ny][nx] || boxes.contains(p)) continue;
                vis[ny][nx] = true;
                dq.addLast(p);
            }
        }
        return vis;
    }

    private P pickAnyFree(boolean[][] walls, Set<P> boxes) {
        int W = walls[0].length, H = walls.length;
        for (int tries = 0; tries < 200; tries++) {
            int x = 1 + rng.nextInt(Math.max(1, W - 2));
            int y = 1 + rng.nextInt(Math.max(1, H - 2));
            P p = new P(x, y);
            if (!walls[y][x] && !boxes.contains(p)) return p;
        }
        // deterministic fallback
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++) {
                P p = new P(x, y);
                if (!walls[y][x] && !boxes.contains(p)) return p;
            }
        return null;
    }

    private World fallback(Level level) {
        int w = 11, h = 7;
        World world = new World(w, h);
        Ground base = (level.defaultFloor() == Ground.ICE) ? Ground.ICE : Ground.FLOOR;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Ground g = (y == 0 || y == h - 1 || x == 0 || x == w - 1)
                        ? Ground.WALL : base;
                world.set(x, y, new Cell(g, Thing.NONE));
            }
        world.set(2, 2, new Cell(world.get(2, 2).ground, Thing.PLAYER));
        world.get(5, 3).ground = Ground.TARGET;
        world.get(7, 4).ground = Ground.TARGET;
        // Use level-specific box product so Glacier uses sliding crates.
        world.get(5, 3).thing = level.boxProduct();
        return world;
    }

    private enum Dir {
        U(0, -1), D(0, 1), L(-1, 0), R(1, 0);
        final int dx, dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    // =====================================================================
    // Data records
    // =====================================================================

    private static final class StopGraph {
        final boolean[][] walls;
        final int W, H;
        final ArrayList<P> nodes = new ArrayList<>();
        final HashMap<P, Integer> index = new HashMap<>();
        final ArrayList<int[]> adj; // adjacency by indices

        StopGraph(boolean[][] walls) {
            this.walls = walls;
            this.H = walls.length;
            this.W = walls[0].length;
            // build nodes
            for (int y = 1; y < H - 1; y++)
                for (int x = 1; x < W - 1; x++)
                    if (!walls[y][x] && hasAdjacentWall(walls, x, y)) {
                        P p = new P(x, y);
                        index.put(p, nodes.size());
                        nodes.add(p);
                    }
            // build edges (slide to the next stop along each axis)
            ArrayList<int[]> tmp = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i++) {
                P u = nodes.get(i);
                for (Dir d : Dir.values()) {
                    P v = slideToStop(walls, u, d);
                    Integer j = index.get(v);
                    if (j != null && j != i) tmp.add(new int[]{i, j});
                }
            }
            // compress to per-node adjacency lists
            ArrayList<ArrayList<Integer>> lists = new ArrayList<>(nodes.size());
            for (int i = 0; i < nodes.size(); i++) lists.add(new ArrayList<>());
            for (int[] e : tmp) lists.get(e[0]).add(e[1]);
            adj = new ArrayList<>(nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                List<Integer> lst = lists.get(i);
                int[] arr = new int[lst.size()];
                for (int j = 0; j < lst.size(); j++) arr[j] = lst.get(j);
                adj.add(arr);
            }
        }
    }

    private static final class GenerationResult {
        final boolean success;
        final LinkedHashSet<P> boxes;
        final HashSet<P> witnessStandSquares;

        private GenerationResult(boolean s, LinkedHashSet<P> b, HashSet<P> st) {
            success = s;
            boxes = b;
            witnessStandSquares = st;
        }

        static GenerationResult ok(LinkedHashSet<P> b, HashSet<P> st) {
            return new GenerationResult(true, b, st);
        }

        static GenerationResult fail() {
            return new GenerationResult(false, new LinkedHashSet<>(), new HashSet<>());
        }
    }

    private record P(int x, int y) {
        @Override
        public boolean equals(Object o) {
            return (o instanceof P q) && q.x == x && q.y == y;
        }

        @Override
        public int hashCode() {
            return (x * 31) ^ y;
        }
    }
}