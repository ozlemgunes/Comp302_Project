public class SpecialCrushes {
	private Board board;
	private int width;
	private int height;

	public SpecialCrushes(Board board) {
		this.board = board;
		width = board.getWidth();
		height = board.getHeight();
	}

	public void crushByColorBomb(Piece lokum, Piece colorbomb) {

		if (colorbomb.isColorBomb() && lokum.isStrippedLokum()) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) lokum, (Lokum) colorbomb);
			board.setScore(Score.usingStrippedColorBombCalculation(n));
			board.handleScoreModification();
			return;

		} else if (colorbomb.isColorBomb() && lokum.isWrappedLokum()) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) lokum, (Lokum) colorbomb);
			board.setScore(Score.usingWrappedColorBombCalculation(n));
			board.handleScoreModification();
			return;

		} else if (colorbomb.isColorBomb() && lokum.isColorBomb()
				&& !colorbomb.equals(lokum)) {
			board.clear();
			board.setScore(Score
					.usingDoubleColorBombCalculation(height * width));
			board.handleScoreModification();
			return;

		} else if (colorbomb.isColorBomb() && (lokum.isBasicLokum() || lokum.isTimerLokum())) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) lokum, (Lokum) colorbomb);
			board.setScore(Score.usingColorBombCalculation(n));
			board.handleScoreModification();
			return;
		} else if (lokum.isColorBomb() && colorbomb.isStrippedLokum()) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) colorbomb, (Lokum) lokum);
			board.setScore(Score.usingStrippedColorBombCalculation(n));
			board.handleScoreModification();
			return;
		} else if (lokum.isColorBomb() && colorbomb.isWrappedLokum()) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) colorbomb, (Lokum) lokum);
			board.setScore(Score.usingWrappedColorBombCalculation(n));
			board.handleScoreModification();
			return;
		} else if (lokum.isColorBomb() && (colorbomb.isBasicLokum() || colorbomb.isTimerLokum())) {
			int n = 0;
			n = board.removedByUsingColorBomb((Lokum) colorbomb, (Lokum) lokum);
			board.setScore(Score.usingColorBombCalculation(n));
			board.handleScoreModification();
		}
	}

	public void crushByStrippedAndWrapped(Piece firstPressed,
			Piece secondPressed) {
		int x = secondPressed.getPosX();
		int y = secondPressed.getPosY();
		if ((firstPressed.isStrippedLokum() && secondPressed.isWrappedLokum())
				|| (firstPressed.isWrappedLokum() && secondPressed
						.isStrippedLokum())) {
			if (x == 0 && y == 0) {
				for (int i = 0; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[0][i]);
					board.removeRow((Lokum) board.getArrayOfPieces()[i][0]);
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			} else if (x > 0 && x < width - 1 && y == 0) {
				for (int i = -1; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[0][x
							+ i]);
					if (i >= 0) {
						board.removeRow((Lokum) board.getArrayOfPieces()[i][0]);
					}
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			} else if (x == width - 1 && y == 0) {
				for (int i = 0; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[0][width
							- 1 - i]);
					board.removeRow((Lokum) board.getArrayOfPieces()[i][0]);
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			} else if (x == 0 && y > 0 && y < height - 1) {
				for (int i = -1; i < 2; i++) {
					board.removeRow((Lokum) board.getArrayOfPieces()[y + i][x]);
					if (i >= 0) {
						board.removeColumn((Lokum) board.getArrayOfPieces()[y][i]);
					}
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
			} else if (x == 0 && y == height - 1) {
				for (int i = 0; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[0][i]);
					board.removeRow((Lokum) board.getArrayOfPieces()[height - i
							- 1][0]);
				}

				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
			} else if (x > 0 && x < width - 1 && y == height - 1) {
				for (int i = -1; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[0][x
							+ i]);
					if (i >= 0) {
						board.removeRow((Lokum) board.getArrayOfPieces()[height
								- 1 - i][0]);
					}
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
			} else if (x == width - 1 && y == height - 1) {
				for (int i = 0; i < 2; i++) {
					board.removeColumn((Lokum) board.getArrayOfPieces()[y][x - i]);
					board.removeRow((Lokum) board.getArrayOfPieces()[y - i][x]);
				}

				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			} else if (x == width - 1 && y > 0 && y < height - 1) {
				for (int i = -1; i < 2; i++) {
					board.removeRow((Lokum) board.getArrayOfPieces()[y + i][x]);
					if (i >= 0) {
						board.removeColumn((Lokum) board.getArrayOfPieces()[y][x
								- i]);
					}
				}
				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			} else {

				for (int i = -1; i < 2; i++) {
					board.removeRow((Lokum) board.getArrayOfPieces()[y + i][x]);
					board.removeColumn((Lokum) board.getArrayOfPieces()[y][x
							+ i]);
				}

				board.setScore(Score.usingWrappedAndStrippedCalculation(width));
				board.handleScoreModification();
				return;
			}
		}
	}

	public void crushByDoubleWrapped(Piece firstPressed, Piece secondPressed) {
		if (secondPressed.isWrappedLokum() && firstPressed.isWrappedLokum()
				&& !secondPressed.equals(firstPressed)) {
			board.removeRange((Lokum) secondPressed, 2);
			board.setScore(Score.usingDoubleWrappedCalculation());
			board.handleScoreModification();
		}
	}

	public void crushByDoubleStripped(Piece firstPressed, Piece secondPressed) {
		if (secondPressed.isStrippedLokum() && firstPressed.isStrippedLokum()
				&& !secondPressed.equals(firstPressed)) {
			board.removeColumn((Lokum) secondPressed);
			board.removeRow((Lokum) secondPressed);
			board.setScore(Score.usingDoubleStrippedCalculation(width));
			board.handleScoreModification();
		}
	}
}
