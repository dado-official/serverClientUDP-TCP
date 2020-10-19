package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ComToServerClient {
    private Socket socket;
    public ComToServerClient(Socket socket){
        this.socket = socket;
    }

    public void writeMessageToServer(String nachricht) throws Exception {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(nachricht);
        printWriter.flush();
    }

    public String readMessageFromServer() throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String string = bufferedReader.readLine();
        return string;
    }

}
