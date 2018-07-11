package com.redhat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lyu on 01/08/16.
 */
public class MySSLMain {


    public static void main(String[] args) {

        // this is the swallow server
       System.setProperty("javax.net.ssl.trustStore", "/home/lyu/bin/certs/truststore.jks");
       System.setProperty("javax.net.ssl.trustStorePassword", "12345678");
        System.setProperty("javax.net.ssl.keyStore", "/home/lyu/bin/certs/swallow.redhat.com.keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        System.setProperty("sun.net.spi.nameservice.nameservers","swallow.redhat.com");

        MySSLMain sslMain = new MySSLMain();
        try {
            sslMain.myMethod();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void myMethod() throws IOException{

            URL jboss = new URL("https://totoro.usersys.redhat.com:8443");
            URLConnection yc = jboss.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null)
            {
                System.out.println(inputLine);
            }


            in.close();


    }
}
