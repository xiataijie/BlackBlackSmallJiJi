package com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client;

import com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener.CloseListener;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener.SendListener;
import com.highcoldcoder.blackblacksmalljiji.imdemo.im2.client.eventListener.StartListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * IM通讯日记-2
 */
public class ClientSocketExample2 extends JFrame {

    private JPanel contentPane;
    private JTextField ipTextField = new JTextField();
    private JTextField portTextField = new JTextField();
    private JTextField messageTextField = new JTextField();
    private JTextArea txtMessage = new JTextArea();
    Socket socket;

    private Map<String,Object> map = new HashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientSocketExample2 frame = new ClientSocketExample2();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public ClientSocketExample2() {
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

        ipTextField.setText("localhost");
        ipTextField.setBounds(82, 10, 114, 19);
        panel.add(ipTextField);
        ipTextField.setColumns(10);
        JLabel label = new JLabel("端口：");
        label.setBounds(214, 12, 49, 15);
        panel.add(label);

        portTextField.setText("8000");
        portTextField.setBounds(265, 10, 114, 19);
        panel.add(portTextField);
        portTextField.setColumns(10);
        JButton button = new JButton("连接");
        button.setBounds(80, 50, 80, 20);
        panel.add(button);

        JButton button_1 = new JButton("断开");
        button_1.setBounds(270, 50, 80, 20);
        panel.add(button_1);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 110, 416, 120);
        contentPane.add(scrollPane);
        scrollPane.setViewportView(txtMessage);
        txtMessage.setBackground(Color.LIGHT_GRAY);
        txtMessage.setColumns(35);
        txtMessage.setRows(5);
        txtMessage.setTabSize(4);

        messageTextField.setBounds(20, 245, 300, 25);
        contentPane.add(messageTextField);
        messageTextField.setColumns(10);
        JButton button_2 = new JButton("发送");
        button_2.addActionListener(new SendListener(map,messageTextField, txtMessage));
        button_2.setBounds(330, 245, 60, 25);
        contentPane.add(button_2);

        button.addActionListener(new StartListener(map,ipTextField, portTextField,txtMessage));
        button_1.addActionListener(new CloseListener(map,txtMessage,socket));
        messageTextField.addActionListener(new SendListener(map,messageTextField, txtMessage));
    }


}