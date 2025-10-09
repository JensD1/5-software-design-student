package be.uantwerpen.sd.labs.lab5.register_entry;

import java.util.Calendar;

public class RegisterEntry {
    protected boolean checkedIn;
    protected int hours;
    protected int minutes;
    protected int seconds;

    public RegisterEntry(boolean checkedIn) {
        this.checkedIn = checkedIn;

        Calendar calendar = Calendar.getInstance();
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.seconds = calendar.get(Calendar.SECOND);
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        String status;

        if (this.checkedIn) {
            status = "in";
        } else {
            status = "out";
        }

        return String.format("checked %s at %02d:%02d:%02d", status, getHours(), getMinutes(), getSeconds());
    }
}
