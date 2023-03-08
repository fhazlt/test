package mvc.model;

public class BoardDTO {
	private String num;
	private String id;
	private String name;
	private String subject;
	private String content;
	private String regist_day;
	private String hit;
	private String ip;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegist_day() {
		return regist_day;
	}
	public void setRegist_day(String regist_day) {
		this.regist_day = regist_day;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "BoardDTO [num=" + num + ", id=" + id + ", name=" + name + ", subject=" + subject + ", content="
				+ content + ", regist_day=" + regist_day + ", hit=" + hit + ", ip=" + ip + "]";
	}
}

	
