package user;

import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.JColorChooser;
import java.awt.Color;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import javax.swing.JTextField;
import java.awt.TextArea;

@SuppressWarnings("serial")
public class UserWhiteBoard extends JFrame{
	static Color color = Color.black;
	static String RGB = "0 0 0";
	static int x_start;
	static int y_start;
	static int x_end;
	static int y_end;
	static Graphics2D graph;
	static String text = "";
	static String type = "draw";
	
	JSONObject paintData;
	ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	private JFrame frame;
	static JPanel panel = new JPanel();
	static PaintPanel paintPanel;
	private JTextField chatField;
	static String chatText = "";
	public static TextArea chatArea = new TextArea();
	
	private static JSONObject createChatJSON() {
		JSONObject jsonData = new JSONObject();
		jsonData.put("type", type);
		jsonData.put("chatText", chatText);
		jsonData.put("userName", Client.userName);
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
		frame.setBounds(100, 100, 806, 594);
		frame.getContentPane().setLayout(null);

		Button lineButton = new Button("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Line";
			}
		});
		lineButton.setBounds(21, 11, 70, 22);
		frame.getContentPane().add(lineButton);

		Button circleButton = new Button("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Circle";
			}
		});
		circleButton.setBounds(21, 39, 70, 22);
		frame.getContentPane().add(circleButton);

		Button rectButton = new Button("Rectangle");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Rect";
			}
		});
		rectButton.setBounds(21, 67, 70, 22);
		frame.getContentPane().add(rectButton);

		Button triButton = new Button("Triangle");
		triButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Triangle";
			}
		});
		triButton.setBounds(21, 95, 70, 22);
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
		colorButton.setBounds(21, 151, 70, 22);
		frame.getContentPane().add(colorButton);

		Button textButton = new Button("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaintPanel.tool = "Text";
			}
		});
		textButton.setBounds(21, 123, 70, 22);
		frame.getContentPane().add(textButton);

		Button kickButton = new Button("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		kickButton.setBounds(21, 179, 70, 22);
		frame.getContentPane().add(kickButton);
	
		paintPanel = new PaintPanel();
		paintPanel.setBackground(Color.WHITE);
		paintPanel.setForeground(Color.BLACK);
		paintPanel.setBounds(110, 11, 659, 332);
		PaintPanel.setList(Client.paintDataList);
		frame.getContentPane().add(paintPanel);
		
		Button chatButton = new Button("Send");
		chatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "chat";
				chatText = chatField.getText();
				if(!chatText.isEmpty()) {
//					JSONObject chatJson = createChatJSON();
					try {
						Client.output.writeUTF("chat-"+chatText+"-"+Client.userName);
						Client.output.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
				}
			}
		});
		chatButton.setBounds(699, 511, 70, 22);
		frame.getContentPane().add(chatButton);
		
		chatField = new JTextField();
		chatField.setBounds(110, 496, 585, 36);
		frame.getContentPane().add(chatField);
		chatField.setColumns(10);
		
		
		chatArea.setEditable(false);
		chatArea.setBounds(110, 364, 659, 118);
		frame.getContentPane().add(chatArea);

		frame.setVisible(true);
		graph = (Graphics2D) panel.getGraphics();
	}
}
