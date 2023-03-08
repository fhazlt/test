package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;

public class JDBConnect {
	
	public Connection con = null;
	public Statement stmt = null; //+
	public PreparedStatement psmt = null; //?
	public ResultSet rs = null;
	
	public JDBConnect() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/WebMarketDB?useUnicode=true&characterEncoding=UTF-8";
//		String url = "jdbc:mysql://localhost:3306/WebMarketDB";
		String id = "root";
		String pwd = "rpass";
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("conn1 성공");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public JDBConnect(String driver, String url, String id, String pwd) {
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("conn2 성공");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(psmt != null) psmt.close();
			if(con != null) con.close();
			System.out.println("JDBC 자원 해제");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
