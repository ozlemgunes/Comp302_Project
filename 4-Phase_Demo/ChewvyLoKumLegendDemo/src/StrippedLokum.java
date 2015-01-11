public class StrippedLokum extends Lokum {
	private int type;
	private int posX;
	private int posY;
	private int lokumWidth;
	private int lokumHeight;
	private boolean vertical;
	private boolean horizontal;
	private String source;

	public StrippedLokum(int type, String direction) {
		this.type = type;
		if (direction == "vertical")
			vertical = true;
		else if (direction == "horizontal")
			horizontal = true;

		lokumWidth = 40;
		lokumHeight = 40;
		posX = 0;
		posY = 0;
		initSourceString();

	}

	public int getLokumWidth() {
		// TODO Auto-generated method stub
		return lokumWidth;
	}

	@Override
	public int getLokumHeight() {
		// TODO Auto-generated method stub
		return lokumHeight;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public int getPosX() {
		// TODO Auto-generated method stub
		return posX;
	}

	@Override
	public int getPosY() {
		// TODO Auto-generated method stub
		return posY;
	}

	public void setPosX(int x) {
		this.posX = x;
	}

	public void setPosY(int y) {
		this.posY = y;
	}

	public boolean isVertical() {
		return vertical;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	private void initSourceString() {
		if (isVertical())
			switch (type) {

			case 1:
				source = "greenpistchostrippedvertical.png";
				break;
			case 2:
				source = "hazelnutstrippedvertical.png";
				break;
			case 3:
				source = "coconutstrippedvertical.png";
				break;
			case 4:
				source = "redstrippedvertical.png";
				break;
			default:
				source = "";
				break;
			}

		else if (isHorizontal())
			switch (type) {

			case 1:
				source = "greenpistchostrippedhorizontal.png";
				break;
			case 2:
				source = "hazelnutstrippedhorizontal.png";
				break;
			case 3:
				source = "horizontalpurplestripped.png";
				break;
			case 4:
				source = "redstrippedhorizontal.png";
				break;
			default:
				source = "";
				break;
			}
	}

	public String getSourceString() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
		return true;
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
		if (o instanceof StrippedLokum) {
			if (repOk()) {
				if (((StrippedLokum) o).repOk()) {
					if (((StrippedLokum) o).getLokumHeight() == this.lokumHeight
							&& ((StrippedLokum) o).getLokumWidth() == this.lokumWidth
							&& ((StrippedLokum) o).getPosX() == this.posX
							&& ((StrippedLokum) o).getPosY() == this.posY
							&& ((StrippedLokum) o).getType() == this.type
							&& ((StrippedLokum) o).isHorizontal() == this.horizontal
							&& ((StrippedLokum) o).isVertical() == this.vertical
							&& ((StrippedLokum) o).getSourceString()
									.equals(source))
						statement = true;
					else
						statement = false;
				} else
					throw new NullPointerException(
							"Invalid StrippedLokum Object in StrippedLokum.equals method");
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
