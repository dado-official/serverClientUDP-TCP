package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktions;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktion;

public class FunktionHalloWelt implements Funktion {
    public FunktionHalloWelt(){

    }
    @Override
    public String action(String con) {
        return "hallo Welt";
    }
}
