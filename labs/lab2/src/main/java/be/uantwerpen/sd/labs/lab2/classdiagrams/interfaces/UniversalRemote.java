package be.uantwerpen.sd.labs.lab2.classdiagrams.interfaces;

import java.util.ArrayList;
import java.util.List;

class UniversalRemote {
    private List<VolumeDevice> devices = new ArrayList<>();

    public void addDevice(VolumeDevice device) {
        devices.add(device);
        System.out.println("Device added: " + device.getClass().getSimpleName());
    }

    public void lowerVolume() {
        System.out.println("Lowering volume on all devices:");
        for (VolumeDevice device : devices) {
            device.volumeDown();
        }
    }

    public void riseVolume() {
        System.out.println("Rising volume on all devices:");
        for (VolumeDevice device : devices) {
            device.volumeUp();
        }
    }
}