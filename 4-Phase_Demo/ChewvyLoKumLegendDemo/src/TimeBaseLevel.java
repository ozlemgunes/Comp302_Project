import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.swing.text.Document;


import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TimeBaseLevel extends Level {
	
	private Level gameLevel;
	private int target;
	private int highScore;
	private int currentLevel;
	private boolean specialRightActive;
	private int specialRightNumber;
	private Chronometer chronometer;
	private boolean isTimeBased;
	private boolean isMovementBased;
	private Document document;
	private final PropertyChangeSupport PCS = new PropertyChangeSupport(this);

	/**
	 * @requires Choosing to specific level which is timeBaseLevel.
	 * @modifies target,highScore and timer and currentLevel.
	 * @ensures According to information from XML, initialization of such as
	 *          target=22000, highScore=0, currentLevel=4 and timer=180sec.
	 */
	public TimeBaseLevel() {
		chronometer = new Chronometer();
		isTimeBased = true;
		isMovementBased=false;
		specialRightActive=false;
		target = 22000;
		highScore = 0;
		currentLevel = 2;
		setSpecialRightActive(false);
		specialRightNumber=currentLevel;
		setSpecialRightNumber(2);

	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		PCS.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		PCS.removePropertyChangeListener(l);
	}
	
	@Override
	public void handleSpecialRightModification() {
		// TODO Auto-generated method stub
		PCS.firePropertyChange("specialRightNumber", -1, specialRightNumber);
	}

	public boolean isTimeBased() {
		return isTimeBased;
	}

	/**
	 * @requires timer=0 and target=>22000.
	 * @modifies highScore and timer
	 * @ensures the beginning of the highScore and at the end of the highScore
	 *          will compare then it will change if last highScore will be
	 *          bigger than the beginning score.
	 */

	public void saveCompletedLevel() {
		
	}

	/**
	 * @requires selection of the specific level
	 * @ensures target, highScore, currentLevel and timer will be set on the
	 *          board from XML file.
	 */

	public void loadLevel() {
	}

	public Chronometer getChronometer() {
		return chronometer;
	}

	/**
	 * @requires obtaining to TimerLokum
	 * @modifies timer
	 * @ensures timer will increase after every crush with +5 seconds.
	 * @param timer
	 *            integer
	 */

	public void incrementChronometer() {
		chronometer.incrementTimer();
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public boolean equals(Object o) {
		boolean statement = false;
		if (o instanceof TimeBaseLevel) {
			if (repOk()) {
				if (((TimeBaseLevel) o).repOk()) {
					if (((TimeBaseLevel) o).getTarget() == this.target
							&& ((TimeBaseLevel) o).getCurrentLevel() == this.currentLevel
							&& ((TimeBaseLevel) o).getHighScore() == this.highScore
							&& ((TimeBaseLevel) o).getChronometer() == this.chronometer)
						statement = true;

					else
						statement = false;
				} else
					throw new NullPointerException(
							"Invalid TimeBaseLevel Object in TimeBaseLevel.equals method");
			}
		}
		return statement;
	}

	public boolean repOk() {
		boolean statement = false;
		if (currentLevel <= 0 || currentLevel > 5
				|| chronometer.getCentiseconds() < 0
				|| chronometer.getMinutes() < 0 || chronometer.getSeconds() < 0
				|| target <= 0 || highScore < 0 || this == null)
			statement = false;
		else
			statement = true;
		return statement;
	}

	public String toString() {
		String result = "TimeBaseLevel.toString :\n";
		result += "The target of TimeBaseLevel is :\n" + getTarget() + "\n";
		result += "The highscore of TimeBaseLevel is :\n" + getHighScore()
				+ "\n";
		result += "The chronometer of TimeBaseLevel is :\n" + chronometer.getMinutes()+":"+
				chronometer.getSeconds()+":"+chronometer.getCentiseconds()+":"+ "\n";
		result += "The current level of TimeBaseLevel is :\n"
				+ getCurrentLevel() + "\n";
		return result;

	}

	@Override
	public boolean isSpecialRightActive() {
		// TODO Auto-generated method stub
		return specialRightActive;
	}

	@Override
	public void setSpecialRightActive(boolean specialRightActivity) {
		// TODO Auto-generated method stub
		if (specialRightNumber > 0 && specialRightActivity)
			this.specialRightActive = specialRightActivity;
		else if (!specialRightActivity)
			this.specialRightActive = specialRightActivity;
		
	}

	@Override
	public int getSpecialRightNumber() {
		// TODO Auto-generated method stub
		return specialRightNumber;
	}

	@Override
	public void setSpecialRightNumber(int specialRightNumber) {
		// TODO Auto-generated method stub
		this.specialRightNumber=specialRightNumber;
	}


	@Override
	public void saveCompletedLevel(Level level) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void decrementSpecialRightNumber() {
		// TODO Auto-generated method stub
		specialRightNumber--;
		
	}

	@Override
	public int getHighScore() {
		// TODO Auto-generated method stub
		return highScore;
	}

	@Override
	public void setHighScore(int highscore) {
		// TODO Auto-generated method stub
		this.highScore=highscore;
	}

	@Override
	public boolean isMovementBased() {
		// TODO Auto-generated method stub
		return isMovementBased;
	}

	
	
	


}