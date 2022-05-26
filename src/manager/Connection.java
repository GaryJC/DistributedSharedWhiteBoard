package manager;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.server.RemoteStub;
//import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import user.UserWhiteBoard;

//import server.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection extends Thread {
	public Socket socket;
	public static String userName;
	public DataInputStream input;
	public DataOutputStream output;

	public static List<Connection> connections = new ArrayList<>();
	public static List<String> userNames = new ArrayList<>();
	public static List<String> chatList = new ArrayList<>();
	public static int clientID = 0;

	public boolean kick = false;
	static String type = "join";
	static String response = "";

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	private static JSONObject createJSON() {
		JSONObject userJson = new JSONObject();
		userJson.put("type", type);
		userJson.put("response", response);
		userJson.put("chatList", chatList);
		return userJson;
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

	public void run() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			String request;

			while ((request = input.readUTF()) != null) {
				System.out.println("req: " + request);

				String[] list = request.split("-");
				if (list[0].equals("userName")) {
					userName = list[1];
					type = "join";
					if (userNames.contains(userName)) {
						response = "existed";
						String jsonData = createJSON().toJSONString();
						output.writeUTF(jsonData);
						output.flush();
						connections.remove(this);
//						socket.close();
					} else {
						int joinPopup = JOptionPane.showConfirmDialog(null, userName + " wants to share the board",
								"Ok", JOptionPane.INFORMATION_MESSAGE);
						if (JOptionPane.YES_OPTION == joinPopup) {
							userNames.add(userName);
							response = "authorized";
							ArrayList<JSONObject> dataList = new ArrayList<JSONObject>();
							JSONObject jsonData = createJSON();
							dataList.add(jsonData);
							dataList.addAll(WhiteBoard.paintDataList);
//							System.out.println("za: " + WhiteBoard.paintDataList);
							String js = dataList.stream().map(Object::toString).collect(Collectors.joining("-"));
							output.writeUTF(js);
							output.flush();
						} else {
							response = "refused";
							String jsonData = createJSON().toJSONString();
							output.writeUTF(jsonData);
							output.flush();
							connections.remove(this);
							socket.close();
						}
					}
				} else if (list[0].equals("chat")) {
					syncChat(list);
					
				} else {
					WhiteBoard.draw(list);
					syncData(request);
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("User disconnected");
			connections.remove(this);
			userNames.remove(userName);
			JOptionPane.showConfirmDialog(null, userName + " disconnected");
		} catch (Exception e1) {
			// TODO: handle exception
			System.out.println("User connection error");
		}
	}

	private void syncData(String request) {
		// TODO Auto-generated method stub
		for (int i = 0; i < connections.size(); i++) {
			Connection con = connections.get(i);
			try {
				con.output.writeUTF(request);
				con.output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected static void launch(int port, String userName) {
		System.out.println(port);
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					System.out.println("IOException: " + e);
				}
				Connection connection = new Connection(socket);
				connections.add(connection);
				connection.start();
				System.out.println("Server received new connection");
			}
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}

	public static void fetchData(ArrayList<JSONObject> paintDataList) {
		String jsonString = paintDataList.stream().map(Object::toString).collect(Collectors.joining("-"));
		if (!jsonString.isEmpty()) {
			for (int i = 0; i < connections.size(); i++) {
				Connection con = connections.get(i);
				try {
					con.output.writeUTF(jsonString);
					con.output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void syncChat(String[] list) {
		chatList.add(list[2] + ":" + list[1]);
		WhiteBoard.chatArea.setText("");
		type = "chat";
//		System.out.println(chatList);
		for (int i = 0; i < chatList.size(); i++) {
			String[] arr = chatList.get(i).split(":");
			System.out.println(chatList.get(i).split(":"));
			WhiteBoard.chatArea.append(arr[0] + ": " + arr[1] + "\n");
		}
		String chatJson = createJSON().toJSONString();
		for (int i = 0; i < connections.size(); i++) {
			Connection con = connections.get(i);
			try {
				con.output.writeUTF(chatJson);
				con.output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
