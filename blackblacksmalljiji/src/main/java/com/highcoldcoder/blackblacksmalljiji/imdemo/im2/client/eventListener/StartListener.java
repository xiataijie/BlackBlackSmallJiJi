package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.thread.MessageThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StartListener implements ActionListener {

    private JTextArea txtMessage ;
    private JTextField ipTextField ;
    private JTextField portTextField ;
    private DataOutputStream dataOutputStreamToServer;
    private DataInputStream dataInputStreamFromServer;

    Socket socket;

    public StartListener(JTextField ipTextField, JTextField portTextField, DataOutputStream dataOutputStreamToServer, DataInputStream dataInputStreamFromServer, JTextArea txtMessage) {
        this.ipTextField = ipTextField;
        this.portTextField = portTextField;
        this.dataOutputStreamToServer = dataOutputStreamToServer;
        this.dataInputStreamFromServer = dataInputStreamFromServer;
        this.txtMessage = txtMessage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String ip = ipTextField.getText();
        int port = Integer.parseInt(portTextField.getText());
        try {
            socket = new Socket(ip, port);

            dataInputStreamFromServer = new DataInputStream(socket.getInputStream());
            dataOutputStreamToServer = new DataOutputStream(socket.getOutputStream());

            MessageThread messageThread = new MessageThread(dataOutputStreamToServer, dataInputStreamFromServer, txtMessage,socket);
            Thread thread = new Thread(messageThread);
            thread.start();
        } catch (IOException e1) {
            txtMessage.append(e1.toString() + '\n');
        }
    }
}
