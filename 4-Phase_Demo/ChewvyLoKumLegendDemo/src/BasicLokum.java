public class BasicLokum extends Lokum {
	private int type;
	private int posX;
	private int posY;
	private int lokumWidth;
	private int lokumHeight;
	private String source;

	public BasicLokum(int n) {
		type = n;
		lokumWidth = 40;
		lokumHeight = 40;
		posX = 0;
		posY = 0;
		initSourceString();
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getLokumWidth() {
		return lokumWidth;
	}

	public void setLokumWidth(int lokumWidth) {
		this.lokumWidth = lokumWidth;
	}

	public int getLokumHeight() {
		return lokumHeight;
	}

	public void setLokumHeight(int lokumHeight) {
		this.lokumHeight = lokumHeight;
	}

	private void initSourceString() {
		switch (type) {
		case 1:
			source = "greenpistcho.png";
			break;
		case 2:
			source = "hazelnut.png";
			break;
		case 3:
			source = "purplecoconut.png";
			break;
		case 4:
			source = "redrose.png";
			break;
		default:
			source = "";
			break;
		}
	}

	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		this.type = type;
	}

	public String getSourceString() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public boolean isBasicLokum() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isWrappedLokum() {
		// TODO Auto-generated method stub
		return false;
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
		if (o instanceof BasicLokum) {
			if (repOk()) {
				if (((BasicLokum) o).repOk()) {
					if (((BasicLokum) o).getLokumHeight() == this.lokumHeight
							&& ((BasicLokum) o).getLokumWidth() == this.lokumWidth
							&& ((BasicLokum) o).getPosX() == this.posX
							&& ((BasicLokum) o).getPosY() == this.posY
							&& ((BasicLokum) o).getType() == this.type
							&& ((BasicLokum) o).getSourceString()
									.equals(source))
						statement = true;
					else
						statement = false;
				} else
					throw new NullPointerException(
							"Invalid BasicLokum Object in BasicLokum.equals method");
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
