package be.uantwerpen.sd.labs.lab2.classdiagrams.interfaces;

class Radio implements VolumeDevice {
    private int volume; // Default volume level

    public Radio() {
        this.volume = 5;
    }

    public Radio(int volume) {
        this.volume = volume;
    }

    @Override
    public void volumeUp() {
        volume++;
        System.out.println("Radio volume increased to: " + volume);
    }

    @Override
    public void volumeDown() {
        volume--;
        System.out.println("Radio volume decreased to: " + volume);
    }
}
