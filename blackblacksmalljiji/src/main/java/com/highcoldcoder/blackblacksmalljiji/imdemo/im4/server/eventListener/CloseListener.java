package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CloseListener implements ActionListener {

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 存储容器 **/
    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();


    public CloseListener(JTextArea textMessage, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap) {
        this.textMessage = textMessage;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (Map.Entry<String, Socket> entry : socketMap.entrySet()) {

            }
            textMessage.append("全部连接已断开～～～" + '\n');
        } catch (Exception e2) {
            System.out.print("断开出现异常：" + e2);
        }
    }
}
