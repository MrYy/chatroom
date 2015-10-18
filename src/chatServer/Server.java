package chatServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	/*
	 * Server�������������������� ������һ���ͻ��˵�socket�Ϳ���һ���µ��̴߳���
	 */
	public static final int PORT = 12345;

	public static void main(String args[]){
		try {
			ServerSocket serverSocket=new ServerSocket(PORT);
			System.out.println("Server is running");
			while(true){
				ServerThread serverThread=new ServerThread(serverSocket.accept());
				new Thread(serverThread).start();
			}
		}catch(IOException exception){
			System.exit(1);
			System.out.println("Server is closed");
			
		}
	}
}
