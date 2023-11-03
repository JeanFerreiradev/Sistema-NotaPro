package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

	public static final String url = "jdbc:mysql://127.0.0.1/projetoa3mmtes";
	public static final String user = "Jean";
	public static final String password = "j1e2a3n4";

	public static Connection conn;

	public static Connection getConexao() {
		try {
			if (conn == null) {
				conn = DriverManager.getConnection(url, user, password);
				return conn;
			} else {
				return conn;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}