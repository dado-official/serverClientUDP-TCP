package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.CommtoClient;
import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer.ComToClientMainServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.SQLOutput;
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
        System.out.println(str);
        try {
            String[] strings = str.split(",");
            if(strings[0].equals("funktion")) {
                System.out.println("FUNKTIONN: " + strings[1]);
                writeMessageToServer(str);
                String string = readMessageFromServer();
                messageTextArea.append(string + "\n");
            } else {
                Scanner scanner = new Scanner(new File("src/it/bx/fallmerayer/tfo/ServerClietntTCPUDP/client/dns.csv"));
                boolean su = false;

                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    System.out.println(line.split(";")[0] +" = " + strings[0]);
                    if(strings[0].equals(line.split(";")[0])){
                        if(strings[1].startsWith("file:")){
                            File file = new File(strings[1].split(":")[1]);
                            Scanner sc = new Scanner(file);
                            StringBuilder fileContent = new StringBuilder();
                            while(sc.hasNextLine()){
                                fileContent.append(sc.nextLine()).append("\n");
                            }
                            byte[] buff = null;
                            buff = (benutzername + ":FILEx0x;" + file.getName() + ";x0headerEnD0x" + fileContent).getBytes();
                            su = writeUDP(buff, Integer.parseInt(line.split(";")[1]));
                        } else {
                            System.out.println(Integer.parseInt(line.split(";")[1]));
                            byte[] buff = null;
                            buff = str.replace(strings[0] + ",", benutzername + ":").getBytes();
                            su = writeUDP(buff, Integer.parseInt(line.split(";")[1]));
                        }
                    }
                }
                if(!su){
                    System.out.println("fehler");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean writeUDP(byte[] buff, int port) throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length, ip, port);
        datagramSocket.send(datagramPacket);
        return true;
    }

    private void writeMessageToServer(String nachricht) throws Exception {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("printer: " + nachricht);
        printWriter.println(nachricht);
        printWriter.flush();
    }

    private String readMessageFromServer() throws Exception{
        String string = null;
        while (string == null && socket.isConnected()){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            string = bufferedReader.readLine();
        }
        return string;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()){
            case "senden":
                writeMessage(inputTextArea.getText());
                messageTextArea.append(inputTextArea.getText().replace(benutzername, " -->"));
                inputTextArea.setText("");
                break;
        }

    }
}
