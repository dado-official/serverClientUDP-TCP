package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServerMain {
    private static final int PORT = 6666;
    public static void main(String[] args) {
        System.out.println("#server");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            ExecutorService executor;
            while(true){
                Socket socket = serverSocket.accept();
                executor = Executors.newCachedThreadPool();
                Runnable runnable = new ComToClientMainServer(socket);
                executor.submit(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}