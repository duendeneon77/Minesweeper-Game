package minesweeperImportantPackages.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

	private final int row;
	private final int column;

	private boolean opened = false;
	private boolean mined = false;
	private boolean marked = false;

	private List<Field> neighbors = new ArrayList<>();
	private List<FieldObserver> observers = new ArrayList<>();

	Field(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void registerObserver(FieldObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(FieldEvent event) {
		observers.stream().forEach(o -> o.eventOccurred(this, event));
	}

	boolean addNeighbor(Field neighbor) {
		boolean rowDifferent = row != neighbor.row;
		boolean columnDifferent = column != neighbor.column;
		boolean diagonal = rowDifferent && columnDifferent;

		int rowDelta = Math.abs(row - neighbor.row);
		int colDelta = Math.abs(column - neighbor.column);
		int generalDelta = rowDelta + colDelta;

		if (generalDelta == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;
		} else if (generalDelta == 2 && diagonal) {
			neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}

	public void toggleMark() {
		if (!opened) {
			marked = !marked;

			if (marked) {
				notifyObservers(FieldEvent.MARK);
			} else {
				notifyObservers(FieldEvent.UNMARK);
			}
		}
	}

	public boolean open() {
		if (!opened && !marked) {
			if (mined) {
				notifyObservers(FieldEvent.EXPLODE);
				return true;
			}

			setOpened(true);

			if (safeNeighborhood()) {
				neighbors.forEach(n -> n.open());
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean safeNeighborhood() {
		return neighbors.stream().noneMatch(n -> n.mined);
	}

	void mine() {
		mined = true;
	}

	public boolean isMined() {
		return mined;
	}

	public boolean isMarked() {
		return marked;
	}

	void setOpened(boolean opened) {
		this.opened = opened;

		if (opened) {
			notifyObservers(FieldEvent.OPEN);
		}
	}

	public boolean isOpened() {
		return opened;
	}

	public boolean isClosed() {
		return !isOpened();
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	boolean goalAchieved() {
		boolean uncovered = !mined && opened;
		boolean protectedField = mined && marked;
		return uncovered || protectedField;
	}

	public int minesInNeighborhood() {
		return (int) neighbors.stream().filter(n -> n.mined).count();
	}

	void restart() {
		opened = false;
		mined = false;
		marked = false;
		notifyObservers(FieldEvent.RESTART);
	}
}
