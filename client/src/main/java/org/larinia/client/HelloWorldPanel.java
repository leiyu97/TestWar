package org.larinia.client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lyu on 08/07/16.
 */
public class HelloWorldPanel extends JPanel {


    public static void main(String args[]) {
        new HelloWorldPanel();
    }

    public HelloWorldPanel() {
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter username:");
        JTextField userName = new JTextField(20);
        JLabel jlbHelloWorld = new JLabel("Hello World");
        this.add(jlbHelloWorld, BorderLayout.NORTH);
        this.add(userName, BorderLayout.CENTER);
        this.add(label, BorderLayout.EAST);
        this.setSize(200, 200);

        //pack();
        this.setVisible(true);

      /*  JPanel anotherPanel = new JPanel();
        anotherPanel.setLayout(new BorderLayout());
        anotherPanel.add(this, BorderLayout.CENTER);*/

        JFrame s = new JFrame("frustrating");
        s.setLayout(new BorderLayout());
        s.setSize(300, 300);
        s.add(this);
        s.setVisible(true);

        Canvas can = new Canvas();

        can.setSize(300, 300);
        //  can.add(this);
    }

}

