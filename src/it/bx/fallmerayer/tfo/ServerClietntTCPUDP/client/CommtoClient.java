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
                System.out.println(data(recevie).toString());
                area.append("\n" + data(recevie).toString());
                recevie = new byte[65535];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
