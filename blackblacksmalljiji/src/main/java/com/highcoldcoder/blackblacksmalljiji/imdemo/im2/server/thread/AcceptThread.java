package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.server.thread;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im2.server.ServerSocketMain;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptThread implements Runnable {

    ServerSocket serverSocket;

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 存储容器 **/
    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();
    private Map<String, DataOutputStream> dataOutputStreamMap = new HashMap();

    public AcceptThread(ServerSocket serverSocket, JTextArea textMessage, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap) {
        this.serverSocket = serverSocket;
        this.textMessage = textMessage;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("接收客户端请求：" + socket);

                socketMap.put(socket.getInetAddress() + ":" + socket.getPort(), socket);
                System.out.println("在线客户端数量：" + socketMap.size());

                textMessage.append("接收客户端请求：" + socket + '\n');
                textMessage.append("在线客户端数量：" + socketMap.size() + '\n');

                //启动消息读写子线程
                MessageThread messageThread = new MessageThread(textMessage, socket, socketMap, dataInputStreamMap, dataOutputStreamMap);
                Thread thread = new Thread(messageThread);
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
