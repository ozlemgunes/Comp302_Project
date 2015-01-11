import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.w3c.dom.Attr;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

public class MovementBaseLevel extends Level {

	private int target;
	private int highscore;
	private int numberOfMovement;
	private int currentLevel;
	private boolean isMovementBased;
	private boolean specialRightActive;
	private int specialRightNumber;
	private final PropertyChangeSupport PCS = new PropertyChangeSupport(this);
	private boolean isTimeBased;
	

	public MovementBaseLevel() {
		specialRightActive = false;
		specialRightNumber = 1;
		isTimeBased = false;
		isMovementBased=true;
		target = 25000;
		highscore = 0;
		numberOfMovement = 5;
		handleMovementModification();
		currentLevel = 1;
		specialRightNumber=currentLevel;
	}
	
	public boolean isMovementBased() {
		return isMovementBased;
	}


	public void addPropertyChangeListener(PropertyChangeListener l) {
		PCS.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		PCS.removePropertyChangeListener(l);
	}

	public void handleMovementModification() {
		PCS.firePropertyChange("numberOfMovement", -1, numberOfMovement);
	}

	public void handleSpecialRightModification() {
		PCS.firePropertyChange("specialRightNumber", -1, specialRightNumber);
	}

	public boolean isSpecialRightActive() {
		return specialRightActive;
	}

	public void setSpecialRightActive(boolean specialRightActivity) {
		if (specialRightNumber > 0 && specialRightActivity)
			this.specialRightActive = specialRightActivity;
		else if (!specialRightActivity)
			this.specialRightActive = specialRightActivity;
	}

	public int getSpecialRightNumber() {
		return specialRightNumber;
	}

	public void setSpecialRightNumber(int specialRightNumber) {
		this.specialRightNumber = specialRightNumber;
	}

	public int getTarget() {
		return target;
	}

	public boolean isTimeBased() {
		return isTimeBased;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getHighScore() {
		return highscore;
	}

	public void setHighScore(int highscore) {
		this.highscore = highscore;
	}

	public int getNumberOfMovement() {
		return numberOfMovement;
	}

	public void setNumberOfMovement() {
		numberOfMovement--;
	}
	
	public void setMovements() {
		numberOfMovement=0;
	}
	
	public void setMovements(int numberOfMovement) {
		this.numberOfMovement = numberOfMovement;
	}
	
	public int getMovements() {
		return numberOfMovement;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void saveCompletedLevel(Level level) {

	}

	public void loadLevel() {

	}

	public void updateNumOfMovements(int parseInt) {
		// TODO Auto-generated method stub
		numberOfMovement = parseInt;
	}

	public void decrementSpecialRightNumber() {
		// TODO Auto-generated method stub
		specialRightNumber--;
	}

}
