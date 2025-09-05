package be.uantwerpen.sd.labs.lab1;

import java.util.*;
import java.lang.reflect.*;

public class Main {

    /**
     * Safe reflection helper that never throws out of the grader.
     */
    private static final class SafeRef {
        final String pkg = Main.class.getPackage().getName();
        String lastError = "";

        Class<?> cls(String simpleName) {
            try {
                return Class.forName(pkg + "." + simpleName);
            } catch (Throwable t) {
                lastError = simpleName + " class not found: " + t.getClass().getSimpleName() + ": " + t.getMessage();
                return null;
            }
        }

        Object newInstance(Class<?> c, Class<?>[] sig, Object[] args) {
            if (c == null) return null;
            try {
                Constructor<?> cons = c.getDeclaredConstructor(sig);
                cons.setAccessible(true);
                return cons.newInstance(args);
            } catch (Throwable t) {
                lastError = "Failed to construct " + c.getSimpleName() + ": " + t.getClass().getSimpleName() + ": " + t.getMessage();
                return null;
            }
        }

        Object call(Object target, String method, Class<?>[] sig, Object[] args) {
            if (target == null) return null;
            try {
                Method m = target.getClass().getMethod(method, sig);
                m.setAccessible(true);
                return m.invoke(target, args);
            } catch (NoSuchMethodException e) {
                lastError = "Missing method '" + method + "' on " + target.getClass().getSimpleName();
                return null;
            } catch (InvocationTargetException ite) {
                Throwable t = ite.getTargetException();
                lastError = target.getClass().getSimpleName() + "." + method + " threw " + t.getClass().getSimpleName() + ": " + (t.getMessage() == null ? "(no message)" : t.getMessage());
                return null;
            } catch (Throwable t) {
                lastError = "Failed calling " + method + " on " + target.getClass().getSimpleName() + ": " + t.getClass().getSimpleName() + ": " + t.getMessage();
                return null;
            }
        }

        int callInt(Object target, String method, Class<?>[] sig, Object[] args, int fallback) {
            Object o = call(target, method, sig, args);
            if (o == null) return fallback;
            try { return ((Number) o).intValue(); } catch (Throwable t) { lastError = "Expected int result from '"+method+"'"; return fallback; }
        }

        double callDouble(Object target, String method, Class<?>[] sig, Object[] args, double fallback) {
            Object o = call(target, method, sig, args);
            if (o == null) return fallback;
            try { return ((Number) o).doubleValue(); } catch (Throwable t) { lastError = "Expected double result from '"+method+"'"; return fallback; }
        }
    }

    /** Simple holder for a prepared booking fixture. */
    private static final class Fixture {
        final Object booking;            // instance of students' Booking (or null)
        final Object[] hotels;           // instances of students' Hotel (or nulls)
        final SafeRef R;
        Fixture(Object booking, Object[] hotels, SafeRef R) { this.booking = booking; this.hotels = hotels; this.R = R; }
    }

    public static void main(String[] args) {
        new Main().runGrader();
    }

