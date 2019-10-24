package com.highcoldcoder.imserver.imdemo;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author xiaxia
 */
public class ServerSocketExample extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField txtXiaoXi;
    private JTextArea textMessage;
    private DataInputStream inputFromClient;
    private DataOutputStream outputToClient;
    ServerSocket serverSocket;
    Socket socket;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerSocketExample frame = new ServerSocketExample();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public ServerSocketExample() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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
        button.addActionListener(new buttonListener());

        JButton button_1 = new JButton("停止");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    inputFromClient.close();
                    outputToClient.close();
                    socket.close();
                    textMessage.append("连接已断开～～～");
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
        });
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
        txtXiaoXi.addActionListener(new sendListener());
        txtXiaoXi.setBounds(12, 5, 300, 25);
        panel_2.add(txtXiaoXi);
        txtXiaoXi.setColumns(10);
        JButton button_2 = new JButton("发送");
        button_2.setBounds(324, 5, 70, 25);
        panel_2.add(button_2);
        button_2.addActionListener(new sendListener());
    }
    private class sendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                outputToClient.writeUTF(txtXiaoXi.getText().trim() + '\n');    //向服务器发送消息
                textMessage.append("发送的消息：" + txtXiaoXi.getText().trim() +'\n');
                txtXiaoXi.setText("");        //输出后清空输入框
            } catch (IOException e1) {
                System.err.println(e1);
            }
        }
    }
    private class buttonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int port = Integer.parseInt(textField.getText());
            try {
                serverSocket = new ServerSocket(port);
                textMessage.append("服务器已启动～～    启动时间：" + new Date() +'\n');
                //监听连接请求
                socket = serverSocket.accept();
                textMessage.append("连接成功～～～" + '\n');
                Server_thread server3_thread = new Server_thread();
                Thread thread = new Thread(server3_thread);
                thread.start();
            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
    }

    //输入输出线程
    private class Server_thread implements Runnable{
        public Server_thread(){
        }
        public void run(){
            try {
                //IO流
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
                //获取客户端的名称 和 IP
                InetAddress inetAddress = socket.getInetAddress();
                String clientName = inetAddress.getHostName();
                String clientIP = inetAddress.getHostAddress();
                while(true){
                    String fromClient = inputFromClient.readUTF();
                    textMessage.append("客户端" + clientName + ";  " + clientIP + "发来消息："+fromClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







}