package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.server.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

public class SendListener implements ActionListener {

    /** 发送消息文本框 **/
    private JTextField sendMessageTextField;

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 输出流管道 **/
    private DataOutputStream outputToClient;


    public SendListener(JTextField sendMessageTextField, JTextArea textMessage, DataOutputStream outputToClient) {
        this.sendMessageTextField = sendMessageTextField;
        this.textMessage = textMessage;
        this.outputToClient = outputToClient;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            //向服务器发送消息
            outputToClient.writeUTF(sendMessageTextField.getText().trim() + '\n');
            textMessage.append("发送的消息：" + sendMessageTextField.getText().trim() + '\n');

            //输出后清空输入框
            sendMessageTextField.setText("");
        } catch (IOException e1) {
            System.err.println(e1);
        }
    }
}
