package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.thread.AcceptThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

public class StartListener implements ActionListener {


    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 端口文本框 **/
    private JTextField portTextField;

    ServerSocket serverSocket;

    /** 存储容器 **/
    private Map<String, Socket> socketMap;
    private Map<String, DataInputStream> dataInputStreamMap;
    private Map<String, DataOutputStream> dataOutputStreamMap;
    private Map<String, Boolean> shutdownMap;

    public StartListener(JTextArea textMessage, JTextField portTextField, ServerSocket serverSocket, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap, Map<String, Boolean> shutdownMap) {
        this.textMessage = textMessage;
        this.portTextField = portTextField;
        this.serverSocket = serverSocket;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
        this.shutdownMap = shutdownMap;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int port = Integer.parseInt(portTextField.getText());
        try {
            serverSocket = new ServerSocket(port);
            textMessage.append("服务器已启动～～    启动时间：" + new Date() + '\n');
            shutdownMap.put(Constant.SHUTDOWN, false);
            AcceptThread acceptThread = new AcceptThread(serverSocket, textMessage, socketMap, dataInputStreamMap, dataOutputStreamMap, shutdownMap);
            Thread acceptThread0 = new Thread(acceptThread);
            acceptThread0.start();
            textMessage.append("连接成功～～～" + '\n');

        } catch (IOException e1) {
            System.out.println(e1);
        }
    }
}
