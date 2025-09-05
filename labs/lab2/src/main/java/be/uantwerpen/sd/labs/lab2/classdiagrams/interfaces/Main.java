package be.uantwerpen.sd.labs.lab2.classdiagrams.interfaces;

public class Main {
    public static void main(String[] args) {
        UniversalRemote remote = new UniversalRemote();

        // Create devices
        TV tv = new TV();
        Radio radio = new Radio();
        CDPlayer cdPlayer = new CDPlayer();

        // Add devices to the remote
        remote.addDevice(tv);
        remote.addDevice(radio);
        remote.addDevice(cdPlayer);

        // Lower and rise volume using the remote
        remote.lowerVolume();
        remote.riseVolume();
    }
}
