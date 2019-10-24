package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CloseListener implements ActionListener {


    private JTextArea txtMessage;
    private DataOutputStream dataOutputStreamToServer;
    private DataInputStream dataInputStreamFromServer;
    Socket socket;

    public CloseListener(JTextArea txtMessage, DataOutputStream dataOutputStreamToServer, DataInputStream dataInputStreamFromServer, Socket socket) {
        this.txtMessage = txtMessage;
        this.dataOutputStreamToServer = dataOutputStreamToServer;
        this.dataInputStreamFromServer = dataInputStreamFromServer;
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            dataOutputStreamToServer.close();
            dataInputStreamFromServer.close();
            socket.close();
            txtMessage.append("连接已断开～～～");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
