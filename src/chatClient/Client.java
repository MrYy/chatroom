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
//				�������˳�����ʱ�������߳̽������ͻ���main�����������ر�socket��
			}
			socket.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
			
		}
		
		System.exit(0);
//		����Ϊ0����ʾ�����˳���
	}
}
