import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Board {
	private int width;
	private int height;
	private int score;
	private Piece[][] arrayOfPieces;
	private Piece firstPressed;
	private Piece secondPressed;
	private Exception NullPointerException;
	private static boolean rightSequence = false;
	private static boolean leftSequence = false;
	private static boolean upSequence = false;
	private static boolean downSequence = false;
	private static boolean horizontalMiddleSequence = false;
	private static boolean verticalMiddleSequence = false;
	private static GameWindow window;
	private SpecialCrushes specialCrush;
	private Level gameLevel;
	private File file;
	private Document document;
	private Chronometer chronometer;
	private static JComboBox LoadedLevel;
	private String state = "";

	private final PropertyChangeSupport PCS = new PropertyChangeSupport(this);

	/**
	 * @requires parameter width != 0 , height != 0
	 * @param width
	 * @param height
	 * @modifies this width, this height, arrayOfPieces
	 * @ensures this width and this height must be initialized by width and
	 *          height, respectively. Also,arrayOfPieces is initialized.
	 */
	public Board(int width, int height, Level level) {
		this.width = width;
		this.height = height;
		score = 0;
		handleScoreModification();
		arrayOfPieces = new Piece[width][height];
		window = new GameWindow();
		specialCrush = new SpecialCrushes(this);
		gameLevel = level;
		setState();
		handleStateModification();
	}

	public String getState() {
		return state;
	}

	public void setState() {
		if (gameLevel.isMovementBased()){
			if (((MovementBaseLevel) gameLevel).getNumberOfMovement() > 0){
				state = "PLAYING";
				handleStateModification();
			} else if (((MovementBaseLevel) gameLevel).getNumberOfMovement() == 0 
					&& gameLevel.getTarget() <= getScore()) {
				state = "COMPLETED";
				handleStateModification();
			} else if (((MovementBaseLevel) gameLevel).getNumberOfMovement() == 0 
					&& gameLevel.getTarget() > getScore()){
				state = "GAMEOVER";
				handleStateModification();
			}
		} else if (gameLevel.isTimeBased()){
			System.out.println("Set state methodu içinde: "+((TimeBaseLevel) gameLevel).getChronometer().isEqualZero());
			if (!((TimeBaseLevel) gameLevel).getChronometer().isEqualZero()){
				state = "PLAYING";
				handleStateModification();
			} else if (((TimeBaseLevel) gameLevel).getChronometer().isEqualZero() 
					&& gameLevel.getTarget() <= getScore()) {
				state = "COMPLETED";
				handleStateModification();
			} else if (((TimeBaseLevel) gameLevel).getChronometer().isEqualZero()
					&& gameLevel.getTarget() > getScore()){
				state = "GAMEOVER";
				handleStateModification();
			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		PCS.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		PCS.removePropertyChangeListener(l);
	}

	public void handleScoreModification() {
		PCS.firePropertyChange("score", -1, score);
	}

	public void handleStateModification() {
		PCS.firePropertyChange("state", -1, state);
	}

	/**
	 * @requires gameLevel != null
	 * @param gameLevel
	 * @modifies arrayOfPieces,scoreBox,starBox, movementBox,targetBox and
	 *           boardFrame.
	 * @ensures Lokums and obstacles are randomly created and they are put into
	 *          arrayOfPieces. According to the fields of game level, boxes are
	 *          initialized and board frame is displayed.
	 */

	public void initBoard() {
		if (gameLevel != null) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					BasicLokum temporary = createRandomLokum();
					temporary.setPosX(i);
					temporary.setPosY(j);
					arrayOfPieces[j][i] = temporary;

					while (checkSequenceLeft(arrayOfPieces[j][i])
							|| checkSequenceUp(arrayOfPieces[j][i])) {

						BasicLokum temporaryOther = createRandomLokum();
						temporaryOther.setPosX(i);
						temporaryOther.setPosY(j);
						arrayOfPieces[j][i] = temporaryOther;
					}
				}
			}
		} else
			return;
	}

	/**
	 * @requires arrayOfPieces != null
	 * @modifies XML file
	 * @ensures the elements of arrayOfPieces should be written to XML file.
	 */
	public void saveGameBoard() {



		if (arrayOfPieces != null) {



		try {



		DocumentBuilderFactory docFactory = DocumentBuilderFactory

		.newInstance();



		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();



		Document doc = docBuilder.newDocument();



		Element rootElement = doc.createElement("game");



		doc.appendChild(rootElement);



		Element player = doc.createElement("player");



		rootElement.appendChild(player);



		Element p_name = doc.createElement("name");



		p_name.appendChild(doc.createTextNode("emre"));



		player.appendChild(p_name);



		getXmlLevel(doc, rootElement);



		getXmlTarget(doc, rootElement);



		getXmlScore(doc, rootElement);



		getXmlMoves(doc, rootElement);



		getXmlArray(doc, rootElement);



		TransformerFactory transformerFactory = TransformerFactory

		.newInstance();



		Transformer transformer = transformerFactory.newTransformer();



		DOMSource source = new DOMSource(doc);



		StreamResult result = new StreamResult(new File(

		"C:/Users/Pc/Desktop/last/src/xmls/saved.xml"));



		// StreamResult console = new StreamResult (System.out);



		// transformer.transform(source, console);



		transformer.transform(source, result);



		System.out.println("file saved!");

		}



		catch (ParserConfigurationException pce) {



		pce.printStackTrace();



		} catch (TransformerException tfe) {



		tfe.printStackTrace();



		}



		}



		}

	public void saveGameTimeBoard() {
		if (arrayOfPieces != null) {

			try {

				DocumentBuilderFactory docFactory = DocumentBuilderFactory
						.newInstance();

				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document doc = docBuilder.newDocument();

				Element rootElement = doc.createElement("game");

				doc.appendChild(rootElement);

				Element player = doc.createElement("player");

				rootElement.appendChild(player);

				Element p_name = doc.createElement("name");

				p_name.appendChild(doc.createTextNode("emre"));

				player.appendChild(p_name);

				getXmlTarget(doc, rootElement);

				getXmlScore(doc, rootElement);

				// getXmlTime (doc, rootElement);

				getXmlArray(doc, rootElement);

				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();

				Transformer transformer = transformerFactory.newTransformer();

				DOMSource source = new DOMSource(doc);

				StreamResult result = new StreamResult(new File(
						"C:/Users/Pc/Desktop/last/src/xmls/saved.xml"));

				transformer.transform(source, result);

				System.out.println("file saved!");
			} catch (ParserConfigurationException pce) {

				pce.printStackTrace();

			} catch (TransformerException tfe) {

				tfe.printStackTrace();

			}
		}
	}

	/*
	 * public void getXmlTime (Document doc, Element element) { String time_val
	 * = Integer.toString("21" ); Element time = doc.createElement("time");
	 * time.appendChild(doc.createTextNode(time_val));
	 * element.appendChild(time);
	 * 
	 * }
	 */
	public void getXmlLevel(Document doc, Element element) {

		String level_val = Integer.toString(gameLevel.getCurrentLevel());

		Attr no = doc.createAttribute("level");

		no.setValue(level_val);

		element.setAttributeNode(no);

	}


