package be.uantwerpen.sd.labs.lab3.singleton;

public class Ticket {
    private long id;
    private String title;

    public Ticket(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", title=" + title + '}';
    }

}
