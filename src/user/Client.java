package user;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import manager.WhiteBoard;

public class Client {
	static String serverIPAddress;
	static int serverPort;
	static String userName;
//	public static UserConnection userConnection;
//	public static Socket userSocket;
	public static Socket socket;
	public static DataInputStream input;
	public static DataOutputStream output;

	private static JSONObject createJSON() {
		JSONObject userJson = new JSONObject();
		userJson.put("userName", userName);
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
			output.writeUTF(createJSON().toJSONString());
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
//				new UserWhiteBoard();
				System.out.println(request.equals("authorized"));
				if(request.equals("authorized")) {
					System.out.println("sss");
					new UserWhiteBoard();
				}
				
//				JSONObject resJSON = parseResString(request);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
