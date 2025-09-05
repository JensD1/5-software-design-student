package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;


public class Main {

    public static void main(String[] args)
    {
        Main main = new Main();
        main.run();
    }

    Car car;

    public Main()
    {
        this.car = new Car();

        car.setAccPedal(new AccPedal(car));
        car.setBreakPedal(new BreakPedal(car));

        car.addWiper(new FrontWiper());
        car.addWiper(new FrontWiper());
        car.addWiper(new BackWiper());
    }

    public void run()
    {
       Pedal accPedal = car.getAccPedal();
       Pedal breakPedal = car.getBreakPedal();

       accPedal.press();
       accPedal.press();
       accPedal.press();
       car.readSpeed();

       car.wipeWindows();

       breakPedal.press();
       car.readSpeed();

    }
}