public void getXmlArray(Document doc, Element element) {



Element board = doc.createElement("board");



element.appendChild(board);



Element lokums = doc.createElement("lokums");



board.appendChild(lokums);

int a1 =11;

int a2 =22;

int a3 =33;

int a4 =44;



for (int i = 0; i < width; i++) {



for (int j = 0; j < height; j++) {



Element lokum = doc.createElement("lokum");



lokums.appendChild(lokum);



Element ty =doc.createElement("ty");

if(arrayOfPieces[j][i].isStrippedLokum()){

if(((StrippedLokum) arrayOfPieces[j][i]).isVertical()){

ty.appendChild(doc.createTextNode(Integer.toString(a1)));



} if(((StrippedLokum) arrayOfPieces[j][i]).isHorizontal()) {

ty.appendChild(doc.createTextNode(Integer.toString(a2)));

}



}else if(arrayOfPieces[j][i].isWrappedLokum()) {

ty.appendChild(doc.createTextNode(Integer.toString(a3)));



} else {

ty.appendChild(doc.createTextNode(Integer.toString(a4)));



}

lokum.appendChild(ty);

Element type = doc.createElement("type");



type.appendChild(doc.createTextNode(Integer

.toString(arrayOfPieces[j][i].getType())));



lokum.appendChild(type);



Element position = doc.createElement("position");



lokum.appendChild(position);



Element xPos = doc.createElement("xcoord");



xPos.appendChild(doc.createTextNode(Integer

.toString(arrayOfPieces[j][i].getPosX())));



position.appendChild(xPos);



Element yPos = doc.createElement("ycoord");



yPos.appendChild(doc.createTextNode(Integer

.toString(arrayOfPieces[j][i].getPosY())));



position.appendChild(yPos);



}



}



}




	public void getXmlTarget(Document doc, Element element) {

		String target_val = Integer.toString(gameLevel.getTarget());

		Element target = doc.createElement("target");

		target.appendChild(doc.createTextNode(target_val));

		element.appendChild(target);

	}

	public void getXmlScore(Document doc, Element element) {

		String score_val = Integer.toString(getScore());

		Element score = doc.createElement("score");

		score.appendChild(doc.createTextNode(score_val));

		element.appendChild(score);

	}

	public void getXmlMoves(Document doc, Element element) {

		String move_val = Integer.toString(((MovementBaseLevel) gameLevel)
				.getNumberOfMovement());

		Element moves = doc.createElement("moves");

		moves.appendChild(doc.createTextNode(move_val));

		element.appendChild(moves);

	}

	/**
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @requires arrayOfPieces = null
	 * 
	 * @modifies arrayOfPieces
	 * 
	 * @ensures XML file should be read and arrayOfPieces is initialized
	 * 
	 *          according to XML file. After, XML file is cleared.
	 */
	public void loadGameBoard() throws ParserConfigurationException, SAXException, IOException {



		try{



		file = new File(

		"C:/Users/Pc/Desktop/last/src/xmls/saved.xml"); 



		//else if (gameLevel.isTimeBased()==true) {

//			file = new File("../LastChewyLokum/src/xmls/savedTime.xml");

		//}

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		document = db.parse(file);

		Element root =document.getDocumentElement();

		root.normalize();

		//gameLevel.setCurrentLevel(Integer.parseInt(root.getAttribute("level")));

		gameLevel.setTarget(Integer.parseInt(root.getElementsByTagName("target").item(0).getTextContent()));

		initScore(Integer.parseInt(root.getElementsByTagName("score").item(0).getTextContent()));

		score = Integer.parseInt(root.getElementsByTagName("score").item(0).getTextContent());

		handleScoreModification();

//			System.out.println(""+Integer.parseInt(root.getElementsByTagName("score").item(0).getTextContent()));

		gameLevel.setMovements(Integer.parseInt(root.getElementsByTagName("moves").item(0).getTextContent()));



		initScore( Integer.parseInt(root.getElementsByTagName("score").item(0).getTextContent()));

		NodeList lokums = document.getElementsByTagName("lokum");

		for(int temp=0; temp<lokums.getLength(); temp++){

		Element node = (Element) lokums.item(temp);

		node.getAttributes();

//			System.out.println("emre");

		int type = Integer.parseInt(node.getElementsByTagName("type").item(0).getTextContent());

		int ty = Integer.parseInt(node.getElementsByTagName("ty").item(0).getTextContent());

		Lokum lok;



		if(ty==11) {

		lok = new StrippedLokum(type,"vertical");

		lok.setPosX(Integer.parseInt(node.getElementsByTagName("xcoord").item(0).getTextContent()));

		lok.setPosY(Integer.parseInt(node.getElementsByTagName("ycoord").item(0).getTextContent()));

		arrayOfPieces[lok.getPosY()][lok.getPosX()]=lok;

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosX(lok.getPosX());

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosY(lok.getPosY());

		} else if(ty==22) {

		lok = new StrippedLokum(type,"horizontal");

		lok.setPosX(Integer.parseInt(node.getElementsByTagName("xcoord").item(0).getTextContent()));

		lok.setPosY(Integer.parseInt(node.getElementsByTagName("ycoord").item(0).getTextContent()));

		arrayOfPieces[lok.getPosY()][lok.getPosX()]=lok;

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosX(lok.getPosX());

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosY(lok.getPosY());

		} else if(ty==33) {

		lok = new WrappedLokum(type);

		lok.setPosX(Integer.parseInt(node.getElementsByTagName("xcoord").item(0).getTextContent()));

		lok.setPosY(Integer.parseInt(node.getElementsByTagName("ycoord").item(0).getTextContent()));

		arrayOfPieces[lok.getPosY()][lok.getPosX()]=lok;

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosX(lok.getPosX());

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosY(lok.getPosY());

		}  else

		if(ty==44){

		if(type == 5) {

		lok = new ColorBombLokum();

		lok.setPosX(Integer.parseInt(node.getElementsByTagName("xcoord").item(0).getTextContent()));

		lok.setPosY(Integer.parseInt(node.getElementsByTagName("ycoord").item(0).getTextContent()));

		arrayOfPieces[lok.getPosY()][lok.getPosX()]=lok;

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosX(lok.getPosX());

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosY(lok.getPosY());

		} else {

		lok = new BasicLokum(type);

		lok.setPosX(Integer.parseInt(node.getElementsByTagName("xcoord").item(0).getTextContent()));

		lok.setPosY(Integer.parseInt(node.getElementsByTagName("ycoord").item(0).getTextContent()));

		arrayOfPieces[lok.getPosY()][lok.getPosX()]=lok;

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosX(lok.getPosX());

		arrayOfPieces[lok.getPosY()][lok.getPosX()].setPosY(lok.getPosY());

		}

		}

		}

		System.out.println("Board Loaded.");

		}catch (SAXException e){

		e.printStackTrace();

		}catch (IOException e) {

		e.printStackTrace();

		}

		}




	public void loadGameTimeBoard() throws ParserConfigurationException,
			SAXException, IOException {
		try {

			file = new File("C:/Users/Pc/Desktop/last/src/xmls/savedTime.xml");

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			document = db.parse(file);
			Element root = document.getDocumentElement();
			gameLevel.setTarget(Integer.parseInt(root
					.getElementsByTagName("target").item(0).getTextContent()));
			setScore(Integer.parseInt(root.getElementsByTagName("score")
					.item(0).getTextContent()));

			NodeList lokums = document.getElementsByTagName("lokum");
			for (int temp = 0; temp < lokums.getLength(); temp++) {
				Element node = (Element) lokums.item(temp);
				node.getAttributes();
				BasicLokum lok = new BasicLokum(Integer.parseInt(node
						.getElementsByTagName("type").item(0).getTextContent()));
				lok.setPosX(Integer.parseInt(node
						.getElementsByTagName("xcoord").item(0)
						.getTextContent()));
				lok.setPosY(Integer.parseInt(node
						.getElementsByTagName("ycoord").item(0)
						.getTextContent()));
				arrayOfPieces[lok.getPosX()][lok.getPosY()] = lok;
				arrayOfPieces[lok.getPosX()][lok.getPosY()].setPosX(lok
						.getPosX());
				arrayOfPieces[lok.getPosX()][lok.getPosY()].setPosY(lok
						.getPosY());
				System.out.println("("
						+ Integer.parseInt(node.getElementsByTagName("xcoord")
								.item(0).getTextContent())
						+ ","
						+ Integer.parseInt(node.getElementsByTagName("ycoord")
								.item(0).getTextContent()));
			}

			System.out.println("Board Loaded.");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 * @requires Number of movement > 0, some element of arrayOfPieces should be
	 *           null.
	 * @modifies arrayOfPieces
	 * @ensures Existing lokums must fall down and fill the null elements of
	 *          arrayOfPieces. Afterwards randomly lokums must be created
	 *          according to the amount of null elements in arrayOfPieces.
	 *          Created Lokums are placed into arrayOfPieces. Then
	 *          checkSequence()is called for all positions of arrayOfPieces,
	 *          isAnyCombinationLeft() is updated, and also scoreBox is updated.
	 */

	public void updateCrush() {
		for (int i = 0; i < width - 1; i++) {
			for (int j = height - 1; j > 0; j--) {
				if (arrayOfPieces[j][i].getType() == -1) {
					int posy = j;
					while (posy - 1 >= 0
							&& arrayOfPieces[posy - 1][i].getType() == -1) {
						posy--;
					}
					if (posy - 1 >= 0) {
						Piece temp;
						temp = arrayOfPieces[posy - 1][i];
						temp.setPosX(i);
						temp.setPosY(j);
						arrayOfPieces[j][i] = temp;
						Null nullLokum = new Null();
						nullLokum.setPosX(i);
						nullLokum.setPosY(posy - 1);
						arrayOfPieces[posy - 1][i] = nullLokum;
					}
				}
			}
		}
		randomlyFallDown();
	}

	/**
	 * @throws Exception
	 * @requires numOfMovement > 0 , isAnyCombinationLeft() = #false ,
	 *           arrayOfPieces != null
	 * @modifies arrayOfPieces
	 * @ensures isAnyCombinationLeft() = #false
	 */

	public void replaceBoard() {
		if (arrayOfPieces != null
				&& ((MovementBaseLevel) gameLevel).getNumberOfMovement() > 0) {
			Piece[] replaceHelper = new Piece[width * height];
			int counter = 0;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					replaceHelper[counter++] = arrayOfPieces[j][i];
				}
			}

			Collections.shuffle(Arrays.asList(replaceHelper));
			counter--;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					Piece temp = replaceHelper[counter--];
					temp.setPosX(i);
					temp.setPosY(j);
					arrayOfPieces[j][i] = temp;
				}
			}
		}
	}

	/**
	 * @requires Some element of arrayOfPieces should be null.
	 * @modifies arrayOfPieces
	 * @ensures Randomly lokums must be created according to the amount of null
	 *          elements in arrayOfPieces. Created Lokums are placed into
	 *          arrayOfPieces. After that,scoreBox is updated.
	 */

	public void randomlyFallDown() {
		for (int i = 0; i < width; i++) {
			for (int j = height - 1; j >= 0; j--) {
				if (arrayOfPieces[j][i].getType() == -1) {
					if (gameLevel.isMovementBased()) {
						Lokum randomLokum = createRandomLokum();
						randomLokum.setPosX(i);
						randomLokum.setPosY(j);
						arrayOfPieces[j][i] = randomLokum;
					} else if (gameLevel.isTimeBased()) {
						Random rand = new Random();
						int n = rand.nextInt(8) + 1;
						if (n == 1) {
							Lokum randomLokum = createRandomTimerLokum();
							randomLokum.setPosX(i);
							randomLokum.setPosY(j);
							arrayOfPieces[j][i] = randomLokum;
						} else {
							Lokum randomLokum = createRandomLokum();
							randomLokum.setPosX(i);
							randomLokum.setPosY(j);
							arrayOfPieces[j][i] = randomLokum;
						}

					}
				}
			}
		}
	}

	/**
	 * @requires piece.x >= 0 and piece.x < width, piece.y >= 0 and piece.y <
	 *           height, piece != null
	 * @param piece
	 * @return boolean true if the coordinates are in the specific allowed
	 *         boundary.
	 * @throws Exception
	 */

	public boolean isWithinBoard(Piece piece) {
		boolean within = false;
		if (0 <= piece.getPosX() && piece.getPosX() < width
				&& 0 <= piece.getPosY() && piece.getPosY() < height)
			within = true;
		return within;
	}

	/**
	 * @requires arrayOfPieces != null
	 * @return boolean true if any crush can happen in the board.
	 * @throws Exception
	 */
	public boolean isAnyCombinationLeft() {
		boolean state = false;
		Piece temp;
		if (arrayOfPieces != null) {
			outerLoop: for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					temp = arrayOfPieces[j][i];
					temp.setPosX(i);
					temp.setPosY(j);
					if (j - 1 >= 0) {
						swapPiece(temp, arrayOfPieces[j - 1][i]);
						if (checkSequenceLeft(temp)) {
							// Deðiþirse eðer solunda sequence olacak mý diye
							// bakýyor.

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceRight(temp)) {
							// Deðiþirse saðýnda sequence olacak mý diye bakýyor

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceUp(temp)) {
							// Deðiþirse üstünde sequence olacak mý diye
							// bakýyor.

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceHorizontalMiddle(temp)) {
							// Deðiþirse ortada sequence olacak mý diye bakýyor.

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else { // yoksa lokumu eski yerine koyuyor.
							swapPiece(temp, arrayOfPieces[j][i]);
						}

					}
					if (j + 1 < height) {
						swapPiece(temp, arrayOfPieces[j + 1][i]);
						if (checkSequenceLeft(temp)) {
							// Deðiþirse eðer solunda sequence olacak mý diye
							// bakýyor.

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceRight(temp)) {
							// Deðiþirse saðýnda sequence olacak mý diye
							// bakýyor.

							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceUp(temp)) {
							// Deðiþirse üsttünde sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceHorizontalMiddle(temp)) {
							// Deðiþirse ortada sequence olacak mý diye bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else { // yoksa lokumu eski yerine koyuyor.
							swapPiece(temp, arrayOfPieces[j][i]);
						}

					}
					if (i - 1 >= 0) {
						swapPiece(temp, arrayOfPieces[j][i - 1]);
						if (checkSequenceLeft(temp)) {
							// Deðiþirse eðer solunda sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceRight(temp)) {
							// Deðiþirse saðýnda sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceUp(temp)) {
							// Deðiþirse üsttünde sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceHorizontalMiddle(temp)) {
							// Deðiþirse ortada sequence olacak mý diye bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else { // yoksa lokumu eski yerine koyuyor.
							swapPiece(temp, arrayOfPieces[j][i]);
						}

					}
					if (i + 1 < width) {
						swapPiece(temp, arrayOfPieces[j][i + 1]);
						if (checkSequenceLeft(temp)) {
							// Deðiþirse eðer solunda sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceRight(temp)) {
							// Deðiþirse saðýnda sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceUp(temp)) {
							// Deðiþirse üsttünde sequence olacak mý diye
							// bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else if (checkSequenceHorizontalMiddle(temp)) {
							// Deðiþirse ortada sequence olacak mý diye bakýyor.
							state = true;
							swapPiece(temp, arrayOfPieces[j][i]);
							break outerLoop;
						} else { // yoksa lokumu eski yerine koyuyor.
							swapPiece(temp, arrayOfPieces[j][i]);
						}

					}
				}
			}
		}
		return state;
	}

	public Level getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(Level gameLevel) {
		this.gameLevel = gameLevel;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Piece[][] getArrayOfPieces() {
		return arrayOfPieces;
	}

	public void setArrayOfPieces(Piece[][] arrayOfPieces) {
		this.arrayOfPieces = arrayOfPieces;
	}

	public Piece getFirstPressed() {
		return firstPressed;
	}

	public void setFirstPressed(Piece piece) {
		firstPressed = piece;
	}

	public void setSecondPressed(Piece piece) {
		secondPressed = piece;
	}

	public Piece getSecondPressed() {
		return secondPressed;
	}

	/**
	 * @requires Whether (lokum.x + 1), (lokum.x + 2), (lokum.x - 2), (lokum.x -
	 *           1), (lokum.y + 1), (lokum.y + 2), (lokum.y - 1), (lokum.y - 2)
	 *           positions in arrayOfPieces are same as lokum or not is checked.
	 * @param lokum
	 * @return boolean true if there is any crush in the position of lokum.
	 */
	public boolean checkSequenceDown(Piece piece) {

		boolean bool = false;

		// lokumlar isWithinBoard() mu diye kontrol edilecek.
		if (piece.getType() > 0) {

			if (piece.getPosY() + 2 < height) {

				if (piece.getType() == arrayOfPieces[piece.getPosY() + 1][piece

				.getPosX()].getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY() + 2][piece

				.getPosX()].getType())

					bool = true;

				// varsa true.

			}
		}
		return bool;

	}

	// arrayOfPieces[j][i].getType()==arrayOfPieces[j][i-1].getType() &&
	// arrayOfPieces[j][i].getType()==arrayOfPieces[j][i-2].getType())||(arrayOfPieces[j][i].getType()==arrayOfPieces[j-1][i].getType()
	// && arrayOfPieces[j][i].getType()==arrayOfPieces[j-2][i].getType())

	public boolean checkSequenceUp(Piece piece) {

		boolean bool = false;
		if (piece.getType() > 0) {

			if (piece.getPosY() - 2 >= 0) {

				if (piece.getType() == arrayOfPieces[piece.getPosY() - 1][piece

				.getPosX()].getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY() - 2][piece

				.getPosX()].getType())

					bool = true; // lokumun yukarsnda ard arda 2 tane ayn

				// varsa true.

			}
		}
		return bool;

	}

	public boolean checkSequenceRight(Piece piece) {

		boolean bool = false;
		if (piece.getType() > 0) {

			if (piece.getPosX() + 2 < width) {

				if (piece.getType() == arrayOfPieces[piece.getPosY()][piece

				.getPosX() + 1].getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY()][piece

				.getPosX() + 2].getType())

					bool = true;

				// varsa

				// true.

			}
		}
		return bool;

	}

	public boolean checkSequenceLeft(Piece piece) {

		boolean bool = false;

		if (piece.getType() > 0) {
			if (piece.getPosX() - 2 >= 0) {

				if (piece.getType() == arrayOfPieces[piece.getPosY()][piece

				.getPosX() - 1].getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY()][piece

				.getPosX() - 2].getType())

					bool = true; // lokumun solunda ard arda 2 tane ay

				// varsa

				// true.

			}
		}
		return bool;

	}

	public boolean checkSequenceHorizontalMiddle(Piece piece) {

		boolean bool = false;
		if (piece.getType() > 0) {
			if (piece.getPosX() - 1 >= 0 && piece.getPosX() + 1 < width) {

				if (piece.getType() == arrayOfPieces[piece.getPosY()][piece
						.getPosX() + 1]

				.getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY()][piece

				.getPosX() - 1].getType())

					bool = true; // lokumun
			}
			// varsa true.
		}
		return bool;

	}

	public boolean checkSequenceVerticalMiddle(Piece piece) {

		boolean bool = false;
		if (piece.getType() > 0) {
			if (piece.getPosY() - 1 >= 0 && piece.getPosY() + 1 < height) {

				if (piece.getType() == arrayOfPieces[piece.getPosY() + 1][piece

				.getPosX()].getType()

				&& piece.getType() == arrayOfPieces[piece.getPosY() - 1][piece

				.getPosX()].getType())

					bool = true; // lokumun
			}
		}
		return bool;

	}

	/**
	 * @requires checkSequence() = true. Whether sequence of crush is upwards or
	 *           downwards or rightwards or leftwards is controlled.
	 * @param lokum
	 * @modifies arrayOfPieces
	 * @ensures the positions of sequence of crush should be null after
	 *          controlling. Then lokums in the sequence should be disappeared.
	 */

	public void remove(Lokum lokum) {
		int posX = lokum.getPosX();
		int posY = lokum.getPosY();
		int type = lokum.getType();
		if (arrayOfPieces != null) {
			if (type > 0) {
				if (checkSequenceUp(lokum))
					upSequence = true;

				if (checkSequenceRight(lokum))
					rightSequence = true;

				if (checkSequenceLeft(lokum))
					leftSequence = true;

				if (checkSequenceHorizontalMiddle(lokum))
					horizontalMiddleSequence = true;

				if (checkSequenceVerticalMiddle(lokum))
					verticalMiddleSequence = true;

				if (checkSequenceDown(lokum))
					downSequence = true;

				if (upSequence == false && rightSequence == false
						&& verticalMiddleSequence == false
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& downSequence == false)
					return;
				else if (upSequence == true && rightSequence == false
						&& verticalMiddleSequence == false
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY - i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY - i);
						arrayOfPieces[posY - i][posX] = nullLokum;
					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					upSequence = false;
					return;
				} else if (upSequence == false && rightSequence == true
						&& verticalMiddleSequence == false
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					rightSequence = false;
					return;
				} else if (upSequence == false && rightSequence == false
						&& verticalMiddleSequence == true
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 2; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;

					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					verticalMiddleSequence = false;
					return;
				} else if (upSequence == false && rightSequence == false
						&& verticalMiddleSequence == false
						&& leftSequence == true
						&& horizontalMiddleSequence == false
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY][posX - i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX - i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX - i] = nullLokum;

					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					leftSequence = false;
					return;
				} else if (upSequence == false && rightSequence == false
						&& verticalMiddleSequence == false
						&& leftSequence == false
						&& horizontalMiddleSequence == true
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 2; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;

					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					horizontalMiddleSequence = false;
					return;
				} else if (upSequence == false && rightSequence == false
						&& verticalMiddleSequence == false
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& downSequence == true) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;

					}
					setScore(Score.basicCalculation());
					handleScoreModification();
					downSequence = false;
					return;
				}
				// 2 li olasýlýklar
				// Special lokums can occur right after crushes.
				// Codes below create special lokums and remove crushed lokums.

				else if (upSequence == true && rightSequence == true
						&& verticalMiddleSequence == false
						&& downSequence == false
						&& horizontalMiddleSequence == false
						&& leftSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY - i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY - i);
						arrayOfPieces[posY - i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokumOther = new Null();
							nullLokumOther.setPosX(posX + i);
							nullLokumOther.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokumOther;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					rightSequence = false;
					return;
				} else if (upSequence == true && verticalMiddleSequence == true
						&& horizontalMiddleSequence == false
						&& leftSequence == false && downSequence == false
						&& rightSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 2; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;

					}
					// create a horizontal stripped in a given type of input
					// lokum
					// in the
					// arrayOfPieces[posY][posX]
					StrippedLokum temp = new StrippedLokum(type, "horizontal");
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (upSequence == true && leftSequence == true
						&& rightSequence == false && downSequence == false
						&& horizontalMiddleSequence == false
						&& verticalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY - i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY - i);
						arrayOfPieces[posY - i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									- i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokumOther = new Null();
							nullLokumOther.setPosX(posX - i);
							nullLokumOther.setPosY(posY);
							arrayOfPieces[posY][posX - i] = nullLokumOther;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					leftSequence = false;
					return;
				} else if (upSequence == true
						&& horizontalMiddleSequence == true
						&& downSequence == false
						&& verticalMiddleSequence == false
						&& rightSequence == false && leftSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY][posX - 1 + i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX - 1 + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX - 1 + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY - i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokumOther = new Null();
							nullLokumOther.setPosX(posX);
							nullLokumOther.setPosY(posY - i);
							arrayOfPieces[posY - i][posX] = nullLokumOther;
						}

					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (upSequence == true && downSequence == true
						&& rightSequence == false && leftSequence == false
						&& horizontalMiddleSequence == false
						&& verticalMiddleSequence == true) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
					}
					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					downSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (rightSequence == true
						&& verticalMiddleSequence == true
						&& leftSequence == false
						&& horizontalMiddleSequence == false
						&& upSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum temp = (Lokum) arrayOfPieces[posY + i - 1][posX];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i - 1);
						arrayOfPieces[posY + i - 1][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (rightSequence == true && leftSequence == true
						&& horizontalMiddleSequence == true
						&& verticalMiddleSequence == false
						&& upSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX + i];
						if (otherTemp.isStrippedLokum()) {
							if (((StrippedLokum) otherTemp).isVertical())
								removeColumn(otherTemp);
							removeRow(otherTemp);
						} else if (otherTemp.isWrappedLokum())
							removeRange(otherTemp, 1);
						else if (gameLevel.isTimeBased()) {
							if (otherTemp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;

					}
					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					leftSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (rightSequence == true
						&& horizontalMiddleSequence == true
						&& leftSequence == false
						&& verticalMiddleSequence == false
						&& upSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX + i];
						if (otherTemp.isStrippedLokum()) {
							if (((StrippedLokum) otherTemp).isVertical())
								removeColumn(otherTemp);
							removeRow(otherTemp);
						} else if (otherTemp.isWrappedLokum())
							removeRange(otherTemp, 1);
						else if (gameLevel.isTimeBased()) {
							if (otherTemp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;

					}
					// create a vertical stripped in a given type of input
					// lokum in the arrayOfPieces[posY][posX]
					StrippedLokum temp = new StrippedLokum(type, "vertical");
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (rightSequence == true && downSequence == true
						&& leftSequence == false && upSequence == false
						&& horizontalMiddleSequence == false
						&& verticalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}

					}

					// create a wrapped in a given type of input
					// lokum in the arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					downSequence = false;
					return;
				} else if (verticalMiddleSequence == true
						&& leftSequence == true && rightSequence == false
						&& horizontalMiddleSequence == false
						&& upSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i - 1][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i - 1);
						arrayOfPieces[posY + i - 1][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									- i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX - i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX - i] = nullLokum2;
						}

					}
					// create a wrapped in a given type of input
					// lokum in the arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					verticalMiddleSequence = false;
					leftSequence = false;
					return;
				} else if (verticalMiddleSequence == true
						&& downSequence == true && upSequence == false
						&& horizontalMiddleSequence == false
						&& rightSequence == false && leftSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
						if (otherTemp.isStrippedLokum()) {
							if (((StrippedLokum) otherTemp).isVertical())
								removeColumn(otherTemp);
							removeRow(otherTemp);
						} else if (otherTemp.isWrappedLokum())
							removeRange(otherTemp, 1);
						else if (gameLevel.isTimeBased()) {
							if (otherTemp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;

					}
					// create a horizontal in a given type of input
					// lokum in the arrayOfPieces[posY][posX]
					StrippedLokum temp = new StrippedLokum(type, "horizontal");
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					verticalMiddleSequence = false;
					downSequence = false;
					return;
				} else if (leftSequence == true
						&& horizontalMiddleSequence == true
						&& rightSequence == false
						&& verticalMiddleSequence == false
						&& upSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 2; i++) {
						Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX + i];
						if (otherTemp.isStrippedLokum()) {
							if (((StrippedLokum) otherTemp).isVertical())
								removeColumn(otherTemp);
							removeRow(otherTemp);
						} else if (otherTemp.isWrappedLokum())
							removeRange(otherTemp, 1);
						else if (gameLevel.isTimeBased()) {
							if (otherTemp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;

					}
					// create a vertical stripped in a given type of input lokum
					// in
					// the
					// arrayOfPieces[posY][posX]
					StrippedLokum temp = new StrippedLokum(type, "vertical");
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					leftSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (leftSequence == true && downSequence == true
						&& verticalMiddleSequence == false
						&& horizontalMiddleSequence == false
						&& upSequence == false && rightSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX - i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX - i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX - i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}

					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					leftSequence = false;
					downSequence = false;
					return;
				} else if (horizontalMiddleSequence == true
						&& downSequence == true && upSequence == false
						&& verticalMiddleSequence == false
						&& rightSequence == false && leftSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = 0; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX - 1 + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX - 1 + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX - 1 + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					horizontalMiddleSequence = false;
					downSequence = false;
					return;
				}
				// 3 lü olasýlýklar
				else if (upSequence == true && rightSequence == true
						&& verticalMiddleSequence == true
						&& horizontalMiddleSequence == false
						&& leftSequence == false && downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY - i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY - i);
						arrayOfPieces[posY - i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					rightSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (upSequence == true && rightSequence == true
						&& leftSequence == true
						&& horizontalMiddleSequence == true
						&& verticalMiddleSequence == false
						&& downSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i < 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}
					}

					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					leftSequence = false;
					rightSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (upSequence == true && rightSequence == true
						&& horizontalMiddleSequence == true
						&& verticalMiddleSequence == false
						&& downSequence == false && leftSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY - i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY - i);
							arrayOfPieces[posY - i][posX] = nullLokum2;
						}
					}
					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					rightSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (upSequence == true && rightSequence == true
						&& downSequence == true
						&& verticalMiddleSequence == true
						&& leftSequence == false
						&& horizontalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					rightSequence = false;
					downSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (upSequence == true && verticalMiddleSequence == true
						&& leftSequence == true && rightSequence == false
						&& downSequence == false
						&& horizontalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 2; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
						if (i < 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					verticalMiddleSequence = false;
					leftSequence = false;
					return;
				} else if (upSequence == true && leftSequence == true
						&& horizontalMiddleSequence == true
						&& downSequence == false && rightSequence == true
						&& verticalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 2; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i < 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					leftSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (upSequence == true && leftSequence == true
						&& downSequence == true
						&& verticalMiddleSequence == true
						&& rightSequence == false
						&& horizontalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
						if (i < 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					upSequence = false;
					leftSequence = false;
					downSequence = false;
					verticalMiddleSequence = false;
					return;
				} else if (rightSequence == true
						&& verticalMiddleSequence == true
						&& downSequence == true) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									+ i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX + i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX + i] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					verticalMiddleSequence = false;
					downSequence = false;
					return;
				} else if (rightSequence == true && leftSequence == true
						&& downSequence == true
						&& horizontalMiddleSequence == true
						&& verticalMiddleSequence == false
						&& upSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}
					}

					// create a color bomb in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					ColorBombLokum temp = new ColorBombLokum();
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					leftSequence = false;
					downSequence = false;
					horizontalMiddleSequence = false;
					return;
				} else if (rightSequence == true
						&& horizontalMiddleSequence == true
						&& downSequence == true && leftSequence == false
						&& upSequence == false
						&& verticalMiddleSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY + i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY + i);
							arrayOfPieces[posY + i][posX] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					rightSequence = false;
					horizontalMiddleSequence = false;
					downSequence = false;
					return;
				} else if (leftSequence == true
						&& horizontalMiddleSequence == true
						&& downSequence == true && rightSequence == false
						&& verticalMiddleSequence == false
						&& upSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -2; i < 2; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY][posX + i];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX + i);
						nullLokum.setPosY(posY);
						arrayOfPieces[posY][posX + i] = nullLokum;
						if (i < 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY - i][posX];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX);
							nullLokum2.setPosY(posY - i);
							arrayOfPieces[posY - i][posX] = nullLokum2;

						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					leftSequence = false;
					horizontalMiddleSequence = false;
					downSequence = false;
					return;
				} else if (leftSequence == true
						&& verticalMiddleSequence == true
						&& downSequence == true && rightSequence == false
						&& horizontalMiddleSequence == false
						&& upSequence == false) {
					// swap edildikten sonra kontrol edip siliyoruz.
					for (int i = -1; i < 3; i++) {
						Lokum Temp = (Lokum) arrayOfPieces[posY + i][posX];
						if (Temp.isStrippedLokum()) {
							if (((StrippedLokum) Temp).isVertical())
								removeColumn(Temp);
							removeRow(Temp);
						} else if (Temp.isWrappedLokum())
							removeRange(Temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (Temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(posX);
						nullLokum.setPosY(posY + i);
						arrayOfPieces[posY + i][posX] = nullLokum;
						if (i > 0) {
							Lokum otherTemp = (Lokum) arrayOfPieces[posY][posX
									- i];
							if (otherTemp.isStrippedLokum()) {
								if (((StrippedLokum) otherTemp).isVertical())
									removeColumn(otherTemp);
								removeRow(otherTemp);
							} else if (otherTemp.isWrappedLokum())
								removeRange(otherTemp, 1);
							else if (gameLevel.isTimeBased()) {
								if (otherTemp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum2 = new Null();
							nullLokum2.setPosX(posX - i);
							nullLokum2.setPosY(posY);
							arrayOfPieces[posY][posX - i] = nullLokum2;
						}
					}

					// create a wrapped in a given type of input lokum in the
					// arrayOfPieces[posY][posX]
					WrappedLokum temp = new WrappedLokum(type);
					temp.setPosX(posX);
					temp.setPosY(posY);
					arrayOfPieces[posY][posX] = temp;
					setScore(Score.wrappedCombinationCalculation());
					handleScoreModification();
					leftSequence = false;
					verticalMiddleSequence = false;
					downSequence = false;
					return;
				}
			} else
				return;
		} else
			return;
	}

	public void removeRange(Lokum lokum, int range) {
		Lokum forColorBomb = lokum;
		int X = lokum.getPosX();
		int Y = lokum.getPosY();
		if (range != 1 && range != 2) {
			System.out.println("Invalid input range");
		} else if (lokum.getType() > 0) {
			if (range == 1) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (Y + j < height && 0 <= Y + j && X + i < width
								&& 0 <= X + i) {

							Lokum Temp = (Lokum) arrayOfPieces[Y + j][X + i];
							if (Temp.isStrippedLokum()) {
								if (((StrippedLokum) Temp).isVertical())
									removeColumn(Temp);
								else
									removeRow(Temp);
							} else if (gameLevel.isTimeBased()) {
								if (Temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							} else if (Temp.isColorBomb())
								removedByUsingColorBomb(forColorBomb, Temp);

							Null lokumInst = new Null();
							lokumInst.setPosY(Y + j);
							lokumInst.setPosX(X + i);
							arrayOfPieces[Y + j][X + i] = lokumInst;
						}
					}
				}
				System.out.println("Range silindi.");
				System.out.println("X coor: " + X + " " + "Y coor: " + Y);
				setScore(Score.usingWrappedCalculation());
				handleScoreModification();
				return;
			} else if (range == 2) {
				for (int i = -2; i < 3; i++) {
					for (int j = -2; j < 3; j++) {
						if (Y + j < height && 0 <= Y + j && X + i < width
								&& 0 <= X + i) {
							Lokum Temp = (Lokum) arrayOfPieces[Y + j][X + i];
							if (Temp.isStrippedLokum()) {
								if (((StrippedLokum) Temp).isVertical())
									removeColumn(Temp);
								else
									removeRow(Temp);
							} else if (gameLevel.isTimeBased()) {
								if (Temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							} else if (Temp.isColorBomb())
								removedByUsingColorBomb(forColorBomb, Temp);
							Null lokumInst = new Null();
							lokumInst.setPosY(Y + j);
							lokumInst.setPosX(X + i);
							arrayOfPieces[Y + j][X + i] = lokumInst;
						}
					}
				}
				System.out.println("Range silindi.");
				System.out.println("X coor: " + X + " " + "Y coor: " + Y);
				setScore(Score.usingDoubleWrappedCalculation());
				handleScoreModification();
				return;
			}
		}
	}

	public int removedByUsingColorBomb(Lokum lokum, Lokum colorBomb) {
		int type = lokum.getType();
		int X = colorBomb.getPosX();
		int Y = colorBomb.getPosY();
		int counter = 0;
		if (type > 0) {
			if (colorBomb.isColorBomb()) {
				Null nullLokum = new Null();
				nullLokum.setPosX(X);
				nullLokum.setPosY(Y);
				arrayOfPieces[Y][X] = nullLokum;
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						if (arrayOfPieces[j][i].getType() == type) {
							counter++;

							Lokum Temp = (Lokum) arrayOfPieces[j][i];
							if (Temp.isStrippedLokum()) {
								if (((StrippedLokum) Temp).isVertical())
									removeColumn(Temp);
								removeRow(Temp);
							} else if (Temp.isWrappedLokum())
								removeRange(Temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (Temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}

							Null nullLokum2 = new Null();
							nullLokum2.setPosX(i);
							nullLokum2.setPosY(j);
							arrayOfPieces[j][i] = nullLokum2;
						}
					}
				}
			} else if (lokum.isColorBomb()) {
				Null nullLokum = new Null();
				nullLokum.setPosX(lokum.getPosX());
				nullLokum.setPosY(lokum.getPosY());
				arrayOfPieces[lokum.getPosY()][lokum.getPosX()] = nullLokum;
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						if (arrayOfPieces[j][i].getType() == type) {
							counter++;

							Lokum Temp = (Lokum) arrayOfPieces[j][i];
							if (Temp.isStrippedLokum()) {
								if (((StrippedLokum) Temp).isVertical())
									removeColumn(Temp);
								removeRow(Temp);
							} else if (Temp.isWrappedLokum())
								removeRange(Temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (Temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}

							Null nullLokum2 = new Null();
							nullLokum2.setPosX(i);
							nullLokum2.setPosY(j);
							arrayOfPieces[j][i] = nullLokum2;
						}
					}
				}

			}
		}
		System.out.println("Bonibon patlatýldý.");
		setScore(Score.usingColorBombCalculation(counter));
		handleScoreModification();
		return counter;
	}

	/**
	 * @requires checkSequence = true.
	 * @param lokum
	 * @throws Exception
	 * @modifies arrayOfPieces
	 * @ensures the whole elements in arrayOfPieces[lokum.y] should be null and
	 *          the whole piece in the horizontal line should be disappeared.
	 */

	public void removeRow(Lokum lokum) {
		Lokum forColorBomb = lokum;
		int Y = lokum.getPosY();
		for (int i = 0; i < width; i++) {
			Lokum Temp = (Lokum) arrayOfPieces[Y][i];
			if (Temp.isStrippedLokum()) {
				if (((StrippedLokum) Temp).isVertical())
					removeColumn(Temp);
			} else if (Temp.isWrappedLokum())
				removeRange(Temp, 1);
			else if (gameLevel.isTimeBased()) {
				if (Temp.isTimerLokum())
					((TimeBaseLevel) gameLevel).incrementChronometer();
			} else if (Temp.isColorBomb())
				removedByUsingColorBomb(forColorBomb, Temp);
			Null nullLokum = new Null();
			nullLokum.setPosX(i);
			nullLokum.setPosY(Y);
			arrayOfPieces[Y][i] = nullLokum;
		}
		System.out.println(Y + ". sýra silindi.");
		setScore(Score.usingStrippedCalculation(width));
		handleScoreModification();
	}

	/**
	 * @requires checkSequence = true.
	 * @param lokum
	 * @throws Exception
	 * @modifies arrayOfPieces
	 * @ensures the whole elements in arrayOfPieces[lokum.x] should be null and
	 *          the whole piece in the vertical line should be disappeared.
	 */

	public void removeColumn(Lokum lokum) {
		Lokum forColorBomb = lokum;
		int X = lokum.getPosX();
		for (int j = 0; j < height; j++) {
			Lokum Temp = (Lokum) arrayOfPieces[X][j];
			if (Temp.isStrippedLokum()) {
				if (((StrippedLokum) Temp).isHorizontal())
					removeRow(Temp);
			} else if (Temp.isWrappedLokum())
				removeRange(Temp, 1);
			else if (gameLevel.isTimeBased()) {
				if (Temp.isTimerLokum())
					((TimeBaseLevel) gameLevel).incrementChronometer();
			} else if (Temp.isColorBomb())
				removedByUsingColorBomb(forColorBomb, Temp);

			Null nullLokum = new Null();
			nullLokum.setPosX(X);
			nullLokum.setPosY(j);
			arrayOfPieces[j][X] = nullLokum;
		}
		System.out.println(X + ". sütun silindi.");
		setScore(Score.usingStrippedCalculation(height));
		handleScoreModification();
	}

	/**
	 * @throws Exception
	 * @requires arrayOfPieces != null
	 * @modifies arrayOfPieces
	 * @ensures the whole element in the arrayOfPieces should be null. And the
	 *          whole piece should be disappeared. Then, exit the game.
	 */
	public void clear() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// arrayOfPieces[i][j] = null;
				/*
				 * Lokum Temp = (Lokum) arrayOfPieces[j][i]; if
				 * (Temp.isStrippedLokum()) { if (((StrippedLokum)
				 * Temp).isVertical()) removeColumn(Temp); removeRow(Temp); }
				 * else if (Temp.isWrappedLokum()) removeRange(Temp, 1); else if
				 * (gameLevel.isTimeBased()) { if (Temp.isTimerLokum())
				 * ((TimeBaseLevel) gameLevel) .incrementChronometer(); }
				 */
				Null nullLokum = new Null();
				nullLokum.setPosX(i);
				nullLokum.setPosY(j);
				arrayOfPieces[j][i] = nullLokum;
			}
		}
		System.out
				.println("Board tamamen temizlendi veya lokumlarýn hepsi patlatýldý.");
	}

	public void checkAndRemoveColorBombProb(Lokum lokum) {
		int X = lokum.getPosX();
		int Y = lokum.getPosY();
		int type = lokum.getType();

		if (type > 0 && type < 5) {
			if (Y + 4 < height) {
				if (type == arrayOfPieces[Y + 4][X].getType()
						&& type == arrayOfPieces[Y + 3][X].getType()
						&& type == arrayOfPieces[Y + 2][X].getType()
						&& type == arrayOfPieces[Y + 1][X].getType()) {
					for (int i = 0; i < 5; i++) {
						Lokum temp = (Lokum) arrayOfPieces[Y + i][X];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(X);
						nullLokum.setPosX(Y + i);
						arrayOfPieces[Y + i][X] = nullLokum;
					}
					ColorBombLokum bomb = new ColorBombLokum();
					bomb.setPosX(X);
					bomb.setPosY(Y + 4);
					arrayOfPieces[Y + 4][X] = bomb;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					return;
				}
			} else if (X + 4 < width) {
				if (type == arrayOfPieces[Y][X + 4].getType()
						&& type == arrayOfPieces[Y][X + 3].getType()
						&& type == arrayOfPieces[Y][X + 2].getType()
						&& type == arrayOfPieces[Y][X + 1].getType()) {
					for (int i = 0; i < 5; i++) {
						Lokum temp = (Lokum) arrayOfPieces[Y][X + i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(X + i);
						nullLokum.setPosX(Y);
						arrayOfPieces[Y][X + i] = nullLokum;
					}
					ColorBombLokum bomb = new ColorBombLokum();
					bomb.setPosX(X + 4);
					bomb.setPosY(Y);
					arrayOfPieces[Y][X + 4] = bomb;
					setScore(Score.colorBombCombinationCalculation());
					handleScoreModification();
					return;
				}
			}
		}

	}

	public void checkAndRemoveStrippedProb(Lokum lokum) {
		int X = lokum.getPosX();
		int Y = lokum.getPosY();
		int type = lokum.getType();

		if (type > 0 && type < 5) {
			if (Y + 3 < height) {
				if (type == arrayOfPieces[Y + 3][X].getType()
						&& type == arrayOfPieces[Y + 2][X].getType()
						&& type == arrayOfPieces[Y + 1][X].getType()) {
					for (int i = 0; i < 4; i++) {
						Lokum temp = (Lokum) arrayOfPieces[Y + i][X];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(X);
						nullLokum.setPosX(Y + i);
						arrayOfPieces[Y + i][X] = nullLokum;
					}
					StrippedLokum strpLokum = new StrippedLokum(type,
							"horizontal");
					strpLokum.setPosX(X);
					strpLokum.setPosY(Y + 3);
					arrayOfPieces[Y + 3][X] = strpLokum;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					return;
				}
			} else if (X + 4 < width) {
				if (type == arrayOfPieces[Y][X + 3].getType()
						&& type == arrayOfPieces[Y][X + 2].getType()
						&& type == arrayOfPieces[Y][X + 1].getType()) {
					for (int i = 0; i < 4; i++) {
						Lokum temp = (Lokum) arrayOfPieces[Y][X + i];
						if (temp.isStrippedLokum()) {
							if (((StrippedLokum) temp).isVertical())
								removeColumn(temp);
							removeRow(temp);
						} else if (temp.isWrappedLokum())
							removeRange(temp, 1);
						else if (gameLevel.isTimeBased()) {
							if (temp.isTimerLokum())
								((TimeBaseLevel) gameLevel)
										.incrementChronometer();
						}
						Null nullLokum = new Null();
						nullLokum.setPosX(X + i);
						nullLokum.setPosX(Y);
						arrayOfPieces[Y][X + i] = nullLokum;
					}
					StrippedLokum strpLokum = new StrippedLokum(type,
							"vertical");
					strpLokum.setPosX(X + 3);
					strpLokum.setPosY(Y);
					arrayOfPieces[Y][X + 3] = strpLokum;
					setScore(Score.strippedCombinationCalculation());
					handleScoreModification();
					return;
				}
			}
		}

	}

	public void checkAndRemoveLShapeProb(Lokum lokum) {
		int X = lokum.getPosX();
		int Y = lokum.getPosY();
		int type = lokum.getType();

		if (type > 0 && type < 5) {
			if (X + 2 < width) {
				if (Y + 2 < height) {
					if (type == arrayOfPieces[Y + 1][X].getType()
							&& type == arrayOfPieces[Y + 2][X].getType()
							&& type == arrayOfPieces[Y + 2][X + 1].getType()
							&& type == arrayOfPieces[Y + 2][X + 2].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y + i][X];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X);
							nullLokum.setPosX(Y + i);
							arrayOfPieces[Y + i][X] = nullLokum;
							if (i > 0) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y + 2][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y + 2);
								arrayOfPieces[Y + 2][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y + 2);
						arrayOfPieces[Y + 2][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					} else if (type == arrayOfPieces[Y + 1][X + 2].getType()
							&& type == arrayOfPieces[Y + 2][X + 2].getType()
							&& type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y + i][X + 2];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X + 2);
							nullLokum.setPosX(Y + i);
							arrayOfPieces[Y + i][X + 2] = nullLokum;
							if (i < 2) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y);
								arrayOfPieces[Y][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y);
						arrayOfPieces[Y][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					} else if (type == arrayOfPieces[Y + 1][X].getType()
							&& type == arrayOfPieces[Y + 2][X].getType()
							&& type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y + i][X];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X);
							nullLokum.setPosX(Y + i);
							arrayOfPieces[Y + i][X] = nullLokum;
							if (i > 0) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y);
								arrayOfPieces[Y][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y + 2);
						arrayOfPieces[Y + 2][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					}
				}
				if (Y - 2 >= 0) {
					if (type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()
							&& type == arrayOfPieces[Y - 1][X + 2].getType()
							&& type == arrayOfPieces[Y - 2][X + 2].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y - i][X + 2];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X + 2);
							nullLokum.setPosX(Y - i);
							arrayOfPieces[Y - i][X + 2] = nullLokum;
							if (i < 2) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y);
								arrayOfPieces[Y][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y);
						arrayOfPieces[Y][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					}
				}
			}
		}
	}

	public void checkAndRemoveTShapeProb(Lokum lokum) {
		int X = lokum.getPosX();
		int Y = lokum.getPosY();
		int type = lokum.getType();

		if (type > 0 && type < 5) {
			if (X + 2 < width) {
				if (Y + 2 < height) {
					if (type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()
							&& type == arrayOfPieces[Y + 1][X + 1].getType()
							&& type == arrayOfPieces[Y + 2][X + 1].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y][X + i];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X + i);
							nullLokum.setPosX(Y);
							arrayOfPieces[Y][X + i] = nullLokum;
							if (i > 0) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y + i][X + 1];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + 1);
								otherNullLokum.setPosX(Y + i);
								arrayOfPieces[Y + i][X + 1] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y);
						arrayOfPieces[Y][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					} else if (type == arrayOfPieces[Y + 1][X].getType()
							&& type == arrayOfPieces[Y + 2][X].getType()
							&& type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y + i][X];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X);
							nullLokum.setPosX(Y + i);
							arrayOfPieces[Y + i][X] = nullLokum;
							if (i > 0) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y);
								arrayOfPieces[Y][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y + 2);
						arrayOfPieces[Y + 2][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					}
				}
				if (Y - 2 >= 0) {
					if (type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()
							&& type == arrayOfPieces[Y - 1][X + 1].getType()
							&& type == arrayOfPieces[Y - 2][X + 1].getType()) {
						for (int i = 0; i < 3; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y][X + i];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X + i);
							nullLokum.setPosX(Y);
							arrayOfPieces[Y][X + i] = nullLokum;
							if (i > 0) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y - i][X + 1];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + 1);
								otherNullLokum.setPosX(Y - i);
								arrayOfPieces[Y - i][X + 1] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y);
						arrayOfPieces[Y][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					}
				}
				if (Y + 1 < height && Y - 1 >= 0) {
					if (type == arrayOfPieces[Y][X + 1].getType()
							&& type == arrayOfPieces[Y][X + 2].getType()
							&& type == arrayOfPieces[Y - 1][X + 2].getType()
							&& type == arrayOfPieces[Y + 1][X + 2].getType()) {
						for (int i = -1; i < 2; i++) {
							Lokum temp = (Lokum) arrayOfPieces[Y + i][X + 2];
							if (temp.isStrippedLokum()) {
								if (((StrippedLokum) temp).isVertical())
									removeColumn(temp);
								removeRow(temp);
							} else if (temp.isWrappedLokum())
								removeRange(temp, 1);
							else if (gameLevel.isTimeBased()) {
								if (temp.isTimerLokum())
									((TimeBaseLevel) gameLevel)
											.incrementChronometer();
							}
							Null nullLokum = new Null();
							nullLokum.setPosX(X + 2);
							nullLokum.setPosX(Y + i);
							arrayOfPieces[Y + i][X + 2] = nullLokum;
							if (i > -1) {
								Lokum otherTemp = (Lokum) arrayOfPieces[Y][X
										+ i];
								if (otherTemp.isStrippedLokum()) {
									if (((StrippedLokum) otherTemp)
											.isVertical())
										removeColumn(otherTemp);
									removeRow(otherTemp);
								} else if (otherTemp.isWrappedLokum())
									removeRange(otherTemp, 1);
								else if (gameLevel.isTimeBased()) {
									if (temp.isTimerLokum())
										((TimeBaseLevel) gameLevel)
												.incrementChronometer();
								}
								Null otherNullLokum = new Null();
								otherNullLokum.setPosX(X + i);
								otherNullLokum.setPosX(Y);
								arrayOfPieces[Y][X + i] = otherNullLokum;
							}
						}
						WrappedLokum wrLok = new WrappedLokum(type);
						wrLok.setPosX(X);
						wrLok.setPosY(Y);
						arrayOfPieces[Y][X] = wrLok;
						setScore(Score.wrappedCombinationCalculation());
						handleScoreModification();
						return;
					}
				}
			}
		}
	}

	public boolean canPiecesSwap(Piece piece1, Piece piece2) {
		// TODO Auto-generated method stub
		boolean state = false;

		int p1X = piece1.getPosX();
		int p1Y = piece1.getPosY();
		int p2X = piece2.getPosX();
		int p2Y = piece2.getPosY();
		if (Math.abs(p1X - p2X) + Math.abs(p1Y - p2Y) == 1)
			state = true;
		else if (gameLevel.isSpecialRightActive())
			state = true;
		return state;
	}

	public void swapPiece(Piece piece1, Piece piece2) {
		Piece temp, temp2;
		// printPieces();
		int p1X = piece1.getPosX();
		int p1Y = piece1.getPosY();
		int p2X = piece2.getPosX();
		int p2Y = piece2.getPosY();

		if (Math.abs(p1X - p2X) + Math.abs(p1Y - p2Y) == 1) {
			temp = arrayOfPieces[piece1.getPosY()][piece1.getPosX()];
			temp2 = arrayOfPieces[piece2.getPosY()][piece2.getPosX()];
			temp.setPosX(p2X);
			temp.setPosY(p2Y);
			temp2.setPosX(p1X);
			temp2.setPosY(p1Y);
			arrayOfPieces[p1Y][p1X] = arrayOfPieces[p2Y][p2X];
			arrayOfPieces[p2Y][p2X] = temp;
		} else if (gameLevel.isSpecialRightActive()) {
			temp = arrayOfPieces[piece1.getPosY()][piece1.getPosX()];
			temp2 = arrayOfPieces[piece2.getPosY()][piece2.getPosX()];
			temp.setPosX(p2X);
			temp.setPosY(p2Y);
			temp2.setPosX(p1X);
			temp2.setPosY(p1Y);

			arrayOfPieces[p1Y][p1X] = arrayOfPieces[p2Y][p2X];
			arrayOfPieces[p2Y][p2X] = temp;
		}
		// System.out.println("After Swap:");
		// printPieces();
	}

	public void printPieces() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.print(arrayOfPieces[j][i].getType());
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	/**
	 * 
	 * @return a Lokum object.
	 * @ensures Created lokum should be initialized.
	 */

	public BasicLokum createRandomLokum() {
		Random rand = new Random();
		int n = rand.nextInt(4) + 1;

		return new BasicLokum(n);
	}

	public TimerLokum createRandomTimerLokum() {
		Random rand = new Random();
		int n = rand.nextInt(4) + 1;

		return new TimerLokum(n);
	}

	/**
	 * 
	 * @return a Obstacle object.
	 * @ensures Created obstacle should be initialized.
	 */
	public Obstacle createRandomObstacle() {
		return new Obstacle();
	}

	public void removeBoardSequences() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				checkAndRemoveColorBombProb((Lokum) arrayOfPieces[j][i]);
				checkAndRemoveLShapeProb((Lokum) arrayOfPieces[j][i]);
				checkAndRemoveTShapeProb((Lokum) arrayOfPieces[j][i]);
				checkAndRemoveStrippedProb((Lokum) arrayOfPieces[j][i]);
				remove((Lokum) arrayOfPieces[j][i]);
				updateCrush();
			}
		}
	}

	public boolean checkBoard() {
		boolean state = false;
		outerLoop: for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (checkSequenceDown(arrayOfPieces[j][i])
						|| checkSequenceRight(arrayOfPieces[j][i])) {
					state = true;
					break outerLoop;
				}
			}
		}
		return state;
	}

	public GameWindow getWindow() {
		return window;
	}

	public static void setWindow(GameWindow window) {
		Board.window = window;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score += score;
	}

	public void initScore(int score) {
		this.score = score;
	}

	

	private class GameWindow extends JPanel implements MouseListener {
		private BufferedImage image;
		private int numClick;

		public GameWindow() {
			addMouseListener(this);
			numClick = 0;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					// Lokum temp = (Lokum) arrayOfPieces[j][i];
					try {
						image = ImageIO.read(getClass().getResourceAsStream(
								arrayOfPieces[j][i].getSourceString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					g.drawImage(image, arrayOfPieces[j][i].getPosX() * 50,
							arrayOfPieces[j][i].getPosY() * 50, 50, 50, null);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			// TODO Auto-generated method stub

			numClick++;

			if (numClick % 2 == 1) {

				firstPressed = arrayOfPieces[(int) e.getY() / 50][(int) e
						.getX() / 50];
				System.out.println("1type:" + firstPressed.getType()
						+ "---1xcoord:" + firstPressed.getPosX()
						+ "---1ycoord:" + firstPressed.getPosY() + " NUM-CLICK"
						+ numClick);

			}

			if (numClick % 2 == 0) {

				secondPressed = arrayOfPieces[(int) e.getY() / 50][(int) e
						.getX() / 50];
				System.out.println("2type:" + secondPressed.getType()
						+ "---2xcoord:" + secondPressed.getPosX()
						+ "---2ycoord:" + secondPressed.getPosY()
						+ " NUM-CLICK" + numClick);
				if (getState() == "PLAYING") {
					if (canPiecesSwap(firstPressed, secondPressed)) {
						swapPiece(firstPressed, secondPressed);
						revalidate();
						repaint();
						// if (firstPressed.getPosX() != secondPressed.getPosX()
						// && firstPressed.getPosY() != secondPressed
						// .getPosY()) {
						if (firstPressed.isColorBomb()
								|| secondPressed.isColorBomb()) {
							specialCrush.crushByColorBomb(firstPressed,
									secondPressed);
							if (gameLevel.isSpecialRightActive()) {
								gameLevel.setSpecialRightActive(false);
								gameLevel.decrementSpecialRightNumber();
								gameLevel.handleSpecialRightModification();
							}
							if (gameLevel.isMovementBased()) {
								((MovementBaseLevel) gameLevel)
										.setNumberOfMovement();
								((MovementBaseLevel) gameLevel)
										.handleMovementModification();
							}
							updateCrush();

							while (!isAnyCombinationLeft() || checkBoard()) {
								if (!isAnyCombinationLeft())
									replaceBoard();
								while (checkBoard()) {
									removeBoardSequences();
								}
							}
							setState();
							revalidate();
							repaint();
							return;
						} else if ((firstPressed.isStrippedLokum() && secondPressed
								.isWrappedLokum())
								|| (secondPressed.isStrippedLokum() && firstPressed
										.isWrappedLokum())) {
							specialCrush.crushByStrippedAndWrapped(
									firstPressed, secondPressed);
							if (gameLevel.isSpecialRightActive()) {
								gameLevel.setSpecialRightActive(false);
								gameLevel.decrementSpecialRightNumber();
								gameLevel.handleSpecialRightModification();
							}
							if (gameLevel.isMovementBased()) {
								((MovementBaseLevel) gameLevel)
										.setNumberOfMovement();
								((MovementBaseLevel) gameLevel)
										.handleMovementModification();
							}
							updateCrush();
							while (!isAnyCombinationLeft() || checkBoard()) {
								if (!isAnyCombinationLeft())
									replaceBoard();
								while (checkBoard()) {
									removeBoardSequences();
								}
							}
							setState();
							revalidate();
							repaint();
							return;
						} else if ((firstPressed.isWrappedLokum() && secondPressed
								.isWrappedLokum())) {
							specialCrush.crushByDoubleWrapped(firstPressed,
									secondPressed);
							if (gameLevel.isSpecialRightActive()) {
								gameLevel.setSpecialRightActive(false);
								gameLevel.decrementSpecialRightNumber();
								gameLevel.handleSpecialRightModification();
							}
							if (gameLevel.isMovementBased()) {
								((MovementBaseLevel) gameLevel)
										.setNumberOfMovement();
								((MovementBaseLevel) gameLevel)
										.handleMovementModification();
							}
							updateCrush();
							while (!isAnyCombinationLeft() || checkBoard()) {
								if (!isAnyCombinationLeft())
									replaceBoard();
								while (checkBoard()) {
									removeBoardSequences();
								}
							}
							setState();
							revalidate();
							repaint();
							return;
						} else if ((firstPressed.isStrippedLokum() && secondPressed
								.isStrippedLokum())) {
							specialCrush.crushByDoubleStripped(firstPressed,
									secondPressed);
							if (gameLevel.isSpecialRightActive()) {
								gameLevel.setSpecialRightActive(false);
								gameLevel.decrementSpecialRightNumber();
								gameLevel.handleSpecialRightModification();
							}
							if (gameLevel.isMovementBased()) {
								((MovementBaseLevel) gameLevel)
										.setNumberOfMovement();
								((MovementBaseLevel) gameLevel)
										.handleMovementModification();
							}
							updateCrush();
							while (!isAnyCombinationLeft() || checkBoard()) {
								if (!isAnyCombinationLeft())
									replaceBoard();
								while (checkBoard()) {
									removeBoardSequences();
								}
							}
							setState();
							revalidate();
							repaint();
							return;
						} else if (checkSequenceDown(firstPressed)
								|| checkSequenceUp(firstPressed)
								|| checkSequenceHorizontalMiddle(firstPressed)
								|| checkSequenceVerticalMiddle(firstPressed)
								|| checkSequenceLeft(firstPressed)
								|| checkSequenceRight(firstPressed)

								|| checkSequenceDown(secondPressed)
								|| checkSequenceUp(secondPressed)
								|| checkSequenceHorizontalMiddle(secondPressed)
								|| checkSequenceVerticalMiddle(secondPressed)
								|| checkSequenceLeft(secondPressed)
								|| checkSequenceRight(secondPressed)) {
							printPieces();
							Board.this.remove((Lokum) firstPressed);
							Board.this.remove((Lokum) secondPressed);

							if (gameLevel.isSpecialRightActive()) {
								gameLevel.setSpecialRightActive(false);
								gameLevel.decrementSpecialRightNumber();
								gameLevel.handleSpecialRightModification();
							}
							if (gameLevel.isMovementBased()) {
								((MovementBaseLevel) gameLevel)
										.setNumberOfMovement();
								((MovementBaseLevel) gameLevel)
										.handleMovementModification();
							}
							updateCrush();
							while (!isAnyCombinationLeft() || checkBoard()) {
								if (!isAnyCombinationLeft())
									replaceBoard();
								while (checkBoard()) {
									removeBoardSequences();
								}
							}
							printPieces();
							setState();
							revalidate();
							repaint();
							return;
						} else {
							swapPiece(firstPressed, secondPressed);
							setState();
							revalidate();
							repaint();
						}
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

}
