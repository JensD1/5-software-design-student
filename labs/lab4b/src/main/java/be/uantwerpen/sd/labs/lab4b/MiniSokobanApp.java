package be.uantwerpen.sd.labs.lab4b;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.gen.SolvedGridGenerator;
import be.uantwerpen.sd.labs.lab4b.level.Level;
import be.uantwerpen.sd.labs.lab4b.level.WarehouseLevel;
import be.uantwerpen.sd.labs.lab4b.logic.Movement;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.ui.GridRenderer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayDeque;

public class MiniSokobanApp extends Application {
    private final AppConfig CFG = AppConfig.get();
    private final SolvedGridGenerator gen = new SolvedGridGenerator();
    private final ArrayDeque<World> undo = new ArrayDeque<>();
    private Level level = new WarehouseLevel(); // 1/2 to switch
    private int levelNumber = Math.max(1, CFG.startLevel);
    private World world;
    private Canvas canvas;
    private Stage stage;
    private GridRenderer renderer;
    private javafx.scene.control.Label lblLeft, lblRight;
    private VBox helpOverlay;
    private StackPane centerStack;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // Renderer + world
        renderer = new GridRenderer(level.palette());
        buildNewLevel(); // builds world but DOES NOT call draw()

        // Canvas
        canvas = new Canvas(world.w * CFG.tilePx + CFG.paddingPx * 2, world.h * CFG.tilePx + CFG.paddingPx * 2);

        // HELP OVERLAY (hidden by default)
        helpOverlay = new VBox(8);
        helpOverlay.setPadding(new Insets(16));
        helpOverlay.setAlignment(Pos.CENTER_LEFT);
        helpOverlay.setMaxWidth(520);
        helpOverlay.setVisible(false);
        helpOverlay.setStyle(
                "-fx-background-color: rgba(20,20,20,0.85);"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-radius: 12;"
                        + "-fx-border-color: rgba(255,255,255,0.25);"
                        + "-fx-border-width: 1;"
        );

        Label title = new Label("Sokoban — Help");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        String commonStyle = "-fx-text-fill: white; -fx-font-size: 14px;";
        helpOverlay.getChildren().addAll(
                title,
                new Label("• Move: ← ↑ → ↓"),      // style them all:
                new Label("• Undo: U"),
                new Label("• Restart: R"),
                new Label("• Next level: N or Space"),
                new Label("• Switch theme: 1 (warehouse), 2 (glacier)"),
                new Label("• Exit: Esc"),
                new Label(" "),
                new Label("Press H to close this menu.")
        );
        helpOverlay.getChildren().stream()
                .filter(n -> n instanceof Label && n != title)
                .forEach(n -> n.setStyle(commonStyle));

        centerStack = new StackPane(canvas);
        StackPane.setAlignment(helpOverlay, Pos.CENTER);
        centerStack.getChildren().add(helpOverlay);

        // bottom status (compact)
        lblLeft = new Label();   // "Targets: X/Y"
        lblRight = new Label();   // "help: H"
        HBox status = new HBox(12, lblLeft, new Region(), lblRight);
        HBox.setHgrow(status.getChildren().get(1), Priority.ALWAYS);
        status.setPadding(new Insets(6, 12, 6, 12));
        status.setStyle("-fx-background-color: " + CFG.statusBg + ";");

        // root
        BorderPane root = new BorderPane();
        root.setCenter(centerStack);
        root.setBottom(status);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Arrow keys always go to the game
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        canvas.setOnMouseClicked(e -> canvas.requestFocus());
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case UP -> {
                    onMove(0, -1);
                    e.consume();
                }
                case DOWN -> {
                    onMove(0, 1);
                    e.consume();
                }
                case LEFT -> {
                    onMove(-1, 0);
                    e.consume();
                }
                case RIGHT -> {
                    onMove(1, 0);
                    e.consume();
                }
                case U -> {
                    if (!undo.isEmpty()) {
                        world = undo.pop();
                        draw();
                    }
                    e.consume();
                }
                case R -> {
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case N, SPACE -> {
                    nextLevel();
                    e.consume();
                }
                case ESCAPE, Q -> {
                    stage.close();
                    e.consume();
                }
                case DIGIT1 -> {
                    level = new be.uantwerpen.sd.solutions.lab4b.level.WarehouseLevel();
                    renderer = new GridRenderer(level.palette());
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case DIGIT2 -> {
                    level = new be.uantwerpen.sd.solutions.lab4b.level.GlacierLevel();
                    renderer = new GridRenderer(level.palette());
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case H -> {
                    helpOverlay.setVisible(!helpOverlay.isVisible());
                    e.consume();
                }
                default -> {
                }
            }
        });

        // safe to draw
        draw();
    }

    private void nextLevel() {
        if (levelNumber < CFG.maxLevel) levelNumber++;
        buildNewLevel();
        resizeCanvasIfNeeded();
        undo.clear();
        draw();
    }

    private void buildNewLevel() {
        var p = be.uantwerpen.sd.solutions.lab4b.logic.DifficultyModel.paramsForLevel(levelNumber, level);

        world = gen.generate(p.w(), p.h(), p.crates(), p.wallDensity(), p.seed(), p.minPulls(), p.maxPulls(), level);
        setWindowTitle();
    }

    private void onMove(int dx, int dy) {
        World snapshot = world.copy(); // record for undo
        if (Movement.move(world, dx, dy)) {
            undo.push(snapshot);
            draw();
            if (world.coveredTargets() == world.totalTargets()) {
                nextLevel();
            }
        }
    }

    private void draw() {
        renderer.draw(canvas.getGraphicsContext2D(), world);
        if (lblLeft != null) lblLeft.setText("Targets: " + world.coveredTargets() + "/" + world.totalTargets());
        if (lblRight != null) lblRight.setText("help: H");
    }

    private void resizeCanvasIfNeeded() {
        int wantW = world.w * CFG.tilePx + CFG.paddingPx * 2;
        int wantH = world.h * CFG.tilePx + CFG.paddingPx * 2;
        if ((int) canvas.getWidth() != wantW || (int) canvas.getHeight() != wantH) {
            canvas.setWidth(wantW);
            canvas.setHeight(wantH);
            if (stage != null) stage.sizeToScene();
        }
    }

    private void setWindowTitle() {
        if (stage != null) stage.setTitle("Sokoban — " + level.theme() + "  L" + levelNumber);
    }
}
