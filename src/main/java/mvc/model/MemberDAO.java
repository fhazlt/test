package mvc.model;

import java.sql.SQLException;

import util.JDBConnect;

public class MemberDAO extends JDBConnect{
	
	public MemberDAO() {
		super();
	}
	
	public String getMemberId(String uid, String upass) {
		MemberDTO dto = new MemberDTO();
		String id =null;
		String query = "SELECT * FROM MEMBER WHERE ID=? and password=?";
		System.out.println(query);
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, uid);
			psmt.setString(2, upass);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				id = rs.getString("id");
				System.out.println(id);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public int updateEdit(MemberDTO dto) {
		int totalcount = 0;
		String sql = "UPDATE member SET address=?, birth=?, gender=?, mail=?, ";
			   sql += " name=?, password=?, phone=?, regist_day =? WHERE id=?";	
			   System.out.println(sql);
		
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, dto.getAddress());
			psmt.setString(2, dto.getBirth());
			psmt.setString(3, dto.getGender());
			psmt.setString(4, dto.getMail());
			psmt.setString(5, dto.getName());
			psmt.setString(6, dto.getPassword());
			psmt.setString(7, dto.getPhone());
			psmt.setTimestamp(8, dto.getRegist_day());
			psmt.setString(9, dto.getId());
			totalcount = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	public MemberDTO selectView(String id) {
		MemberDTO dto = new MemberDTO();
		String sql = "select * from member m where id= ?";
		System.out.println(sql);
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			while(rs.next()) {
				dto.setAddress(rs.getString("address"));
				dto.setBirth(rs.getString("birth"));
				dto.setGender(rs.getString("gender"));
				dto.setMail(rs.getString("mail"));
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setPhone(rs.getString("phone"));
				dto.setRegist_day(rs.getTimestamp("regist_day"));
				dto.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public int deletePost(String id) {
		int totalcount = 0;
		String sql = "Delete from member WHERE id=?";
		
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			totalcount = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalcount;
	}
	
	public int insertWrite(MemberDTO dto) {
		int totalcount = 0;
		String sql = "insert into member (id, password, name, gender, birth, mail, phone, ";
			sql +=" address, regist_day) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(sql);
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getPassword());
			psmt.setString(3, dto.getName());
			psmt.setString(4, dto.getGender());
			psmt.setString(5, dto.getBirth());
			psmt.setString(6, dto.getMail());
			psmt.setString(7, dto.getPhone());
			psmt.setString(8, dto.getAddress());
			psmt.setTimestamp(9, dto.getRegist_day());
			totalcount = psmt.executeUpdate();
			//			totalcount =  rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalcount;
	}
	
	
}

