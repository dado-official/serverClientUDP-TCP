package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WaitForClientsToFunk implements Runnable{
    private Socket socket;
    public  WaitForClientsToFunk(Socket socket){
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

    @Override
    public void run() {

        while(true){
            try {
                String s = readMessageFromClient();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
