import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import java.awt.color.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class DrawPictureFrame extends JFrame {
	BufferedImage img = null;
	BufferedImage image = new BufferedImage(900, 830, BufferedImage.TYPE_INT_BGR);
	Graphics gs = image.getGraphics();
	Graphics2D g = (Graphics2D) gs;
	
	DrawPictureCanvas canvas = new DrawPictureCanvas();
	Color forecColor = Color.black;
	Color backgroundColor = Color.white;
	
	static Map<String, String> list = new Hashtable<String, String>();
	

	int x = -1;
	int y = -1;
	int stroke_value = 0;
	static boolean rubber = false;
	int eraser_valjue = 0;
	int shape =0;
	int x1,y1,x2,y2;
	String s;
	String username = "User1";
	

	/*
	 * 创建按钮，菜单组件
	 */
	private JTextArea room;
	private JPanel jp;
	private JPanel jp1;
	private JPanel jp2;
	private JButton sendBt;
	private JTextField inputField;
	private JToolBar toolBar;
	private JButton straightButton;
	private JButton RectangleButton;
	private JButton OvalButton;
	private JButton CircleButton;
	private JButton TextButton;
	private JToggleButton strokeButton1;
	private JToggleButton strokeButton2;
	private JToggleButton strokeButton3;
	private JButton backgroundButton;
	private JButton foreroundButton;
	private JButton savebButton;
	private JTextArea chatContent;
	private JTextArea editinglist;

//	private JMenuItem strokeMenuItem1;
//	private JMenuItem strokeMenuItem2;
//	private JMenuItem strokeMenuItem3;
	private JMenuItem clearMenuItem;
	private JMenuItem foregroundMenuItem;
	private JMenuItem backgroundItem;
	private JMenuItem eraserMenuItem;
	private JMenuItem eraserMenuItem1;
	private JMenuItem eraserMenuItem2;
	private JMenuItem exitMenuItem;
	private JMenuItem newMenuItem;
	private JMenuItem saveasMenuItem;
	private JMenuItem uploadMenuItem;
	private JMenuItem saveMenuItem;


	public DrawPictureFrame() {
		setResizable(false);
		setTitle("CANVAS");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 100, 1160, 920);
		init();
		addListener();

	}
	
	
	public void getback() {
		eraserMenuItem.setText("Small Eraser");
		eraserMenuItem1.setText("Medium Eraser");
		eraserMenuItem2.setText("Large Eraser");
		TextButton.setText("Text");
		RectangleButton.setText("Rectangle");
		OvalButton.setText("Oval");
		CircleButton.setText("Circle");
		straightButton.setText("Straight");
		rubber = false;
		shape = 0;
		g.setColor(forecColor);
	}
	
	public void getMenuback() {
		eraserMenuItem.setText("Small Eraser");
		eraserMenuItem1.setText("Medium Eraser");
		eraserMenuItem2.setText("Large Eraser");
	}
	
	public void getButtonback() {
		TextButton.setText("Text");
		RectangleButton.setText("Rectangle");
		OvalButton.setText("Oval");
		CircleButton.setText("Circle");
		straightButton.setText("Straight");
	}
	
	private void init() {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, 1160, 830);
		g.setColor(forecColor);
		canvas.setImage(image);
		getContentPane().add(canvas);
		

		toolBar = new JToolBar();
		
		
		jp = new JPanel();
		jp.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
		//jp.setSize(50, 10);
		jp1 = new JPanel();
		jp2 = new JPanel();


		getContentPane().add(jp, BorderLayout.EAST);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		
		
		editinglist = new JTextArea(10,10);
		chatContent = new JTextArea(200,10);
		room = new JTextArea(1,1);
		room.setFont(room.getFont().deriveFont(20f));
		
		JScrollPane showPanel = new JScrollPane(chatContent);
		chatContent.setEditable(false);
		chatContent.setBounds(10,10,10,100);
		jp.setBackground(Color.GREEN);
		
		inputField = new JTextField(20);	
		inputField.setPreferredSize(new Dimension(150,20));
		sendBt = new JButton("Send");
		jp.add(room);
		jp.add(jp1);
		jp.add(jp2);
		jp.add(editinglist);
		editinglist.setPreferredSize(new Dimension(30,30));

		jp1.setLayout(new BorderLayout());
		jp1.add(showPanel,BorderLayout.CENTER);
		jp1.setPreferredSize(new Dimension(100,350));
				
		jp2.add(inputField);
		jp2.add(sendBt);


		room.append("\n                Chat Room");
		room.setBorder (BorderFactory.createLineBorder(Color.green,1));
		room.setEditable(false);
		
		savebButton = new JButton("Save");
		savebButton.setToolTipText("Save");
		toolBar.add(savebButton);
		toolBar.addSeparator();

		strokeButton1 = new JToggleButton("Thin");
		strokeButton1.setSelected(true);
		toolBar.add(strokeButton1);

		strokeButton2 = new JToggleButton("Medium");
		toolBar.add(strokeButton2);

		strokeButton3 = new JToggleButton("Thick");

		ButtonGroup strokeGroup = new ButtonGroup();
		strokeGroup.add(strokeButton1);
		strokeGroup.add(strokeButton2);
		strokeGroup.add(strokeButton3);
		toolBar.add(strokeButton3);
		toolBar.addSeparator();
		backgroundButton = new JButton("Background Color");
		toolBar.add(backgroundButton);
		foreroundButton = new JButton("Pencil Color");
		toolBar.add(foreroundButton);
		toolBar.addSeparator();

		straightButton = new JButton("Straight");
		toolBar.add(straightButton);
		RectangleButton = new JButton("Rectangle");
		toolBar.add(RectangleButton);
		OvalButton = new JButton("Oval");
		toolBar.add(OvalButton);
		CircleButton = new JButton("Circle");
		toolBar.add(CircleButton);
		toolBar.addSeparator();
		
		TextButton = new JButton("Text");
		toolBar.add(TextButton);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu systemMenu = new JMenu("System");
		menuBar.add(systemMenu);
		newMenuItem = new JMenuItem("New");
		systemMenu.add(newMenuItem);
		saveMenuItem = new JMenuItem("Save");
		systemMenu.add(saveMenuItem);
		saveasMenuItem = new JMenuItem("Save as");
		systemMenu.add(saveasMenuItem);
		uploadMenuItem = new JMenuItem("Upload");
		systemMenu.add(uploadMenuItem);
		systemMenu.addSeparator();
		
		exitMenuItem = new JMenuItem("Close");
		systemMenu.add(exitMenuItem);

		JMenu colorMenu = new JMenu("Color");
		menuBar.add(colorMenu);
		foregroundMenuItem = new JMenuItem("Pencil Color");
		colorMenu.add(foregroundMenuItem);
		backgroundItem = new JMenuItem("Background Color");
		colorMenu.add(backgroundItem);

		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		JMenu erasers = new JMenu("Erasers");
		editMenu.add(erasers);
		clearMenuItem = new JMenuItem("Clear");
		editMenu.add(clearMenuItem);
		eraserMenuItem = new JMenuItem("Small Eraser");
		erasers.add(eraserMenuItem);
		eraserMenuItem1 = new JMenuItem("Medium Eraser");
		erasers.add(eraserMenuItem1);
		eraserMenuItem2 = new JMenuItem("Large Eraser");
		erasers.add(eraserMenuItem2);


	}

	private void addListener() {
		
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			
			public void mouseDragged(MouseEvent e) { 
				if (shape == 0) {
					if (x > 0 && y > 0) {
						if (rubber) {
							if(eraser_valjue == 1) {
								g.setColor(backgroundColor);
								g.fillRect(x, y, 30, 30);
							}
							else if (eraser_valjue == 2){
								g.setColor(backgroundColor);
								g.fillRect(x, y, 50, 50);
							}
							else {
								g.setColor(backgroundColor);
								g.fillRect(x, y, 10, 10);
							}
						} else {
							g.setColor(forecColor);
							g.drawLine(x, y, e.getX(), e.getY());
						}
					}
					x = e.getX();
					y = e.getY();
					canvas.repaint();
				}
				list.put(username, "is editing");
				editinglist.setText("");
				for (String key : list.keySet()) {
				    editinglist.append(key + ":" + list.get(key));
				}
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (rubber) {
					Toolkit kit = Toolkit.getDefaultToolkit();
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}
			}
		});
		
		canvas.addMouseListener(new MouseAdapter() {
			//mouse pressed
			public void mousePressed(MouseEvent e){
				if (rubber == false) {
					x1=e.getX();
				    y1=e.getY();
				    if (shape == 5) {
				    	s = JOptionPane.showInputDialog("Plz input your text beneath:");
				    	g.setColor(forecColor);
				    	if (stroke_value == 3) {
				    		g.setFont(new Font("Tahoma", Font.BOLD,25));
				    	}
				    	else if (stroke_value == 2) {
				    		g.setFont(new Font("Tahoma", Font.BOLD, 20));
				    	}
				    	
				    	else {
				    		g.setFont(new Font("Tahoma", Font.BOLD, 15));
				    	}
				    	
				    	
				    	g.drawString(s, x1, y1);
				    	canvas.repaint();
				    }
				}
				else {
					x=-1;
					y=-1;
				}
				list.put(username, "is editing");
				editinglist.setText("");
				for (String key : list.keySet()) {
				    editinglist.append(key + ":" + list.get(key));
				}
			}
					
			public void mouseReleased(MouseEvent e){
				  //获取松开信息
				if (rubber == false) {
					if (shape == 1) {
						  x = -1;
						  y = -1;
						  x2=e.getX();
						  y2=e.getY();
						  //画线
						  g.setColor(forecColor);
						  g.drawLine(x1,y1,x2,y2);
						  canvas.repaint();
						
					}
					else if (shape == 2) {
						x2=e.getX();
						y2=e.getY();
						g.setColor(forecColor);
						g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
						canvas.repaint();
					}
					else if (shape == 3) {
						x2=e.getX();
						y2=e.getY();
						g.setColor(forecColor);
						g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
						canvas.repaint();
					}
					else if (shape == 4) {
						x2=e.getX();
						y2=e.getY();
						g.setColor(forecColor);
						g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
								Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
						canvas.repaint();
					}

					else {
						x=-1;
						y=-1;
					}
				} 
				else{
					x=-1;
					y=-1;
				}
				list.put(username, "in the room");
				editinglist.setText("");
				for (String key : list.keySet()) {
				    editinglist.append(key + ":" + list.get(key));
				}
				
				
			}
			
			
		});

		
		
		strokeButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				rubber = false;
				getMenuback();
				stroke_value = 0;
			}
		});

		strokeButton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				rubber = false;
				getMenuback();
				stroke_value = 1;
			}
		});

		strokeButton3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
				rubber = false;
				getMenuback();
				stroke_value = 2;
			}
		});

		backgroundButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "Choose Color", Color.CYAN);
				if (bgColor != null) {
					backgroundColor = bgColor;
					backgroundButton.setBackground(backgroundColor);
					g.setColor(backgroundColor);
					g.fillRect(0, 0, 1160, 830);
					g.setColor(forecColor);
					canvas.repaint();
					list.put(username, "changed background color");
					editinglist.setText("");
					for (String key : list.keySet()) {
					    editinglist.append(key + ":" + list.get(key));
					}

				}

				
			}
		});

		foreroundButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "Choose Color", Color.CYAN);
				if (fColor != null) {
					forecColor = fColor;
					foreroundButton.setBackground(forecColor);
					g.setColor(forecColor);
				}

				

			}
		});
		
		sendBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String content = inputField.getText();
				if(content != null && !content.trim().equals("")){
					if (content.length() > 60) {
						chatContent.append("Usre 1:"+content.substring(0,29 )+"\n" + content.substring(30,59)+"\n" + 
									content.substring(60,content.length() -1) + "\n");
					}
					else if (content.length() > 30) {
						chatContent.append("Usre 1:"+content.substring(0,29 )+"\n" + 
									content.substring(30,content.length() -1) + "\n");
					}
					else {
						chatContent.append("Usre 1:"+content+"\n");
					}
					
					
					
				}else{
					JOptionPane.showMessageDialog(null, "Input can't be empty", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
				inputField.setText("");
			}
		});

		TextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (TextButton.getText().equals("Text")) {
					rubber = false;
					shape = 5;
					getMenuback();
					TextButton.setText("Free");
					straightButton.setText("Straight");
					RectangleButton.setText("Rectangle");
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
					//s = JOptionPane.showInputDialog("Plz input your text beneath:");
				} else {
					getback();
					list.put(username, "in the room");
					editinglist.setText("");
					for (String key : list.keySet()) {
					    editinglist.append(key + ":" + list.get(key));
					}
				}

			}
		});
		
		straightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (straightButton.getText().equals("Straight")) {
					rubber = false;
					shape = 1;
					getMenuback();
					straightButton.setText("Free");
					TextButton.setText("Text");
					RectangleButton.setText("Rectangle");
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
				} else {
					getback();
				}

			}
		});
		
		RectangleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (RectangleButton.getText().equals("Rectangle")) {
					rubber = false;
					shape = 2;
					getMenuback();
					RectangleButton.setText("Free");
					TextButton.setText("Text");
					CircleButton.setText("Circle");
					OvalButton.setText("Oval");
					straightButton.setText("Straight");
				} else {
					getback();
				}

			}
		});
		
		OvalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OvalButton.getText().equals("Oval")) {
					rubber = false;
					shape = 3;
					getMenuback();
					OvalButton.setText("Free");
					CircleButton.setText("Circle");
					TextButton.setText("Text");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
				} else {
					getback();
				}

			}
		});

		CircleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (CircleButton.getText().equals("Circle")) {
					rubber = false;
					shape = 4;
					getMenuback();
					CircleButton.setText("Free");
					OvalButton.setText("Oval");
					TextButton.setText("Text");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
				} else {
					getback();
				}

			}
		});
		
		savebButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					ImageIO.write(image, "jpeg", new File("1.jpeg"));
					System.out.println("Saved!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		

		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					ImageIO.write(image, "jpeg", new File("1.jpeg"));
					System.out.println("Saved!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		saveasMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					s = JOptionPane.showInputDialog("Plz input name beneath:");
					String s2 = JOptionPane.showInputDialog("Plz input the directory beneath:");
					ImageIO.write(image, "jpeg", new File( s2 + "\\" + s + ".jpeg"));
					System.out.println("Saved!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		uploadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String filename = JOptionPane.showInputDialog("Plz input your file name beneath:");
				try {
					img = ImageIO.read(new File(filename + ".jpeg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				g.drawImage(img, 0, 0, null);
				canvas.repaint();
			}
		});
		

		foregroundMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "Choose Color", Color.CYAN);
				if (fColor != null) {
					forecColor = fColor;
				}

				foreroundButton.setBackground(forecColor);
				g.setColor(forecColor);

			}
		});

		backgroundItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "Choose Color", Color.CYAN);
				if (bgColor != null) {
					backgroundColor = bgColor;
				}

				backgroundButton.setBackground(backgroundColor);
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 1160, 830);
				g.setColor(forecColor);
				canvas.repaint();

			}
		});

		clearMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 1160, 830);
				g.setColor(forecColor);
				canvas.repaint();
				list.put(username, "clear the canvas");
				editinglist.setText("");
				for (String key : list.keySet()) {
				    editinglist.append(key + ":" + list.get(key));
				}

			}
		});
		
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 1160, 830);
				g.setColor(forecColor);
				canvas.repaint();

			}
		});

		eraserMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem.getText().equals("Small Eraser")) {
					rubber = true;
					eraser_valjue = 0;
					shape = 0;
					eraserMenuItem.setText("Draw");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Large Eraser");
					getButtonback();
				} else {
					getback();
				}

			}
		});
		
		eraserMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem1.getText().equals("Medium Eraser")) {
					rubber = true;
					eraser_valjue = 1;
					shape = 0;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Draw");
					eraserMenuItem2.setText("Large Eraser");
					getButtonback();
				} else {
					getback();
				}

			}
		});
		
		eraserMenuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem2.getText().equals("Large Eraser")) {
					rubber = true;
					eraser_valjue = 2;
					shape = 0;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Draw");
					getButtonback();
				} else {
					getback();
				}
				

			}
		});
		

		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});


//		toolBar.addMouseMotionListener(new MouseMotionAdapter() {//工具栏添加鼠标事件监听
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));//设置鼠标的形状为默认光标
//
//			}
//
//		});
	}


	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		DrawPictureFrame frame = new DrawPictureFrame();
		frame.setVisible(true);

	}

}