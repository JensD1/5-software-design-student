package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;

public class FrontWiper implements Wiper
{
    public FrontWiper()
    {

    }

    @Override
    public void wipe()
    {
        System.out.println("Wiping front window");
    }
}
