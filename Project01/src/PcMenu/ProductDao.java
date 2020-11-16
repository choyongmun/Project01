package PcMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * DB의 product TABLE을 관리하기 위한 class
 */
public class ProductDao {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String URL = "jdbc:mysql://127.0.0.1/pcmenu";
	private Connection conn;

	private static ProductDao instance;
	/**
	 * product TABLE 에 접근하기 위한 정보를 저장한다.
	 */
	private ProductDao() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				URL,
				USERNAME,
				PASSWORD);
	}
	/**
	 * ProductDao 싱글톤 패턴
	 */
	public static ProductDao getInstance() throws ClassNotFoundException, SQLException {
		if(null==instance) {
			instance = new ProductDao();
		}
		return instance;
	}
	/**
	 * 해당 카테고리의 모든 product를 list에 저장하여 return한다.
	 * @param category 카테고리
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
	 * 해당 searchTextField를 포함하는 product를 검색
	 * @param searchTextField 찾고자 하는 String
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
