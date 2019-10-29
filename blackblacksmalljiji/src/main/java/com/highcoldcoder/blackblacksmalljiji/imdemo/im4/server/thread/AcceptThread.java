package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.thread;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.ServerSocketMain;

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

    /** 是否关闭服务器标识，默认服务器启动 **/
    private boolean isShutDown = false;

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 存储容器 **/
    private Map<String, Socket> socketMap;
    private Map<String, DataInputStream> dataInputStreamMap;
    private Map<String, DataOutputStream> dataOutputStreamMap;
    private Map<String, Boolean> shutdownMap;

    public AcceptThread(ServerSocket serverSocket, JTextArea textMessage, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap, Map<String, Boolean> shutdownMap) {
        this.serverSocket = serverSocket;
        this.textMessage = textMessage;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
        this.shutdownMap = shutdownMap;
    }

    @Override
    public void run() {
        // 初始化线程池大小，并启动线程
        ThreadPool threadPool = new ThreadPool(4);
        while (!shutdownMap.get(Constant.SHUTDOWN)) {
            try {
                Socket socket = serverSocket.accept();
                /** 禁止关闭后，因为阻塞而最后一次请求被通过 **/
                if (!shutdownMap.get(Constant.SHUTDOWN)) {
                    socketMap.put(String.valueOf(socket.getPort()), socket);
                    textMessage.append("接收客户端请求：" + socket + '\n');
                    textMessage.append("在线客户端数量：" + socketMap.size() + '\n');
                    //任务存放至线程池
                    MessageThread messageThread = new MessageThread(textMessage, socket, socketMap, dataInputStreamMap, dataOutputStreamMap, shutdownMap);
                    threadPool.addTask(messageThread);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        try {
            serverSocket.close();
            textMessage.append("serverSocket 执行关闭..." + '\n');
            threadPool.close();
            textMessage.append("threadPool 线程池执行关闭..." + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
