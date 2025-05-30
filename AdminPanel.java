import java.awt.*;
import java.sql.*;
import javax.swing.*;

class Database {
    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "");
        } catch (Exception e) {
            System.out.println("DB Error:" + e);
            return null;
        }
    }
}

public class AdminPanel {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(400, 350);
        f.setLayout(new GridLayout(7, 2));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField q = new JTextField();
        JTextField a = new JTextField();
        JTextField b = new JTextField();
        JTextField c = new JTextField();
        JTextField d = new JTextField();
        JTextField ans = new JTextField();

        JButton btn = new JButton("Add the Question");

        f.add(new Label("Question : "));
        f.add(q);
        f.add(new Label("Option 1 : "));
        f.add(a);
        f.add(new Label("Option 2 : "));
        f.add(b);
        f.add(new Label("Option 3 : "));
        f.add(c);
        f.add(new Label("Option 4 : "));
        f.add(d);
        f.add(new Label("Answer : "));
        f.add(ans);
        f.add(btn);

        btn.addActionListener(e -> {
            try {
                Connection con = Database.connect();
                String sql = "  INSERT INTO questions (question, optionA , optionB , optionC , optionD , correctAnswer ) VALUES (?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, q.getText());
                ps.setString(2, a.getText());
                ps.setString(3, b.getText());
                ps.setString(4, c.getText());
                ps.setString(5, d.getText());
                ps.setString(6, ans.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Question has been added!!!");
                q.setText("");
                a.setText("");
                b.setText("");
                c.setText("");
                d.setText("");
                ans.setText("");
                ps.close();
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error : " + ex.getMessage());
            }
        });
        f.setVisible(true);
    }
}