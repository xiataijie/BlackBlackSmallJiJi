package com.highcoldcoder.blackblacksmalljiji.imdemo.im3.server.thread;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MessageThread implements Runnable {

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 存储容器 **/
    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();
    private Map<String, DataOutputStream> dataOutputStreamMap = new HashMap();

    private Socket socket;

    public MessageThread(JTextArea textMessage, Socket socket, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap) {
        this.textMessage = textMessage;
        this.socket = socket;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
    }

    @Override
    public void run() {
        try {

            //获取客户端的名称 和 IP
            InetAddress inetAddress = socket.getInetAddress();
            String clientName = inetAddress.getHostName();

            //IO流
            DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
            dataInputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), inputFromClient);
            dataOutputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), outputToClient);

            while (true) {
                String fromClient = inputFromClient.readUTF();
                textMessage.append("客户端" + clientName + ":" + socket.getPort() + " 发来消息：" + fromClient);

                //服务端客户端消息
                outputToClient.writeUTF("服务端已收到消息:" + fromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
