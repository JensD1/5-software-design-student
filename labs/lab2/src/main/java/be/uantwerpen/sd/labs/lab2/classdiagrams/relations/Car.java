package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private Pedal accPedal;
    private Pedal breakPedal;
    private List<Wiper> wipers;
    private List<Wheel> wheels;


    public Car() {
        this.wipers = new ArrayList<>();
        this.wheels = new ArrayList<>();

        this.wheels.add(new Wheel());
        this.wheels.add(new Wheel());
        this.wheels.add(new Wheel());
        this.wheels.add(new Wheel());
    }

    public List<Wiper> getWipers() {
        return wipers;
    }

    public void setWipers(List<Wiper> wipers) {
        this.wipers = wipers;
    }

    public void accelerate() {
        for (Wheel w : wheels) {
            w.speedUp();
        }
    }

    public void decelerate() {
        for (Wheel w : wheels) {
            w.speedDown();
        }
    }

    public double readSpeed() {
        double sum = 0;
        for (Wheel w : wheels) {
            sum = sum + w.getSpeed();
        }

        double mean = sum / 4.0;

        return mean;
    }

    public void wipeWindows() {
        for (Wiper w : this.wipers) {
            w.wipe();
        }
    }

    public void addWiper(Wiper wiper) {
        this.wipers.add(wiper);
    }

    public Pedal getAccPedal() {
        return this.accPedal;
    }

    public Pedal getBreakPedal() {
        return this.breakPedal;
    }

    public void setAccPedal(Pedal accPedal) {
        this.accPedal = accPedal;
    }

    public void setBreakPedal(Pedal breakPedal) {
        this.breakPedal = breakPedal;
    }

    public List<Wheel> getWheels() {
        return this.wheels;
    }

    public void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
    }

    public void addWheel(Wheel wheel) {
        this.wheels.add(wheel);
    }


}
