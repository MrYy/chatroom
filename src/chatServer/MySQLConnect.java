package chatServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnect {
	private String SQL = null;
	private int status;
	private static final String url = "jdbc:mysql://localhost:3306/chat";
	private static final String name = "com.mysql.jdbc.Driver";
	private static final String user = "root";
	private static final String password = "root";
	private Connection connection;
	private ResultSet resultSet;
	private String message = null;
	private PreparedStatement preparedStatement = null;
	private PrintStream printStream;
	private Socket socket;
	private boolean login_status=false;
	private String userName;
	public MySQLConnect(String message, int status, Socket socket) {
		this.status = status;
		this.message = message;
		this.socket = socket;
		// System.out.println("contructor");
	}
	public boolean returnStatus(){
		return login_status;
	}
	public String returnName(){
		return userName;
	}
	public void connectMySQL() {
		try {

			Class.forName(name);
			connection = DriverManager.getConnection(url, user, password);
			// System.out.println(connection);
			// System.out.println(message.split(" ")[1]+":"+message.split("
			// ")[2]);
			try {
				printStream = new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (status) {
			case Constant.REGISTER:
				String name_register = message.split(" ")[1].trim();
				String password_register = message.split(" ")[2].trim();
				// ��Ӽ���Ƿ�����û����߼���
				SQL="SELECT * FROM USER_INFORMATION WHERE USER_NAME=?";
				preparedStatement=connection.prepareStatement(SQL);
				preparedStatement.setString(1,name_register);
				ResultSet resultSet=preparedStatement.executeQuery();
				if(resultSet.next()){
					printStream.println("�û����Ѵ���");
					break;
				}

				SQL = "INSERT INTO USER_INFORMATION VALUES(null,?,?) ";
//				SQL="DELETE FROM USER_INFORMATION WHERE ID=?";
//				DELETEҲ����ʹ�á�
//				
				preparedStatement = connection.prepareStatement(SQL);
//				printStream.println("�û�������ע��");
				preparedStatement.setString(1, name_register.trim());
				preparedStatement.setString(2, password_register.trim());
//				System.out.println(preparedStatement.toString());
				preparedStatement.executeUpdate();
				System.out.println("after update");
				printStream.println("ע��ɹ�");
				break;
			case Constant.LOGIN:
				SQL = "SELECT * FROM USER_INFORMATION WHERE USER_NAME=? AND PASSWORD=?";
				preparedStatement = connection.prepareStatement(SQL);
				preparedStatement.setString(1, message.split(" ")[1]);
				preparedStatement.setString(2, message.split(" ")[2]);
//				System.out.println(preparedStatement.toString());
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
//					System.out.println("�û���" + message.split(" ")[1] + "��¼");
					printStream.println("���Ѿ���¼");
					userName=message.split(" ")[1];
//					���û���¼��Ϣ���������С�
				
					login_status=true;
				} else {
					printStream.print("�û��������벻��");
				}
				
				break;
			default:
				break;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		try {
			preparedStatement.close();
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
