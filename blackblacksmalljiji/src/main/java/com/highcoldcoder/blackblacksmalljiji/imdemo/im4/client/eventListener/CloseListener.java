package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.client.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class CloseListener implements ActionListener {


    private JTextArea txtMessage;
    private Map<String,Object> map;
    Socket socket;

    private static final String CLOSE_FLAG = "";

    public CloseListener(Map<String,Object> map, JTextArea txtMessage, Socket socket) {
        this.map = map;
        this.txtMessage = txtMessage;
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        DataOutputStream dataOutputStreamToServer = (DataOutputStream) map.get("outputStream");
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(Calendar.getInstance().getTime());

            // 客户端断线请求服务器，发送标识符”CLIENT_CLOSE_ME“，并停止发送消息

            dataOutputStreamToServer.writeUTF( Constant.CLIENT_CLOSE_ME + '\n');
            txtMessage.append( time + "客户端请求断线关闭：" + Constant.CLIENT_CLOSE_ME +'\n');

            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                //停止发送消息,关闭写入管道流
                if (dataOutputStreamToServer != null) {
                    dataOutputStreamToServer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
