import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Chronometer extends JFrame {
	private byte centiseconds;
	private byte seconds;
	private short minutes;
	private DecimalFormat timeFormatter = new DecimalFormat("00");
	private Timer timer;
	private boolean isEqualZero;
	private final PropertyChangeSupport PCS = new PropertyChangeSupport(this);

	public Chronometer() {
		isEqualZero = false;
		centiseconds = 0;
		seconds = 00;
		minutes = 3;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		PCS.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		PCS.removePropertyChangeListener(l);
	}

	public void handleSayacModification() {
		PCS.firePropertyChange("minute", -1, minutes);
		PCS.firePropertyChange("seconds", -1, seconds);
		PCS.firePropertyChange("centicedons", -1, centiseconds);

	}

	public void GuiyeBasma() {
		System.out.println("girdi");
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.start();
				if (centiseconds > 0) {
					centiseconds--;
				} else {
					if (seconds == 0 && minutes == 0) {
						timer.stop();
					} else if (seconds > 0) {
						seconds--;
						centiseconds = 99;
					} else if (minutes > 0) {
						minutes--;
						seconds = 59;
						centiseconds = 99;
					}
				}

				timeFormatter.format(minutes);
				timeFormatter.format(seconds);
				timeFormatter.format(centiseconds);
				handleSayacModification();
				// if (minutes==00 && seconds ==00 && centiseconds==00) {
				// System.out.println("oldu");}

			}
		});
	}

	public void incrementTimer() {
		seconds += 5;
		while ((int)seconds / 60 > 0) {
			minutes++;
			seconds -= 60;
		}
		handleSayacModification();
		System.out.println("timer lokum patladý.");
	}

	public void startSayac() {
		timer.start();
	}

	public byte getCentiseconds() {
		return centiseconds;
	}

	public void setCentiseconds(byte centiseconds) {
		this.centiseconds = centiseconds;
	}

	public byte getSeconds() {
		return seconds;
	}

	public void setSeconds(byte seconds) {
		this.seconds = seconds;
	}

	public short getMinutes() {
		return minutes;
	}

	public void setMinutes(short minutes) {
		this.minutes = minutes;
	}

	public DecimalFormat getTimeFormatter() {
		return timeFormatter;
	}

	public void setTimeFormatter(DecimalFormat timeFormatter) {
		this.timeFormatter = timeFormatter;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public boolean isEqualZero() {
		System.out.println("isEqualZero methoduna girdi.");
		String zero = "00";
		if (timeFormatter.format(minutes) == zero
				&& timeFormatter.format(seconds) == zero
				&& timeFormatter.format(centiseconds) == zero)
			isEqualZero = true;
		System.out.println(""+isEqualZero);
		return isEqualZero;
	}

	public void setEqualZero(boolean isEqualZero) {
		this.isEqualZero = isEqualZero;
	}

}