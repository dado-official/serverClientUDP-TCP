package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.ComToServerClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;


public class Register implements ActionListener {
    private JFrame frame;
    private Socket socket;
    private JTextField benutzername;
    private JTextField passwort;
    private JTextField passwortW;

    public Register(JFrame frame, Socket socket){
        this. frame = frame;
        this.socket = socket;
        buildRegister();
        System.out.println("Register GUI created");
    }

    private void buildRegister(){
        JPanel jPanel = new JPanel();
        frame.setSize(new Dimension(500, 300));
        frame.setContentPane(jPanel);
        jPanel.setBorder(new EmptyBorder(10,10,10,10));

        Box vBox = Box.createVerticalBox();

        JLabel benutzernameL = new JLabel("Benutzername:");
        JLabel passL = new JLabel("Passwort:");
        JLabel passwL = new JLabel("Passwort wiederholen:");

        benutzernameL.setBorder(new EmptyBorder(10, 0, 0, 0));
        passL.setBorder(new EmptyBorder(10, 0, 0, 0));
        passwL.setBorder(new EmptyBorder(10, 0, 0, 0));

        benutzername = new JTextField();
        passwort = new JTextField();
        passwortW = new JTextField();

        JButton zuruck = new JButton("Zurück");
        JButton registrieren = new JButton("Registrieren");

        zuruck.addActionListener(this);
        registrieren.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(zuruck);
        buttonPanel.add(registrieren);

        vBox.add(benutzernameL);
        vBox.add(benutzername);
        vBox.add(passL);
        vBox.add(passwort);
        vBox.add(passwL);
        vBox.add(passwortW);
        vBox.add(buttonPanel);
        jPanel.add(vBox);
        jPanel.setLayout(new GridLayout());
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()){
            case "Zurück":
                LoginOrRegister loginOrRegister = new LoginOrRegister(socket, frame);
                break;
            case "Registrieren":
                ComToServerClient comToServerClient = new ComToServerClient(socket);
                try {
                    comToServerClient.writeMessageToServer("Rx0x" + benutzername.getText().hashCode() + "x0x" + passwort.getText().hashCode());
                    if("1".equals(comToServerClient.readMessageFromServer())){
                        System.out.println("registrierung erfolgreich");
                        MainGUI mainGUI = new MainGUI(frame, socket, benutzername.getText());
                    } else {
                        System.out.println("Fehler bei der Registrierung");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
