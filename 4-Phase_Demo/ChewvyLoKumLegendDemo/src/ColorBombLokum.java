public class ColorBombLokum extends Lokum{
	private int type;
	private int posX;
	private int posY;
	private int lokumWidth;
	private int lokumHeight;
	private String source ;
	
	public ColorBombLokum() {
		type = 5;
		lokumWidth = 40;
		lokumHeight = 40;
		posX = 0;
		posY = 0;
		initSourceString();
		
		
	}

	
	private void initSourceString() {		
		if (type == 5)
			source ="bonibon.png";
		else
			source = "";
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
		return true;
	}


	@Override
	public boolean isTimerLokum() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getSourceString() {
		// TODO Auto-generated method stub
		return source;
	}

	public boolean equals(Object o) {
		boolean statement = false;
		if (o instanceof ColorBombLokum) {
			if (repOk()) {
				if (((ColorBombLokum) o).repOk()) {
					if (((ColorBombLokum) o).getLokumHeight() == this.lokumHeight
							&& ((ColorBombLokum) o).getLokumWidth() == this.lokumWidth
							&& ((ColorBombLokum) o).getPosX() == this.posX
							&& ((ColorBombLokum) o).getPosY() == this.posY
							&& ((ColorBombLokum) o).getType() == this.type
							&& ((ColorBombLokum) o).getSourceString()
									.equals(source))
						statement = true;
					else
						statement = false;
				} else
					throw new NullPointerException(
							"Invalid ColorBombLokum Object in ColorBombLokum.equals method");
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
