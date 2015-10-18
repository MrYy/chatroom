package chatClient;

import java.io.IOException;
import java.net.Socket;

public class Client {
	public static void main(String[]args){
		try{
			Socket socket=new Socket("localhost", 12345);
			SendThread sendThread=new SendThread(socket);
			new Thread(sendThread).start();
			ReceiveThread receiveThread=new ReceiveThread(socket);
			new Thread(receiveThread).start();
			while(sendThread.flag){
//				当输入退出命令时，发送线程结束，客户端main方法结束，关闭socket。
			}
			socket.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
			
		}
		
		System.exit(0);
//		参数为0，表示正常退出。
	}
}
