package org.larinia.client;

/**
 * Created by lyu on 25/07/16.
 */
public class AppMain2 {
    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            Thread thread = new Thread(arg) {
                public void run() {
                    System.out.println(arg);
                }
            };
            System.out.println("AppMain2.main "+thread.getContextClassLoader().toString());
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
            }
        }
    }
}
