package mvc.model;

import java.util.ArrayList;

import util.JDBConnect;

public class BoardDAO extends JDBConnect {

	private static BoardDAO instance;
	
	private BoardDAO() {
		super();
	}

	public static BoardDAO getInstance() {
		if (instance == null)
			instance = new BoardDAO();
		return instance;
	}	
	//board ���̺��� ���ڵ� ����
	public int getListCount(String items, String text) {

		int x = 0;

		String sql;
		
		if (items == null && text == null)
			sql = "select count(*) from board";
		else
			sql = "SELECT  count(*) FROM board where " + items + " like '%" + text + "%'";
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();

			if (rs.next()) 
				x = rs.getInt(1);
			
		} catch (Exception ex) {
			System.out.println("getListCount() ����: " + ex);
		} 
		return x;
	}
	//board ���̺��� ���ڵ� ��������
	public ArrayList<BoardDTO> getBoardList(int page, int limit, String items, String text) {

		int total_record = getListCount(items, text );
		int start = (page - 1) * limit;
		int index = start + 1;
		String sql;

		if (items == null && text == null)
			sql = "select * from board ORDER BY num DESC";
		else
			sql = "SELECT  * FROM board where " + items + " like '%" + text + "%' ORDER BY num DESC ";

		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();

		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.absolute(index)) {
				BoardDTO board = new BoardDTO();
				board.setNum(rs.getString("num"));
				board.setId(rs.getString("id"));
				board.setName(rs.getString("name"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setRegist_day(rs.getString("regist_day"));
				board.setHit(rs.getString("hit"));
				board.setIp(rs.getString("ip"));
				list.add(board);

				if (index < (start + limit) && index <= total_record)
					index++;
				else
					break;
			}
			return list;
		} catch (Exception ex) {
			System.out.println("getBoardList() ���� : " + ex);
		} 
		return null;
	}
	//member ���̺��� ������ id�� ����ڸ� ��������
	public String getLoginNameById(String id) {
		
		String name=null;
		String sql = "select * from member where id = ? ";

		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();

			if (rs.next()) 
				name = rs.getString("name");	
			
			return name;
		} catch (Exception ex) {
			System.out.println("getBoardByNum() ���� : " + ex);
		} 
		return null;
	}

	//board ���̺� ���ο� �� ��������
	public void insertBoard(BoardDTO board)  {
		try {

			String sql = "insert into board values(?, ?, ?, ?, ?, ?, ?, ?)";
		
			psmt = con.prepareStatement(sql);
			psmt.setString(1, board.getNum());
			psmt.setString(2, board.getId());
			psmt.setString(3, board.getName());
			psmt.setString(4, board.getSubject());
			psmt.setString(5, board.getContent());
			psmt.setString(6, board.getRegist_day());
			psmt.setString(7, board.getHit());
			psmt.setString(8, board.getIp());

			psmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("insertBoard() ���� : " + ex);
		} 	
	} 
	//���õ� ���� ��ȸ�� �����ϱ�
	public void updateHit(int num) {
		
		try {

			String sql = "select hit from board where num = ? ";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, num);
			rs = psmt.executeQuery();
			int hit = 0;

			if (rs.next())
				hit = rs.getInt("hit") + 1;
		

			sql = "update board set hit=? where num=?";
			psmt = con.prepareStatement(sql);		
			psmt.setInt(1, hit);
			psmt.setInt(2, num);
			psmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("updateHit() ���� : " + ex);
		} 
	}
	//���õ� �� �� ���� ��������
	public BoardDTO getBoardByNum(int num, int page) {
		
		BoardDTO board = null;

		updateHit(num);
		String sql = "select * from board where num = ? ";

		try {
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, num);
			rs = psmt.executeQuery();

			if (rs.next()) {
				board = new BoardDTO();
				board.setNum(rs.getString("num"));
				board.setId(rs.getString("id"));
				board.setName(rs.getString("name"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setRegist_day(rs.getString("regist_day"));
				board.setHit(rs.getString("hit"));
				board.setIp(rs.getString("ip"));
			}
			
			return board;
		} catch (Exception ex) {
			System.out.println("getBoardByNum() ���� : " + ex);
		} 
		return null;
	}
	public BoardDTO getBoardByNum(String id) {
		
		BoardDTO board = new BoardDTO();
		
		String sql = "select * from board where id = ? ";
		
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				board.setNum(rs.getString("num"));
				board.setId(rs.getString("id"));
				board.setName(rs.getString("name"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setRegist_day(rs.getString("regist_day"));
				board.setHit(rs.getString("hit"));
				board.setIp(rs.getString("ip"));
			}
			
			return board;
		} catch (Exception ex) {
			System.out.println("getBoardByNum() ���� : " + ex);
		} 
		return null;
	}
	//���õ� �� ���� �����ϱ�
	public void updateBoard(BoardDTO board) {
	
		try {
			String sql = "update board set name=?, subject=?, content=? where num=?";

			psmt = con.prepareStatement(sql);
			
			con.setAutoCommit(false);

			psmt.setString(1, board.getName());
			psmt.setString(2, board.getSubject());
			psmt.setString(3, board.getContent());
			psmt.setString(4, board.getNum());

			psmt.executeUpdate();			
			con.commit();

		} catch (Exception ex) {
			System.out.println("updateBoard() ���� : " + ex);
		} 
	} 
	//���õ� �� �����ϱ�
	public void deleteBoard(int num) {

		String sql = "delete from board where num=?";	

		try {
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, num);
			psmt.executeUpdate();

		} catch (Exception ex) {
			System.out.println("deleteBoard() ���� : " + ex);
		} 
	}	
}
