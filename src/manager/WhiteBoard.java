package manager;

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
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Panel;

//import org.json.JSONObject;

@SuppressWarnings("serial")
public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener {
	static String serverIPAddress;
	static int serverPort;
	static String userName;
//	static Painter canvas;
	static Color color = Color.black;
	String RGB = "0 0 0";
	int x_start, y_start, x_end, y_end;
	static Graphics2D graph;
	static String tool = "Line";
	JSONObject paintData;
	ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	static JPanel panel = new JPanel();
//	Connection connection = new Connection(null);

	private JSONObject createJSON() {
		JSONObject paintJson = new JSONObject();
		paintJson.put("tool", tool);
		paintJson.put("RGB", RGB);
		paintJson.put("x_start", x_start);
		paintJson.put("y_start", y_start);
		paintJson.put("x_end", x_end);
		paintJson.put("y_end", y_end);
		return paintJson;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length >= 3) {
			try {
				serverIPAddress = args[0];
				serverPort = Integer.parseInt(args[1]);
				userName = args[2];
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			serverIPAddress = "localhost";
			serverPort = 8888;
			userName = "Default User";
		}

//		System.out.println(userName);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoard frame = new WhiteBoard();
					frame.setVisible(true);
					graph = (Graphics2D)panel.getGraphics();
//					System.out.println(graph);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
//		Launcher.launch(serverPort, userName);
		Connection.launch(serverPort, userName);
	}

	public int notification(String userName) {
		int option = JOptionPane.showConfirmDialog(null, userName + " wants to join", "OK",
				JOptionPane.INFORMATION_MESSAGE);
		return option;
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WhiteBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 844, 554);
		getContentPane().setLayout(null);
		@SuppressWarnings("rawtypes")
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 11, 75, 22);
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "New", "Open", "Save", "Save as", "Close" }));
		comboBox.addActionListener(event -> {
			String selectedItem = comboBox.getSelectedItem().toString();
			switch (selectedItem) {
			case "New":
				System.out.println("New");
				// boardcast
				break;
			case "Open":
				System.out.println("Open");
				break;
			case "Save":
				System.out.println("Save");
				break;
			case "Save as":
				System.out.println("Save as");
				break;
			case "Close":
				System.out.println("Close");
				System.exit(1);
				break;
			}
		});
		getContentPane().add(comboBox);

		Button lineButton = new Button("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Line";
			}
		});
		lineButton.setBounds(114, 11, 70, 22);
		getContentPane().add(lineButton);

		Button circleButton = new Button("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Circle";
			}
		});
		circleButton.setBounds(217, 11, 70, 22);
		getContentPane().add(circleButton);

		Button rectButton = new Button("Rectangle");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Rect";
			}
		});
		rectButton.setBounds(319, 11, 70, 22);
		getContentPane().add(rectButton);

		Button triButton = new Button("Triangle");
		triButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Triangle";
			}
		});
		triButton.setBounds(422, 11, 70, 22);
		getContentPane().add(triButton);

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
		getContentPane().add(colorButton);

		Button kickButton = new Button("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		kickButton.setBounds(630, 11, 70, 22);
		getContentPane().add(kickButton);

		
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		panel.setBounds(29, 69, 706, 341);
		getContentPane().add(panel);
		panel.addMouseListener(this);
//		panel.repaint();
//		System.out.println(this.graph);
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
			Connection.fetchData(paintDataList);
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
	
		Connection.fetchData(paintDataList);
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

	public void draw(Graphics2D g, ArrayList<JSONObject> paintDataList) {
		System.out.println("ss");
	}
}
