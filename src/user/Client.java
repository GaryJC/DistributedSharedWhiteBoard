package user;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import client.Connection;
//import manager.WhiteBoard;

public class Client {
	static String serverIPAddress;
	static int serverPort;
	static String userName;
//	public static UserConnection userConnection;
//	public static Socket userSocket;
	public static Socket socket;
	public static DataInputStream input;
	public static DataOutputStream output;

	private static String RGB = "0 0 0";
	private static int x_start;
	private static int y_start;
	private static int x_end;
	private static int y_end;
	static ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	private static String text;
	private static String tool;
	static PaintPanel paintPanel;

//	static PaintPanel dsb = new PaintPanel();
	private static JSONObject createJSON() {
		JSONObject jsonData = new JSONObject();
		jsonData.put("tool", tool);
		jsonData.put("type", "draw");
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

		try {
			socket = new Socket(serverIPAddress, serverPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
//			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			output.writeUTF("userName-" + userName);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

//		System.out.println(userName);

//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					UserWhiteBoard frame = new UserWhiteBoard();
//					frame.setVisible(true);
//					graph = (Graphics2D)panel.getGraphics();
////					System.out.println(graph);
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//			}
//		});
//		new UserWhiteBoard();
		launch();
	}

	public static void launch() {
		try {
			while (true) {
				String request = input.readUTF();
				String[] list = request.split("-");
				System.out.println("req: "+request);
				JSONObject paintJson = parseResString(list[0]);
	
				String type = (String) paintJson.get("type");
				if (type.equals("join")) {
					String resp = (String) paintJson.get("response");
					if (resp.equals("authorized")) {
						for (int i = 1; i < list.length; i++) {
							JSONObject data = parseResString(list[i]);
							x_start = (int) (long) data.get("x_start");
							y_start = (int) (long) data.get("y_start");
							x_end = (int) (long) data.get("x_end");
							y_end = (int) (long) data.get("y_end");
							tool = (String) data.get("tool");
							RGB = (String) data.get("RGB");
							JSONObject paintData = createJSON();
							paintDataList.add(paintData);
						}
						PaintPanel.setList(paintDataList);
						new UserWhiteBoard();
						UserWhiteBoard.paintPanel.repaint();
					}
					else if (resp.equals("existed")) {
						JOptionPane.showMessageDialog(null, "The username is already existed");
					} else if (resp.equals("refused")) {
						JOptionPane.showMessageDialog(null, "Your application is refused by manager");
					}
				}

				else if (type.equals("draw")) {
					for (int i = 0; i < list.length; i++) {
						paintJson = parseResString(list[i]);
						x_start = (int) (long) paintJson.get("x_start");
						y_start = (int) (long) paintJson.get("y_start");
						x_end = (int) (long) paintJson.get("x_end");
						y_end = (int) (long) paintJson.get("y_end");
						tool = (String) paintJson.get("tool");
						text = (String) paintJson.get("text");
						RGB = (String) paintJson.get("RGB");
						JSONObject paintData = createJSON();
						paintDataList.add(paintData);
					}

					PaintPanel.setList(paintDataList);
					UserWhiteBoard.paintPanel.repaint();
				}else if(type.equals("chat")) {
					Object chatList = paintJson.get("chatList");
					String slicedList = chatList.toString().substring(1, chatList.toString().length()-1);
					String[] chatArr = slicedList.split(",");
					UserWhiteBoard.chatArea.setText("");
					for(int i =0;i<chatArr.length;i++) {
//						System.out.println(chatArr[i]);
						String[] chatDetail = chatArr[i].substring(1, chatArr[i].length()-1).split(":");
						UserWhiteBoard.chatArea.append(chatDetail[0]+": "+chatDetail[1]+"\n");
					}
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Disconnected with server."); 
			System.out.println("Server closed or not launcehd");
			System.exit(0);
		}
	}

	public static void fetchData(ArrayList<JSONObject> paintDataList) {
//		System.out.println(paintDataList);
		String jsonString = paintDataList.stream().map(Object::toString).collect(Collectors.joining("-"));
		try {
			output.writeUTF(jsonString);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
