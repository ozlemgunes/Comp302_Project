import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.w3c.dom.Attr;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

public abstract class Level {


	public Level() {
		
	}

	public abstract boolean isSpecialRightActive();

	public abstract void setSpecialRightActive(boolean specialRightActivity);

	public abstract int getSpecialRightNumber();

	public abstract void setSpecialRightNumber(int specialRightNumber);

	public abstract int getTarget();

	public abstract boolean isTimeBased();
	
	public abstract boolean isMovementBased();

	public abstract void setTarget(int target);
	
	public abstract int getHighScore();
	
	public abstract void setHighScore(int highscore);
	

	public abstract int getCurrentLevel();

	public abstract void setCurrentLevel(int currentLevel);
		
	public abstract void saveCompletedLevel(Level level);
	
	public abstract void loadLevel();


	public abstract void decrementSpecialRightNumber();
		// TODO Auto-generated method stub

	public abstract void handleSpecialRightModification();

	public void setMovements(int numberOfMovement) {
		// TODO Auto-generated method stub
		
	}
}