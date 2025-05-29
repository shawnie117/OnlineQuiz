import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

class Question {
    String question, optA, optB, optC, optD, correct;

    public Question(String question, String optA, String optB, String optC, String optD, String correct) {
        this.question = question;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.correct = correct;
    }
}

public class QuizApp {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Empty password

    private static int currentIndex = 0;
    private static int score = 0;
    private static List<Question> questions;
    private static JFrame frame;
    private static JLabel questionLabel;
    private static JRadioButton[] options;
    private static ButtonGroup group;

    public static void main(String[] args) {
        loadQuestions();
        createUI();
        showNextQuestion();
    }

    private static void loadQuestions() {
        questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                questions.add(new Question(rs.getString("question"),
                        rs.getString("optionA"),
                        rs.getString("optionB"),
                        rs.getString("optionC"),
                        rs.getString("optionD"),
                        rs.getString("correctAnswer")));
            }
            Collections.shuffle(questions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createUI() {
        frame = new JFrame("Quiz App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel("Question will appear here");
        frame.add(questionLabel);

        options = new JRadioButton[4];
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            frame.add(options[i]);
        }

        JButton nextBtn = new JButton("Next");
        frame.add(nextBtn);

        nextBtn.addActionListener((ActionEvent e) -> {
            checkAnswer();
            showNextQuestion();
        });

        frame.setVisible(true);
    }

    private static void showNextQuestion() {
        if (currentIndex >= questions.size()) {
            JOptionPane.showMessageDialog(frame, "Quiz Over! Your Score: " + score);
            frame.dispose();
            return;
        }

        Question q = questions.get(currentIndex);
        questionLabel.setText(q.question);
        options[0].setText(q.optA);
        options[1].setText(q.optB);
        options[2].setText(q.optC);
        options[3].setText(q.optD);
        group.clearSelection();

        currentIndex++;
    }

    private static void checkAnswer() {
        Question q = questions.get(currentIndex - 1);
        for (JRadioButton option : options) {
            if (option.isSelected() && option.getText().equals(q.correct)) {
                score++;
            }
        }
    }
}
