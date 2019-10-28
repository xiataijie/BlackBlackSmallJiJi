package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.thread;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

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

        //获取客户端的名称 和 IP
        InetAddress inetAddress = socket.getInetAddress();
        String clientName = inetAddress.getHostName();
        //IO流
        DataInputStream inputFromClient = null;
        DataOutputStream outputToClient = null;

        try {
            inputFromClient = new DataInputStream(socket.getInputStream());
            outputToClient = new DataOutputStream(socket.getOutputStream());
            dataInputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), inputFromClient);
            dataOutputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), outputToClient);

            while (true) {
                String fromClient = inputFromClient.readUTF();
                textMessage.append("客户端" + clientName + ":" + socket.getPort() + " 发来消息：" + fromClient);
                /**  收到客户端关闭请求，则等待数据接收完毕，然后断开接收管道流 **/
                if (fromClient.equals(Constant.CLIENT_CLOSE_ME)) {
                    while (fromClient == null) {
                        inputFromClient.close();

                        outputToClient.writeUTF("服务端已停止接收消息，关闭成功:" + Constant.SERVER_CLOSE_OK);
                        outputToClient.flush();
                        outputToClient.close();

                        socket.close();
                    }
                }
//            客户端收到断开完毕标识符”SERVER_CLOSE_OK“，然后关闭socket、流

                //服务端客户端消息
                outputToClient.writeUTF("服务端已收到消息:" + fromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                if (inputFromClient != null) {
                    inputFromClient.close();
                    dataInputStreamMap.remove(socket.getInetAddress() + ":" + socket.getPort());
                }

                if (outputToClient != null) {
                    outputToClient.close();
                    dataOutputStreamMap.remove(socket.getInetAddress() + ":" + socket.getPort());
                }

                if (socket != null) {
                    socket.close();
                    socketMap.remove(socket.getInetAddress() + ":" + socket.getPort());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
