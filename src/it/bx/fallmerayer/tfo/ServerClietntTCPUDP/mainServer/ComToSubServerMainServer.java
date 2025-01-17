package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ComToSubServerMainServer implements Runnable {
    private Socket socket;

    public ComToSubServerMainServer(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Subserver connected");
    }

    public void writeMessageToClient(String nachricht) throws Exception {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(nachricht);
        printWriter.flush();
    }

    public String readMessageFromClient() throws Exception{
        String string = null;
        while (string == null && socket.isConnected()){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            string = bufferedReader.readLine();
        }
        return string;
    }


    @Override
    public void run() {

    }
}