    /* ============================
       Grader entrypoint
       ============================ */
    public void runGrader() {
        System.out.println("\n=== Lab 1 - Auto-Grader (Grouped by method) ===\n");

        int grandTotal = 0;
        int grandPassed = 0;

        // ---- Suite 1: bookRoomInHotel(date, hotelId)
        List<java.util.function.Supplier<TestResult>> s1 = new ArrayList<>();
        s1.add(this::testBookRoom_singleDate_success);
        s1.add(this::testBookRoom_singleDate_overbookingFails);
        s1.add(this::testBookRoom_singleDate_unknownHotel);
        s1.add(this::testBookRoom_singleDate_invalidDate);
        s1.add(this::testBookRoom_singleDate_negativeHotelId);
        s1.add(this::testBookRoom_singleDate_noRoomsInHotel);
        int p1 = printSuite("bookRoomInHotel(date, hotelId)", s1);
        grandPassed += p1; grandTotal += s1.size();

        // ---- Suite 2: bookRoomInHotel(start, end, hotelId)
        List<java.util.function.Supplier<TestResult>> s2 = new ArrayList<>();
        s2.add(this::testBooking_bookRoomInHotel_range_success);
        s2.add(this::testBooking_bookRoomInHotel_range_success_multiDay);
        s2.add(this::testBooking_bookRoomInHotel_range_doubleBookingFails);
        s2.add(this::testBooking_bookRoomInHotel_range_middleDayConflictFailsAtomic);
        s2.add(this::testBooking_bookRoomInHotel_range_zeroLength_success);
        s2.add(this::testBooking_bookRoomInHotel_range_invalidRange);
        s2.add(this::testBooking_bookRoomInHotel_range_negativeHotelId);
        s2.add(this::testBooking_bookRoomInHotel_range_atomicConflictFails);
        s2.add(this::testRangeAPIs_negativeDates);
        s2.add(this::testBooking_bookRoomInHotel_range_badHotel);
        int p2 = printSuite("bookRoomInHotel(start, end, hotelId)", s2);
        grandPassed += p2; grandTotal += s2.size();

        // ---- Suite 3: bookRoomInCheapestHotel(start, end)
        List<java.util.function.Supplier<TestResult>> s3 = new ArrayList<>();
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_success);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_returnsCheapestExpected);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_tiePrice);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_skipsHotelWithMiddleGap);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_picksMoreExpensiveWhenCheapUnavailable);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_ignoresEmptyHotel);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_noHotels);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_doubleBookingFails);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_noAvailability);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_invalidRange);
        s3.add(this::testBooking_bookRoomInCheapestHotel_range_negativeDates);
        int p3 = printSuite("bookRoomInCheapestHotel(start, end)", s3);
        grandPassed += p3; grandTotal += s3.size();

        // Final summary
        System.out.printf("\n=== Overall Summary: %d/%d tests passed ===\n", grandPassed, grandTotal);
        if (grandPassed == grandTotal) {
            System.out.println("All suites green. 🎉");
        } else {
            System.out.println("Check the suite summaries above to see which method to focus on next.");
        }
    }

    /* ============================
       Test fixtures
       ============================ */
    private Fixture baselineFixture() {
        SafeRef R = new SafeRef();
        Class<?> Hotel = R.cls("Hotel");
        Class<?> Booking = R.cls("Booking");

        Object booking = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        if (booking == null) {
            return new Fixture(null, new Object[]{null, null, null}, R);
        }

        // Helper lambdas using reflection
        java.util.function.Function<Double, Object> newHotel = (price) ->
                R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{price});
        java.util.function.BiConsumer<Object, Integer> bookSingle = (h, dayRoom) -> {
            long day = (dayRoom >> 16);
            int room = (dayRoom & 0xFFFF);
            R.call(h, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{day, room});
        };

        Object h0 = newHotel.apply(30.0);
        R.call(h0, "addRoom", new Class<?>[]{}, new Object[]{}); // id 0
        R.call(h0, "addRoom", new Class<?>[]{}, new Object[]{}); // id 1
        R.call(h0, "addRoom", new Class<?>[]{}, new Object[]{}); // id 2
        bookSingle.accept(h0, (10 << 16) | 0);
        bookSingle.accept(h0, (10 << 16) | 1);
        bookSingle.accept(h0, (10 << 16) | 2);
        bookSingle.accept(h0, (11 << 16) | 0);
        bookSingle.accept(h0, (11 << 16) | 1);
        bookSingle.accept(h0, (11 << 16) | 2);
        bookSingle.accept(h0, (12 << 16) | 2);

        Object h1 = newHotel.apply(46.99);
        R.call(h1, "addRoom", new Class<?>[]{}, new Object[]{}); // id 0
        R.call(h1, "addRoom", new Class<?>[]{}, new Object[]{}); // id 1
        bookSingle.accept(h1, (10 << 16) | 0);
        bookSingle.accept(h1, (10 << 16) | 1);
        bookSingle.accept(h1, (11 << 16) | 0);
        bookSingle.accept(h1, (12 << 16) | 1);

        Object h2 = newHotel.apply(40.0);
        R.call(h2, "addRoom", new Class<?>[]{}, new Object[]{}); // id 0
        bookSingle.accept(h2, (11 << 16) | 0);

        // booking.addHotel(...)
        R.call(booking, "addHotel", new Class<?>[]{Hotel}, new Object[]{h0});
        R.call(booking, "addHotel", new Class<?>[]{Hotel}, new Object[]{h1});
        R.call(booking, "addHotel", new Class<?>[]{Hotel}, new Object[]{h2});

        return new Fixture(booking, new Object[]{h0, h1, h2}, R);
    }

    private Fixture emptyFixture() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Object booking = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        return new Fixture(booking, new Object[]{} , R);
    }

    // New fixtures
    private Fixture fixtureWithEmptyHotel() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        Object hEmpty = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{5.0});
        // Intentionally do NOT add any rooms to hEmpty
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{hEmpty});
        return new Fixture(b, new Object[]{hEmpty}, R);
    }

    private Fixture tiePriceFixture() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");

        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        Object hA = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{20.0});
        Object hB = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{20.0});

        R.call(hA, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(hB, "addRoom", new Class<?>[]{}, new Object[]{});

        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{hA}); // id 0
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{hB}); // id 1

        return new Fixture(b, new Object[]{hA, hB}, R);
    }

    private Fixture fixtureRangeFreeHotel() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");

        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        Object h = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{10.0});
        R.call(h, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{h}); // id 0
        return new Fixture(b, new Object[]{h}, R);
    }

    // Cheap hotel exists but is unavailable for part of the range; a more expensive one is free.
    private Fixture fixtureCheapestSkipUnavailable() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});

        Object cheap = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{5.0});  // id 0
        R.call(cheap, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(cheap, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{6L, 0}); // unavailable on 6

        Object expensive = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{7.0}); // id 1
        R.call(expensive, "addRoom", new Class<?>[]{}, new Object[]{});

        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{cheap});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{expensive});
        return new Fixture(b, new Object[]{cheap, expensive}, R);
    }

    // Cheapest price hotel has zero rooms; should be ignored in selection.
    private Fixture fixtureCheapestWithEmptyHotel() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});

        Object empty = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{1.0}); // id 0, no rooms
        Object real  = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{2.0}); // id 1
        R.call(real, "addRoom", new Class<?>[]{}, new Object[]{});

        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{empty});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{real});
        return new Fixture(b, new Object[]{empty, real}, R);
    }

    // Hotel with a single middle-day conflict inside a larger free range.
    private Fixture fixtureRangeMiddleGap() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});

        Object h = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{10.0}); // id 0
        R.call(h, "addRoom", new Class<?>[]{}, new Object[]{});
        // Make middle day (e.g., 8) unavailable; ends 5 and 10 remain free.
        R.call(h, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{8L, 0});

        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{h});
        return new Fixture(b, new Object[]{h}, R);
    }

    // Two hotels: cheap has a middle-day conflict; expensive is fully free for the range.
    private Fixture fixtureCheapestSkipMiddleGap() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});

        Object cheap = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{5.0});  // id 0
        R.call(cheap, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(cheap, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{8L, 0}); // middle-day conflict

        Object expensive = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{7.0}); // id 1
        R.call(expensive, "addRoom", new Class<?>[]{}, new Object[]{});

        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{cheap});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{expensive});
        return new Fixture(b, new Object[]{cheap, expensive}, R);
    }

    /* ============================
       Tests – Single date
       ============================ */

    private TestResult testBookRoom_singleDate_success() {
        Fixture f = baselineFixture();
        // In the baseline fixture, hotel with ID 2 has a free room on day 10.
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, int.class}, new Object[]{10L, 2}, -1);
        return assertEquals("Book single day success", 0, rc,
                rc == -1 ? ("Reflection error: " + f.R.lastError) : "Booking should succeed for a free date in a known hotel.");
    }

    private TestResult testBookRoom_singleDate_overbookingFails() {
        Fixture f = baselineFixture();
        long date = 10L;
        int rc1 = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, int.class}, new Object[]{date, 2}, -1);
        int rc2 = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, int.class}, new Object[]{date, 2}, -1);
        boolean pass = (rc1 == 0) && (rc2 == -1);
        return new TestResult("Overbooking same date fails", pass,
                pass ? "" : (rc1 == -1 || rc2 == -1 ? ("Reflection error: " + f.R.lastError) : shortHint("Second booking on same date should fail in same hotel.")));
    }

    private TestResult testBookRoom_singleDate_unknownHotel() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, int.class}, new Object[]{10L, 999}, -1);
        return assertEquals("Unknown hotel ID", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "Booking in a non-existent hotel should fail gracefully." : ("Reflection error: " + f.R.lastError)) : "");
    }

    private TestResult testBookRoom_singleDate_invalidDate() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, int.class}, new Object[]{-5L, 0}, -1);
        return new TestResult("Invalid date rejected", rc == -1,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : shortHint("Negative dates should be rejected."));
    }

    // Single-date: hotel exists but has no rooms -> should fail gracefully
    private TestResult testBookRoom_singleDate_noRoomsInHotel() {
        Fixture f = fixtureWithEmptyHotel();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, int.class}, new Object[]{10L, 0}, -1);
        return assertEquals("Single day: hotel with no rooms rejected", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : "");
    }

    private TestResult testBookRoom_singleDate_negativeHotelId() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, int.class}, new Object[]{10L, -1}, -1);
        return assertEquals("Single day: negative hotel ID", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : "");
    }

    /* ============================
       Tests – Range via Booking (Student tasks)
       ============================ */
    private TestResult testBooking_bookRoomInHotel_range_success() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{10L, 10L, 2}, -1);
        boolean pass = (rc == 0);
        return new TestResult("Booking.bookRoomInHotel(range) success", pass,
                pass ? "" : shortHint(rc == -1 ? ("Reflection note: " + f.R.lastError) : "Should book a whole range in a specific hotel when available."));
    }

    private TestResult testBooking_bookRoomInHotel_range_success_multiDay() {
        Fixture f = fixtureRangeFreeHotel();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{5L, 7L, 0}, -1);
        return assertEquals("Range booking success (multi-day)", 0, rc,
                rc == -1 ? ("Reflection note: " + f.R.lastError) : "");
    }

    private TestResult testBooking_bookRoomInHotel_range_doubleBookingFails() {
        Fixture f = fixtureRangeFreeHotel();
        int first = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{5L, 7L, 0}, -1);
        int second = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{5L, 7L, 0}, -1);
        boolean pass = (first == 0 && second == -1);
        return new TestResult("Range booking reserves capacity (second identical fails)", pass,
                pass ? "" : shortHint("Second identical range booking should fail once capacity is taken."));
    }

    // Range: first and last dates are free, but a middle date is not -> must fail atomically
    private TestResult testBooking_bookRoomInHotel_range_middleDayConflictFailsAtomic() {
        Fixture f = fixtureRangeMiddleGap();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{5L, 10L, 0}, -1);
        return assertEquals("Range booking fails when a middle day is unavailable (atomic)", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError))
                        : shortHint("Must validate every day in [start..end], not just endpoints."));
    }

    private TestResult testBooking_bookRoomInHotel_range_negativeHotelId() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{10L, 10L, -1}, -1);
        return assertEquals("Range booking: negative hotel ID", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : "");
    }

    private TestResult testBooking_bookRoomInHotel_range_badHotel() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, long.class, int.class}, new Object[]{10L, 12L, 999}, -1);
        return assertEquals("Booking.bookRoomInHotel(range) bad hotel", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "Unknown hotel should be handled." : ("Reflection note: " + f.R.lastError)) : "");
    }

    // 2) Range: zero-length range should behave like single day
    private TestResult testBooking_bookRoomInHotel_range_zeroLength_success() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{10L, 10L, 2}, -1);
        return assertEquals("Range (zero-length) behaves like single day", 0, rc,
                rc == -1 ? ("Reflection note: " + f.R.lastError) : "Should succeed when start == end and a room is free.");
    }

    // 3) Range: invalid (start > end)
    private TestResult testBooking_bookRoomInHotel_range_invalidRange() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{12L, 10L, 2}, -1);
        return assertEquals("Range invalid (start > end)", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "Invalid ranges should be rejected." : ("Reflection note: " + f.R.lastError)) : "");
    }

    // 4) Range: atomic conflict must fail (no partial booking)
    private TestResult testBooking_bookRoomInHotel_range_atomicConflictFails() {
        Fixture f = baselineFixture();
        // Hotel with ID 1 has conflicts across 10..12 in the baseline fixture
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInHotel",
                new Class<?>[]{long.class, long.class, int.class}, new Object[]{10L, 12L, 1}, -1);
        return assertEquals("Range booking conflict fails atomically", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : shortHint("Range booking must be atomic: any conflict should fail."));
    }

    private TestResult testBooking_bookRoomInCheapestHotel_range_success() {
        Fixture f = baselineFixture();
        int hotelID = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInCheapestHotel", new Class<?>[]{long.class, long.class}, new Object[]{10L, 10L}, -1);
        boolean pass = (hotelID != -1);
        return new TestResult("Cheapest hotel booking (range)", pass,
                pass ? "" : shortHint("Should pick the cheapest hotel that can host the whole range and book it. " + ("".equals(f.R.lastError) ? "" : ("(Reflection: " + f.R.lastError + ")"))));
    }

    // 5) Cheapest (range): returns the expected cheapest hotel (baseline expects ID 2)
    private TestResult testBooking_bookRoomInCheapestHotel_range_returnsCheapestExpected() {
        Fixture f = baselineFixture();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{10L, 10L}, -1);
        return assertEquals("Cheapest hotel (range) returns expected hotel ID", 2, hotelID,
                (hotelID == -1 ? ("Reflection error: " + f.R.lastError) : "Should pick the cheapest hotel that can host the whole range and book it."));
    }

    private TestResult testBooking_bookRoomInCheapestHotel_range_noAvailability() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Class<?> Hotel = R.cls("Hotel");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        Object hA = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{10.0});
        R.call(hA, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(hA, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{1L, 0});
        Object hB = R.newInstance(Hotel, new Class<?>[]{double.class}, new Object[]{9.0});
        R.call(hB, "addRoom", new Class<?>[]{}, new Object[]{});
        R.call(hB, "bookRoom", new Class<?>[]{long.class, int.class}, new Object[]{2L, 0});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{hA});
        R.call(b, "addHotel", new Class<?>[]{Hotel}, new Object[]{hB});
        int hotelID = (b == null) ? -1 : R.callInt(b, "bookRoomInCheapestHotel", new Class<?>[]{long.class, long.class}, new Object[]{1L, 2L}, -1);
        return assertEquals("Cheapest hotel booking (no availability)", -1, hotelID,
                hotelID == -1 ? ("".equals(R.lastError) ? "When no hotel fits the entire range, the method should indicate failure." : ("Reflection note: " + R.lastError)) : "");
    }

    // Cheapest: cheap hotel has a middle-day conflict inside the requested range -> pick the next valid hotel
    private TestResult testBooking_bookRoomInCheapestHotel_range_skipsHotelWithMiddleGap() {
        Fixture f = fixtureCheapestSkipMiddleGap();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{5L, 10L}, -1);
        return assertEquals("Cheapest: skip hotel with middle-day conflict", 1, hotelID,
                hotelID == -1 ? ("Reflection error: " + f.R.lastError) : "");
    }

    // 6) Cheapest (range): tie on price -> any valid candidate (id 0 or 1)
    private TestResult testBooking_bookRoomInCheapestHotel_range_tiePrice() {
        Fixture f = tiePriceFixture();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{5L, 6L}, -1);
        boolean pass = (hotelID == 0 || hotelID == 1);
        return new TestResult("Cheapest hotel (tie) picks a valid candidate", pass,
                pass ? "" : (hotelID == -1 ? ("Reflection error: " + f.R.lastError) : shortHint("Should return an available hotel ID with the minimal price when prices tie.")),
                pass ? "" : ("expected one of {0,1}, actual=" + hotelID));
    }

    private TestResult testBooking_bookRoomInCheapestHotel_range_picksMoreExpensiveWhenCheapUnavailable() {
        Fixture f = fixtureCheapestSkipUnavailable();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{5L, 6L}, -1);
        return assertEquals("Cheapest: pick more expensive when cheap can't host", 1, hotelID,
                hotelID == -1 ? ("Reflection error: " + f.R.lastError) : "");
    }

    private TestResult testBooking_bookRoomInCheapestHotel_range_ignoresEmptyHotel() {
        Fixture f = fixtureCheapestWithEmptyHotel();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{5L, 5L}, -1);
        return assertEquals("Cheapest: ignore zero-room hotels", 1, hotelID,
                hotelID == -1 ? ("Reflection error: " + f.R.lastError) : "");
    }

    private TestResult testBooking_bookRoomInCheapestHotel_range_noHotels() {
        Fixture f = emptyFixture();
        int hotelID = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{5L, 5L}, -1);
        return assertEquals("Cheapest: no hotels registered", -1, hotelID,
                hotelID == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : "");
    }

    // 7) Cheapest (range): booking actually reserves capacity (second attempt fails)
    private TestResult testBooking_bookRoomInCheapestHotel_range_doubleBookingFails() {
        Fixture f = baselineFixture();
        int first = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{10L, 10L}, -1);
        int second = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{10L, 10L}, -1);
        boolean pass = (first != -1) && (second == -1);
        return new TestResult("Cheapest hotel booking reserves capacity", pass,
                pass ? "" : (first == -1 || second == -1 ? ("Reflection note: " + f.R.lastError) : shortHint("Second identical cheapest booking should fail when capacity is exhausted.")),
                pass ? "" : ("first=" + first + ", second=" + second));
    }

    // 8) Cheapest (range): invalid (start > end)
    private TestResult testBooking_bookRoomInCheapestHotel_range_invalidRange() {
        Fixture f = baselineFixture();
        int rc = (f.booking == null) ? -1
                : f.R.callInt(f.booking, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{12L, 10L}, -1);
        return assertEquals("Cheapest hotel (range) invalid (start > end)", -1, rc,
                rc == -1 ? ("".equals(f.R.lastError) ? "" : ("Reflection note: " + f.R.lastError)) : "");
    }

    // 9) Cheapest (range): negative dates rejected
    private TestResult testBooking_bookRoomInCheapestHotel_range_negativeDates() {
        SafeRef R = new SafeRef();
        Class<?> Booking = R.cls("Booking");
        Object b = R.newInstance(Booking, new Class<?>[]{}, new Object[]{});
        int rc1 = (b == null) ? -1 : R.callInt(b, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{-1L, 1L}, -1);
        int rc2 = (b == null) ? -1 : R.callInt(b, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{1L, -1L}, -1);
        int rc3 = (b == null) ? -1 : R.callInt(b, "bookRoomInCheapestHotel",
                new Class<?>[]{long.class, long.class}, new Object[]{-2L, -1L}, -1);
        boolean pass = (rc1 == -1 && rc2 == -1 && rc3 == -1) || !(rc1 == 0 || rc2 == 0 || rc3 == 0);
        return new TestResult("Cheapest hotel (range) rejects negative dates", pass,
                pass ? "" : shortHint("Ranges with negative dates should not succeed."));
    }

    /* ============================
       Tests – Misc/edge
       ============================ */
    private TestResult testRangeAPIs_negativeDates() {
        Fixture f = baselineFixture();
        int rc1 = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, long.class, int.class}, new Object[]{-1L, 1L, 0}, -1);
        int rc2 = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, long.class, int.class}, new Object[]{1L, -1L, 0}, -1);
        int rc3 = (f.booking == null) ? -1 : f.R.callInt(f.booking, "bookRoomInHotel", new Class<?>[]{long.class, long.class, int.class}, new Object[]{-2L, -1L, 0}, -1);
        boolean pass = (rc1 == -1 && rc2 == -1 && rc3 == -1) || !(rc1 == 0 || rc2 == 0 || rc3 == 0);
        return new TestResult("Negative dates rejected (range)", pass,
                pass ? "" : shortHint(("".equals(f.R.lastError) ? "Ranges with negative dates should not succeed." : ("Reflection note: " + f.R.lastError))));
    }

    /* ============================
       Minimal reporting utilities
       ============================ */

    /**
     * Prints one grouped test suite with its own header and summary.
     * @return number of passed tests in the suite
     */
    private int printSuite(String title, List<java.util.function.Supplier<TestResult>> tests) {
        System.out.println("\n--- " + title + " ---");
        int passed = 0;
        int idx = 1;
        for (java.util.function.Supplier<TestResult> s : tests) {
            TestResult r = s.get();
            printOne(r, idx++);
            if (r.pass) passed++;
        }
        String badge = (passed == tests.size()) ? "✅" : "❌";
        System.out.printf("%s %d/%d passed for %s\n", badge, passed, tests.size(), title);
        if (passed == tests.size()) {
            System.out.println("   ✓ All checks for this method passed. You can proceed to the next method.");
        } else {
            System.out.println("   • Some checks failed. Fix these before moving on.");
        }
        return passed;
    }

    /** Prints a single test result line in a consistent style (used by suites). */
    private void printOne(TestResult r, int idx) {
        String icon = r.pass ? "✅" : "❌";
        System.out.printf("%2d) %s %s\n", idx, icon, r.name);
        if (!r.pass && r.hint != null && !r.hint.isEmpty()) {
            System.out.println("    » " + r.hint);
        }
        if (!r.pass && r.detail != null && !r.detail.isEmpty()) {
            System.out.println("    · Detail: " + r.detail);
        }
    }

    private TestResult assertEquals(String name, int expected, int actual, String hint) {
        boolean pass = expected == actual;
        String detail = pass ? "" : ("expected=" + expected + ", actual=" + actual);
        return new TestResult(name, pass, pass ? "" : shortHint(hint), detail);
    }

    private String shortHint(String s) {
        // Keep hints short & not too revealing
        return s;
    }

    /* ============================
       Lightweight result holder
       ============================ */
    private static class TestResult {
        final String name;
        final boolean pass;
        final String hint;
        final String detail;
        TestResult(String name, boolean pass, String hint) { this(name, pass, hint, ""); }
        TestResult(String name, boolean pass, String hint, String detail) {
            this.name = name; this.pass = pass; this.hint = hint; this.detail = detail;
        }
    }
}