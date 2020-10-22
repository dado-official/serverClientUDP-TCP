package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client;
import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels.LoginOrRegister;

import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6666);
            LoginOrRegister loginOrRegister = new LoginOrRegister(socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
