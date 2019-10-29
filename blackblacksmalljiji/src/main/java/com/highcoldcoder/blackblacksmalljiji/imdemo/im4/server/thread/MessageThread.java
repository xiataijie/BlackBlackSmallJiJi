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
    private Map<String, Socket> socketMap ;
    private Map<String, DataInputStream> dataInputStreamMap ;
    private Map<String, DataOutputStream> dataOutputStreamMap ;
    private Map<String, Boolean> shutdownMap;

    private Socket socket;

    public MessageThread(JTextArea textMessage, Socket socket, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, DataOutputStream> dataOutputStreamMap, Map<String, Boolean> shutdownMap) {
        this.textMessage = textMessage;
        this.socket = socket;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.dataOutputStreamMap = dataOutputStreamMap;
        this.shutdownMap = shutdownMap;
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

            /** 服务器与客户端正常通信 **/
            while (!shutdownMap.get(Constant.SHUTDOWN)) {
                String fromClient = inputFromClient.readUTF();
                textMessage.append("客户端" + clientName + ":" + socket.getPort() + " 发来消息：" + fromClient + '\n');

                /**  收到客户端关闭请求，则等待数据接收完毕，然后断开接收管道流 **/
                if (fromClient.equals(Constant.CLIENT_CLOSE_ME)) {
                    outputToClient.writeUTF("服务端已停止接收消息，关闭成功!请留意标识符SERVER_CLOSE_OK确认...");
                    outputToClient.writeUTF(Constant.SERVER_CLOSE_OK);
                    socket.shutdownInput();

                    dataInputStreamMap.remove(String.valueOf(socket.getPort()));
                    dataOutputStreamMap.remove(String.valueOf(socket.getPort()));
                    socketMap.remove(String.valueOf(socket.getPort()));

                    System.out.println("socketMap容器 : " + socketMap.entrySet());

                    /** 客户端返回关闭确认 **/
                } else if (Constant.CLIENT_CLOSE_OK.equals(fromClient)) {
                    socket.shutdownInput();
                    socket.close();
                    textMessage.append("服务器收到CLIENT_CLOSE_OK,关闭socket,关闭输入流" + '\n');

                    dataInputStreamMap.remove(String.valueOf(socket.getPort()));
                    dataOutputStreamMap.remove(String.valueOf(socket.getPort()));
                    socketMap.remove(String.valueOf(socket.getPort()));
                    textMessage.append("服务器执行删除dataInputStreamMap容器,剩余：" + dataInputStreamMap.entrySet() + '\n');
                    textMessage.append("服务器执行删除dataOutputStreamMap容器,剩余：" + dataOutputStreamMap.entrySet() + '\n');
                    textMessage.append("服务器执行删除socketMap容器,剩余：" + socketMap.entrySet() + '\n');

                } else {
                    /** 服务端客户端消息 **/
                    outputToClient.writeUTF("服务端已收到消息:" + fromClient);
                }
            }
        } catch (EOFException e) {
            System.out.println("管道流关闭完成！" + '\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
