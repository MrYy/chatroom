package chatServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	/*
	 * Server运行主方法开启服务器 监听到一个客户端的socket就开启一个新的线程处理
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
