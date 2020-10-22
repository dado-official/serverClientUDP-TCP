package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.mainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitForSubserverToCon implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(7777);
            Socket socket = ss.accept();
            MainServerMain.subSocket = socket;
            Executor executor;
            executor = Executors.newCachedThreadPool();
            Runnable runnable = new ComToSubServerMainServer(socket);
            executor.execute(runnable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
