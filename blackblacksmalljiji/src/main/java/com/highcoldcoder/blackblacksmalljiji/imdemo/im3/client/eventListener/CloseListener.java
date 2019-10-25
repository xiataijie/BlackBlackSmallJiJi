package com.highcoldcoder.blackblacksmalljiji.imdemo.im3.client.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class CloseListener implements ActionListener {


    private JTextArea txtMessage;
    private Map<String,Object> map;
    Socket socket;

    public CloseListener(Map<String,Object> map, JTextArea txtMessage, Socket socket) {
        this.map = map;
        this.txtMessage = txtMessage;
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            ((DataInputStream) map.get("inputStream")).close();
            ((DataOutputStream) map.get("outputStream")).close();
            ((Socket) map.get("socket")).close();

            txtMessage.append("连接已断开～～～");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
