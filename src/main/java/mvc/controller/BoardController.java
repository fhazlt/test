package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.model.BoardDAO;
import mvc.model.BoardDTO;
import mvc.model.MemberDAO;
import mvc.model.MemberDTO;

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final int LISTCOUNT = 5; 

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = RequestURI.substring(contextPath.length());
		String id = request.getParameter("id");
		HttpSession session = request.getSession();
		if (command.equals("/DeleteMember.do")) {
			MemberDAO dao = new MemberDAO();
			int rs = dao.deletePost(id);
			session.invalidate();
			if(rs==1) {
				response.sendRedirect("/member/resultMember.jsp");
			}else {
				response.sendRedirect("/welcome.jsp");
			}
			dao.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = RequestURI.substring(contextPath.length());
		String uid = request.getParameter("id");
		String upass = request.getParameter("password");
		MemberDAO dao = new MemberDAO();
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		
		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
	
		if (command.equals("/BoardListAction.do")) {//��ϵ� �� ��� ������ ����ϱ�
			requestBoardList(request);
			RequestDispatcher rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
		} else if (command.equals("/BoardWriteForm.do")) { // �� ��� ������ ����ϱ�
				requestLoginName(request);
				RequestDispatcher rd = request.getRequestDispatcher("./board/writeForm.jsp");
				rd.forward(request, response);				
		} else if (command.equals("/BoardWriteAction.do")) {// ���ο� �� ����ϱ�
				requestBoardWrite(request);
				RequestDispatcher rd = request.getRequestDispatcher("/BoardListAction.do");
				rd.forward(request, response);						
		} else if (command.equals("/BoardViewAction.do")) {//���õ� �� �� ������ ��������
				requestBoardView(request);
				RequestDispatcher rd = request.getRequestDispatcher("/BoardView.do");
				rd.forward(request, response);						
		} else if (command.equals("/BoardView.do")) { //�� �� ������ ����ϱ�
				RequestDispatcher rd = request.getRequestDispatcher("./board/view.jsp");
				rd.forward(request, response);	
		} else if (command.equals("/BoardUpdateAction.do")) { //���õ� ���� ��ȸ�� �����ϱ�
				requestBoardUpdate(request);
				RequestDispatcher rd = request.getRequestDispatcher("/BoardListAction.do");
				rd.forward(request, response);
		}else if (command.equals("/BoardDeleteAction.do")) { //���õ� �� �����ϱ�
				requestBoardDelete(request);
				RequestDispatcher rd = request.getRequestDispatcher("/BoardListAction.do");
				rd.forward(request, response);				
		}else if (command.equals("/loginMember.do")) { //로그인 처리
			String id = dao.getMemberId(uid, upass);
			if(id != null) {
				session.setAttribute("sessionId", id);
				response.sendRedirect("/member/resultMember.jsp?msg=2");
			} else {
				response.sendRedirect("/member/loginMember.jsp?error=1");
			}
			dao.close();
		}else if (command.equals("/UpdateMember.do")) {
			MemberDTO dto = requestUpdateMember(request);
			Date currentDatetime = new Date(System.currentTimeMillis());
			java.sql.Date sqlDate = new java.sql.Date(currentDatetime.getTime());
			java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDatetime.getTime());
			System.out.println(timestamp);
			dto.setRegist_day(timestamp);
			int rs = dao.updateEdit(dto);
			if(rs==1) {
				response.sendRedirect("/member/resultMember.jsp?msg=0");
			}	
			dao.close();
		}else if(command.equals("/AddMember.do")) {
			MemberDTO dto = requestAddMember(request);
			int rs = dao.insertWrite(dto);
			if(rs==1) {
				response.sendRedirect("/member/resultMember.jsp?msg=1");
			}
			dao.close();
		}
		
	}
	//��ϵ� �� ��� ��������	
	public void requestBoardList(HttpServletRequest request){
			
		BoardDAO dao = BoardDAO.getInstance();
		List<BoardDTO> boardlist = new ArrayList<BoardDTO>();
		
	  	int pageNum=1;
		int limit=LISTCOUNT;
		
		if(request.getParameter("pageNum")!=null)
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
				
		String items = request.getParameter("items");
		String text = request.getParameter("text");
		
		int total_record=dao.getListCount(items, text);
		System.out.println("total_record==============" + total_record); //0
		boardlist = dao.getBoardList(pageNum,limit, items, text); // null
		System.out.println("bbbb========="+boardlist);
		int total_page;
		
		if (total_record % limit == 0){     
	     	total_page =total_record/limit;
	     	Math.floor(total_page);  
		}
		else{
		   total_page =total_record/limit;
		   Math.floor(total_page); 
		   total_page =  total_page + 1; 
		}		
   
   		request.setAttribute("pageNum", pageNum);		  
   		request.setAttribute("total_page", total_page);   
		request.setAttribute("total_record",total_record); 
		request.setAttribute("boardlist", boardlist);								
	}
	//������ ����ڸ� ��������
	public void requestLoginName(HttpServletRequest request){
					
		String id = request.getParameter("id");
		
		BoardDAO  dao = BoardDAO.getInstance();
		
		String name = dao.getLoginNameById(id);		
		
		request.setAttribute("name", name);									
	}
	// ���ο� �� ����ϱ�
	public void requestBoardWrite(HttpServletRequest request){
					
		BoardDAO dao = BoardDAO.getInstance();		
		
		BoardDTO board = new BoardDTO();
		board.setId(request.getParameter("id"));
		board.setName(request.getParameter("name"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));	
		
		System.out.println(request.getParameter("name"));
		System.out.println(request.getParameter("subject"));
		System.out.println(request.getParameter("content"));
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
		String regist_day = formatter.format(new java.util.Date()); 
		
		board.setHit("0");
		board.setRegist_day(regist_day);
		board.setIp(request.getRemoteAddr());			
		
		dao.insertBoard(board);								
	}
	//���õ� �� �� ������ ��������
	public void requestBoardView(HttpServletRequest request){
					
		BoardDAO dao = BoardDAO.getInstance();
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));	
		
		BoardDTO board = new BoardDTO();
		board = dao.getBoardByNum(num, pageNum);		
		
		request.setAttribute("num", num);		 
   		request.setAttribute("page", pageNum); 
   		request.setAttribute("board", board);   									
	}
	//���õ� �� ���� �����ϱ�
	public void requestBoardUpdate(HttpServletRequest request){
					
		String num = request.getParameter("num");
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));	
		
		BoardDAO dao = BoardDAO.getInstance();		
		
		BoardDTO board = new BoardDTO();		
		board.setNum(num);
		board.setName(request.getParameter("name"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));		
		
		 java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
		 String regist_day = formatter.format(new java.util.Date()); 
		 
		 board.setHit("0");
		 board.setRegist_day(regist_day);
		 board.setIp(request.getRemoteAddr());			
		
		 dao.updateBoard(board);								
	}
	//���õ� �� �����ϱ�
	public void requestBoardDelete(HttpServletRequest request){
					
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));	
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.deleteBoard(num);							
	}	
	
	public MemberDTO requestUpdateMember(HttpServletRequest request) {
		MemberDTO dto = new MemberDTO();
		
		//birth
		String year = request.getParameter("birthyy");
		String month = request.getParameterValues("birthmm")[0];
		String day = request.getParameter("birthdd");
		String birth = year + "/" + month + "/" + day;
		
		//mail
		String mail1 = request.getParameter("mail1");
		String mail2 = request.getParameterValues("mail2")[0];
		String mail = mail1 + "@" + mail2;
		
		dto.setAddress(request.getParameter("address"));
		dto.setBirth(birth);
		dto.setId(request.getParameter("id"));
		dto.setGender(request.getParameter("gender"));
		dto.setMail(mail);
		dto.setName(request.getParameter("name"));
		dto.setPassword(request.getParameter("password"));
		dto.setPhone(request.getParameter("phone"));
		return dto;
	}
	
	public MemberDTO requestAddMember(HttpServletRequest request)  {
		MemberDTO dto = new MemberDTO();
		String year = request.getParameter("birthyy");
		String month = request.getParameterValues("birthmm")[0];
		String day = request.getParameter("birthdd");
		String birth = year + "/" + month + "/" + day;
		String mail1 = request.getParameter("mail1");
		String mail2 = request.getParameterValues("mail2")[0];
		String mail = mail1 + "@" + mail2;
		
		Date currentDatetime = new Date(System.currentTimeMillis());
		java.sql.Date sqlDate = new java.sql.Date(currentDatetime.getTime());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDatetime.getTime());
		
		dto.setAddress(request.getParameter("address"));
		dto.setBirth(birth);
		dto.setGender(request.getParameter("gender"));
		dto.setId(request.getParameter("id"));
		dto.setMail(mail);
		dto.setName(request.getParameter("name"));
		dto.setPassword(request.getParameter("password"));
		dto.setPhone(request.getParameter("phone"));
		dto.setRegist_day(timestamp);
		return dto;
	}

}
