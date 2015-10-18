package chatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.PseudoColumnUsage;

public class SendThread implements Runnable {
	Socket socket;
	BufferedReader bufferedReader;
	PrintStream printStream;
	public volatile boolean flag = true;
	private String message=null;

	public SendThread(Socket socket) throws IOException {
		this.socket = socket;
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	public void quit(){
		printStream.close();
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.exit(1);
			e.printStackTrace();
		}finally{
			flag=false;
		}
	}
	public void run(){
		try {
			printStream=new PrintStream(socket.getOutputStream());
			while(!socket.isClosed()){
				message=bufferedReader.readLine();
				printStream.println(message);
				if(message.equals("/q")){
					System.exit(0);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
