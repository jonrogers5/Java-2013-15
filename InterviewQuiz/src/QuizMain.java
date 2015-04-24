import javax.swing.JFrame;
import java.io.*;

public class QuizMain {

	public static void main(String[] args) throws IOException {
		QuizUI quiz = new QuizUI();
		quiz.setTitle("Quiz");
		quiz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		quiz.setSize(800, 800);
		quiz.setVisible(true);

	}

}
