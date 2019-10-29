package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.common.Constant;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener.CloseListener;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener.SendListener;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.eventListener.StartListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * IM通讯日记-2
 */
public class ServerSocketMain extends JFrame {

    /** 主面板 **/
    private JPanel contentPane;

    /** 端口文本框 **/
    private JTextField portTextField = new JTextField();;

    /** 发送消息文本框 **/
    private JTextField sendMessageTextField = new JTextField();;

    /** 显示消息文本框 **/
    private JTextArea textMessage = new JTextArea();;

    /** 输入流管道 **/
    private DataInputStream inputFromClient;

    /** 输出流管道 **/
    private DataOutputStream outputToClient;
    ServerSocket serverSocket;

    /** 存储容器 **/
    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();
    private Map<String, DataOutputStream> dataOutputStreamMap = new HashMap();
    private Map<String, Boolean> shutdownMap = new HashMap<>(1);

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerSocketMain frame = new ServerSocketMain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ServerSocketMain() {

        shutdownMap.put(Constant.SHUTDOWN, false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 426, 57);
        contentPane.add(panel);
        panel.setLayout(null);
        JLabel label = new JLabel("端口：");
        label.setBounds(12, 12, 39, 15);
        panel.add(label);
        portTextField.setText("8000");
        portTextField.setBounds(55, 10, 50, 19);
        panel.add(portTextField);
        portTextField.setColumns(10);

        JButton button = new JButton("启动");
        button.setBounds(200, 7, 70, 25);
        panel.add(button);
        button.addActionListener(new StartListener(textMessage, portTextField, serverSocket, socketMap, dataInputStreamMap, dataOutputStreamMap, shutdownMap));

        JButton button_1 = new JButton("停止");
        button_1.addActionListener(new CloseListener(textMessage, socketMap, dataInputStreamMap, shutdownMap));
        button_1.setBounds(300, 7, 70, 25);
        panel.add(button_1);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 69, 416, 170);
        contentPane.add(scrollPane);
        scrollPane.setViewportView(textMessage);

        textMessage.setTabSize(4);
        textMessage.setRows(11);
        textMessage.setColumns(35);
        textMessage.setBackground(Color.LIGHT_GRAY);
        textMessage.setText("123");

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 240, 428, 89);
        contentPane.add(panel_2);
        panel_2.setLayout(null);
        sendMessageTextField.addActionListener(new SendListener(sendMessageTextField, textMessage, dataOutputStreamMap));
        sendMessageTextField.setBounds(12, 5, 300, 25);

        panel_2.add(sendMessageTextField);
        sendMessageTextField.setColumns(10);
        JButton button_2 = new JButton("发送");
        button_2.setBounds(324, 5, 70, 25);
        panel_2.add(button_2);
        button_2.addActionListener(new SendListener(sendMessageTextField, textMessage, dataOutputStreamMap));
    }
}
