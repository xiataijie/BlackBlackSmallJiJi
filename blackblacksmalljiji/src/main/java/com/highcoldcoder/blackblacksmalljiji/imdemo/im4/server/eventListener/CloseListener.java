package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Map;

public class CloseListener implements ActionListener {

    /** 显示消息文本框 **/
    private JTextArea textMessage;

    /** 存储容器 **/
    private Map<String, Socket> socketMap;
    private Map<String, DataInputStream> dataInputStreamMap;
    private Map<String, Boolean> shutdownMap;


    public CloseListener(JTextArea textMessage, Map<String, Socket> socketMap, Map<String, DataInputStream> dataInputStreamMap, Map<String, Boolean> shutdownMap) {
        this.textMessage = textMessage;
        this.socketMap = socketMap;
        this.dataInputStreamMap = dataInputStreamMap;
        this.shutdownMap = shutdownMap;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            shutdownMap.put(Constant.SHUTDOWN, true);
            textMessage.append("服务器正在关闭中......SHUTDOWN = " + shutdownMap.get(Constant.SHUTDOWN) + '\n');
        } catch (Exception e2) {
            System.out.print("断开出现异常：" + e2);
        }
    }
}
