import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
public class QuizUI extends JFrame implements ActionListener, MouseMotionListener{
	//MessagePanel msg;
		
	Scanner scanQuestions;
	Scanner scanAnswer;
	
	String question;
	String answer;
	
	JTextArea lQuestion;
	JTextArea lAnswer;
	
	JButton next;
	JButton banswer;
	
	BasicSplitPaneUI splitPaneUI;
	JSplitPane split;
	
	public QuizUI() throws IOException{
		Container container = getContentPane();
	
		container.setLayout(new BorderLayout());
		
		JPanel btns = new JPanel();
		
		split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		splitPaneUI = (BasicSplitPaneUI) split.getUI();
		
		splitPaneUI.getDivider().addMouseMotionListener(this);
		
		split.setContinuousLayout(true);
		
		
		btns.setLayout(new FlowLayout());
				
		next = new JButton("Next");
		banswer = new JButton("Answer");
		
		
		next.addActionListener(this);
		banswer.addActionListener(this);
		
		container.add(btns, BorderLayout.NORTH);
		
		btns.add(next);
		btns.add(banswer);
		
		lQuestion = new JTextArea();
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		lQuestion.setFont(font);
		lQuestion.setLineWrap(true);
		lQuestion.setWrapStyleWord(true);
		
		lAnswer = new JTextArea();//
		lAnswer.setFont(font);
		lAnswer.setLineWrap(true);
		lAnswer.setWrapStyleWord(true);
				
		container.add(split, BorderLayout.CENTER);
	
		split.setTopComponent(lQuestion);
		split.setBottomComponent(lAnswer);
		
		File fquestions = new File("/home/jrogers/workspace/InterviewQuiz/files/questions");
		File fanswers = new File("/home/jrogers/workspace/InterviewQuiz/files/answers");
		
		scanQuestions = new Scanner(fquestions) ;
		scanAnswer = new Scanner(fanswers);	
		
		displayQuestions();
		
	}
	
	public void displayQuestions(){
		do{
			question = scanQuestions.nextLine();   
		}
		while (question.equals(""));
		
		do{
			answer = scanAnswer.nextLine();
		}
		while (answer.equals(""));
		
		lQuestion.setText(question);
		lAnswer.setText("");
		lQuestion.setEditable(false);
	}
	 
	public void mouseDragged(MouseEvent arg0) {
         final int currentDragY = ( splitPaneUI.getDividerLocation(split) + arg0.getY());
         split.setDividerLocation(currentDragY);
         this.invalidate();
         this.repaint(); 
    }
	 public void mouseMoved(MouseEvent arg0) {

     }
	public void actionPerformed(ActionEvent e){

		try{
			lQuestion.setEditable(true);
			lAnswer.setEditable(true);
			
			if (e.getSource() == next && scanQuestions.hasNext() && scanAnswer.hasNext()){
				displayQuestions();
			}
			else if(e.getSource() == banswer)
				lAnswer.setText(answer);
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			lQuestion.setEditable(false);
			lAnswer.setEditable(false);
		}
		
	}
}
