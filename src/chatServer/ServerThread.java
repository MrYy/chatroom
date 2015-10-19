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
	// 控制线程是否运行
	public volatile boolean flag = true;

	public boolean login_status = false;
	// 状态变量，用来确定是命令还是消息
	private int status;
	private HashMap<String, Integer> hashMap;
	private String user_login;

	//
	public ServerThread(Socket socket) {
		this.socket = socket;
		// hashmap用来存放状态命令值
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
			// 调用线程池进行优化。
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			// 向当前登录的用户输出消息的输出流。

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
					printStream.println("您已经退出");
					break;
				case Constant.REGISTER:
					// 注册的格式是/register account password
					// 登录注册的账号密码相应的格式，在前端规定，不返回服务器进行判断
					MySQLConnect mySQLConnect_register = new MySQLConnect(message, status, socket);
					// 这里最好使用线程池。
					mySQLConnect_register.connectMySQL();
					break;
				case Constant.LOGIN:
					// 登录的格式是/login account password
					MySQLConnect mySQLConnect_login = new MySQLConnect(message, status, socket);
					mySQLConnect_login.connectMySQL();
					// System.out.println("登录的用户数:" +
					// Server.concurrentHashMap.size());
					if (mySQLConnect_login.returnStatus()) {
						login_status = true;
						user_login=mySQLConnect_login.returnName();
						Server.concurrentHashMap.put(user_login,socket);
						
					}
					break;
				case Constant.SEND_MESSAGE:
					System.out.println("message receive:" + message);
					// 返回给当前客户的消息
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

	// 得到socket输入信息
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
