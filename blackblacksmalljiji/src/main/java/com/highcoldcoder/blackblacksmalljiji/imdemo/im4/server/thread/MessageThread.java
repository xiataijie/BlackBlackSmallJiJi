package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.thread;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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

        //获取客户端的名称 和 IP
        InetAddress inetAddress = socket.getInetAddress();
        String clientName = inetAddress.getHostName();
        //IO流
        DataInputStream inputFromClient = null;
        DataOutputStream outputToClient = null;

        try {
            inputFromClient = new DataInputStream(socket.getInputStream());
            outputToClient = new DataOutputStream(socket.getOutputStream());
            dataInputStreamMap.put(String.valueOf(socket.getPort()), inputFromClient);
            dataOutputStreamMap.put(String.valueOf(socket.getPort()), outputToClient);

            while (true) {
                String fromClient = inputFromClient.readUTF();
                textMessage.append("客户端" + clientName + ":" + socket.getPort() + " 发来消息：" + fromClient);

                /**  收到客户端关闭请求，则等待数据接收完毕，然后断开接收管道流 **/
                if (fromClient.equals(Constant.CLIENT_CLOSE_ME)) {

                    outputToClient.writeUTF("服务端已停止接收消息，关闭成功!请留意标识符SERVER_CLOSE_OK确认...");
                    outputToClient.writeUTF(Constant.SERVER_CLOSE_OK);

                    System.out.println("[start]服务器执行关闭socket、管道流...");
                    socket.shutdownInput();
                    System.out.println("[end]服务器执行关闭socket、管道流...");

                    System.out.println("[start]服务器执行删除容器里的socket、管道流...");
                    dataInputStreamMap.remove(String.valueOf(socket.getPort()));
                    dataOutputStreamMap.remove(String.valueOf(socket.getPort()));
                    socketMap.remove(String.valueOf(socket.getPort()));
                    System.out.println("[end]服务器执行删除容器里的socket、管道流...");

                    System.out.println("socketMap容器 : " + socketMap.entrySet());
                } else {
                    //服务端客户端消息
                    outputToClient.writeUTF("服务端已收到消息:" + fromClient);
                }
            }
        } catch (EOFException e) {
            System.out.println("管道流关闭完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
