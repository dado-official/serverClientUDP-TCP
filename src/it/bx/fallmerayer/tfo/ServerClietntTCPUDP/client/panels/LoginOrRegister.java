package it.bx.fallmerayer.tfo.ServerClietntTCPUDP.client.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class LoginOrRegister implements ActionListener {
    private Socket socket;
    private JFrame logOrReg;

    public LoginOrRegister(Socket socket){
        this.socket = socket;
        logOrReg = new JFrame();
        buildAndShowLoginOrRegister();
        System.out.println("LoginOrRegister GUI created");
    }

    public LoginOrRegister(Socket socket,  JFrame logOrReg){
        this.socket = socket;
        this.logOrReg = logOrReg;
        buildAndShowLoginOrRegister();
        System.out.println("LoginOrRegister GUI created");
    }

    private void buildAndShowLoginOrRegister(){
        logOrReg.setSize(new Dimension(500, 250));
        JPanel vPanel = new JPanel();
        logOrReg.setContentPane(vPanel);
        vPanel.setBorder(new EmptyBorder(10,10,10,10));
        vPanel.setSize(new Dimension(logOrReg.getWidth(), logOrReg.getHeight()));

        //BoxLayout boxLayout = new BoxLayout(vPanel, BoxLayout.Y_AXIS);
        //vPanel.setLayout(boxLayout);
        JLabel label = new JLabel("" +
                "<html>Fals Sie schon ein registrierter Benutzer sind, " +
                "<br>dann bitte auf 'login' klicken. " +
                "<br>Falls sie noch kein Konto haben bitte auf 'register' klicken.</html>"
        );
        label.setBorder(new EmptyBorder( 20,20,20,20));
        JButton loginButton = new JButton("login");
        JButton registerButton = new JButton("register");
        loginButton.setBackground(Color.white);
        registerButton.setBackground(Color.white);
        loginButton.setSize(new Dimension(200, 200));
        registerButton.setSize(new Dimension(200, 200));
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        vPanel.add(label);
        vPanel.add(loginButton);
        vPanel.add(registerButton);

        vPanel.setBorder(new EmptyBorder(20,20,20,20));
        logOrReg.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()){
            case "login":
                Login login = new Login(logOrReg, socket);
                break;
            case "register":
                Register register = new Register(logOrReg, socket);
                break;
            default:
                System.out.println("Error switch Action Handler login or Registration");
        }
    }
}
