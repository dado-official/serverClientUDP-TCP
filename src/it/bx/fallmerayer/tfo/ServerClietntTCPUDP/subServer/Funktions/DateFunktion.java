package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktions;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.subServer.Funktion;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunktion implements Funktion {
    @Override
    public String action(String con) {
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateTimeFormatter.format(date);
    }
}
