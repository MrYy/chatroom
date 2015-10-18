package chatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread implements Runnable {
	BufferedReader bufferedReader = null;
	private Socket socket;

	public ReceiveThread(Socket socket) {
		this.socket = socket;

	}

	public void run() {
		try {

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = null;
			while (!socket.isClosed()) {

				message = bufferedReader.readLine();
				if (!message.equals(null)) {
					System.out.println(message);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);

	}
}
