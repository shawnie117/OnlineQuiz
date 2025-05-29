import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminPanel {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Update based on your MySQL container settings

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin - Add Questions");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 2));

        JTextField questionField = new JTextField();
        JTextField optA = new JTextField();
        JTextField optB = new JTextField();
        JTextField optC = new JTextField();
        JTextField optD = new JTextField();
        JTextField correctField = new JTextField();

        JButton submitBtn = new JButton("Add Question");

        frame.add(new JLabel("Question:"));
        frame.add(questionField);
        frame.add(new JLabel("Option A:"));
        frame.add(optA);
        frame.add(new JLabel("Option B:"));
        frame.add(optB);
        frame.add(new JLabel("Option C:"));
        frame.add(optC);
        frame.add(new JLabel("Option D:"));
        frame.add(optD);
        frame.add(new JLabel("Correct Answer:"));
        frame.add(correctField);
        frame.add(submitBtn);

        submitBtn.addActionListener(e -> {
            if (questionField.getText().isEmpty() || optA.getText().isEmpty() || optB.getText().isEmpty()
                    || optC.getText().isEmpty() || optD.getText().isEmpty() || correctField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO questions (question, optionA, optionB, optionC, optionD, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)")) {

                stmt.setString(1, questionField.getText());
                stmt.setString(2, optA.getText());
                stmt.setString(3, optB.getText());
                stmt.setString(4, optC.getText());
                stmt.setString(5, optD.getText());
                stmt.setString(6, correctField.getText());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(frame, "Question successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    questionField.setText("");
                    optA.setText("");
                    optB.setText("");
                    optC.setText("");
                    optD.setText("");
                    correctField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add question!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        frame.setVisible(true);
    }
}