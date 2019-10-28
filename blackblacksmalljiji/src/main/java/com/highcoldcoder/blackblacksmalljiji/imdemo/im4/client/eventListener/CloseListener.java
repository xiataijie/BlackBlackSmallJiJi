package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.client.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            dataOutputStreamToServer.writeUTF( Constant.CLIENT_CLOSE_ME);
            txtMessage.append( time + "客户端请求断线关闭：" + Constant.CLIENT_CLOSE_ME +'\n');

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
