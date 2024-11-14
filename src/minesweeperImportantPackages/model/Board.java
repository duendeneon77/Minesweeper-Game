package minesweeperImportantPackages.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver {

	private final int rows;
	private final int columns;
	private final int mines;

	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<ResultEvent>> observers = new ArrayList<>();

	private boolean gameOver = false; 

	public Board(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;

		generateFields();
		associateNeighbors();
		randomizeMines();
	}

	public void forEachField(Consumer<Field> function) {
		fields.forEach(function);
	}

	public void registerObserver(Consumer<ResultEvent> observer) {
		observers.add(observer);
	}

	private void notifyObservers(boolean result) {
		observers.stream().forEach(o -> o.accept(new ResultEvent(result)));
	}

	public void open(int row, int column) {
		if (gameOver)
			return;

		fields.parallelStream().filter(f -> f.getRow() == row && f.getColumn() == column).findFirst()
				.ifPresent(f -> f.open());
	}

	public void toggleMark(int row, int column) {
		if (gameOver)
			return;

		fields.parallelStream().filter(f -> f.getRow() == row && f.getColumn() == column).findFirst()
				.ifPresent(f -> f.toggleMark());
	}

	private void generateFields() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Field field = new Field(row, column);
				field.registerObserver(this);
				fields.add(field);
			}
		}
	}

	private void associateNeighbors() {
		for (Field f1 : fields) {
			for (Field f2 : fields) {
				f1.addNeighbor(f2);
			}
		}
	}

	private void randomizeMines() {
		long minesCount = 0;
		Predicate<Field> mined = f -> f.isMined();

		do {
			int random = (int) (Math.random() * fields.size());
			fields.get(random).mine();
			minesCount = fields.stream().filter(mined).count();
		} while (minesCount < mines);
	}

	public boolean goalAchieved() {
		return fields.stream().allMatch(f -> f.goalAchieved());
	}

	public void restart() {
		if (!gameOver)
			return;

		fields.stream().forEach(f -> f.restart());
		randomizeMines();
		gameOver = false; 
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	@Override
	public void eventOccurred(Field field, FieldEvent event) {
	    if (gameOver) {
	        return;
	    }

	    if (event == FieldEvent.EXPLODE) {
	        revealMines();
	        notifyObservers(false); 
	        gameOver = true; 
	        return;
	    }

	    if (goalAchieved() && !gameOver) {
	        notifyObservers(true); 
	        gameOver = true; 
	    }
	}
	private void revealMines() {
		fields.stream().filter(f -> f.isMined()).filter(f -> !f.isMarked()).forEach(f -> f.setOpened(true));
	}
}
