import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GameEngine {
	private Board gameBoard;
	private Level gameLevel;
	private GUI gui;
	private boolean bol = false;

	/**
	 * @requires selectedLevel != null
	 * @param selectedLevel
	 *            The level,chosen by a player, assigns the features of game
	 *            such as number of movements, target.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @modifies gameLevel
	 * @ensures selectedLevel should be assigned to gameLevel.
	 */
	public GameEngine() {
		try {
			gui = new GUI();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @requires gameBoard = null
	 * @modifies gameBoard
	 * @ensures gameBoard should be initialized.
	 */
	public boolean isCompleted() {
		
		if(((MovementBaseLevel) gameLevel).getNumberOfMovement() == 0 && gameBoard.getScore() >= gameLevel.getTarget()) {
			bol = true;
		}
		return bol;
	}
	
	public void nextLevel() throws ParserConfigurationException {
		if(isCompleted()==true){
			JOptionPane.showMessageDialog(null, "Wooowwww Congratulations, You complete the level.\n"+
												"Now, Lets try next level","Congratulations", JOptionPane.PLAIN_MESSAGE );
		
			gameLevel.setCurrentLevel(gameLevel.getCurrentLevel()+1);
		//	gameLevel.getLevelProperties(gameLevel.getCurrentLevel());
			JOptionPane.showMessageDialog(null,"Next Level is "+gameBoard.getGameLevel()+".\n\n\n Good Luck." );
			gameLevel.setTarget(gameLevel.getTarget()+5000);
			((MovementBaseLevel) gameLevel).updateNumOfMovements(15);
			
			gameBoard.initBoard();
		}
	}
	public void playNewGame() {
		gui.createSettingsFrame();
	}

	/**
	 * @requires When numberOfMOvements = 0, targetOfLevel should not be
	 *           completed or if targetOfLevel is completed, then starNumber <
	 *           1.
	 * @modifies gameBoard
	 * @ensures gameBoard should be cleared. Settings should be displayed.
	 */
	public void GameOver() {
		if (((MovementBaseLevel) gameLevel).getNumberOfMovement() == 0
				&& gameBoard.getScore() < gameLevel.getTarget()) {
			System.out.println("Game Over");
			JOptionPane.showMessageDialog(null, "GAME OVER","Game Over =(", JOptionPane.PLAIN_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * @requires exit() and save() methods of Settings , numberOfMovement != 0
	 * @modifies XML file
	 * @ensures if save() method is invoked, the whole feature of gameBoard
	 *          should be saved, and written to XML file. Also, the fields of
	 *          gameLevel should be saved and written to XML file. Afterwards,
	 *          Settings should be displayed.
	 */
	public void exitGame() {

	}

	/**
	 * @requires loadedBoard != null. gameLevel != null. gameBoard = null.
	 *           Whether gameLevel is written to XML file as a saved game should
	 *           be checked.
	 * @param loadedBoard
	 * @throws IOException 
	 * @throws SAXException 
	 * @modifies gameBoard
	 * @ensures gameBoard should be initialized as a loadedBoard.
	 */
	public void loadGame() throws SAXException, IOException {
		try {
			gameBoard.loadGameBoard();
			gameLevel.loadLevel();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
