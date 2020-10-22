package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktions.Funktion;
import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktions.FunktionHalloWelt;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SubServerMain {
    private static ArrayList<String> preFunktion;
    private static ArrayList<Funktion> funktion;
    private static Socket socket;

    public static void conNewFunktion(Funktion funk, String pre){
        preFunktion.add(pre);
        funktion.add(funk);
    }

    public static String funktionAction(String pre, String con){
        System.out.println("funktion Action");
        for (int i = 0; i < preFunktion.size(); i++) {
            System.out.println(i);
            if(preFunktion.get(i).equals(pre)){
                System.out.println("funktion gefunden");
                return funktion.get(i).action(con);
            }
        }
        System.out.println("Funktion nicht gefunden: " + pre);
        return "funktion nicht gefunden";
    }

    public static void main(String[] args) throws IOException {
        preFunktion = new ArrayList<>();
        funktion = new ArrayList<>();
        socket = new Socket("localhost", 7777);
        Funktion halloWelt = new FunktionHalloWelt();
        conNewFunktion(halloWelt, "halloWelt");
        System.out.println(preFunktion.toString());
        System.out.println(funktion.toString());
        while(true){
            String string = null;
            while (string == null && socket.isConnected()){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                string = bufferedReader.readLine();
                System.out.println(string);
            }
            System.out.println("Funktion: " + string);
            assert string != null;
            String[] strings = string.split(",");
            String[] subStrings = strings[1].split(":");
            System.out.println(Arrays.toString(subStrings));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            if(subStrings.length == 1){
                printWriter.println(funktionAction(subStrings[0], ""));
            } else {
                printWriter.println(funktionAction(subStrings[0], subStrings[1]));
            }
            printWriter.flush();
        }
    }
}
