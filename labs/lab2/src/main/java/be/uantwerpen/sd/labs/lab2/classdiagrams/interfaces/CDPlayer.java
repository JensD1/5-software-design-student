package be.uantwerpen.sd.labs.lab2.classdiagrams.interfaces;


class CDPlayer implements VolumeDevice {
    private int volume; // Default volume level

    public CDPlayer() {
        this.volume = 7;
    }

    public CDPlayer(int volume) {
        this.volume = volume;
    }

    @Override
    public void volumeUp() {
        volume++;
        System.out.println("CDPlayer volume increased to: " + volume);
    }

    @Override
    public void volumeDown() {
        volume--;
        System.out.println("CDPlayer volume decreased to: " + volume);
    }
}