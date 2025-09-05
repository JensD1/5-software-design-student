package be.uantwerpen.sd.labs.lab1;


import java.util.HashMap;
import java.util.Map;

public class Hotel
{
    private HashMap<Integer, Room> rooms;
    private int roomCount = 0;
    private double pricePerRoom;

    public Hotel(double pricePerRoom)
    {
        this.rooms = new HashMap<>();
        this.pricePerRoom = pricePerRoom;
    }

    /** Utility: validate a single date (allows zero and positive values). */
    private boolean isValidDate(long date) {
        return date >= 0L;
    }

    /** Utility: validate an inclusive date range. */
    private boolean isValidDateRange(long startDate, long endDate) {
        return isValidDate(startDate) && isValidDate(endDate) && startDate <= endDate;
    }

    /** Utility: check if a room is free for every day in [startDate, endDate] inclusive. */
    private boolean isRoomFree(Room room, long startDate, long endDate) {
        for (long d = startDate; d <= endDate; d++) {
            if (room.getBooking(d)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds an empty room to the hotel.
     * Function needs to give ID to the room.
     * It's OK to use room count as ID
     * -> first room has ID 0
     * -> second room has ID 1
     * -> third room has ID 2
     * ...
     */
    public void addRoom()
    {
       Room newRoom = new Room();
       this.rooms.put(roomCount, newRoom);

       this.roomCount++;
    }

    /**
     * Checks available rooms on a specific date
     * @param date date to check for available room
     * @return int containing ID of first available room, -1 if no room is available
     */
    public int checkAvailability(long date)
    {
        if (!isValidDate(date) || this.rooms.isEmpty()) {
            return -1;
        }
        for (Map.Entry<Integer, Room> e : this.rooms.entrySet()) {
            int roomID = e.getKey();
            Room room = e.getValue();
            if (!room.getBooking(date)) {
                return roomID;
            }
        }
        return -1;
    }

    /**
     * Checks availability between a range of dates
     * @param startDate starting date to check for available room
     * @param endDate ending date to check for available room
     * @return int containing ID of first available room in range, -1 if no room is available
     */
    public int checkAvailability(long startDate, long endDate)
    {
        /*
            TODO: Check availability across [startDate, endDate].
            TIP: Read this entire file first.
        */
        return -1;
    }

    /**
     * Books a given room on a given date
     * @param date date on which room needs to be booked
     * @param roomID ID of room that needs to be booked
     * @return -1 for error, 0 for OK
     */
    public int bookRoom(long date, int roomID)
    {
        if (!isValidDate(date)) {
            return -1;
        }
        Room room = this.rooms.get(roomID);
        if (room == null) {
            return -1;
        }
        boolean booked = room.getBooking(date);
        if (booked) {
            return -1;
        }
        room.setBooking(date, true);
        return 0;
    }

    /**
     * Books a given room for every date in the inclusive range [startDate, endDate].
     * The booking is atomic: if any day in the range is already booked, nothing is changed.
     * @param startDate starting date (inclusive)
     * @param endDate ending date (inclusive)
     * @param roomID ID of room that needs to be booked
     * @return -1 for error (invalid range, room not found, or any day unavailable), 0 for OK
     */
    public int bookRoom(long startDate, long endDate, int roomID)
    {
        if (!isValidDateRange(startDate, endDate)) {
            return -1;
        }
        Room room = this.rooms.get(roomID);
        if (room == null) {
            return -1;
        }
        if (!isRoomFree(room, startDate, endDate)) {
            return -1;
        }
        for (long d = startDate; d <= endDate; d++) {
            room.setBooking(d, true);
        }
        return 0;
    }

    public double getPricePerRoom()
    {
        return this.pricePerRoom;
    }
}
