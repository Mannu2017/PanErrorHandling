package com.mannu;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import javax.swing.JMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainPage extends Thread {
	Connection con;
	String lognam;
	private JTable table;
	DefaultTableModel model;
	private JTextField frm;
	private JTextField to;
	JComboBox comboBox;
	
	public MainPage(String string, Connection connection) {
		this.con=connection;
		this.lognam=string;
	}
	public MainPage() {
		run();
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {
		JFrame frame=new JFrame("Pan Error Management"+"      Hello     "+lognam);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try {
					comboBox.removeAllItems();
					comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Operator"}));
					PreparedStatement ps=con.prepareStatement("select u.FullName from PanErrorUser u inner join PanErrorFlag f on u.UserId=f.userid where f.activ=2 order by FullName");
					ResultSet rs=ps.executeQuery();
					while (rs.next()) {
						comboBox.addItem(rs.getString(1));
					}
					ps.close();
					rs.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: "+e);
				}
				
			}
		});
		frame.setSize(807, 437);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Point p=new Point((int)width/3, (int)height/6);
        frame.setLocation(p);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 11, 781, 61);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		JButton btnGetInwordNo = new JButton("Get Inword No");
		btnGetInwordNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model=(DefaultTableModel) table.getModel();
				model.setRowCount(0);
				try {
					Object[] row=new Object[4];
					int slno = 0;
					PreparedStatement ps=con.prepareStatement("select distinct InwardNo from PanError order by InwardNo");
					ResultSet rs=ps.executeQuery();
					while (rs.next()) {
						slno=1+slno;
						row[0]=slno;
						row[1]=rs.getString(1);
						model.addRow(row);
					}
					ps.close();
					rs.close();
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Error: "+e2);
				}
				
			}
		});
		btnGetInwordNo.setBounds(10, 21, 113, 24);
		panel.add(btnGetInwordNo);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Operator"}));
		comboBox.setBounds(131, 21, 158, 24);
		panel.add(comboBox);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setBounds(294, 25, 46, 14);
		panel.add(lblFrom);
		
		frm = new JTextField();
		frm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (frm.getText().length() == 10)) {
		          event.consume();
		        }
			}
		});
		frm.setBounds(332, 20, 86, 24);
		panel.add(frm);
		frm.setColumns(10);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(428, 26, 46, 14);
		panel.add(lblTo);
		
		to = new JTextField();
		to.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (to.getText().length() == 10)) {
		          event.consume();
		        }
			}
		});
		to.setColumns(10);
		to.setBounds(459, 21, 86, 24);
		panel.add(to);
		
		JButton btnWorkAssign = new JButton("Work Assign");
		btnWorkAssign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (frm.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please Enter From Inward No");
						
					} else if(to.getText().isEmpty()){
						JOptionPane.showMessageDialog(null, "Please Enter To Inward No");
						
					}else if(comboBox.getSelectedItem().equals("Select Operator")){
						
						JOptionPane.showMessageDialog(null, "Please Select Operator Name");
					}else {	
						PreparedStatement ps=con.prepareStatement("select UserId from PanErrorUser where FullName='"+comboBox.getSelectedItem()+"'");
						ResultSet rs=ps.executeQuery();
						if (rs.next()) {
							PreparedStatement ps1=con.prepareStatement("insert into PanErrorWorkassin (Userid,FromInward,ToInward,AssignDate) values ('"+rs.getString(1)+"','"+frm.getText()+"','"+to.getText()+"',GETDATE())");
							ps1.execute();
						}
						frm.setText("");
						to.setText("");
						frm.requestFocus();
						ps.close();
						rs.close();
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Error: "+e2);
				}
				
			}
		});
		btnWorkAssign.setBounds(555, 22, 110, 23);
		panel.add(btnWorkAssign);
		
		JButton btnReport = new JButton("Report");
		btnReport.setBounds(675, 22, 89, 23);
		panel.add(btnReport);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 83, 781, 293);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Sl No", "User Id", "Name", "Date", "Count"
			}
		));
		scrollPane.setViewportView(table);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnAddNewOperator = new JMenu("Add New Operator");
		mnAddNewOperator.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				AddNewOperator ano=new AddNewOperator(con);
				ano.start();
				frame.dispose();
			}
		});
		menuBar.add(mnAddNewOperator);
		
		frame.setVisible(true);
	}
}
