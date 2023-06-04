package booksearch;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBHelper {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost:3307/cheshire?serverTimezone=UTC";
	String jdbc_id = "root";
	String jdbc_password = "qwerty123";
	
	public void connect() {
		try {
			Class.forName(jdbc_driver);
			conn = DriverManager.getConnection(jdbc_url, jdbc_id, jdbc_password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	public static void createTable() {		//"체셔" 도서검색대 테이블 생성(최초 1번 사용)... 이건 그냥 미리 할까 고민...
		Connection con = makeConnection();
		try {
			Statement stmt = con.createStatement();
			String s = "CREATE TABLE IF NOT EXISTS booksearch (id_ INT NOT NULL AUTO_INCREMENT, name VARCHAR(50), bookshelf VARCHAR(50), memo VARCHAR(200), PRIMARY KEY(id_))";
			int i = stmt.executeUpdate(s);
						
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}*/
	
	//전체 입력 코드
	public void insertBook(String name, String bookshelf, int bookcount, String memo) {
		connect();
		String sql = "INSERT INTO booksearch (name, bookshelf, bookcount, memo) VALUES (?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, bookshelf);
			pstmt.setInt(3, bookcount);
			pstmt.setString(4, memo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		
	}
	
	public ArrayList<String[]> printBook () {	//DB 내용 전체 가져오기
		connect();
		String sql = "SELECT * FROM booksearch ORDER BY name";
		
		ResultSet rs;
		ArrayList<String[]> contents = new ArrayList<String[]>();
		int i = 1;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String content[] = {" " + Integer.toString(i++), " " + rs.getString("name"),
						rs.getString("bookshelf"), " " + Integer.toString(rs.getInt("bookcount")), " " + rs.getString("memo")};
				contents.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return contents;
	}
	
	public ArrayList<String[]> printSelectedBook (String name) {	//선택된 DB 가져오기
		connect();
		System.out.println("name " + name);
		String sql = "SELECT * FROM booksearch WHERE name LIKE '%" + name + "%' ORDER BY name";
		System.out.println("sql " + sql);
		ResultSet rs;
		
		int i = 1;
		ArrayList<String[]> contents = new ArrayList<String[]>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("여기까지1");
			while (rs.next()) {
				String content[] = {" " + Integer.toString(i++), " " + rs.getString("name"),
						rs.getString("bookshelf"), " " + Integer.toString(rs.getInt("bookcount")), " " + rs.getString("memo")};
				contents.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return contents;
	} 
	
	// 레코드 선택 코드(책 제목ver.) ===> id_ 값 반환
	public int selectBookByName (String name) {
		connect();
		String sql = "SELECT * FROM booksearch WHERE name=?";
		ResultSet rs;
		
		int id_ = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				id_ = rs.getInt("id_");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return id_;
	}
	
	
	
	//삭제 코드
	public void deleteBook(int id_) { 								//데이터베이스에서 내역 삭제
		connect();
		String sql = "DELETE FROM booksearch WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	
	
	
	
	
	//수정 코드
	public void modifyBook_0001(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memo.trim());
			pstmt.setInt(2, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0010(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookcount=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookcount);
			pstmt.setInt(2, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0011(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookcount=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookcount);
			pstmt.setString(2, memo.trim());
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0100(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookshelf=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookshelf.trim());
			pstmt.setInt(2, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0101(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookshelf=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookshelf.trim());
			pstmt.setString(2, memo.trim());
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0110(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookshelf=?, bookcount=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookshelf.trim());
			pstmt.setInt(2, bookcount);
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_0111(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET bookshelf=?, bookcount=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookshelf.trim());
			pstmt.setInt(2, bookcount);
			pstmt.setString(3, memo.trim());
			pstmt.setInt(4, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1000(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1001(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, memo.trim());
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1010(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookcount=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, bookcount);
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1011(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookcount=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, bookcount);
			pstmt.setString(3, memo.trim());
			pstmt.setInt(4, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1100(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookshelf=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, bookshelf.trim());
			pstmt.setInt(3, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1101(int id_, String name, String bookshelf, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookshelf=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, bookshelf.trim());
			pstmt.setString(3, memo.trim());
			pstmt.setInt(4, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1110(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookshelf=?, bookcount=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, bookshelf.trim());
			pstmt.setInt(3, bookcount);
			pstmt.setInt(4, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	public void modifyBook_1111(int id_, String name, String bookshelf, int bookcount, String memo) {	//레코드 선택 코드에서 id_값을 받아 수정함
		connect();
		String sql = "UPDATE booksearch SET name=?, bookshelf=?, bookcount=?, memo=? WHERE id_=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, bookshelf.trim());
			pstmt.setInt(3, bookcount);
			pstmt.setString(4, memo.trim());
			pstmt.setInt(5, id_);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	

}
