package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.exception;

public class SocketDisconnected extends Exception {
    public SocketDisconnected(String errorMessage){
        super(errorMessage);
    }
}
