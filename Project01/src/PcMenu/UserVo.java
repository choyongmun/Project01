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
	 * ����� ��(id, password)�� StringŸ������ return�Ѵ�.
	 */
	public String toString(){
		return "���̵� : "+getId()+" / ��й�ȣ : "+getPassword();
	}
}
