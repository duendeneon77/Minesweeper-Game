package minesweeperImportantPackages.model;

import java.util.ArrayList;
import java.util.List;

import minesweeperImportantPackages.exception.ExplosionException;

public class Camp {

	private final int liNe;
	private final int coluMn;

	private boolean open = false;
	private boolean mined = false;
	private boolean marked = false;

	private List<Camp> neighborhood = new ArrayList<>();

	Camp(int liNe, int coluMn) {
		this.liNe = liNe;
		this.coluMn = coluMn;
	}

	boolean addNeighbors(Camp neighbor) {
		boolean differentLine = liNe != neighbor.liNe;
		boolean differentColumn = coluMn != neighbor.coluMn;
		boolean diagonal = differentLine && differentColumn;

		int lineDelta = Math.abs(liNe - neighbor.liNe);
		int columnDelta = Math.abs(coluMn - neighbor.coluMn);
		int geralDelta = columnDelta + lineDelta;

		if (geralDelta == 1 && !diagonal) {
			neighborhood.add(neighbor);
			return true;
		} else if (geralDelta == 2 && diagonal) {
			neighborhood.add(neighbor);
			return true;
		} else {
			return false;
		}
	}

	void toggleMarking() {
		if (!open) {
			marked = !marked;
		}
	}

	boolean toOpen() {

		if (!open && !marked) {
			open = true;

			if (mined) {
				throw new ExplosionException();
			}
			if (secureNeigborhood()) {
				neighborhood.forEach(neigh -> neigh.toOpen());
			}
			return true;
		} else {
			return false;
		}
	}

	boolean secureNeigborhood() {
		return neighborhood.stream().noneMatch(neigh -> neigh.mined);
	}

	void toMine() {
		if (!mined) {
			mined = true;
		}
	}

	public boolean isMined() {
		return marked;
	}

	public boolean isMarked() {
		return marked;

	}

	void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isNotOpen() {
		return !isOpen();
	}

	public int getLiNe() {
		return liNe;
	}

	public int getColuMn() {
		return coluMn;
	}

	boolean goalAchieved() {
		boolean revealed = !mined && open;
		boolean isProtected = mined && marked;
		return revealed || isProtected;
	}

	long minesInTheNeighborhood() {
		return neighborhood.stream().filter(neigh -> neigh.mined).count();

	}

	void restart() {
		open = false;
		mined = false;
		marked = false;
	}

	public String toString() {
		if (marked) {
			return "x";
		} else if (open) {
			if (mined) {
				return "*";
			} else if (minesInTheNeighborhood() > 0) {
				return Long.toString(minesInTheNeighborhood());
			} else {
				return " ";
			}
		} else {
			return "?";
		}
	}

}
