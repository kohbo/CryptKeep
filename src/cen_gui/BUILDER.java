
package cen_gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BUILDER extends JFrame{
    
    private JPanel p1;
    private Cursor hand;
    
    private Scanner scan;
    private final int WIDTH = 4;
    private final int HEIGHT = 4;
    
    private JLabel userName;
    private JLabel passWord;
    private JButton send;
    private JTextField takeUserName;
    private JTextField takePassWord;
    
    public BUILDER() {
        
        super("Welcome to -Name of Group");
        p1 = new JPanel(new GridLayout(3, 2, 200, 20));
        
        userName = new JLabel("Enter Username");
        passWord = new JLabel("Enter Password");
        send = new JButton("Submit");
        takeUserName = new JTextField();
        takePassWord = new JTextField();
        hand = new Cursor(Cursor.HAND_CURSOR);
        
        p1.setCursor(hand);
        p1.add(userName);
        p1.add(takeUserName);
        p1.add(passWord);
        p1.add(takePassWord);
        p1.add(send);
        
        add(p1);
    }
}
