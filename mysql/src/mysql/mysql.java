package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mysql {
    
    // MySQL 連接資訊
    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USER = "root";
    private static final String PASSWORD = "tony0306";

    // 資料庫連接物件
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // 插入數據
    public void insertData(String name, int age) {
        String sql = "INSERT INTO persons(name, age) VALUES(?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 查詢數據
    public void selectData() {
        String sql = "SELECT id, name, age FROM persons";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                                   rs.getString("name") + "\t" +
                                   rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 更新數據
    public void updateData(int id, String newName, int newAge) {
        String sql = "UPDATE persons SET name = ?, age = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 刪除數據
    public void deleteData(int id) {
        String sql = "DELETE FROM persons WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
    	mysql example = new mysql();

        // 插入數據
        example.insertData("John Doe", 30);
        example.insertData("Jane Doe", 25);

        // 查詢數據
        System.out.println("Querying data:");
        example.selectData();

        // 更新數據
        example.updateData(1, "John Smith", 35);

        // 查詢數據
        System.out.println("Querying data after update:");
        example.selectData();

        // 刪除數據
        example.deleteData(2);

        // 查詢數據
        System.out.println("Querying data after deletion:");
        example.selectData();
    }
}
