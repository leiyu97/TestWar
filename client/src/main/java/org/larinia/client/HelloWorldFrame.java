package org.larinia.client;

/**
 * Created by lyu on 07/07/16.
 */
import javax.swing.*;


public class HelloWorldFrame extends JFrame {

    public static void main(String args[]) {
      new HelloWorldFrame();

    }

    public HelloWorldFrame() {

        JLabel jlbHelloWorld = new JLabel("Hello World");
        add(jlbHelloWorld);
        this.setSize(100, 100);
        //pack();
        setVisible(true);
    }
}