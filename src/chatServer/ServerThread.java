package chatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread implements Runnable {
	private Socket socket = null;
	// �����߳��Ƿ�����
	public volatile boolean flag = true;

	//
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			socket.setKeepAlive(true);
			while (flag) {
				getMessage();
			}
		} catch (SocketException e) {
			e.printStackTrace();

		}
	}

	private BufferedReader bufferedReader = null;

	// �õ�socket������Ϣ
	public String getMessage() {
		String message = null;
		while (flag) {
			try {
				bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				message=bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message=null;
			}
			System.out.println("message receive:"+message);
			return message;
		}
		return null;
	}

}
