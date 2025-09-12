package be.uantwerpen.sd.labs.lab4b.factory;

public class FactoryProvider {
    // Use the following structure to make new factories in this provider.
    // Change the name from "YourNewFactory" to a more suitable name for your factories.

    /*
    public static TeslaFactory yourNewFactory()
    {
        return new YourNewFactory();
    }
     */

    // In this way, the main function can call FactoryProvider.yourNewFactory() and gets a factory to work with.

    public static TeslaFactory modelXFactory() {
        return new ModelXFactory();
    }

    public static TeslaFactory modelSFactory() {
        return new ModelSFactory();
    }

    public static TeslaFactory model3Factory() {
        return new Model3Factory();
    }

    public static TeslaFactory modelYFactory() {
        return new ModelYFactory();
    }
}
