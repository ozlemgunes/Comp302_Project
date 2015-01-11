public class WrappedLokum extends Lokum {
	private int type;
	private int posX;
	private int posY;
	private int lokumWidth;
	private int lokumHeight;
	private String source;

	public WrappedLokum(int n) {
		type = n;
		lokumWidth = 40;
		lokumHeight = 40;
		posX = 0;
		posY = 0;
		initSourceString();
	}

	private void initSourceString() {
		// TODO Auto-generated method stub
		switch (type) {
		case 1:
			source = "greenpistchowrapped.png";
			break;

		case 2:
			source = "hazelnutwrapped.png";
			break;

		case 3:
			source = "purplewrapped.png";
			break;

		case 4:
			source = "redrosewrapped.png";
			break;
		default:
			source = "";
			break;
		}
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getType() {
		return type;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getLokumWidth() {
		return lokumWidth;
	}

	public int getLokumHeight() {
		return lokumHeight;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setLokumWidth(int lokumWidth) {
		this.lokumWidth = lokumWidth;
	}

	public void setLokumHeight(int lokumHeight) {
		this.lokumHeight = lokumHeight;
	}

	public String getSourceString() {
		return source;
	}

	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		this.type = type;
	}

	@Override
	public boolean isBasicLokum() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWrappedLokum() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isStrippedLokum() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isColorBomb() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTimerLokum() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean equals(Object o) {
		boolean statement = false;
		if (o instanceof WrappedLokum) {
			if (repOk()) {
				if (((WrappedLokum) o).repOk()) {
					if (((WrappedLokum) o).getLokumHeight() == this.lokumHeight
							&& ((WrappedLokum) o).getLokumWidth() == this.lokumWidth
							&& ((WrappedLokum) o).getPosX() == this.posX
							&& ((WrappedLokum) o).getPosY() == this.posY
							&& ((WrappedLokum) o).getType() == this.type
							&& ((WrappedLokum) o).getSourceString()
									.equals(source))
						statement = true;
					else
						statement = false;
				} else
					throw new NullPointerException(
							"Invalid WrappedLokum Object in WrappedLokum.equals method");
			}
		}
		return statement;
	}

	/**
	 * 
	 * @ensures Returns true if the rep invariant holds for this; otherwise
	 *          returns false
	 */
	public boolean repOk() {
		boolean statement = false;
		if (this == null || type < 1 || type > 4 || lokumHeight <= 0
				|| lokumWidth <= 0 || posX < 0 || posY < 0 || source == null)
			statement = false;
		else if (lokumWidth < posX)
			statement = false;
		else if (lokumHeight < posY)
			statement = false;
		else if (isTimerLokum() == false)
			statement = false;
		else
			statement = true;
		return statement;
	}

}