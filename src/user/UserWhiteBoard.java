package user;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Panel;

//import org.json.JSONObject;

@SuppressWarnings("serial")
public class UserWhiteBoard extends JFrame{
//	static String serverIPAddress;
//	static int serverPort;
//	static String userName;
//	static Painter canvas;
	static Color color = Color.black;
	static String RGB = "0 0 0";
	static int x_start;
	static int y_start;
	static int x_end;
	static int y_end;
	static Graphics2D graph;
//	static String tool = "Line";
	static String text = "";
	static String type = "draw";
	
	JSONObject paintData;
	ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	private JFrame frame;
	static JPanel panel = new JPanel();
	static PaintPanel paintPanel;

//	public static Socket userSocket;

	private static JSONObject createJSON() {
		JSONObject jsonData = new JSONObject();
//		jsonData.put("userName", userName);
		jsonData.put("tool", PaintPanel.tool);
		jsonData.put("type", type);
		jsonData.put("RGB", RGB);
		jsonData.put("x_start", x_start);
		jsonData.put("y_start", y_start);
		jsonData.put("x_end", x_end);
		jsonData.put("y_end", y_end);
		jsonData.put("text", text);
		return jsonData;
	}

	private static JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Exception: " + e);
		}
		return resJSON;
	}

	/**
	 * Launch the application.
	 * @param test 
	 */

	public UserWhiteBoard() {
		initialize();
//		frame.setVisible(true);
//		graph = (Graphics2D)panel.getGraphics();
//		draw(test);
	}


	/**
	 * Create the frame.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 844, 554);
		frame.getContentPane().setLayout(null);

		Button lineButton = new Button("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Line";
			}
		});
		lineButton.setBounds(114, 11, 70, 22);
		frame.getContentPane().add(lineButton);

		Button circleButton = new Button("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Circle";
			}
		});
		circleButton.setBounds(217, 11, 70, 22);
		frame.getContentPane().add(circleButton);

		Button rectButton = new Button("Rectangle");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Rect";
			}
		});
		rectButton.setBounds(319, 11, 70, 22);
		frame.getContentPane().add(rectButton);

		Button triButton = new Button("Triangle");
		triButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Triangle";
			}
		});
		triButton.setBounds(422, 11, 70, 22);
		frame.getContentPane().add(triButton);

		Button colorButton = new Button("Color");
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Panel colorPanel = new Panel();
				Color chosenColor = JColorChooser.showDialog(colorPanel, "Choose Color", null);
				if (chosenColor != null) {
					PaintPanel.color = chosenColor;
				}
			}
		});
		colorButton.setBounds(614, 11, 70, 22);
		frame.getContentPane().add(colorButton);

		Button textButton = new Button("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Text";
			}
		});
		textButton.setBounds(518, 11, 70, 22);
		frame.getContentPane().add(textButton);

		Button kickButton = new Button("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		kickButton.setBounds(710, 11, 70, 22);
		frame.getContentPane().add(kickButton);
		
//		frame.add(new JPanel() {
//		    @Override
//		    protected void paintComponent(Graphics graph) {
//		        super.paintComponent(graph);
//		        setBackground(Color.WHITE);
//				setForeground(Color.BLACK);
//				setBounds(29, 69, 706, 341);
//		    }
//		});
		
		paintPanel = new PaintPanel();
		paintPanel.setBackground(Color.WHITE);
		paintPanel.setForeground(Color.BLACK);
		paintPanel.setBounds(29, 69, 706, 341);
		PaintPanel.setList(Client.paintDataList);
		frame.getContentPane().add(paintPanel);

//		panel.setBackground(Color.WHITE);
//		panel.setForeground(Color.BLACK);
//		panel.setBounds(29, 69, 706, 341);
//		frame.getContentPane().add(panel);
//
//		panel.addMouseListener(this);
//		panel.repaint();
//		System.out.println(this.graph);
		frame.setVisible(true);
		graph = (Graphics2D) panel.getGraphics();
	}

//	public void setG(Graphics g) {
//		this.graph = (Graphics2D) g;
//	}

//	@Override
//	public void mousePressed(MouseEvent e) {
//		// TODO Auto-generated method stub
//		x_start = e.getX();
//		y_start = e.getY();
////		graph.setColor(color);
////		if (!graph.getColor().equals(color)) {
////			graph.setColor(color);
////		}
//		graph.setColor(color);
////		System.out.println(x_start);
//	}
//
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		// TODO Auto-generated method stub
//		x_end = e.getX();
//		y_end = e.getY();
//		RGB = color.getRed() + " " + color.getGreen() + " " + color.getBlue();
//		switch (tool) {
//		case "Line":
////			graph.setColor(color);
//			graph.setStroke(new BasicStroke(1));
//			graph.drawLine(x_start, y_start, x_end, y_end);
//			paintData = createJSON();
//			paintDataList.add(paintData);
////			System.out.println(paintDataList);
//			Client.fetchData(paintDataList);
////			try {
////				
////			}catch{
////				
////			}
//			break;
//		case "Circle":
//			int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
//			graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
//			paintData = createJSON();
//			paintDataList.add(paintData);
//			Client.fetchData(paintDataList);
//			break;
//		case "Text":
//			text = JOptionPane.showInputDialog("Input text");
//			if (text != null) {
//				graph.drawString(text, x_end, y_end);
//				paintData = createJSON();
//				paintDataList.add(paintData);
//				Client.fetchData(paintDataList);
//			}
//			break;
//		case "Rect":
//			graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
//					Math.abs(y_start - y_end));
//			paintData = createJSON();
//			paintDataList.add(paintData);
//			Client.fetchData(paintDataList);
//			break;
//		}
////		 boardCast
//	}
//
//	@Override
//	public void mouseMoved(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseExited(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseDragged(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}

//	public void paint(Graphics g) {
//		super.paint(g);
////		draw((Graphics2D)g, paintDataList);
//		System.out.println("SSss");
//	}

	public static void draw(String paintDataList) {
		System.out.println("dddd: "+paintDataList);
		if(paintDataList.isEmpty()) {
			graph.drawLine(0, 0, 0, 0);
		}else {
			String[] list = paintDataList.split("-");
			for (int i = 0; i < list.length; i++) {
				JSONObject paintJson = parseResString(list[i]);
				x_start = (int) (long) paintJson.get("x_start");
				y_start = (int) (long) paintJson.get("y_start");
				x_end = (int) (long) paintJson.get("x_end");
				y_end = (int) (long) paintJson.get("y_end");
				RGB = (String) paintJson.get("RGB");
				String[] RGBList = RGB.split(" ");
				graph.setColor(new Color(Integer.parseInt(RGBList[0]), Integer.parseInt(RGBList[1]),
						Integer.parseInt(RGBList[2])));
				if (paintJson.get("tool").equals("Line")) {
					graph.setStroke(new BasicStroke(1));
					graph.drawLine(x_start, y_start, x_end, y_end);
//						panel.repaint();
				} else if (paintJson.get("tool").equals("Circle")) {
					int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
					graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
				} else if (paintJson.get("tool").equals("Rect")) {
					graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
							Math.abs(y_start - y_end));
				} else if (paintJson.get("tool").equals("Text")) {
					text = (String) paintJson.get("text");
//					System.out.println(text);
					graph.drawString(text, x_end, y_end);
				}
			}
		}	
	}
	
}
