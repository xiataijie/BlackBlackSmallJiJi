package com.highcoldcoder.imserver.imdemo.im3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author xiaxia
 */
public class ServerSocketExample3 extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField txtXiaoXi;
    private JTextArea textMessage;
    private DataInputStream inputFromClient;
    private DataOutputStream outputToClient;
    ServerSocket serverSocket;

    private Map<String, Socket> socketMap = new HashMap();
    private Map<String, DataInputStream> dataInputStreamMap = new HashMap();
    private Map<String, DataOutputStream> dataOutputStreamMap = new HashMap();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerSocketExample3 frame = new ServerSocketExample3();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ServerSocketExample3() {
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
        textField = new JTextField();
        textField.setText("8000");
        textField.setBounds(55, 10, 50, 19);
        panel.add(textField);
        textField.setColumns(10);

        JButton button = new JButton("启动");
        button.setBounds(200, 7, 70, 25);
        panel.add(button);
        button.addActionListener(new StartListener());

        JButton button_1 = new JButton("停止");
        button_1.addActionListener(new CloseListener());
        button_1.setBounds(300, 7, 70, 25);
        panel.add(button_1);
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 69, 416, 170);
        contentPane.add(panel_1);
        textMessage = new JTextArea();
        textMessage.setTabSize(4);
        textMessage.setRows(11);
        textMessage.setColumns(35);
        textMessage.setBackground(Color.LIGHT_GRAY);
        textMessage.setText("123");
        panel_1.add(textMessage);
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 240, 428, 89);
        contentPane.add(panel_2);
        panel_2.setLayout(null);
        txtXiaoXi = new JTextField();
        txtXiaoXi.addActionListener(new SendListener());
        txtXiaoXi.setBounds(12, 5, 300, 25);
        panel_2.add(txtXiaoXi);
        txtXiaoXi.setColumns(10);
        JButton button_2 = new JButton("发送");
        button_2.setBounds(324, 5, 70, 25);
        panel_2.add(button_2);
        button_2.addActionListener(new SendListener());
    }


    private class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                outputToClient.writeUTF(txtXiaoXi.getText().trim() + '\n');    //向服务器发送消息
                textMessage.append("发送的消息：" + txtXiaoXi.getText().trim() + '\n');
                txtXiaoXi.setText("");        //输出后清空输入框
            } catch (IOException e1) {
                System.err.println(e1);
            }
        }
    }

    private class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                for (Map.Entry<String, Socket> entry : socketMap.entrySet()) {

                    String key = entry.getKey();
                    Socket socket = entry.getValue();
                    dataInputStreamMap.get(key).close();
                    dataInputStreamMap.get(key).close();
                    socket.close();
                    textMessage.append(entry.getKey() + "输入管道连接已断开～～～" + '\n');
                    textMessage.append(entry.getKey() + "输出管道连接已断开～～～" + '\n');
                    textMessage.append(entry.getKey() + "Socket连接已断开～～～" + '\n');
                }
                textMessage.append("全部连接已断开～～～" + '\n');
            } catch (Exception e2) {
                System.out.print("断开出现异常：" + e2);
            }
        }
    }

    private class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int port = Integer.parseInt(textField.getText());
            try {
                serverSocket = new ServerSocket(port);
                textMessage.append("服务器已启动～～    启动时间：" + new Date() + '\n');
                AcceptThread acceptThread = new AcceptThread(serverSocket);
                Thread acceptThread0 = new Thread(acceptThread);
                acceptThread0.start();
                textMessage.append("连接成功～～～" + '\n');

            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
    }


    // 接收请求的线程
    private class AcceptThread implements Runnable {

        private ServerSocket serverSocket;

        public AcceptThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        public void run() {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("接收客户端请求：" + socket);
                    socketMap.put(socket.getInetAddress() + ":" + socket.getPort(), socket);
                    System.out.println("在线客户端数量：" + socketMap.size());

                    textMessage.append("接收客户端请求：" + socket + '\n');
                    textMessage.append("在线客户端数量：" + socketMap.size() + '\n');

                    //启动消息读写子线程
                    MessageThread server3_thread = new MessageThread(socket);
                    Thread thread = new Thread(server3_thread);
                    thread.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerSocketExample3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //输入输出线程
    private class MessageThread implements Runnable {
        private Socket socket;

        public MessageThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {

                //获取客户端的名称 和 IP
                InetAddress inetAddress = socket.getInetAddress();
                String clientName = inetAddress.getHostName();
                String clientIP = inetAddress.getHostAddress();

                //IO流
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
                dataInputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), inputFromClient);
                dataOutputStreamMap.put(socket.getInetAddress() + ":" + socket.getPort(), outputToClient);

                while (true) {
                    String fromClient = inputFromClient.readUTF();
                    textMessage.append("客户端" + clientName + ";  " + clientIP + "发来消息：" + fromClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
