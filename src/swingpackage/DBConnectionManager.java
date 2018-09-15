package swingpackage;
import java.sql.Connection;     
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class DBConnectionManager {

	private Connection conn;
	private static DBConnectionManager DBConnection;
	
	private DBConnectionManager() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=DBname;", "username", "password");
		} catch (Exception e) {
			System.out.println("Bad connection");
		}
	}
	
	public static DBConnectionManager getinstance() {
		if(DBConnection == null) {
			DBConnection = new DBConnectionManager();
		}
		return DBConnection;                                       
	}
	
	public Connection getConnection() {
		return conn;
	}
	
}
