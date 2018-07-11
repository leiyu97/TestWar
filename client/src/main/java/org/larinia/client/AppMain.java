package org.larinia.client;

/**
 * Created by lyu on 25/07/16.
 */
public class AppMain {

    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }

        AppMain am = new AppMain();
        am.printSomething();
    }

    public void printSomething () {
        System.out.println("AppMain.printSomething: it was funny");

    }
}
