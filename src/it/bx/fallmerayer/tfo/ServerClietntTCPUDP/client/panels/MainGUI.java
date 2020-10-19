package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.CommtoClient;
import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer.ComToClientMainServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainGUI implements ActionListener {
    private JFrame frame;
    private Socket socket;
    private JPanel jPanel;

    private JTextArea inputTextArea;
    private JTextArea messageTextArea;
    private JButton sendButton;
    private Runnable commtoClient;
    private String benutzername;

    private int PORT;
    private DatagramSocket datagramSocket;

    public MainGUI(JFrame frame, Socket socket, String benutzername){
        this.frame = frame;
        this.socket = socket;
        this.benutzername = benutzername;

        jPanel = new JPanel();
        frame.setSize(new Dimension(800, 700));
        frame.setContentPane(jPanel);
        jPanel.setBorder(new EmptyBorder(10,10,10,10));
        buildGUI();

        conn();
    }

    private void buildGUI(){
        frame.setTitle(benutzername);
        jPanel.setLayout(new BorderLayout());
        messageTextArea = new JTextArea();
        messageTextArea.setEditable(false);
        jPanel.add(messageTextArea, BorderLayout.CENTER);

        Box vBoxBottom = Box.createHorizontalBox();
        inputTextArea = new JTextArea();
        inputTextArea.setBackground(Color.LIGHT_GRAY);
        vBoxBottom.add(inputTextArea);
        sendButton = new JButton("senden");
        sendButton.addActionListener(this);
        vBoxBottom.add(sendButton);
        jPanel.add(vBoxBottom, BorderLayout.SOUTH);

        frame.repaint();
    }

    private void conn(){
        try {
            File dns = new File("src/it/bx/fallmerayer/tfo/ServerClietntTCPUDP/client/dns.csv");
            dns.createNewFile();
            FileWriter fileWriter = new FileWriter(dns,true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            Scanner sc = new Scanner(dns);
            Random random = new Random();
            int ra;
            while(true){
                ra = random.nextInt(65535 - 49152 + 1) + 48152;
                System.out.println();
                while(sc.hasNextLine()){
                    if(sc.nextLine().contains(String.valueOf(ra))){
                        break;
                    }
                }
                PORT = ra;
                bw.write(benutzername + ";" + PORT + "\n");
                bw.close();
                break;
            }

            this.datagramSocket = new DatagramSocket(ra);
            ExecutorService executor = Executors.newCachedThreadPool();
            Runnable runnable = new CommtoClient(messageTextArea, datagramSocket);
            executor.submit(runnable);
            System.out.println("cont end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(String str){
        try {
            String[] strings = str.split(",");

            Scanner scanner = new Scanner(new File("src/it/bx/fallmerayer/tfo/ServerClietntTCPUDP/client/dns.csv"));
            boolean su = false;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println(line.split(";")[0] +" = " + strings[0]);
                if(strings[0].equals(line.split(";")[0])){
                    System.out.println(Integer.parseInt(line.split(";")[1]));
                    InetAddress ip = InetAddress.getLocalHost();
                    byte[] buff = null;
                    buff = str.replace(strings[0] + ",", benutzername + ":").getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length, ip, Integer.parseInt(line.split(";")[1]));
                    datagramSocket.send(datagramPacket);
                    su = true;
                }
            }
            if(!su){
                System.out.println("client nicht gefunden");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()){
            case "senden":
                writeMessage(inputTextArea.getText());
                inputTextArea.setText("");
                break;
        }

    }
}
