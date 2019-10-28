package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.client.thread;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * 消息收发线程
 */
public class MessageThread implements Runnable{

    /** 显示消息文本框 **/
    JTextArea txtMessage;

    Socket socket;

    private Map<String,Object> map;


    public MessageThread( Map<String,Object> map, JTextArea txtMessage, Socket socket) {
        this.map = map;
        this.txtMessage = txtMessage;
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            while(true){
                String fromStr = ((DataInputStream) map.get("inputStream")).readUTF();
                txtMessage.append("服务端发来消息：" +fromStr);

                if (Constant.SERVER_CLOSE_OK.equals(fromStr)) {
                    while (fromStr == null) {
                        ((DataInputStream) map.get("inputStream")).close();
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                if (socket != null) {
                    socket.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}