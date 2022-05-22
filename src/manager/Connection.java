package manager;

import java.net.ServerSocket;
import java.net.Socket;
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
	public static int clientID = 0;

	public boolean kick = false;
	static Boolean isAuth = false;

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}
	
	private static JSONObject createJSON() {
		JSONObject userJson = new JSONObject();
		userJson.put("isAuth", isAuth);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String request;
		try {
			while ((request = input.readUTF()) != null) {
				System.out.println(request);
				String [] list = request.split("-");
				if(list[0].equals("userName")) {
					userName = list[1];
					if(userNames.contains(userName)) {
						output.writeUTF("existed");
						output.flush();
						socket.close();
					}else {
						int joinPopup = JOptionPane.showConfirmDialog(null, userName + " wants to share the board", "Ok", JOptionPane.INFORMATION_MESSAGE);
						if(JOptionPane.YES_OPTION == joinPopup) {
							userNames.add(userName);
							isAuth = true;
//							output.writeUTF(createJSON().toJSONString());
							output.writeUTF("authorized");
							output.flush();
						}else {
							isAuth = false;
							output.writeUTF("refused");
							output.flush();
							//LaunchServer.connections.remove(this)
						}
					}
				}else {
					WhiteBoard.draw(list);
					syncData(request);
				}
//				JSONObject resJSON = parseResString(request);
//				userName = (String) resJSON.get("userName");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void syncData(String request) {
		// TODO Auto-generated method stub
		for(int i=0;i<connections.size();i++) {
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
		System.out.println(paintDataList);
		String jsonString = paintDataList.stream().map(Object::toString)
                .collect(Collectors.joining("-"));

		for(int i=0;i<connections.size();i++) {
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
