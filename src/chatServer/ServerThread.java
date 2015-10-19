package chatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread implements Runnable {
	private Socket socket = null;
	// �����߳��Ƿ�����
	public volatile boolean flag = true;

	public boolean login_status = false;
	// ״̬����������ȷ�����������Ϣ
	private int status;
	private HashMap<String, Integer> hashMap;
	private String user_login;

	//
	public ServerThread(Socket socket) {
		this.socket = socket;
		// hashmap�������״̬����ֵ
		hashMap = new HashMap<String, Integer>();
		hashMap.put("/quit", Constant.QUIT);
		hashMap.put("/login", Constant.LOGIN);
		hashMap.put("/register", Constant.REGISTER);

	}

	private SendThread sendThread;

	public void run() {
		try {
			String message = null;
			socket.setKeepAlive(true);
			// �����̳߳ؽ����Ż���
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			// ��ǰ��¼���û������Ϣ���������

			PrintStream printStream = new PrintStream(socket.getOutputStream());
			while (flag) {
				message = getMessage();
				if (hashMap.containsKey(message.split(" ")[0])) {
					status = hashMap.get(message.split(" ")[0]);
				} else if (login_status) {
					status = Constant.SEND_MESSAGE;
				} else {
					status = Constant.PLEASE_LOGIN;
				}
				switch (status) {
				case Constant.QUIT:
					login_status = false;
					Server.concurrentHashMap.remove(user_login);
					printStream.println("���Ѿ��˳�");
					break;
				case Constant.REGISTER:
					// ע��ĸ�ʽ��/register account password
					// ��¼ע����˺�������Ӧ�ĸ�ʽ����ǰ�˹涨�������ط����������ж�
					MySQLConnect mySQLConnect_register = new MySQLConnect(message, status, socket);
					// �������ʹ���̳߳ء�
					mySQLConnect_register.connectMySQL();
					break;
				case Constant.LOGIN:
					// ��¼�ĸ�ʽ��/login account password
					MySQLConnect mySQLConnect_login = new MySQLConnect(message, status, socket);
					mySQLConnect_login.connectMySQL();
					// System.out.println("��¼���û���:" +
					// Server.concurrentHashMap.size());
					if (mySQLConnect_login.returnStatus()) {
						login_status = true;
						user_login=mySQLConnect_login.returnName();
						Server.concurrentHashMap.put(user_login,socket);
						
					}
					break;
				case Constant.SEND_MESSAGE:
					System.out.println("message receive:" + message);
					// ���ظ���ǰ�ͻ�����Ϣ
					printStream.println("You said:" + message);
					break;
				case Constant.PLEASE_LOGIN:
					printStream.println("Please Login");
					break;
				default:
					break;
				}

				// sendThread=new SendThread(socket, message);
				// executorService.execute(sendThread);

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedReader bufferedReader = null;

	// �õ�socket������Ϣ
	public String getMessage() {
		String message = null;
		while (flag) {
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				message = bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!message.equals(null)) {

				return message;
			}
		}
		return null;
	}

}
