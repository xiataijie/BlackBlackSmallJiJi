package com.highcoldcoder.blackblacksmalljiji.imdemo.im3.server.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im3.server.thread.AcceptThread;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im3.server.thread.ThreadPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StartListener implements ActionListener {


    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 端口文本框 **/
    private JTextField portTextField;

    ServerSocket serverSocket;

    /** 存储容器 **/
    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();
    private Map<String, DataOutputStream> dataOutputStreamMap = new HashMap();

    public StartListener(JTextArea textMessage, JTextField portTextField, ServerSocket serverSocket, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap) {
        this.textMessage = textMessage;
        this.portTextField = portTextField;
        this.serverSocket = serverSocket;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int port = Integer.parseInt(portTextField.getText());
        try {
            serverSocket = new ServerSocket(port);
            textMessage.append("服务器已启动～～    启动时间：" + new Date() + '\n');

            AcceptThread acceptThread = new AcceptThread(serverSocket, textMessage, socketMap, dataInputStreamMap, dataOutputStreamMap);
            Thread acceptThread0 = new Thread(acceptThread);
            acceptThread0.start();
            textMessage.append("连接成功～～～" + '\n');

        } catch (IOException e1) {
            System.out.println(e1);
        }
    }
}
