import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import java.awt.color.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class DrawPictureFrame extends JFrame {

	BufferedImage image = new BufferedImage(1160, 830, BufferedImage.TYPE_INT_BGR);
	Graphics gs = image.getGraphics();
	Graphics2D g = (Graphics2D) gs;
	DrawPictureCanvas canvas = new DrawPictureCanvas();
	Color forecColor = Color.black;
	Color backgroundColor = Color.white;

	int x = -1;
	int y = -1;
	boolean rubber = false;
	int eraser_valjue = 0;
	int shape =0;
	int x1,y1,x2,y2;
	/*
	 * 创建按钮，菜单组件
	 */
	private JToolBar toolBar;
	private JButton straightButton;
	private JButton RectangleButton;
	private JButton OvalButton;
	private JButton CircleButton;
	private JToggleButton strokeButton1;
	private JToggleButton strokeButton2;
	private JToggleButton strokeButton3;
	private JButton backgroundButton;
	private JButton foreroundButton;
	private JButton savebButton;

	private JMenuItem strokeMenuItem1;
	private JMenuItem strokeMenuItem2;
	private JMenuItem strokeMenuItem3;
	private JMenuItem clearMenuItem;
	private JMenuItem foregroundMenuItem;
	private JMenuItem backgroundItem;
	private JMenuItem eraserMenuItem;
	private JMenuItem eraserMenuItem1;
	private JMenuItem eraserMenuItem2;
	private JMenuItem exitMenuItem;
	private JMenuItem saveMenuItem;


	public DrawPictureFrame() {
		setResizable(false);//设为不可更改大小
		setTitle("CANVAS");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 100, 1160, 920);
		init();
		addListener();

	}

	private void init() {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, 1160, 830);
		g.setColor(forecColor);
		canvas.setImage(image);
		getContentPane().add(canvas);

		toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);//将工具栏放在窗体上边
		
		/*
		 * 设置各组件位置
		 */
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

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu systemMenu = new JMenu("System");
		menuBar.add(systemMenu);
		saveMenuItem = new JMenuItem("Save");
		systemMenu.add(saveMenuItem);
		systemMenu.addSeparator();
		exitMenuItem = new JMenuItem("Close");
		systemMenu.add(exitMenuItem);

		JMenu strokeMenu = new JMenu("Lines");
		menuBar.add(strokeMenu);
		strokeMenuItem1 = new JMenuItem("Thin");
		strokeMenu.add(strokeMenuItem1);
		strokeMenuItem2 = new JMenuItem("Medium");
		strokeMenu.add(strokeMenuItem2);
		strokeMenuItem3 = new JMenuItem("Thick");
		strokeMenu.add(strokeMenuItem3);

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
							g.drawLine(x, y, e.getX(), e.getY());
						}
					}
					x = e.getX();
					y = e.getY();
					
					canvas.repaint();

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
			public void mousePressed(MouseEvent e){
				 //获取按下信息

				 x1=e.getX();
				 y1=e.getY();

			}
					
			public void mouseReleased(MouseEvent e){
				  //获取松开信息
				if (shape == 1) {
					  x = -1;
					  y = -1;
					  x2=e.getX();
					  y2=e.getY();
					  //画线
					  g.drawLine(x1,y1,x2,y2);
					  canvas.repaint();
					
				}
				else if (shape == 2) {
					x2=e.getX();
					y2=e.getY();
					g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
					canvas.repaint();
				}
				else if (shape == 3) {
					x2=e.getX();
					y2=e.getY();
					g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
					canvas.repaint();
				}
				else if (shape == 4) {
					x2=e.getX();
					y2=e.getY();
					g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
							Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
					canvas.repaint();
				}
				else {
					x=-1;
					y=-1;
				}
				  
			}
		});

		
		
		strokeButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
			}
		});

		strokeButton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
			}
		});

		strokeButton3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
			}
		});

		backgroundButton.addActionListener(new ActionListener() {

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

		foreroundButton.addActionListener(new ActionListener() {

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


		
		straightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (straightButton.getText().equals("Straight")) {
					rubber = false;
					shape = 1;
					straightButton.setText("Free");
					RectangleButton.setText("Rectangle");
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
				} else {
					rubber = false;
					shape = 0;
					RectangleButton.setText("Rectangle");
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
					straightButton.setText("Straight");
					g.setColor(forecColor);
				}

			}
		});
		
		RectangleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (RectangleButton.getText().equals("Rectangle")) {
					rubber = false;
					shape = 2;
					RectangleButton.setText("Free");
					CircleButton.setText("Circle");
					OvalButton.setText("Oval");
					straightButton.setText("Straight");
				} else {
					rubber = false;
					shape = 0;
					RectangleButton.setText("Rectangle");
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
					straightButton.setText("Straight");
					g.setColor(forecColor);
				}

			}
		});
		
		OvalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OvalButton.getText().equals("Oval")) {
					rubber = false;
					shape = 3;
					OvalButton.setText("Free");
					CircleButton.setText("Circle");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
				} else {
					rubber = false;
					shape = 0;
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
					g.setColor(forecColor);
				}

			}
		});

		CircleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (CircleButton.getText().equals("Circle")) {
					rubber = false;
					shape = 4;
					CircleButton.setText("Free");
					OvalButton.setText("Oval");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
				} else {
					rubber = false;
					shape = 0;
					OvalButton.setText("Oval");
					CircleButton.setText("Circle");
					RectangleButton.setText("Rectangle");
					straightButton.setText("Straight");
					g.setColor(forecColor);
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

		// Thin菜单项
		strokeMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
			}
		});

		// Medium菜单项
		strokeMenuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
			}
		});

		// Thick菜单项
		strokeMenuItem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs);
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

			}
		});

		eraserMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem.getText().equals("Small Eraser")) {
					rubber = true;
					eraser_valjue = 0;
					eraserMenuItem.setText("Draw");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Large Eraser");
				} else {
					rubber = false;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Large Eraser");
					g.setColor(forecColor);
				}

			}
		});
		
		eraserMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem1.getText().equals("Medium Eraser")) {
					rubber = true;
					eraser_valjue = 1;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Draw");
					eraserMenuItem2.setText("Large Eraser");
				} else {
					rubber = false;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Large Eraser");
					g.setColor(forecColor);
				}

			}
		});
		
		eraserMenuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eraserMenuItem2.getText().equals("Large Eraser")) {
					rubber = true;
					eraser_valjue = 2;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Draw");
				} else {
					rubber = false;
					eraserMenuItem.setText("Small Eraser");
					eraserMenuItem1.setText("Medium Eraser");
					eraserMenuItem2.setText("Large Eraser");
					g.setColor(forecColor);
				}

			}
		});
		

		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});


		toolBar.addMouseMotionListener(new MouseMotionAdapter() {//工具栏添加鼠标事件监听
			@Override
			public void mouseMoved(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));//设置鼠标的形状为默认光标

			}

		});
	}


	public static void main(String[] args) {
		DrawPictureFrame frame = new DrawPictureFrame();
		frame.setVisible(true);

	}

}
