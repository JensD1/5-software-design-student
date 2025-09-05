package be.uantwerpen.sd.labs.lab2.classdiagrams.interfaces;


class TV implements VolumeDevice {
    private int volume; // Default volume level

    public TV() {
        this.volume = 10;
    }

    public TV(int volume) {
        this.volume = volume;
    }

    @Override
    public void volumeUp() {
        volume++;
        System.out.println("TV volume increased to: " + volume);
    }

    @Override
    public void volumeDown() {
        volume--;
        System.out.println("TV volume decreased to: " + volume);
    }
}
