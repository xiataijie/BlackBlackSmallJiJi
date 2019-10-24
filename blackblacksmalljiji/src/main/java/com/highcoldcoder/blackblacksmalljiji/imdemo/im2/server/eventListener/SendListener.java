package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.server.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class SendListener implements ActionListener {

    /** 发送消息文本框 **/
    private JTextField sendMessageTextField;

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    Map<String, DataOutputStream> dataOutputStreamMap;


    public SendListener(JTextField sendMessageTextField, JTextArea textMessage, Map<String, DataOutputStream> dataOutputStreamMap) {
        this.sendMessageTextField = sendMessageTextField;
        this.textMessage = textMessage;
        this.dataOutputStreamMap = dataOutputStreamMap;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            //发送消息给所有客户端
            for (DataOutputStream dataOutputStream : dataOutputStreamMap.values()) {
                dataOutputStream.writeUTF(sendMessageTextField.getText().trim() + '\n');
                textMessage.append("发送的消息：" + sendMessageTextField.getText().trim() + '\n');
            }
            //输出后清空输入框
            sendMessageTextField.setText("");
        } catch (IOException e1) {
            System.err.println(e1);
        }
    }
}
