package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.exception.SocketDisconnected;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ComToClientMainServer implements Runnable {
    private Socket socket;

    public ComToClientMainServer(Socket socket){
        this.socket = socket;
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
        switch (splitMsg[0]){
            case "R":
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
        }
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
