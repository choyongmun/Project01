package PcMenu;

public class UserVo {
	private int no;
	private String id;
	private String password;
	private String name;
	private String birth;
	private String regdate;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	/**
	 * 저장된 값(id, password)을 String타입으로 return한다.
	 */
	public String toString(){
		return "아이디 : "+getId()+" / 비밀번호 : "+getPassword();
	}
}
