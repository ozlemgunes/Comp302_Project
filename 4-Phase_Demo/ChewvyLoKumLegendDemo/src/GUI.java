import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GUI extends JFrame {

	private static int Score = 0;
	private static int Target = 0;
	private static int Moves = 50;
	private static DefaultComboBoxModel LevelModels;
	private static DefaultComboBoxModel LoadedLevelModels;
	private static JComboBox SelectLevel;
	private static JComboBox LoadedLevel;
	private Board board;
	private Level level;
	private JTextField field;
	private JTextField moveField;
	private JTextField targetField;
	private JTextField specialRightField;
	private JTextField timeField;
	private DecimalFormat timeFormatter;

	public GUI() throws ParserConfigurationException, SAXException, IOException {
		timeFormatter = new DecimalFormat("00");
		targetField = new JTextField();
		moveField = new JTextField();
		field = new JTextField();
		specialRightField = new JTextField();
		timeField = new JTextField();
		this.board = new Board(9, 9, new MovementBaseLevel());
		board.initBoard();
		level = board.getGameLevel();
		// if (level.isTimeBased())
		// chronometer = ((Object) level).getChronometer();
		
		//board.loadGameBoard();

		LevelModels = new DefaultComboBoxModel();
		SelectLevel = new JComboBox(LevelModels);
		SelectLevel.setPreferredSize(new Dimension(100, 50));
		addCompletedLevel("Select Level");
		addCompletedLevel("1");
		addCompletedLevel("2");

		LoadedLevelModels = new DefaultComboBoxModel();
		LoadedLevel = new JComboBox(LoadedLevelModels);
		LoadedLevel.setPreferredSize(new Dimension(100, 50));
		addSavedLevel("Saved Levels");
		addSavedLevel("1");
		addSavedLevel("2");
	}

	public void createAskingSave() {
		JFrame frame = new JFrame();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		JButton save = createButton("SAVE");
		save.addActionListener(new SaveAction());
		buttonPanel.add(save);

		JButton exit = createButton("EXIT");
		exit.addActionListener(new ExitAction());
		buttonPanel.add(exit);

		JLabel label = createLabel("SAVE ?");
		label.setLocation(50, 0);
		frame.add(label, BorderLayout.NORTH);
		frame.add(buttonPanel);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(150, 150);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public void createPlayFrame() {
		((MovementBaseLevel) level).addPropertyChangeListener(new ChangeLevelListener());
		board.addPropertyChangeListener(new ChangeBoardListener());
		field.setEditable(false);
		field.setText("" + 0);
		moveField.setEditable(false);
		moveField.setText("" + ((MovementBaseLevel) level).getNumberOfMovement());
		targetField.setEditable(false);
		targetField.setText("" + level.getTarget());
		specialRightField.setEditable(false);
		specialRightField.setText("" + level.getSpecialRightNumber());
		JFrame frame = new JFrame();
		JPanel showingPanel = new JPanel();
		JPanel exitPanel = new JPanel();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setTitle("Chewy Lokum Legend");
		frame.setSize(460, 600);
		showingPanel.setLayout(new GridLayout(1, 6));
		showingPanel.add(createLabel("Moves: "));
		showingPanel.add(moveField);
		showingPanel.add(createLabel("Target: "));
		showingPanel.add(targetField);
		showingPanel.add(createLabel("Scores: "));
		showingPanel.add(field);

		exitPanel.setLayout(new GridLayout(1, 5));
		JButton exitButton = createButton("Exit");
		exitButton.addActionListener(new CreatingExitFrame());
		exitPanel.add(exitButton);
		exitPanel.add(createLabel(""));
		exitPanel.add(createLabel(""));
		JButton swapButton = createButton("Swap: ");
		swapButton.addActionListener(new SpecialSwapAction());
		exitPanel.add(swapButton);
		exitPanel.add(specialRightField);
		exitPanel.add(createLabel(""));
		frame.add(showingPanel, BorderLayout.NORTH);
		frame.add(board.getWindow(), BorderLayout.CENTER);
		frame.add(exitPanel, BorderLayout.SOUTH);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void createTimeBasedPlayFrame() {
		((TimeBaseLevel) level).addPropertyChangeListener(new ChangeTimeLevelListener());
		board.addPropertyChangeListener(new ChangeBoardListener());
		if (level.isTimeBased()){
			((TimeBaseLevel) level).getChronometer().addPropertyChangeListener(new ChangeSayacListener());
		}
		((TimeBaseLevel) level).getChronometer().GuiyeBasma();
		field.setEditable(false);
		field.setText("" + 0);
		targetField.setEditable(false);
		targetField.setText("" + level.getTarget());
		timeField.setEditable(false);
		timeField.setText(timeFormatter.format(((TimeBaseLevel) level).getChronometer().getMinutes())+ ":"
                + timeFormatter.format(((TimeBaseLevel) level).getChronometer().getSeconds()) + "."
                + timeFormatter.format(((TimeBaseLevel) level).getChronometer().getCentiseconds()));
		System.out.println(timeFormatter.format(((TimeBaseLevel) level).getChronometer().getCentiseconds()));
		specialRightField.setEditable(false);
		specialRightField.setText("" + level.getSpecialRightNumber());
		JFrame frame = new JFrame();
		JPanel showingPanel = new JPanel();
		JPanel exitPanel = new JPanel();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setTitle("Chewy Lokum Legend");
		frame.setSize(460, 600);
		showingPanel.setLayout(new GridLayout(1, 6));
		showingPanel.add(createLabel("Target: "));
		showingPanel.add(targetField);
		showingPanel.add(createLabel("Scores: "));
		showingPanel.add(field);
		showingPanel.add(createLabel("Time: "));
		showingPanel.add(timeField);

		exitPanel.setLayout(new GridLayout(1, 5));
		JButton exitButton = createButton("Exit");
		exitButton.addActionListener(new CreatingExitFrame());
		exitPanel.add(exitButton);
		exitPanel.add(createLabel(""));
		exitPanel.add(createLabel(""));
		JButton swapButton = createButton("Swap: ");
		swapButton.addActionListener(new SpecialSwapAction());
		exitPanel.add(swapButton);
		exitPanel.add(specialRightField);
		exitPanel.add(createLabel(""));
		frame.add(showingPanel, BorderLayout.NORTH);
		frame.add(board.getWindow(), BorderLayout.CENTER);
		frame.add(exitPanel, BorderLayout.SOUTH);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void createSettingsFrame() {

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setTitle("Chewy Lokum Legend");
		frame.setSize(400, 100);

		JButton gameButton = createButton("Play New Game");
		panel.add(gameButton);
		gameButton.addActionListener(new creatingPlayFrameAction());
		panel.add(SelectLevel);
		panel.add(LoadedLevel);
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		if(SelectLevel.getSelectedItem()=="Select Level") {
			gameButton.addActionListener(new creatingPlayLoadFrameAction());
		}
	}

	public static JButton createButton(String string) {
		JButton button = new JButton(string);
		button.setPreferredSize(new Dimension(150, 50));
		// button.addActionListener(ActionListener l);
		return button;
	}

	public static JLabel createLabel(String string) {
		JLabel label = new JLabel(string);
		label.setPreferredSize(new Dimension(50, 50));
		return label;
	}
	
	public void GameOver(){
		JOptionPane.showMessageDialog(null, "GAME OVER","Game Over =(", JOptionPane.PLAIN_MESSAGE);
		System.exit(1);
	}
	
	public void Completed(){
		JOptionPane.showMessageDialog(null, "Succeeded","Completed Level :)	", JOptionPane.PLAIN_MESSAGE);
		System.exit(1);
	}
	
	public static void addCompletedLevel(String string) {
		LevelModels.addElement(string);
	}

	public static void addSavedLevel(String string) {
		LoadedLevelModels.addElement(string);
	}

	public int getTarget() {
		return Target;
	}

	public void setTarget() {
		Target = level.getTarget();
	}

	public int getUpdateScore() {
		return Score;
	}

	public void setScore() {
		GUI.Score = board.getScore();
	}

	public int getMoves() {
		return Moves;
	}

	public void setNumOfMoves(int move) {
		// TODO Auto-generated method stub
		Moves = ((MovementBaseLevel) level).getNumberOfMovement();
	}

	// ActionListenerlar buradan baþlýyor.
	private class ChangeBoardListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			System.out.println("PCE " + evt.getPropertyName() + " "
					+ evt.getNewValue());

			field.setText("" + board.getScore());
			if (board.getState() == "GAMEOVER"){
				GameOver();
			} else if (board.getState() == "COMPLETED"){
				Completed();
				int newlevel = level.getCurrentLevel() + 1 ;
				addCompletedLevel(""+newlevel);
			}
		}

	}

	private class ChangeLevelListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			System.out.println("PCE " + evt.getPropertyName() + " "
					+ evt.getNewValue());

			moveField.setText("" + ((MovementBaseLevel) level).getNumberOfMovement());
			specialRightField.setText("" + level.getSpecialRightNumber());
		}

	}
	
	private class ChangeTimeLevelListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			System.out.println("PCE " + evt.getPropertyName() + " "
					+ evt.getNewValue());

			specialRightField.setText("" + level.getSpecialRightNumber());
		}

	}
	
	private class ChangeSayacListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			//System.out.println("PCE " + evt.getPropertyName()
				//	+ " " + evt.getNewValue());
			
	        //field.setText(""+board.getScore());
			 timeField.setText(timeFormatter.format(((TimeBaseLevel) level).getChronometer().getMinutes()) + ":"
		                + timeFormatter.format(((TimeBaseLevel) level).getChronometer().getSeconds()) + "."
		                + timeFormatter.format(((TimeBaseLevel) level).getChronometer().getCentiseconds()));
		}
		
	}


	private class SpecialSwapAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			level.setSpecialRightActive(true);
		}
	}

	private class ExitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
	}

	private class SaveAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(level.isMovementBased()==true) {
				board.saveGameBoard();
			} else
			board.saveGameTimeBoard();
		}
	}

	private class CreatingExitFrame implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			createAskingSave();
		}
	}

	private class creatingPlayFrameAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (LevelModels.getSelectedItem() == "2") {
				board.setGameLevel(new TimeBaseLevel());
				level = board.getGameLevel();
				createTimeBasedPlayFrame();
				((TimeBaseLevel) level).getChronometer().startSayac();
				board.setState();
			} else if (LevelModels.getSelectedItem() == "1"){
				createPlayFrame();
				board.setState();
			}
		}
	}
	
	private class creatingPlayLoadFrameAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(LoadedLevelModels.getSelectedItem() == "1") {
				
				try {
					//board.setGameLevel(new MovementBaseLevel());
					board.initBoard();
					board.loadGameBoard();
					createPlayFrame();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//createPlayFrame();
			} else if (LoadedLevelModels.getSelectedItem() =="2") {
				board.setGameLevel(new TimeBaseLevel());
				level = board.getGameLevel();
				
			
			}
		}
	}

}
