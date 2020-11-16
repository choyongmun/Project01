package PcMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB�� user TABLE�� �����ϱ� ���� class
 */
public class UserDao {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String URL = "jdbc:mysql://127.0.0.1/pcmenu";
	private Connection conn;

	private static UserDao instance;
	/**
	 * product TABLE �� �����ϱ� ���� ������ �����Ѵ�.
	 */
	private UserDao() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				URL,
				USERNAME,
				PASSWORD);
	}
	/**
	 * UserDao �̱��� ����
	 */
	public static UserDao getInstance() throws ClassNotFoundException, SQLException {
		if(null==instance) {
			instance = new UserDao();
		}
		return instance;
	}

	/**
	 * login - �ش� ������ db�� ���� ���Ѵ�.
	 * @param id = ���̵�
	 * @param password = password
	 * @return	������ ��ġ�� ��� true, �ƴҰ�� false
	 * @throws SQLException
	 */
	public boolean login(String id, String password) throws SQLException {

		String sql = "SELECT * FROM user WHERE id = ? && password = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, password);

		ResultSet rs = ps.executeQuery();
		if(!rs.next()) {
			return false;	//ȸ�������� �ٸ�
		}
		else {
			return true;	//�α���
		}
	}
	/**
	 * ȸ������
	 * @param name
	 * @param id
	 * @param password
	 * @param email
	 * @param phone
	 * @return ȸ������ ������ true, ���н� false
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
	 * id �ߺ�Ȯ��
	 * @param id
	 * @return �ߺ��̶�� true, �ߺ��� �ƴ϶�� false
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
	 * ���̵� ã��
	 * @param name
	 * @param email
	 * @param phone
	 * @return �ش��ϴ� ������ �ִٸ� UserVo ��ü�� id�� password�� �����Ͽ� return, �ƴ� ��� null
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
