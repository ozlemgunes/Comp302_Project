
public class Null extends Lokum {
	private int type;
	private int posX;
	private int posY;
	private int lokumWidth;
	private int lokumHeight;
	private String source;

	
	public Null() {
		type=-1;
		posX = 0;
		posY = 0;
		lokumWidth = 40;
		lokumHeight = 40;
		initSourceString();
	}
	
	private void initSourceString(){
		source = ((type == -1) ? "nullpiece.png" : "");
	}
	
	@Override
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
	public void setType(int type) {
		this.type= type;
		
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
	
	public void setPosX(int x){
		posX = x;
	}
	
	public void setPosY(int y){
		posY = y;
	}

	@Override
	public String getSourceString() {
		return source;
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
		return false;
	}

	@Override
	public boolean isTimerLokum() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}

