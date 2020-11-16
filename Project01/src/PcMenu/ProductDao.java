package PcMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * DB�� product TABLE�� �����ϱ� ���� class
 */
public class ProductDao {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String URL = "jdbc:mysql://127.0.0.1/pcmenu";
	private Connection conn;

	private static ProductDao instance;
	/**
	 * product TABLE �� �����ϱ� ���� ������ �����Ѵ�.
	 */
	private ProductDao() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				URL,
				USERNAME,
				PASSWORD);
	}
	/**
	 * ProductDao �̱��� ����
	 */
	public static ProductDao getInstance() throws ClassNotFoundException, SQLException {
		if(null==instance) {
			instance = new ProductDao();
		}
		return instance;
	}
	/**
	 * �ش� ī�װ��� ��� product�� list�� �����Ͽ� return�Ѵ�.
	 * @param category ī�װ�
	 * @throws SQLException
	 */
	public ArrayList<ProductVo> getProduct(int category) throws SQLException {
		String sql = "SELECT * FROM product WHERE category = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, category);
		
		ResultSet rs = ps.executeQuery();
		ArrayList<ProductVo> list = new ArrayList<>();
		while(rs.next()) {
			ProductVo vo = new ProductVo();
			vo.setName(rs.getString(3));
			vo.setPrice(rs.getInt(4));
			list.add(vo);
		}
		return list;
	}
	/**
	 * �ش� searchTextField�� �����ϴ� product�� �˻�
	 * @param searchTextField ã���� �ϴ� String
	 * @throws SQLException
	 */
	public ArrayList<ProductVo> searchProduct(String searchTextField) throws SQLException {
		String sql = "SELECT * FROM product WHERE name LIKE ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%" + searchTextField + "%");

		ResultSet rs = ps.executeQuery();
		ArrayList<ProductVo> list = new ArrayList<>();
		while(rs.next()) {
			ProductVo vo = new ProductVo();
			vo.setCategory(rs.getInt(2));
			vo.setName(rs.getString(3));
			vo.setPrice(rs.getInt(4));
			list.add(vo);
		}
		return list;
	}
}
