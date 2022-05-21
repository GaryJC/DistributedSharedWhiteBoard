package manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//import server.Connection;

public class Launcher {
	public static List<Connection> connections = new ArrayList<>();
	public static List<String> userNames = new ArrayList<>();
	public static int clientID = 0;
	@SuppressWarnings("resource")
	protected static void launch(int port, String userName) {
		Connection t1 = null;
		ServerSocket serverSocket = null;
		userNames.add(userName);
		System.out.println(port);
//		try {
//			serverSocket = new ServerSocket(port);
//		}catch (Exception e) {
//			System.out.println("Connection Failed");
////			System.exit(1);
//		}
//		while(true) {
//			Socket socket = null;
//			try {
//				socket = serverSocket.accept();
//			} catch (IOException e) {
//				System.out.println("IOException: " + e);
//			}
//			clientID++;
//			Connection connection = new Connection(socket);
//			connection.start();
//			System.out.println("Server received new connection");
////			while (true) {
////				client = server.accept();
////				System.out.println(client);
////				clientID++;
////				t1 = new Connection(client);
////				t1.start();
////			}
//		} 
		try {
			serverSocket = new ServerSocket(port);
			Socket client;
			while (true) {
				client = serverSocket.accept();
				System.out.println(client);
				clientID++;
				t1 = new Connection(client);
				t1.start();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
