import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class QuizApp {
    static JFrame frame;
    static JLabel questionLabel;
    static JRadioButton opt1, opt2, opt3, opt4;
    static ButtonGroup group;
    static JButton nextBtn;
    static ArrayList<String[]> quizList = new ArrayList<>();
    static int index = 0, score = 0;

    public static void main(String[] args) {
        loadQuestions();
        setupUI();
        showQuestion();
    }

    static void loadQuestions() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions");

            while (rs.next()) {
                String[] q = {
                    rs.getString("question"),
                    rs.getString("optionA"),
                    rs.getString("optionB"),
                    rs.getString("optionC"),
                    rs.getString("optionD"),
                    rs.getString("correctAnswer")
                };
                quizList.add(q);
            }

            Collections.shuffle(quizList);
        } catch (Exception e) {
            System.out.println("DB Error: " + e);
        }
    }

    static void setupUI() {
        frame = new JFrame("Quiz App");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        questionLabel = new JLabel("Question here");
        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();
        group = new ButtonGroup();
        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);

        nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> {
            checkAnswer();
            showQuestion();
        });

        frame.add(questionLabel);
        frame.add(opt1);
        frame.add(opt2);
        frame.add(opt3);
        frame.add(opt4);
        frame.add(nextBtn);

        frame.setVisible(true);
    }

    static void showQuestion() {
        if (index >= quizList.size()) {
            JOptionPane.showMessageDialog(frame, "Quiz Over! Score: " + score);
            frame.dispose();
            return;
        }

        String[] q = quizList.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q[0]);
        opt1.setText(q[1]);
        opt2.setText(q[2]);
        opt3.setText(q[3]);
        opt4.setText(q[4]);
        group.clearSelection();
        index++;
    }

    static void checkAnswer() {
        String correct = quizList.get(index - 1)[5];
        if (opt1.isSelected() && opt1.getText().equals(correct)) score++;
        if (opt2.isSelected() && opt2.getText().equals(correct)) score++;
        if (opt3.isSelected() && opt3.getText().equals(correct)) score++;
        if (opt4.isSelected() && opt4.getText().equals(correct)) score++;
    }
}