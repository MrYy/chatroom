package chatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SendThread implements Runnable {
	private Socket socket = null;
	private String message = null;

	public SendThread(Socket socket, String message) {
		this.socket = socket;
		this.message = message;
	}

	
	private PrintStream printStream = null;
	private boolean flag = false;

	public void run() {
			try {
//				System.out.println("sendThread");
				printStream=new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (flag) {
				if (!message.equals(null)) {
					if (message.equals("/quit")) {
						flag = false;
						printStream.close();
					} else {
						printStream.println(message);
					}
				}
			}
			printStream.close();
		

	}

}
