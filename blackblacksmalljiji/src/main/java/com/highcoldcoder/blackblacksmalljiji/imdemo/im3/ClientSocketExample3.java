package com.highcoldcoder.imserver.imdemo.im3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author xiaxia
 */
public class ClientSocketExample3 extends JFrame {

    private JPanel contentPane;
    private JTextField textField_IP;
    private JTextField textField_Port;
    private JTextField txtXiaoxi;
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    JTextArea txtMessage;
    Socket socket;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientSocketExample3 frame = new ClientSocketExample3();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public ClientSocketExample3() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setToolTipText("Client");
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(12, 5, 415, 120);
        contentPane.add(panel);
        panel.setLayout(null);
        JLabel lblip = new JLabel("服务器IP：");
        lblip.setBounds(12, 12, 65, 15);
        panel.add(lblip);
        textField_IP = new JTextField();
        textField_IP.setText("localhost");
        textField_IP.setBounds(82, 10, 114, 19);
        panel.add(textField_IP);
        textField_IP.setColumns(10);
        JLabel label = new JLabel("端口：");
        label.setBounds(214, 12, 49, 15);
        panel.add(label);
        textField_Port = new JTextField();
        textField_Port.setText("8000");
        textField_Port.setBounds(265, 10, 114, 19);
        panel.add(textField_Port);
        textField_Port.setColumns(10);
        JButton button = new JButton("连接");
        button.setBounds(80, 50, 80, 20);
        panel.add(button);
        JButton button_1 = new JButton("断开");
        button_1.addActionListener(new CloseListener());
        button_1.setBounds(270, 50, 80, 20);
        panel.add(button_1);
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(12, 100, 415, 118);
        contentPane.add(panel_1);
        txtMessage = new JTextArea();
        txtMessage.setBackground(Color.LIGHT_GRAY);
        txtMessage.setColumns(35);
        txtMessage.setRows(10);
        txtMessage.setTabSize(4);

        panel_1.add(txtMessage);
        txtXiaoxi = new JTextField();
        txtXiaoxi.addActionListener(new SendListener());

        txtXiaoxi.setBounds(20, 245, 300, 25);
        contentPane.add(txtXiaoxi);
        txtXiaoxi.setColumns(10);
        JButton button_2 = new JButton("发送");
        button_2.addActionListener(new SendListener());
        button_2.setBounds(330, 245, 60, 25);
        contentPane.add(button_2);
        button.addActionListener(new StartListener());
    }
    private class SendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //得到当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //  设置日期格式
                String time = df.format(Calendar.getInstance().getTime());      //得到时间
                toServer.writeUTF(txtXiaoxi.getText().trim() + '\n');    //向服务器发送消息
                txtMessage.append( time + "发送的消息：" + txtXiaoxi.getText().trim() +'\n');
                txtXiaoxi.setText("");        //输出后清空输入框
            } catch (IOException e1) {
                System.err.println(e1);
            }
        }
    }

    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.close();
                fromServer.close();
                socket.close();
                txtMessage.append("连接已断开～～～");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class StartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String ip = textField_IP.getText();
            int port = Integer.parseInt(textField_Port.getText());
            try {
                socket = new Socket(ip, port);
                MessageThread client3_thread = new MessageThread();
                Thread thread = new Thread(client3_thread);
                thread.start();
            } catch (IOException e1) {
                txtMessage.append(e1.toString() + '\n');
            }
        }
    }
    //消息收发线程
    public class MessageThread implements Runnable{

        @Override
        public void run(){
            try {
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());
                while(true){
                    String fromStr = fromServer.readUTF();
                    txtMessage.append("服务端发来消息：" +fromStr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}