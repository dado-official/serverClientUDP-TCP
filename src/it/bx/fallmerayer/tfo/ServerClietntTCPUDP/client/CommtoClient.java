package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.Scanner;

public class CommtoClient implements Runnable {
    private JTextArea area;
    private DatagramSocket datagramSocket;

    public CommtoClient(JTextArea area, DatagramSocket datagramSocket){
        this.area = area;
        this.datagramSocket = datagramSocket;
    }

    private StringBuilder data(byte[] a){
        if(a == null){
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0){
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }


    @Override
    public void run() {
        System.out.println("run");
        try {
            byte[] recevie = new byte[65535];
            while (true){
                DatagramPacket datagramPacket = new DatagramPacket(recevie, recevie.length);
                datagramSocket.receive(datagramPacket);
                //System.out.println(data(recevie).toString());
                String msg = data(recevie).toString();
                System.out.println(msg);
                if(msg.contains("FILEx0x;")){
                    System.out.println(1);
                    String[] string = msg.split(";");
                    System.out.println(2);
                    File file = new File(string[1]);
                    System.out.println(3);
                    file.createNewFile();
                    System.out.println(4);
                    if(file.exists()){
                        System.out.println("Datei wurde erstellt: " + file.getAbsolutePath());
                    } else {
                        System.out.println("fehler beim erstellen der Datei");
                    }

                    FileWriter fileWriter = new FileWriter(file,true);
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(msg.split("x0headerEnD0x")[1]);
                    bw.close();
                }
                area.append("\n" + msg);
                recevie = new byte[65535];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
