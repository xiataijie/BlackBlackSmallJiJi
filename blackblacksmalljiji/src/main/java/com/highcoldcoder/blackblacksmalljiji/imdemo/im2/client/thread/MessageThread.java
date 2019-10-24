package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.thread;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 消息收发线程
 */
public class MessageThread implements Runnable{

    /** 显示消息文本框 **/
    private DataOutputStream dataOutputStreamToServer;

    /** 显示消息文本框 **/
    private DataInputStream dataInputStreamFromServer;

    /** 显示消息文本框 **/
    JTextArea txtMessage;

    Socket socket;


    public MessageThread(DataOutputStream dataOutputStreamToServer, DataInputStream dataInputStreamFromServer, JTextArea txtMessage, Socket socket) {
        this.dataOutputStreamToServer = dataOutputStreamToServer;
        this.dataInputStreamFromServer = dataInputStreamFromServer;
        this.txtMessage = txtMessage;
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            dataInputStreamFromServer = new DataInputStream(socket.getInputStream());
            dataOutputStreamToServer = new DataOutputStream(socket.getOutputStream());
            while(true){
                String fromStr = dataInputStreamFromServer.readUTF();
                txtMessage.append("服务端发来消息：" +fromStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}