package com.mannu;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.channels.SelectableChannel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OperatorPage extends Thread{
	Connection con;
	String lognam;
	double w,h;
	JScrollPane imgscr;
	JLabel img;
	private JTable fnamtab;
	DefaultTableModel model;
	JLabel lblTxtFieldFocus;
	String userid;
	private JScrollPane scrollPane_1;
	private JTable inwardtab;
	private JLabel lblSetPath;
	private JTextField pdfpath;
	private JLabel lblInwardNo;
	private JTextField inwardno;
	private JPanel panel;
	private JLabel lblAppFirstName;
	private JTextField afnam;
	private JTextField sfnam;
	private JLabel lblSamFirstName;
	private JLabel lblAppMiddleName;
	private JTextField amnam;
	private JLabel lblSamMiddleName;
	private JTextField smnam;
	private JLabel lblAppLastName;
	private JTextField alnam;
	private JLabel lblSamLastName;
	private JTextField slnam;
	private JLabel lblAppNameOn;
	private JTextField afullnam;
	private JTextField sfullnam;
	private JLabel lblSamNameOn;
	private JLabel lblAppDob;
	private JTextField adob;
	private JTextField sdob;
	private JLabel lblSamDob;
	private JButton btnSetPath;
	private JButton btnPrevious;
	private JButton btnNext;
	private JLabel lblStatus;
	private JComboBox error;
	private JButton btnSave;
	private JButton btnExit;
	private JLabel lblSaveAlts;
	private JLabel lblSaveAlts1;
	String finward,tinward;
	DefaultTableModel model2;
	String filepath;
	int inselrow,fiselrow;
	
	
	
	public OperatorPage(Connection connection, String string, String string2) {
		this.con=connection;
		this.lognam=string;
		this.userid=string2;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {
		JFrame frame=new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try {
					PreparedStatement ps=con.prepareStatement("select FromInward,ToInward from PanErrorWorkassin where userid='"+userid+"'");
					ResultSet rs=ps.executeQuery();
					if (rs.next()) {
						JOptionPane.showMessageDialog(null, "Your Work Assign "+rs.getString(1)+" to "+rs.getString(2)+" Inward No");
						finward=rs.getString(1);
						tinward=rs.getString(2);
					} else {
						JOptionPane.showMessageDialog(null, "Your Work not Assigned\nPlease contact with admin");
						System.exit(0);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: "+e);
				}
			}
		});
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		Toolkit tk=Toolkit.getDefaultToolkit();
		double w=tk.getScreenSize().getWidth();
		double h=tk.getScreenSize().getHeight();
		frame.setSize((int)w, (int)h);
	//	frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		imgscr=new JScrollPane();
		imgscr.setBounds(1, 1, (int)w-450, (int)h);
		frame.getContentPane().add(imgscr);

		img = new JLabel();
		imgscr.setViewportView(img);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int)w-440, 8, 210, 164);
		frame.getContentPane().add(scrollPane);
		
		fnamtab = new JTable();
		fnamtab.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File Name"
			}
		));
		scrollPane.setViewportView(fnamtab);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds((int)w-220, 8, 210, 164);
		frame.getContentPane().add(scrollPane_1);
		
		inwardtab = new JTable();
		inwardtab.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				inselrow=inwardtab.getSelectedRow();
	            
	            inwardno.setText((String) model.getValueAt(inselrow, 0));
	            
	            int selval = 0;
				for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
					String val=inwardno.getText().trim();
					String tval=fnamtab.getValueAt(row, 0).toString();
				 	if (tval.matches(val+"(.*)")) {
						System.out.println("Enter Row Number: "+row);
							selval=row;
					}
				}
				
				if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
					System.out.println("DD: "+selval);
					fnamtab.setRowSelectionInterval(selval, selval);
					String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
					try {
						PDDocument document = PDDocument.load(new File(selpdf));
						PDFRenderer pdfRenderer = new PDFRenderer(document);
						for (int i = 0; i < document.getNumberOfPages(); i++) {
							BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
							ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
						}
						document.close();
						img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error: "+e2);
					}
					
					try {
						PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
						ResultSet rs1=ps1.executeQuery();
						if (rs1.next()) {
							if (!rs1.getString(1).equals(rs1.getString(2))) {
								afnam.setText(rs1.getString(1));
								sfnam.setText(rs1.getString(2));
								afnam.setEditable(true);
								sfnam.setEditable(true);
								afnam.setForeground(Color.RED);
								sfnam.setForeground(Color.RED);
							} else if(rs1.getString(1).equals(rs1.getString(2))){
								afnam.setText(rs1.getString(1));
								sfnam.setText(rs1.getString(2));
								afnam.setEditable(false);
								sfnam.setEditable(false);
								afnam.setForeground(Color.BLACK);
								sfnam.setForeground(Color.BLACK);
							}
							
							if (!rs1.getString(3).equals(rs1.getString(4))) {
								amnam.setText(rs1.getString(3));
								smnam.setText(rs1.getString(4));
								amnam.setEditable(true);
								smnam.setEditable(true);
								amnam.setForeground(Color.RED);
								smnam.setForeground(Color.RED);
								
							} else if(rs1.getString(3).equals(rs1.getString(4))) {
								amnam.setText(rs1.getString(3));
								smnam.setText(rs1.getString(4));
								amnam.setEditable(false);
								smnam.setEditable(false);
								amnam.setForeground(Color.BLACK);
								smnam.setForeground(Color.BLACK);
							}
							
							if (!rs1.getString(5).equals(rs1.getString(6))) {
								alnam.setText(rs1.getString(5));
								slnam.setText(rs1.getString(6));
								alnam.setEditable(true);
								slnam.setEditable(true);
								alnam.setForeground(Color.RED);
								slnam.setForeground(Color.RED);
							} else if(rs1.getString(5).equals(rs1.getString(6))){
								alnam.setText(rs1.getString(5));
								slnam.setText(rs1.getString(6));
								alnam.setEditable(false);
								slnam.setEditable(false);
								alnam.setForeground(Color.BLACK);
								slnam.setForeground(Color.BLACK);
							}
							
							if (!rs1.getString(7).equals(rs1.getString(8))) {
								afullnam.setText(rs1.getString(7));
								sfullnam.setText(rs1.getString(8));
								afullnam.setEditable(true);
								sfullnam.setEditable(true);
								afullnam.setForeground(Color.RED);
								sfullnam.setForeground(Color.RED);
							} else if(rs1.getString(7).equals(rs1.getString(8))){
								afullnam.setText(rs1.getString(7));
								sfullnam.setText(rs1.getString(8));
								afullnam.setEditable(false);
								sfullnam.setEditable(false);
								afullnam.setForeground(Color.BLACK);
								sfullnam.setForeground(Color.BLACK);
							}
							
							if (!rs1.getString(9).equals(rs1.getString(10))) {
								adob.setText(rs1.getString(9));
								sdob.setText(rs1.getString(10));
								adob.setEditable(true);
								sdob.setEditable(true);
								adob.setForeground(Color.RED);
								sdob.setForeground(Color.RED);
								
							} else if(rs1.getString(9).equals(rs1.getString(10))) {
								adob.setText(rs1.getString(9));
								sdob.setText(rs1.getString(10));
								adob.setEditable(false);
								sdob.setEditable(false);
								adob.setForeground(Color.BLACK);
								sdob.setForeground(Color.BLACK);
								
							}	
						}
						
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error: "+e2);
					}
				}else {
					JOptionPane.showMessageDialog(null, "File Not Found");
				}
			}
		});
		inwardtab.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"InwardNo"
			}
		));
		scrollPane_1.setViewportView(inwardtab);
		
		lblSetPath = new JLabel("Set Path");
		lblSetPath.setBounds((int)w-440, 179, 62, 14);
		frame.getContentPane().add(lblSetPath);
		
		pdfpath = new JTextField();
		pdfpath.setBounds((int)w-440, 204, 202, 28);
		frame.getContentPane().add(pdfpath);
		pdfpath.setColumns(10);
		
		lblInwardNo = new JLabel("Inward No");
		lblInwardNo.setBounds((int)w-220, 179, 62, 14);
		frame.getContentPane().add(lblInwardNo);
		
		inwardno = new JTextField();
		inwardno.setEditable(false);
		inwardno.setColumns(10);
		inwardno.setBounds((int)w-220, 204, 202, 28);
		frame.getContentPane().add(inwardno);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "MetaData panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds((int)w-440, 237, 429, 285);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		lblAppFirstName = new JLabel("App First Name:");
		lblAppFirstName.setBounds(10, 18, 128, 14);
		panel.add(lblAppFirstName);
		
		afnam = new JTextField();
		//afnam.setForeground(Color.RED);
		afnam.setBounds(10, 38, 184, 28);
		panel.add(afnam);
		afnam.setColumns(10);
		
		sfnam = new JTextField();
		sfnam.setColumns(10);
		sfnam.setBounds(222, 38, 184, 28);
		panel.add(sfnam);
		
		lblSamFirstName = new JLabel("Sam First Name:");
		lblSamFirstName.setBounds(222, 18, 128, 14);
		panel.add(lblSamFirstName);
		
		lblAppMiddleName = new JLabel("App Middle Name:");
		lblAppMiddleName.setBounds(10, 69, 128, 14);
		panel.add(lblAppMiddleName);
		
		amnam = new JTextField();
		amnam.setColumns(10);
		amnam.setBounds(10, 89, 184, 28);
		panel.add(amnam);
		
		lblSamMiddleName = new JLabel("Sam Middle Name:");
		lblSamMiddleName.setBounds(222, 69, 128, 14);
		panel.add(lblSamMiddleName);
		
		smnam = new JTextField();
		smnam.setColumns(10);
		smnam.setBounds(222, 89, 184, 28);
		panel.add(smnam);
		
		lblAppLastName = new JLabel("App Last Name:");
		lblAppLastName.setBounds(10, 120, 128, 14);
		panel.add(lblAppLastName);
		
		alnam = new JTextField();
		alnam.setColumns(10);
		alnam.setBounds(10, 140, 184, 28);
		panel.add(alnam);
		
		lblSamLastName = new JLabel("Sam Last Name:");
		lblSamLastName.setBounds(222, 120, 128, 14);
		panel.add(lblSamLastName);
		
		slnam = new JTextField();
		slnam.setColumns(10);
		slnam.setBounds(222, 140, 184, 28);
		panel.add(slnam);
		
		lblAppNameOn = new JLabel("App Name On Card");
		lblAppNameOn.setBounds(10, 174, 128, 14);
		panel.add(lblAppNameOn);
		
		afullnam = new JTextField();
		afullnam.setColumns(10);
		afullnam.setBounds(10, 194, 184, 28);
		panel.add(afullnam);
		
		sfullnam = new JTextField();
		sfullnam.setColumns(10);
		sfullnam.setBounds(222, 194, 184, 28);
		panel.add(sfullnam);
		
		lblSamNameOn = new JLabel("Sam Name On Card");
		lblSamNameOn.setBounds(222, 174, 128, 14);
		panel.add(lblSamNameOn);
		
		lblAppDob = new JLabel("App DOB");
		lblAppDob.setBounds(10, 227, 128, 14);
		panel.add(lblAppDob);
		
		adob = new JTextField();
		adob.setColumns(10);
		adob.setBounds(10, 247, 184, 28);
		panel.add(adob);
		
		sdob = new JTextField();
		sdob.setColumns(10);
		sdob.setBounds(222, 247, 184, 28);
		panel.add(sdob);
		
		lblSamDob = new JLabel("Sam DOB");
		lblSamDob.setBounds(222, 227, 128, 14);
		panel.add(lblSamDob);
		
		btnSetPath = new JButton("Set Path");
		btnSetPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pdfpath.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please set PDF Path");
				} else {
				
				File tmp=new File("C:\\temp\\");
				if(!tmp.exists()) {
					tmp.mkdir();
				}
				pdfpath.setEditable(false);
				
				model=(DefaultTableModel) inwardtab.getModel();
				model2=(DefaultTableModel) fnamtab.getModel();
				model.setRowCount(0);
				model2.setRowCount(0);
				try {
					Object[] irow=new Object[1];
					PreparedStatement ps=con.prepareStatement("select InwardNo from panerror where InwardNo between "+finward+" and "+tinward+" and Flag='P'");
					ResultSet rs=ps.executeQuery();
					while (rs.next()) {
						irow[0]=rs.getString(1);
						model.addRow(irow);
					}
					
					if (model.getRowCount()==0) {
						JOptionPane.showMessageDialog(null, "No Pending Work ");
					} else {
			            filepath=pdfpath.getText();
			            File folder=new File(filepath);
			            File[] listofFiles=folder.listFiles();
			            Object[] frow = new Object[1];
			            for (int i = 0; i < listofFiles.length; i++) {
			            	 if (listofFiles[i].getName().endsWith(".pdf")) {
			            			 frow[0]=listofFiles[i].getName();
				                        model2.addRow(frow);
			            	 }else if (listofFiles[i].getName().endsWith(".PDF")) {
			            		 frow[0]=listofFiles[i].getName();
			                        model2.addRow(frow);
							}
			            }
			            
			            inwardtab.setRowSelectionInterval(0, 0);
			            
			            inselrow=inwardtab.getSelectedRow();
			            
			            inwardno.setText((String) model.getValueAt(inselrow, 0));
			            
			            int selval = 0;
						for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
							String val=inwardno.getText().trim();
							String tval=fnamtab.getValueAt(row, 0).toString();
						 	if (tval.matches(val+"(.*)")) {
								System.out.println("Enter Row Number: "+row);
									selval=row;
							}
						}
						
						if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
							System.out.println("DD: "+selval);
							fnamtab.setRowSelectionInterval(selval, selval);
							String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
							try {
								PDDocument document = PDDocument.load(new File(selpdf));
								PDFRenderer pdfRenderer = new PDFRenderer(document);
								for (int i = 0; i < document.getNumberOfPages(); i++) {
									BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
									ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
								}
								document.close();
								img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
							
							try {
								PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
								ResultSet rs1=ps1.executeQuery();
								if (rs1.next()) {
									if (!rs1.getString(1).equals(rs1.getString(2))) {
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(true);
										sfnam.setEditable(true);
										afnam.setForeground(Color.RED);
										sfnam.setForeground(Color.RED);
									} else if(rs1.getString(1).equals(rs1.getString(2))){
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(false);
										sfnam.setEditable(false);
										afnam.setForeground(Color.BLACK);
										sfnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(true);
										smnam.setEditable(true);
										amnam.setForeground(Color.RED);
										smnam.setForeground(Color.RED);
										
									} else if(rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(false);
										smnam.setEditable(false);
										amnam.setForeground(Color.BLACK);
										smnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(5).equals(rs1.getString(6))) {
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(true);
										slnam.setEditable(true);
										alnam.setForeground(Color.RED);
										slnam.setForeground(Color.RED);
									} else if(rs1.getString(5).equals(rs1.getString(6))){
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(false);
										slnam.setEditable(false);
										alnam.setForeground(Color.BLACK);
										slnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(7).equals(rs1.getString(8))) {
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(true);
										sfullnam.setEditable(true);
										afullnam.setForeground(Color.RED);
										sfullnam.setForeground(Color.RED);
									} else if(rs1.getString(7).equals(rs1.getString(8))){
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(false);
										sfullnam.setEditable(false);
										afullnam.setForeground(Color.BLACK);
										sfullnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(true);
										sdob.setEditable(true);
										adob.setForeground(Color.RED);
										sdob.setForeground(Color.RED);
										
									} else if(rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(false);
										sdob.setEditable(false);
										adob.setForeground(Color.BLACK);
										sdob.setForeground(Color.BLACK);
										
									}	
								}
								
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
						}else {
							JOptionPane.showMessageDialog(null, "File Not Found");
						}
					}
						
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Error: "+e2);
				}
			}
			}
		});
		btnSetPath.setBounds((int)w-430, 572, 94, 28);
		frame.getContentPane().add(btnSetPath);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String imgfnam=img.getIcon().toString();
				imgfnam=imgfnam.substring(0, imgfnam.length()-4);
				System.out.println("Path: "+imgfnam);
				String lno=imgfnam.substring(imgfnam.length()-1);
				int nn=Integer.parseInt(lno)-1;
				
				String nexnam="C:\\temp\\"+inwardno.getText()+nn+".png";
				File nexfil=new File(nexnam);
				System.out.println("Next Path: "+nexnam);
				
				if (!nexfil.exists()) {
					JOptionPane.showMessageDialog(null, "This is First Page");
				} else if (nexfil.exists()) {
					img.setIcon(new ImageIcon(nexnam));
				}
			}
		});
		btnPrevious.setMnemonic(KeyEvent.VK_P);
		btnPrevious.setBounds((int)w-330, 572, 94, 28);
		frame.getContentPane().add(btnPrevious);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String imgfnam=img.getIcon().toString();
				imgfnam=imgfnam.substring(0, imgfnam.length()-4);
				System.out.println("Path: "+imgfnam);
				String lno=imgfnam.substring(imgfnam.length()-1);
				int nn=Integer.parseInt(lno)+1;
				
				String nexnam="C:\\temp\\"+inwardno.getText()+nn+".png";
				File nexfil=new File(nexnam);
				System.out.println("Next Path: "+nexnam);
				
				if (!nexfil.exists()) {
					JOptionPane.showMessageDialog(null, "This is Last Page");
				} else if (nexfil.exists()) {
					img.setIcon(new ImageIcon(nexnam));
				}
			}
			
		});
		btnNext.setMnemonic(KeyEvent.VK_N);
		btnNext.setBounds((int)w-230, 572, 94, 28);
		frame.getContentPane().add(btnNext);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setBounds((int)w-410, 540, 61, 14);
		frame.getContentPane().add(lblStatus);
		
		error = new JComboBox();
		error.setModel(new DefaultComboBoxModel(new String[] {"First Name Entry Wrong", "Middle Name Entry Wrong", "Last Name Entry Wrong", "Multiple Error", "Sam Error", "Pdf Not Found", "Ok Case"}));
		error.setBounds((int)w-360, 533, 224, 28);
		frame.getContentPane().add(error);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (error.getSelectedItem().equals("Ok Case")) {
						if (!afnam.getText().equals(sfnam.getText()) || !sfnam.getText().equals(afnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct First Name");
						} else if(!amnam.getText().equals(smnam.getText()) || !smnam.getText().equals(amnam.getText())){
							JOptionPane.showMessageDialog(null, "Please check and Correct Middle Name");
						}else if (!alnam.getText().equals(slnam.getText()) || !slnam.getText().equals(alnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Last Name");
						}else if (!afullnam.getText().equals(sfullnam.getText()) || !sfullnam.getText().equals(afullnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Full Name");
						}else if (!adob.getText().equals(sdob.getText()) || !sdob.getText().equals(adob.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct DOB");
						}else {
						
						PreparedStatement pps=con.prepareStatement("update PanError set Flag='B',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
						pps.execute();
						PreparedStatement pss=con.prepareStatement("update pandetail set FileGenerationFlag='B' where InhouseRefno="+inwardno.getText());
						pss.execute();
						model.removeRow(inwardtab.getSelectedRow());
						
						inwardtab.setRowSelectionInterval(0, 0);
			            
			            inselrow=inwardtab.getSelectedRow();
			            
			            inwardno.setText((String) model.getValueAt(inselrow, 0));
			            
			            int selval = 0;
						for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
							String val=inwardno.getText().trim();
							String tval=fnamtab.getValueAt(row, 0).toString();
						 	if (tval.matches(val+"(.*)")) {
								System.out.println("Enter Row Number: "+row);
									selval=row;
							}
						}
						
						if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
							System.out.println("DD: "+selval);
							fnamtab.setRowSelectionInterval(selval, selval);
							String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
							try {
								PDDocument document = PDDocument.load(new File(selpdf));
								PDFRenderer pdfRenderer = new PDFRenderer(document);
								for (int i = 0; i < document.getNumberOfPages(); i++) {
									BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
									ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
								}
								document.close();
								img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
							
							try {
								PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
								ResultSet rs1=ps1.executeQuery();
								if (rs1.next()) {
									if (!rs1.getString(1).equals(rs1.getString(2))) {
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(true);
										sfnam.setEditable(true);
										afnam.setForeground(Color.RED);
										sfnam.setForeground(Color.RED);
									} else if(rs1.getString(1).equals(rs1.getString(2))){
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(false);
										sfnam.setEditable(false);
										afnam.setForeground(Color.BLACK);
										sfnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(true);
										smnam.setEditable(true);
										amnam.setForeground(Color.RED);
										smnam.setForeground(Color.RED);
										
									} else if(rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(false);
										smnam.setEditable(false);
										amnam.setForeground(Color.BLACK);
										smnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(5).equals(rs1.getString(6))) {
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(true);
										slnam.setEditable(true);
										alnam.setForeground(Color.RED);
										slnam.setForeground(Color.RED);
									} else if(rs1.getString(5).equals(rs1.getString(6))){
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(false);
										slnam.setEditable(false);
										alnam.setForeground(Color.BLACK);
										slnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(7).equals(rs1.getString(8))) {
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(true);
										sfullnam.setEditable(true);
										afullnam.setForeground(Color.RED);
										sfullnam.setForeground(Color.RED);
									} else if(rs1.getString(7).equals(rs1.getString(8))){
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(false);
										sfullnam.setEditable(false);
										afullnam.setForeground(Color.BLACK);
										sfullnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(true);
										sdob.setEditable(true);
										adob.setForeground(Color.RED);
										sdob.setForeground(Color.RED);
										
									} else if(rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(false);
										sdob.setEditable(false);
										adob.setForeground(Color.BLACK);
										sdob.setForeground(Color.BLACK);
										
									}	
								}
								
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
						}else {
							JOptionPane.showMessageDialog(null, "File Not Found");
						}
						}
					} else if(error.getSelectedItem().equals("Pdf Not Found")){
						PreparedStatement pps=con.prepareStatement("update PanError set Flag='A',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
						pps.execute();
						PreparedStatement pss=con.prepareStatement("update pandetail set FileGenerationFlag='A' where InhouseRefno="+inwardno.getText());
						pss.execute();
						model.removeRow(inwardtab.getSelectedRow());
						
						inwardtab.setRowSelectionInterval(0, 0);
			            
			            inselrow=inwardtab.getSelectedRow();
			            
			            inwardno.setText((String) model.getValueAt(inselrow, 0));
			            
			            int selval = 0;
						for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
							String val=inwardno.getText().trim();
							String tval=fnamtab.getValueAt(row, 0).toString();
						 	if (tval.matches(val+"(.*)")) {
								System.out.println("Enter Row Number: "+row);
									selval=row;
							}
						}
						
						if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
							System.out.println("DD: "+selval);
							fnamtab.setRowSelectionInterval(selval, selval);
							String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
							try {
								PDDocument document = PDDocument.load(new File(selpdf));
								PDFRenderer pdfRenderer = new PDFRenderer(document);
								for (int i = 0; i < document.getNumberOfPages(); i++) {
									BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
									ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
								}
								document.close();
								img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
							
							try {
								PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
								ResultSet rs1=ps1.executeQuery();
								if (rs1.next()) {
									if (!rs1.getString(1).equals(rs1.getString(2))) {
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(true);
										sfnam.setEditable(true);
										afnam.setForeground(Color.RED);
										sfnam.setForeground(Color.RED);
									} else if(rs1.getString(1).equals(rs1.getString(2))){
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(false);
										sfnam.setEditable(false);
										afnam.setForeground(Color.BLACK);
										sfnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(true);
										smnam.setEditable(true);
										amnam.setForeground(Color.RED);
										smnam.setForeground(Color.RED);
										
									} else if(rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(false);
										smnam.setEditable(false);
										amnam.setForeground(Color.BLACK);
										smnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(5).equals(rs1.getString(6))) {
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(true);
										slnam.setEditable(true);
										alnam.setForeground(Color.RED);
										slnam.setForeground(Color.RED);
									} else if(rs1.getString(5).equals(rs1.getString(6))){
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(false);
										slnam.setEditable(false);
										alnam.setForeground(Color.BLACK);
										slnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(7).equals(rs1.getString(8))) {
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(true);
										sfullnam.setEditable(true);
										afullnam.setForeground(Color.RED);
										sfullnam.setForeground(Color.RED);
									} else if(rs1.getString(7).equals(rs1.getString(8))){
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(false);
										sfullnam.setEditable(false);
										afullnam.setForeground(Color.BLACK);
										sfullnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(true);
										sdob.setEditable(true);
										adob.setForeground(Color.RED);
										sdob.setForeground(Color.RED);
										
									} else if(rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(false);
										sdob.setEditable(false);
										adob.setForeground(Color.BLACK);
										sdob.setForeground(Color.BLACK);
										
									}	
								}
								
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
						}else {
							JOptionPane.showMessageDialog(null, "File Not Found");
						}
						
					}else if (error.getSelectedItem().equals("Sam Error")) {
						PreparedStatement pps=con.prepareStatement("update PanError set Flag='S',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
						pps.execute();
						PreparedStatement pss=con.prepareStatement("update pandetail set FileGenerationFlag='S' where InhouseRefno="+inwardno.getText());
						pss.execute();
						model.removeRow(inwardtab.getSelectedRow());
						
						inwardtab.setRowSelectionInterval(0, 0);
			            
			            inselrow=inwardtab.getSelectedRow();
			            
			            inwardno.setText((String) model.getValueAt(inselrow, 0));
			            
			            int selval = 0;
						for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
							String val=inwardno.getText().trim();
							String tval=fnamtab.getValueAt(row, 0).toString();
						 	if (tval.matches(val+"(.*)")) {
								System.out.println("Enter Row Number: "+row);
									selval=row;
							}
						}
						
						if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
							System.out.println("DD: "+selval);
							fnamtab.setRowSelectionInterval(selval, selval);
							String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
							try {
								PDDocument document = PDDocument.load(new File(selpdf));
								PDFRenderer pdfRenderer = new PDFRenderer(document);
								for (int i = 0; i < document.getNumberOfPages(); i++) {
									BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
									ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
								}
								document.close();
								img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
							
							try {
								PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
								ResultSet rs1=ps1.executeQuery();
								if (rs1.next()) {
									if (!rs1.getString(1).equals(rs1.getString(2))) {
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(true);
										sfnam.setEditable(true);
										afnam.setForeground(Color.RED);
										sfnam.setForeground(Color.RED);
									} else if(rs1.getString(1).equals(rs1.getString(2))){
										afnam.setText(rs1.getString(1));
										sfnam.setText(rs1.getString(2));
										afnam.setEditable(false);
										sfnam.setEditable(false);
										afnam.setForeground(Color.BLACK);
										sfnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(true);
										smnam.setEditable(true);
										amnam.setForeground(Color.RED);
										smnam.setForeground(Color.RED);
										
									} else if(rs1.getString(3).equals(rs1.getString(4))) {
										amnam.setText(rs1.getString(3));
										smnam.setText(rs1.getString(4));
										amnam.setEditable(false);
										smnam.setEditable(false);
										amnam.setForeground(Color.BLACK);
										smnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(5).equals(rs1.getString(6))) {
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(true);
										slnam.setEditable(true);
										alnam.setForeground(Color.RED);
										slnam.setForeground(Color.RED);
									} else if(rs1.getString(5).equals(rs1.getString(6))){
										alnam.setText(rs1.getString(5));
										slnam.setText(rs1.getString(6));
										alnam.setEditable(false);
										slnam.setEditable(false);
										alnam.setForeground(Color.BLACK);
										slnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(7).equals(rs1.getString(8))) {
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(true);
										sfullnam.setEditable(true);
										afullnam.setForeground(Color.RED);
										sfullnam.setForeground(Color.RED);
									} else if(rs1.getString(7).equals(rs1.getString(8))){
										afullnam.setText(rs1.getString(7));
										sfullnam.setText(rs1.getString(8));
										afullnam.setEditable(false);
										sfullnam.setEditable(false);
										afullnam.setForeground(Color.BLACK);
										sfullnam.setForeground(Color.BLACK);
									}
									
									if (!rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(true);
										sdob.setEditable(true);
										adob.setForeground(Color.RED);
										sdob.setForeground(Color.RED);
										
									} else if(rs1.getString(9).equals(rs1.getString(10))) {
										adob.setText(rs1.getString(9));
										sdob.setText(rs1.getString(10));
										adob.setEditable(false);
										sdob.setEditable(false);
										adob.setForeground(Color.BLACK);
										sdob.setForeground(Color.BLACK);
										
									}	
								}
								
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "Error: "+e2);
							}
						}else {
							JOptionPane.showMessageDialog(null, "File Not Found");
						}
						
					}else if (error.getSelectedItem().equals("Multiple Error")) {
						if (!afnam.getText().equals(sfnam.getText()) || !sfnam.getText().equals(afnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct First Name");
						} else if(!amnam.getText().equals(smnam.getText()) || !smnam.getText().equals(amnam.getText())){
							JOptionPane.showMessageDialog(null, "Please check and Correct Middle Name");
						}else if (!alnam.getText().equals(slnam.getText()) || !slnam.getText().equals(alnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Last Name");
						}else if (!afullnam.getText().equals(sfullnam.getText()) || !sfullnam.getText().equals(afullnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Full Name");
						}else if (!adob.getText().equals(sdob.getText()) || !sdob.getText().equals(adob.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct DOB");
						}else {
							
							String sadate=sdob.getText().substring(4)+"-"+sdob.getText().substring(2,4)+"-"+sdob.getText().substring(0,2);
							
//							System.out.println("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
//							
			//				System.out.println("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							PreparedStatement pps=con.prepareStatement("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
							
							pps.execute();
							
							PreparedStatement pss=con.prepareStatement("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							pss.execute();
							
							model.removeRow(inwardtab.getSelectedRow());
							
							inwardtab.setRowSelectionInterval(0, 0);
				            
				            inselrow=inwardtab.getSelectedRow();
				            
				            inwardno.setText((String) model.getValueAt(inselrow, 0));
				            
				            int selval = 0;
							for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
								String val=inwardno.getText().trim();
								String tval=fnamtab.getValueAt(row, 0).toString();
							 	if (tval.matches(val+"(.*)")) {
									System.out.println("Enter Row Number: "+row);
										selval=row;
								}
							}
							
							if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
								System.out.println("DD: "+selval);
								fnamtab.setRowSelectionInterval(selval, selval);
								String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
								try {
									PDDocument document = PDDocument.load(new File(selpdf));
									PDFRenderer pdfRenderer = new PDFRenderer(document);
									for (int i = 0; i < document.getNumberOfPages(); i++) {
										BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
										ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
									}
									document.close();
									img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
								
								try {
									PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
									ResultSet rs1=ps1.executeQuery();
									if (rs1.next()) {
										if (!rs1.getString(1).equals(rs1.getString(2))) {
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(true);
											sfnam.setEditable(true);
											afnam.setForeground(Color.RED);
											sfnam.setForeground(Color.RED);
										} else if(rs1.getString(1).equals(rs1.getString(2))){
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(false);
											sfnam.setEditable(false);
											afnam.setForeground(Color.BLACK);
											sfnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(true);
											smnam.setEditable(true);
											amnam.setForeground(Color.RED);
											smnam.setForeground(Color.RED);
											
										} else if(rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(false);
											smnam.setEditable(false);
											amnam.setForeground(Color.BLACK);
											smnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(5).equals(rs1.getString(6))) {
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(true);
											slnam.setEditable(true);
											alnam.setForeground(Color.RED);
											slnam.setForeground(Color.RED);
										} else if(rs1.getString(5).equals(rs1.getString(6))){
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(false);
											slnam.setEditable(false);
											alnam.setForeground(Color.BLACK);
											slnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(7).equals(rs1.getString(8))) {
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(true);
											sfullnam.setEditable(true);
											afullnam.setForeground(Color.RED);
											sfullnam.setForeground(Color.RED);
										} else if(rs1.getString(7).equals(rs1.getString(8))){
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(false);
											sfullnam.setEditable(false);
											afullnam.setForeground(Color.BLACK);
											sfullnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(true);
											sdob.setEditable(true);
											adob.setForeground(Color.RED);
											sdob.setForeground(Color.RED);
											
										} else if(rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(false);
											sdob.setEditable(false);
											adob.setForeground(Color.BLACK);
											sdob.setForeground(Color.BLACK);
											
										}	
									}
									
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
							}else {
								JOptionPane.showMessageDialog(null, "File Not Found");
							}	
						}	
					}else if (error.getSelectedItem().equals("First Name Entry Wrong")) {
						if (!afnam.getText().equals(sfnam.getText()) || !sfnam.getText().equals(afnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct First Name");
						} else if(!amnam.getText().equals(smnam.getText()) || !smnam.getText().equals(amnam.getText())){
							JOptionPane.showMessageDialog(null, "Please check and Correct Middle Name");
						}else if (!alnam.getText().equals(slnam.getText()) || !slnam.getText().equals(alnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Last Name");
						}else if (!afullnam.getText().equals(sfullnam.getText()) || !sfullnam.getText().equals(afullnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Full Name");
						}else if (!adob.getText().equals(sdob.getText()) || !sdob.getText().equals(adob.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct DOB");
						}else {
							
							String sadate=sdob.getText().substring(4)+"-"+sdob.getText().substring(2,4)+"-"+sdob.getText().substring(0,2);
							
//							System.out.println("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
//							
//							System.out.println("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							PreparedStatement pps=con.prepareStatement("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
							
							pps.execute();
							
							PreparedStatement pss=con.prepareStatement("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							pss.execute();
							
							model.removeRow(inwardtab.getSelectedRow());
							
							inwardtab.setRowSelectionInterval(0, 0);
				            
				            inselrow=inwardtab.getSelectedRow();
				            
				            inwardno.setText((String) model.getValueAt(inselrow, 0));
				            
				            int selval = 0;
							for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
								String val=inwardno.getText().trim();
								String tval=fnamtab.getValueAt(row, 0).toString();
							 	if (tval.matches(val+"(.*)")) {
									System.out.println("Enter Row Number: "+row);
										selval=row;
								}
							}
							
							if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
								System.out.println("DD: "+selval);
								fnamtab.setRowSelectionInterval(selval, selval);
								String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
								try {
									PDDocument document = PDDocument.load(new File(selpdf));
									PDFRenderer pdfRenderer = new PDFRenderer(document);
									for (int i = 0; i < document.getNumberOfPages(); i++) {
										BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
										ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
									}
									document.close();
									img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
								
								try {
									PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
									ResultSet rs1=ps1.executeQuery();
									if (rs1.next()) {
										if (!rs1.getString(1).equals(rs1.getString(2))) {
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(true);
											sfnam.setEditable(true);
											afnam.setForeground(Color.RED);
											sfnam.setForeground(Color.RED);
										} else if(rs1.getString(1).equals(rs1.getString(2))){
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(false);
											sfnam.setEditable(false);
											afnam.setForeground(Color.BLACK);
											sfnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(true);
											smnam.setEditable(true);
											amnam.setForeground(Color.RED);
											smnam.setForeground(Color.RED);
											
										} else if(rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(false);
											smnam.setEditable(false);
											amnam.setForeground(Color.BLACK);
											smnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(5).equals(rs1.getString(6))) {
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(true);
											slnam.setEditable(true);
											alnam.setForeground(Color.RED);
											slnam.setForeground(Color.RED);
										} else if(rs1.getString(5).equals(rs1.getString(6))){
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(false);
											slnam.setEditable(false);
											alnam.setForeground(Color.BLACK);
											slnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(7).equals(rs1.getString(8))) {
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(true);
											sfullnam.setEditable(true);
											afullnam.setForeground(Color.RED);
											sfullnam.setForeground(Color.RED);
										} else if(rs1.getString(7).equals(rs1.getString(8))){
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(false);
											sfullnam.setEditable(false);
											afullnam.setForeground(Color.BLACK);
											sfullnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(true);
											sdob.setEditable(true);
											adob.setForeground(Color.RED);
											sdob.setForeground(Color.RED);
											
										} else if(rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(false);
											sdob.setEditable(false);
											adob.setForeground(Color.BLACK);
											sdob.setForeground(Color.BLACK);
											
										}	
									}
									
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
							}else {
								JOptionPane.showMessageDialog(null, "File Not Found");
							}	
						}	
					}else if (error.getSelectedItem().equals("Middle Name Entry Wrong")) {
						if (!afnam.getText().equals(sfnam.getText()) || !sfnam.getText().equals(afnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct First Name");
						} else if(!amnam.getText().equals(smnam.getText()) || !smnam.getText().equals(amnam.getText())){
							JOptionPane.showMessageDialog(null, "Please check and Correct Middle Name");
						}else if (!alnam.getText().equals(slnam.getText()) || !slnam.getText().equals(alnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Last Name");
						}else if (!afullnam.getText().equals(sfullnam.getText()) || !sfullnam.getText().equals(afullnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Full Name");
						}else if (!adob.getText().equals(sdob.getText()) || !sdob.getText().equals(adob.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct DOB");
						}else {
							
							String sadate=sdob.getText().substring(4)+"-"+sdob.getText().substring(2,4)+"-"+sdob.getText().substring(0,2);
							
//							System.out.println("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
//							
//							System.out.println("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							PreparedStatement pps=con.prepareStatement("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
							
							pps.execute();
							
							PreparedStatement pss=con.prepareStatement("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							pss.execute();
							
							model.removeRow(inwardtab.getSelectedRow());
							
							inwardtab.setRowSelectionInterval(0, 0);
				            
				            inselrow=inwardtab.getSelectedRow();
				            
				            inwardno.setText((String) model.getValueAt(inselrow, 0));
				            
				            int selval = 0;
							for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
								String val=inwardno.getText().trim();
								String tval=fnamtab.getValueAt(row, 0).toString();
							 	if (tval.matches(val+"(.*)")) {
									System.out.println("Enter Row Number: "+row);
										selval=row;
								}
							}
							
							if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
								System.out.println("DD: "+selval);
								fnamtab.setRowSelectionInterval(selval, selval);
								String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
								try {
									PDDocument document = PDDocument.load(new File(selpdf));
									PDFRenderer pdfRenderer = new PDFRenderer(document);
									for (int i = 0; i < document.getNumberOfPages(); i++) {
										BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
										ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
									}
									document.close();
									img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
								
								try {
									PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
									ResultSet rs1=ps1.executeQuery();
									if (rs1.next()) {
										if (!rs1.getString(1).equals(rs1.getString(2))) {
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(true);
											sfnam.setEditable(true);
											afnam.setForeground(Color.RED);
											sfnam.setForeground(Color.RED);
										} else if(rs1.getString(1).equals(rs1.getString(2))){
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(false);
											sfnam.setEditable(false);
											afnam.setForeground(Color.BLACK);
											sfnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(true);
											smnam.setEditable(true);
											amnam.setForeground(Color.RED);
											smnam.setForeground(Color.RED);
											
										} else if(rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(false);
											smnam.setEditable(false);
											amnam.setForeground(Color.BLACK);
											smnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(5).equals(rs1.getString(6))) {
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(true);
											slnam.setEditable(true);
											alnam.setForeground(Color.RED);
											slnam.setForeground(Color.RED);
										} else if(rs1.getString(5).equals(rs1.getString(6))){
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(false);
											slnam.setEditable(false);
											alnam.setForeground(Color.BLACK);
											slnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(7).equals(rs1.getString(8))) {
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(true);
											sfullnam.setEditable(true);
											afullnam.setForeground(Color.RED);
											sfullnam.setForeground(Color.RED);
										} else if(rs1.getString(7).equals(rs1.getString(8))){
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(false);
											sfullnam.setEditable(false);
											afullnam.setForeground(Color.BLACK);
											sfullnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(true);
											sdob.setEditable(true);
											adob.setForeground(Color.RED);
											sdob.setForeground(Color.RED);
											
										} else if(rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(false);
											sdob.setEditable(false);
											adob.setForeground(Color.BLACK);
											sdob.setForeground(Color.BLACK);
											
										}	
									}
									
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
							}else {
								JOptionPane.showMessageDialog(null, "File Not Found");
							}	
						}	
					}else if (error.getSelectedItem().equals("Last Name Entry Wrong")) {
						if (!afnam.getText().equals(sfnam.getText()) || !sfnam.getText().equals(afnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct First Name");
						} else if(!amnam.getText().equals(smnam.getText()) || !smnam.getText().equals(amnam.getText())){
							JOptionPane.showMessageDialog(null, "Please check and Correct Middle Name");
						}else if (!alnam.getText().equals(slnam.getText()) || !slnam.getText().equals(alnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Last Name");
						}else if (!afullnam.getText().equals(sfullnam.getText()) || !sfullnam.getText().equals(afullnam.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct Full Name");
						}else if (!adob.getText().equals(sdob.getText()) || !sdob.getText().equals(adob.getText())) {
							JOptionPane.showMessageDialog(null, "Please check and Correct DOB");
						}else {
							
							String sadate=sdob.getText().substring(4)+"-"+sdob.getText().substring(2,4)+"-"+sdob.getText().substring(0,2);
							
							//System.out.println("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
							
						//	System.out.println("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							PreparedStatement pps=con.prepareStatement("update PanError set Flag='U',UpdateDate=GETDATE(),REMARKS='"+error.getSelectedItem()+"', Userid='"+lognam+"' where InwardNo="+inwardno.getText());
							
							pps.execute();
							
							PreparedStatement pss=con.prepareStatement("update pandetail set AppFirstName='"+sfnam.getText()+"', AppMiddleName='"+smnam.getText()+"', AppLastName='"+slnam.getText()+"', AppNameOnCard='"+sfullnam.getText()+"', AppDOB='"+sadate+"', FileGenerationFlag='U' where InhouseRefno="+inwardno.getText());
							
							pss.execute();
							
							model.removeRow(inwardtab.getSelectedRow());
							
							inwardtab.setRowSelectionInterval(0, 0);
				            
				            inselrow=inwardtab.getSelectedRow();
				            
				            inwardno.setText((String) model.getValueAt(inselrow, 0));
				            
				            int selval = 0;
							for (int row = 0; row <= fnamtab.getRowCount() - 1; row++) {
								String val=inwardno.getText().trim();
								String tval=fnamtab.getValueAt(row, 0).toString();
							 	if (tval.matches(val+"(.*)")) {
									System.out.println("Enter Row Number: "+row);
										selval=row;
								}
							}
							
							if(model2.getValueAt(selval, 0).toString().matches(inwardno.getText().trim()+"(.*)")) {
								System.out.println("DD: "+selval);
								fnamtab.setRowSelectionInterval(selval, selval);
								String selpdf=filepath+"//"+model2.getValueAt(fnamtab.getSelectedRow(), 0);
								try {
									PDDocument document = PDDocument.load(new File(selpdf));
									PDFRenderer pdfRenderer = new PDFRenderer(document);
									for (int i = 0; i < document.getNumberOfPages(); i++) {
										BufferedImage bim = pdfRenderer.renderImageWithDPI(i,90, ImageType.RGB);
										ImageIOUtil.writeImage(bim,"C:\\temp\\"+inwardno.getText()+i+".png",0);
									}
									document.close();
									img.setIcon(new ImageIcon("C:\\temp\\"+inwardno.getText()+"1.png"));
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
								
								try {
									PreparedStatement ps1=con.prepareStatement("select FirstName,SamFirstName,MiddleName,SamMiddleName,LastName,SamLastName,NameOnCard,SamNameOnCard,DOB,SamDOB from PanError where InwardNo="+inwardno.getText());
									ResultSet rs1=ps1.executeQuery();
									if (rs1.next()) {
										if (!rs1.getString(1).equals(rs1.getString(2))) {
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(true);
											sfnam.setEditable(true);
											afnam.setForeground(Color.RED);
											sfnam.setForeground(Color.RED);
										} else if(rs1.getString(1).equals(rs1.getString(2))){
											afnam.setText(rs1.getString(1));
											sfnam.setText(rs1.getString(2));
											afnam.setEditable(false);
											sfnam.setEditable(false);
											afnam.setForeground(Color.BLACK);
											sfnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(true);
											smnam.setEditable(true);
											amnam.setForeground(Color.RED);
											smnam.setForeground(Color.RED);
											
										} else if(rs1.getString(3).equals(rs1.getString(4))) {
											amnam.setText(rs1.getString(3));
											smnam.setText(rs1.getString(4));
											amnam.setEditable(false);
											smnam.setEditable(false);
											amnam.setForeground(Color.BLACK);
											smnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(5).equals(rs1.getString(6))) {
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(true);
											slnam.setEditable(true);
											alnam.setForeground(Color.RED);
											slnam.setForeground(Color.RED);
										} else if(rs1.getString(5).equals(rs1.getString(6))){
											alnam.setText(rs1.getString(5));
											slnam.setText(rs1.getString(6));
											alnam.setEditable(false);
											slnam.setEditable(false);
											alnam.setForeground(Color.BLACK);
											slnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(7).equals(rs1.getString(8))) {
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(true);
											sfullnam.setEditable(true);
											afullnam.setForeground(Color.RED);
											sfullnam.setForeground(Color.RED);
										} else if(rs1.getString(7).equals(rs1.getString(8))){
											afullnam.setText(rs1.getString(7));
											sfullnam.setText(rs1.getString(8));
											afullnam.setEditable(false);
											sfullnam.setEditable(false);
											afullnam.setForeground(Color.BLACK);
											sfullnam.setForeground(Color.BLACK);
										}
										
										if (!rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(true);
											sdob.setEditable(true);
											adob.setForeground(Color.RED);
											sdob.setForeground(Color.RED);
											
										} else if(rs1.getString(9).equals(rs1.getString(10))) {
											adob.setText(rs1.getString(9));
											sdob.setText(rs1.getString(10));
											adob.setEditable(false);
											sdob.setEditable(false);
											adob.setForeground(Color.BLACK);
											sdob.setForeground(Color.BLACK);
											
										}	
									}
									
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Error: "+e2);
								}
							}else {
								JOptionPane.showMessageDialog(null, "File Not Found");
							}	
						}	
					}
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: "+e);
				}
			}
		});
		btnSave.setMnemonic(KeyEvent.VK_S);
		btnSave.setBounds((int)w-130, 533, 107, 28);
		frame.getContentPane().add(btnSave);
		
		btnExit = new JButton("Exit");
		btnExit.setMnemonic(KeyEvent.VK_X);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds((int)w-130, 572, 94, 28);
		frame.getContentPane().add(btnExit);
		
		lblSaveAlts = new JLabel("Save: Alt+S              Next: Alt+N");
		lblSaveAlts.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSaveAlts.setBounds((int)w-430, 611, 288, 14);
		frame.getContentPane().add(lblSaveAlts);
		
		lblSaveAlts1 = new JLabel("Previous: Alt+P              Exit: Alt+X");
		lblSaveAlts1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSaveAlts1.setBounds((int)w-430, 631, 288, 14);
		frame.getContentPane().add(lblSaveAlts1);
		
		frame.setVisible(true);
	}
}
