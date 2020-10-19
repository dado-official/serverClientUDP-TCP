package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels;

import it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.ComToServerClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class Login implements ActionListener {
    private JFrame frame;
    private Socket socket;
    private JTextField benutzername;
    private JTextField passwort;
    public Login(JFrame frame, Socket socket){
        this.frame = frame;
        this.socket = socket;
        buildAndShowLogin();
        System.out.println("Login GUI created");
    }

    private void buildAndShowLogin(){
        JPanel jPanel = new JPanel();
        frame.setSize(new Dimension(500, 250));
        frame.setContentPane(jPanel);
        jPanel.setBorder(new EmptyBorder(10,10,10,10));

        Box vBox = Box.createVerticalBox();

        JLabel benutzernameL = new JLabel("Benutzername:");
        JLabel passL = new JLabel("Passwort:");

        benutzernameL.setBorder(new EmptyBorder(10, 0, 0, 0));
        passL.setBorder(new EmptyBorder(10, 0, 0, 0));

        benutzername = new JTextField();
        passwort = new JTextField();

        JButton zuruck = new JButton("Zurück");
        JButton login = new JButton("Login");

        zuruck.addActionListener(this);
        login.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(zuruck);
        buttonPanel.add(login);

        vBox.add(benutzernameL);
        vBox.add(benutzername);
        vBox.add(passL);
        vBox.add(passwort);
        vBox.add(buttonPanel);
        jPanel.add(vBox);
        jPanel.setLayout(new GridLayout());
        frame.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()){
            case "Zurück":
                LoginOrRegister loginOrRegister = new LoginOrRegister(socket, frame);
                break;
            case "Login":
                ComToServerClient comToServerClient = new ComToServerClient(socket);
                try {
                    comToServerClient.writeMessageToServer("Lx0x" + benutzername.getText().hashCode() + "x0x" + passwort.getText().hashCode());
                    if("1".equals(comToServerClient.readMessageFromServer())){
                        System.out.println("login erfolgreich");
                        MainGUI mainGUI = new MainGUI(frame, socket, benutzername.getText());
                    } else {
                        System.out.println("Fehler bei Login");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
