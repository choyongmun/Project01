package PcMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB의 user TABLE을 관리하기 위한 class
 */
public class UserDao {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String URL = "jdbc:mysql://127.0.0.1/pcmenu";
	private Connection conn;

	private static UserDao instance;
	/**
	 * product TABLE 에 접근하기 위한 정보를 저장한다.
	 */
	private UserDao() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				URL,
				USERNAME,
				PASSWORD);
	}
	/**
	 * UserDao 싱글톤 패턴
	 */
	public static UserDao getInstance() throws ClassNotFoundException, SQLException {
		if(null==instance) {
			instance = new UserDao();
		}
		return instance;
	}

	/**
	 * login - 해당 정보와 db의 값을 비교한다.
	 * @param id = 아이디
	 * @param password = password
	 * @return	정보가 일치할 경우 true, 아닐경우 false
	 * @throws SQLException
	 */
	public boolean login(String id, String password) throws SQLException {

		String sql = "SELECT * FROM user WHERE id = ? && password = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, password);

		ResultSet rs = ps.executeQuery();
		if(!rs.next()) {
			return false;	//회원정보가 다름
		}
		else {
			return true;	//로그인
		}
	}
	/**
	 * 회원가입
	 * @param name
	 * @param id
	 * @param password
	 * @param email
	 * @param phone
	 * @return 회원가입 성공시 true, 실패시 false
	 * @throws SQLException
	 */
	public boolean regist(String name,String id, String password, String email,String phone) throws SQLException {
		if(!idCheck(id)) {
			String sql = "INSERT INTO user(name, id,password,email,phone) VALUES(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, id);
			ps.setString(3, password);
			ps.setString(4, email);
			ps.setString(5,phone);
			if(ps.executeUpdate()>0) {
				return true;
			}else
				return false;
		}
		else
			return false;
	}
	/**
	 * id 중복확인
	 * @param id
	 * @return 중복이라면 true, 중복이 아니라면 false
	 * @throws SQLException
	 */
	public boolean idCheck(String id) throws SQLException {
		String sql = "SELECT * FROM user WHERE id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		if(!rs.next()) {
			return false;
		}
		else {
			return true;
		}
	}
	/**
	 * 아이디 찾기
	 * @param name
	 * @param email
	 * @param phone
	 * @return 해당하는 정보가 있다면 UserVo 객체에 id와 password를 저장하여 return, 아닐 경우 null
	 * @throws SQLException
	 */
	public UserVo findId(String name,String email, String phone) throws SQLException {
		String sql = "SELECT * FROM user WHERE name = ? && email = ? && phone = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, email);
		ps.setString(3, phone);

		ResultSet rs = ps.executeQuery();
		if(!rs.next()) {
			return null;
		}
		else {
			UserVo vo = new UserVo();
			vo.setId(rs.getString(3));
			vo.setPassword(rs.getString(4));
			return vo;
		}
	}
}
