package com.mannu;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddNewOperator extends Thread{
	Connection connection;
	private JTextField user;
	private JPasswordField pass;
	private JPasswordField confpass;
	private JTextField name;
	private JTextField mob;
	JComboBox comboBox;
	
	public AddNewOperator(Connection con) {
		this.connection=con;
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {
		JFrame frame =new JFrame("New Operator Registration");
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        double width = screenSize.getWidth();
	        double height = screenSize.getHeight();
	        Point p=new Point((int)width/3, (int)height/6);
	        frame.setLocation(p);
		frame.setSize(302, 309);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 11, 277, 258);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		JLabel lblUserId = new JLabel("User Id:");
		lblUserId.setBounds(44, 14, 55, 14);
		panel.add(lblUserId);
		
		user = new JTextField();
		user.setBounds(109, 8, 146, 26);
		panel.add(user);
		user.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(33, 47, 66, 14);
		panel.add(lblPassword);
		
		JLabel lblConform = new JLabel("Conform:");
		lblConform.setBounds(36, 80, 66, 14);
		panel.add(lblConform);
		
		pass = new JPasswordField();
		pass.setBounds(109, 41, 146, 26);
		panel.add(pass);
		
		confpass = new JPasswordField();
		confpass.setBounds(109, 74, 146, 26);
		panel.add(confpass);
		
		JLabel lblFullName = new JLabel("Full Name:");
		lblFullName.setBounds(33, 113, 66, 14);
		panel.add(lblFullName);
		
		name = new JTextField();
		name.setColumns(10);
		name.setBounds(109, 107, 146, 26);
		panel.add(name);
		
		JLabel lblMobile = new JLabel("Mobile:");
		lblMobile.setBounds(43, 146, 56, 14);
		panel.add(lblMobile);
		
		mob = new JTextField();
		mob.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (mob.getText().length() == 10)) {
		          event.consume();
		        }
			}
		});
		mob.setColumns(10);
		mob.setBounds(109, 140, 146, 26);
		panel.add(mob);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter User Id");
				}else if (pass.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Password");
				}else if (confpass.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Conform Password");
				}else if (name.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Full Name");
				}else if (mob.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Mobile No");
				}else if (!pass.getText().equals(confpass.getText())) {
					JOptionPane.showMessageDialog(null, "Password and conform Password Not Match");
				}else {
					try {
						String nwpas=user.getText()+pass.getText();
			               MessageDigest md = MessageDigest.getInstance("MD5");
			                md.update(nwpas.getBytes());
			                byte[] bytes = md.digest();
			                StringBuilder sb = new StringBuilder();
			            for(int i=0; i< bytes.length ;i++)
			            {
			                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			            }
			            PreparedStatement ps=connection.prepareStatement("insert into PanErrorUser (UserId,UPassword,FullName,email) values('"+user.getText()+"','"+sb.toString()+"','"+name.getText()+"','"+mob.getText()+"')");
			            ps.execute();
			            int typ = 0;
			            if(comboBox.getSelectedItem().equals("Admin")) {
			            	typ=1;
			            }else if (comboBox.getSelectedItem().equals("Operator")) {
							typ=2;
						}
			            
			            PreparedStatement pStatement=connection.prepareStatement("insert into PanErrorFlag(userid,activ) values ('"+user.getText()+"',"+typ+")");
			            pStatement.execute();
			            
			            user.setText("");
			            pass.setText("");
			            confpass.setText("");
			            name.setText("");
			            mob.setText("");
			            user.requestFocus();
			            
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error: "+e2);
					}  
				}
			}
		});
		btnAdd.setBounds(31, 213, 89, 26);
		panel.add(btnAdd);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainPage mPage=new MainPage();
				frame.dispose();
			}
		});
		btnClose.setBounds(149, 213, 89, 26);
		panel.add(btnClose);
		
		JLabel lblRole = new JLabel("Role:");
		lblRole.setBounds(45, 179, 46, 14);
		panel.add(lblRole);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Admin", "Operator"}));
		comboBox.setBounds(109, 175, 146, 26);
		panel.add(comboBox);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
