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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Panel;

//import org.json.JSONObject;

@SuppressWarnings("serial")
public class UserWhiteBoard extends JFrame implements MouseListener, MouseMotionListener {
	static String serverIPAddress;
	static int serverPort;
	static String userName;
//	static Painter canvas;
	static Color color = Color.black;
	static String RGB = "0 0 0";
	static int x_start;
	static int y_start;
	static int x_end;
	static int y_end;
	static Graphics2D graph;
	static String tool = "Line";
	static Boolean isAuth = false;
	JSONObject paintData;
	ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	private JFrame frame;
	static JPanel panel = new JPanel();
	
	public static UserConnection userConnection;
	public static Socket userSocket;

	private static JSONObject createJSON() {
		JSONObject jsonData = new JSONObject();
		jsonData.put("isAuth", isAuth);
		jsonData.put("userName", userName);
		jsonData.put("tool", tool);
		jsonData.put("RGB", RGB);
		jsonData.put("x_start", x_start);
		jsonData.put("y_start", y_start);
		jsonData.put("x_end", x_end);
		jsonData.put("y_end", y_end);
		return jsonData;
	}

	/**
	 * Launch the application.
	 */

//	public void run() {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					UserWhiteBoard frame = new UserWhiteBoard();
//					frame.setVisible(true);
//					graph = (Graphics2D)panel.getGraphics();
////					System.out.println(graph);
//				} catch (Exception e) {
//					System.out.println("e");
//				}
//			}
//		});
//	}

//	public int notification(String userName) {
//		int option = JOptionPane.showConfirmDialog(null, userName + " wants to join", "OK",
//				JOptionPane.INFORMATION_MESSAGE);
//		return option;
//	}
	
	public UserWhiteBoard() {
		initialize();
//		frame.setVisible(true);
//		graph = (Graphics2D)panel.getGraphics();
	}

	/**
	 * Create the frame.
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
				tool = "Line";
			}
		});
		lineButton.setBounds(114, 11, 70, 22);
		frame.getContentPane().add(lineButton);

		Button circleButton = new Button("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Circle";
			}
		});
		circleButton.setBounds(217, 11, 70, 22);
		frame.getContentPane().add(circleButton);

		Button rectButton = new Button("Rectangle");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Rect";
			}
		});
		rectButton.setBounds(319, 11, 70, 22);
		frame.getContentPane().add(rectButton);

		Button triButton = new Button("Triangle");
		triButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Triangle";
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
					color = chosenColor;
				}
			}
		});
		colorButton.setBounds(528, 11, 70, 22);
		frame.getContentPane().add(colorButton);

		Button kickButton = new Button("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		kickButton.setBounds(630, 11, 70, 22);
		frame.getContentPane().add(kickButton);

		
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		panel.setBounds(29, 69, 706, 341);
		frame.getContentPane().add(panel);
		panel.addMouseListener(this);
//		panel.repaint();
//		System.out.println(this.graph);
		frame.setVisible(true);
		graph = (Graphics2D)panel.getGraphics();
	}

//	public void setG(Graphics g) {
//		this.graph = (Graphics2D) g;
//	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		x_start = e.getX();
		y_start = e.getY();
//		graph.setColor(color);
//		if (!graph.getColor().equals(color)) {
//			graph.setColor(color);
//		}
		graph.setColor(color);
//		System.out.println(x_start);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		x_end = e.getX();
		y_end = e.getY();
		RGB = color.getRed() + " " + color.getGreen() + " " + color.getBlue();
		switch (tool) {
		case "Line":
//			graph.setColor(color);
			graph.setStroke(new BasicStroke(1));
			graph.drawLine(x_start, y_start, x_end, y_end);
			paintData = createJSON();
			paintDataList.add(paintData);
			System.out.println(paintDataList);
//			try {
//				
//			}catch{
//				
//			}
			break;
		case "Circle":
			int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
			graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
			paintData = createJSON();
			paintDataList.add(paintData);
			break;
		case "Text":
			String text = JOptionPane.showInputDialog("Input text");
			if (text != null) {
//				Font font = new Font(null, Font.PLAIN);
				graph.drawString(text, x_end, y_end);
				paintData = createJSON();
				paintDataList.add(paintData);
			}
			break;
		case "Rect":
			graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
					Math.abs(y_start - y_end));
			paintData = createJSON();
			paintDataList.add(paintData);
			break;
		}
//		 boardCast
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

//	public void paint(Graphics g) {
//		super.paint(g);
////		draw((Graphics2D)g, paintDataList);
//		System.out.println(paintDataList);
//	}

//	public void draw(Graphics2D g, ArrayList<JSONObject> paintDataList) {
//		System.out.println("ss");
//	}
}
