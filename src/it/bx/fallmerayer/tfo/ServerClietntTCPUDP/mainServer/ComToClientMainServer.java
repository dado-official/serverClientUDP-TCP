package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import sun.applet.Main;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ComToClientMainServer implements Runnable {
    private Socket socket;

    public ComToClientMainServer(Socket socketClient){
        this.socket = socketClient;
    }

    private void writeMessageToClient(String nachricht) throws Exception {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(nachricht);
        printWriter.flush();
    }

    private String readMessageFromClient() throws Exception{
        String string = null;
        while (string == null && socket.isConnected()){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            string = bufferedReader.readLine();
        }
        return string;
    }

    private void checkMessage(String msg) throws Exception {
        String[] splitMsg = msg.split("x0x");
        File log = new File("src/logUsers.csv");
        System.out.println("check Message");
        if(msg.startsWith("funktion")){
            System.out.println("is Funktion");
            writeMessageToSubServer(msg);
            String string = readMessageFromSubServer();
            System.out.println(string);
            writeMessageToClient(string);
        } else {
            switch (splitMsg[0]){
                case "R":
                    System.out.println("Client Registriert");
                    //client Registrieren
                    if(!log.exists()){
                        log.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(log,true);
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(splitMsg[1] + ";" + splitMsg[2] + "\n");
                    bw.close();
                    writeMessageToClient("1");

                case "L":
                    System.out.println("Client login");
                    if(!log.exists()){
                        System.out.println("ERR: file not found");
                    } else {
                        Scanner scanner = new Scanner(log);
                        boolean su = false;
                        while (scanner.hasNextLine()){
                            String line = scanner.nextLine();
                            if(line.equals(splitMsg[1] + ";" + splitMsg[2])){
                                writeMessageToClient("1");
                                su = true;
                            }
                        }
                        if(!su){
                            writeMessageToClient("0");
                        }
                    }
                default:
                    //System.out.println("Client funktion");
                    //writeMessageToSubServer(msg);
                    //writeMessageToClient(readMessageFromSubServer());
                    break;
            }
        }
    }

    private void writeMessageToSubServer(String nachricht) throws Exception {
        System.out.println("write to subserver");
        PrintWriter printWriter = new PrintWriter(MainServerMain.subSocket.getOutputStream(), true);
        printWriter.println(nachricht);
        System.out.println("Server printer: " +nachricht);
        printWriter.flush();
    }

    private String readMessageFromSubServer() throws Exception{
        String string = null;
        while (string == null && MainServerMain.subSocket.isConnected()){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(MainServerMain.subSocket.getInputStream()));
            string = bufferedReader.readLine();
        }
        return string;
    }

    @Override
    public void run() {
        System.out.println("#Communicate With Client");
        try {
            while(socket.isConnected()){
                String msg = readMessageFromClient();
                System.out.println("Msg from Client: " + msg);
                checkMessage(msg);
                //writeMessageToClient("Test");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#Client disconnected");
    }
}
