package mysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLExample extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "tony0306";

    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtID;
    private JTextArea textArea;

    public MySQLExample() {
        setTitle("MySQL 資料庫應用程式");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // 輸入面板
        JPanel inputPanel = new JPanel(new FlowLayout());
        txtID = new JTextField(5);
        txtName = new JTextField(10);
        txtAge = new JTextField(5);
        inputPanel.add(new JLabel("編號:"));
        inputPanel.add(txtID);
        inputPanel.add(new JLabel("姓名:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("年齡:"));
        inputPanel.add(txtAge);

        // 按鈕面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnInsert = new JButton("新增");
        JButton btnUpdate = new JButton("更新");
        JButton btnDelete = new JButton("刪除");
        JButton btnQuery = new JButton("查詢");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnQuery);

        // 輸出區域
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // 添加面板到框架
        add(inputPanel);
        add(buttonPanel);
        add(scrollPane);

        // 按鈕動作監聽器
        btnInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertData();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        btnQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryData();
            }
        });
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            textArea.append("已連接到資料庫。\n");
        } catch (SQLException e) {
            textArea.append("連接失敗: " + e.getMessage() + "\n");
        }
        return conn;
    }

    private void insertData() {
        String name = txtName.getText();
        String ageStr = txtAge.getText();
        if (name.isEmpty() || ageStr.isEmpty()) {
            textArea.append("姓名和年齡必須填寫。\n");
            return;
        }
        int age = Integer.parseInt(ageStr);

        String sql = "INSERT INTO persons(name, age) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            textArea.append("記錄新增成功。\n");
        } catch (SQLException e) {
            textArea.append("新增失敗: " + e.getMessage() + "\n");
        }
    }

    private void updateData() {
        String idStr = txtID.getText();
        String name = txtName.getText();
        String ageStr = txtAge.getText();
        if (idStr.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            textArea.append("編號、姓名和年齡必須填寫。\n");
            return;
        }
        int id = Integer.parseInt(idStr);
        int age = Integer.parseInt(ageStr);

        String sql = "UPDATE persons SET name = ?, age = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                textArea.append("記錄更新成功。\n");
            } else {
                textArea.append("未找到對應的記錄。\n");
            }
        } catch (SQLException e) {
            textArea.append("更新失敗: " + e.getMessage() + "\n");
        }
    }

    private void deleteData() {
        String idStr = txtID.getText();
        if (idStr.isEmpty()) {
            textArea.append("編號必須填寫。\n");
            return;
        }
        int id = Integer.parseInt(idStr);

        String sql = "DELETE FROM persons WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                textArea.append("記錄刪除成功。\n");
            } else {
                textArea.append("未找到對應的記錄。\n");
            }
        } catch (SQLException e) {
            textArea.append("刪除失敗: " + e.getMessage() + "\n");
        }
    }

    private void queryData() {
        String sql = "SELECT id, name, age FROM persons";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            textArea.append("編號\t姓名\t年齡\n");
            while (rs.next()) {
                textArea.append(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getInt("age") + "\n");
            }
        } catch (SQLException e) {
            textArea.append("查詢失敗: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MySQLExample().setVisible(true);
            }
        });
    }
}
