import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class QuizApp {
    static JFrame frame = new JFrame("Quiz App");
    static JLabel questionLabel = new JLabel();
    static JRadioButton[] options = new JRadioButton[4];
    static ButtonGroup group = new ButtonGroup();
    static JButton nextBtn = new JButton("Next");
    static ArrayList<String[]> quizList = new ArrayList<>();
    static int index = 0, score = 0;

    public static void main(String[] args) {
        loadQuestions();
        setupUI();
        showQuestion();
    }

    static void loadQuestions() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "");
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM questions")) {
            while (rs.next())
                quizList.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6) });
            Collections.shuffle(quizList);
        } catch (Exception e) {
            System.out.println("DB Error: " + e);
        }
    }

    static void setupUI() {
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            frame.add(options[i]);
        }

        nextBtn.addActionListener(e -> {
            if (index < quizList.size())
                checkAnswer();
            showQuestion();
        });
        frame.add(nextBtn);
        frame.setVisible(true);
    }

    static void showQuestion() {
        if (index >= quizList.size()) {
            JOptionPane.showMessageDialog(frame, "Quiz Over! Score: " + score);
            return;
        }
        String[] q = quizList.get(index++);
        questionLabel.setText("Q" + index + ": " + q[0]);
        for (int i = 0; i < 4; i++)
            options[i].setText(q[i + 1]);
        group.clearSelection();
    }

    static void checkAnswer() {
        if (Arrays.stream(options)
                .anyMatch(opt -> opt.isSelected() && opt.getText().equals(quizList.get(index - 1)[5])))
            score++;
    }
}
