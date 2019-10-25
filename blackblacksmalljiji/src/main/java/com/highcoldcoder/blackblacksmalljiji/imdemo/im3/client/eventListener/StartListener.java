package com.highcoldcoder.blackblacksmalljiji.imdemo.im3.client.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im3.client.thread.MessageThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class StartListener implements ActionListener {

    private JTextArea txtMessage ;
    private JTextField ipTextField ;
    private JTextField portTextField ;
    private DataOutputStream dataOutputStreamToServer;
    private DataInputStream dataInputStreamFromServer;
    private Map<String,Object> map;
    Socket socket;

    public StartListener(Map<String,Object> map, JTextField ipTextField, JTextField portTextField, JTextArea txtMessage) {
        this.map = map;
        this.ipTextField = ipTextField;
        this.portTextField = portTextField;
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

            map.put("socket", socket);
            map.put("inputStream", dataInputStreamFromServer);
            map.put("outputStream", dataOutputStreamToServer);
            System.out.println(map.entrySet());

            MessageThread messageThread = new MessageThread(map, txtMessage,socket);
            Thread thread = new Thread(messageThread);
            thread.start();
        } catch (IOException e1) {
            txtMessage.append(e1.toString() + '\n');
        }
    }
}
