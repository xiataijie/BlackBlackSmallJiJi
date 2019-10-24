package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendListener implements ActionListener {

    private JTextField messageTextField;
    private JTextArea txtMessage ;
    private DataOutputStream dataOutputStreamToServer;

    public SendListener(JTextField messageTextField, JTextArea txtMessage, DataOutputStream dataOutputStreamToServer) {
        this.messageTextField = messageTextField;
        this.txtMessage = txtMessage;
        this.dataOutputStreamToServer = dataOutputStreamToServer;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(Calendar.getInstance().getTime());
            dataOutputStreamToServer.writeUTF(messageTextField.getText().trim() + '\n');
            txtMessage.append( time + "发送的消息：" + messageTextField.getText().trim() +'\n');
            messageTextField.setText("");
        } catch (IOException e1) {
            System.err.println(e1);
        }
    }
}