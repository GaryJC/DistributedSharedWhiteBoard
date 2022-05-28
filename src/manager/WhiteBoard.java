package manager;

import java.awt.BasicStroke;
import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import java.awt.TextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class WhiteBoard extends JFrame implements MouseListener, MouseMotionListener {
	static String serverIPAddress;
	static int serverPort;
	static String userName;
	static Color color = Color.black;
	static String RGB = "0 0 0";
	static int x_start;
	static int y_start;
	static int x_end;
	static int y_end;
	static Graphics2D graph;
	static String tool = "Line";
	static String text = "";
	static String type = "draw";
	static String response = "";
	JSONObject paintData;
	static ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	static JPanel panel = new JPanel();
	private JTextField chatField;
	public static TextArea chatArea = new TextArea();
	static JList userList = new JList();
	static JScrollPane scrollPane;

	@SuppressWarnings("unchecked")
	public static JSONObject createJSON() {
		JSONObject paintJson = new JSONObject();
		paintJson.put("tool", tool);
		paintJson.put("type", type);
		paintJson.put("response", response);
		paintJson.put("RGB", RGB);
		paintJson.put("x_start", x_start);
		paintJson.put("y_start", y_start);
		paintJson.put("x_end", x_end);
		paintJson.put("y_end", y_end);
		paintJson.put("text", text);
		return paintJson;
	}
	
	private static JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			System.out.println("JSON parse error: " + e);
		}
		return resJSON;
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
				System.out.println("Arguments format not correct");
			}
		} else {
			serverIPAddress = "localhost";
			serverPort = 8888;
			userName = "Default Manager";
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
		setBounds(100, 100, 806, 594);
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
		lineButton.setBounds(10, 39, 75, 22);
		getContentPane().add(lineButton);

		Button circleButton = new Button("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Circle";
			}
		});
		circleButton.setBounds(10, 67, 75, 22);
		getContentPane().add(circleButton);

		Button rectButton = new Button("Rectangle");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Rect";
			}
		});
		rectButton.setBounds(10, 95, 75, 22);
		getContentPane().add(rectButton);

		Button triButton = new Button("Triangle");
		triButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Triangle";
			}
		});
		triButton.setBounds(10, 123, 75, 22);
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
		colorButton.setBounds(10, 179, 75, 22);
		getContentPane().add(colorButton);

		Button kickButton = new Button("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(userList.getSelectedValue()!=null) {
					String selectedUser = userList.getSelectedValue().toString();
					System.out.println(selectedUser);
					for (int i= 0; i < Connection.connections.size(); i++) {
						Connection perCon = Connection.connections.get(i);
						if(selectedUser.equals(perCon.userName)) {
							Connection.connections.remove(i);
							Connection.userNames.remove(selectedUser);
							userList.setListData(Connection.userNames.toArray());
							scrollPane.repaint();
							try {
								perCon.socket.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		kickButton.setBounds(10, 327, 75, 22);
		getContentPane().add(kickButton);  
		
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		panel.setBounds(95, 11, 685, 338);
		getContentPane().add(panel);
		
		Button textButton = new Button("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool = "Text";
			}
		});
		textButton.setBounds(10, 151, 75, 22);
		getContentPane().add(textButton);
		
		Button sendButton = new Button("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				type = "chat";
				if(!chatField.getText().isEmpty()) {
					String chatText = "chat-"+chatField.getText()+"-"+userName;
					Connection.syncChat(chatText.split("-"));
				}
			}
		});
		sendButton.setBounds(710, 523, 70, 22);
		getContentPane().add(sendButton);
		
		chatField = new JTextField();
		chatField.setBounds(95, 504, 609, 41);
		getContentPane().add(chatField);
		chatField.setColumns(10);
		
		
		chatArea.setEditable(false);
		chatArea.setBounds(95, 355, 685, 133);
		getContentPane().add(chatArea);
		
		scrollPane = new JScrollPane(userList);
		scrollPane.setBounds(10, 355, 75, 190);
		getContentPane().add(scrollPane);
		
		panel.addMouseListener(this);
//		panel.repaint();
//		System.out.println(this.graph);
	}
	
	@SuppressWarnings("unchecked")
	public static void setUserList(List<String> userNames) {
		userList.setListData(userNames.toArray());
		scrollPane.repaint();
	}


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
		type = "draw";
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
			break;
		case "Circle":
			int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
			graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
			paintData = createJSON();
			paintDataList.add(paintData);
			break;
		case "Text":
			text = JOptionPane.showInputDialog("Input text");
			if (text != null) {
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
		case "Triangle":
			int [] x = {x_end, x_end+50, x_end-50};
			int [] y = {y_end, y_end+100, y_end+100};
			graph.drawPolygon(x, y, 3);
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

	public static void draw(String[] list) {
//		String [] list = paintDataList.split("-");
		System.out.println(list);
		for(int i=0;i<list.length;i++) {
			JSONObject paintJson = parseResString(list[i]);
			x_start = (int) (long) paintJson.get("x_start");
			y_start = (int) (long) paintJson.get("y_start");
			x_end = (int) (long) paintJson.get("x_end");
			y_end = (int) (long) paintJson.get("y_end");
			RGB = (String) paintJson.get("RGB");	
			String[] RGBList = RGB.split(" ");
			graph.setColor(new Color(Integer.parseInt(RGBList[0]), Integer.parseInt(RGBList[1]), Integer.parseInt(RGBList[2])));
			if(paintJson.get("tool").equals("Line")) {
				graph.setStroke(new BasicStroke(1));
				graph.drawLine(x_start,y_start,x_end,y_end);
//				panel.repaint();
			}else if(paintJson.get("tool").equals("Circle")) {
				int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
				graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
			}else if(paintJson.get("tool").equals("Rect")) {
				graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
						Math.abs(y_start - y_end));
			}else if(paintJson.get("tool").equals("Text")) {
				text = (String) paintJson.get("text");
				System.out.println(text);
				graph.drawString(text, x_end, y_end);
			}else if(paintJson.get("tool").equals("Triangle")) {
				int [] x = {x_end, x_end+50, x_end-50};
				int [] y = {y_end, y_end+100, y_end+100};
				graph.drawPolygon(x, y, 3);
			}
		}
	}
}
