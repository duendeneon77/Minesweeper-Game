package minesweeperImportantPackages.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import minesweeperImportantPackages.exception.ExplosionException;

public class Board {

	private int liNes;
	private int coluMns;
	private int mines;

	private final List<Camp> camps = new ArrayList<>();

	public Board(int liNes, int coluMns, int mines) {
		this.liNes = liNes;
		this.coluMns = coluMns;
		this.mines = mines;
		generateCamps();
		associatingNeighbors();
		toRandomlyPlaceMines();
	}

	public void opening(int liNe, int coluMn) {
		try {
			camps.parallelStream().filter(caMp -> caMp.getLiNe() == liNe && caMp.getColuMn() == coluMn).findFirst()
					.ifPresent(caMp -> caMp.toOpen());
		} catch (ExplosionException e) {
			camps.forEach(c -> c.setOpen(true));
			throw e;
		}
	}

	public void changingMark(int liNe, int coluMn) {
		camps.parallelStream().filter(caMp -> caMp.getLiNe() == liNe && caMp.getColuMn() == coluMn).findFirst()
				.ifPresent(caMp -> caMp.toggleMarking());

	}

	private void generateCamps() {
		for (int line = 0; line < liNes; line++) {
			for (int column = 0; column < coluMns; column++) {
				camps.add(new Camp(line, column));
			}
		}
	}

	private void associatingNeighbors() {
		for (Camp c1 : camps) {
			for (Camp c2 : camps) {
				if (c1 != c2 && c1.addNeighbors(c2))
					;
			}
		}

	}

	private void toRandomlyPlaceMines() {
		long armedMines = 0;
		Predicate<Camp> mined = cAmp -> cAmp.isMined();

		while (armedMines < mines) {
			int raMdom = (int) (Math.random() * camps.size());
			Camp selectedCamp = camps.get(raMdom);

			if (!selectedCamp.isMined()) {
				selectedCamp.toMine();
				armedMines++;
			}
		}
	}

	public boolean goalAchieved() {
		return camps.stream().allMatch(allGoal -> allGoal.goalAchieved());
	}

	public void reStart() {
		camps.stream().forEach(init -> init.restart());
		toRandomlyPlaceMines();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		for (int c = 0; c < coluMns; c++) {
			sb.append(" ");
			sb.append(c);
			sb.append(" ");
		}
		sb.append("\n");

		int i = 0;
		for (int l = 0; l < liNes; l++) {
			sb.append(l);
			sb.append(" ");
			for (int c = 0; c < coluMns; c++) {
				sb.append(" ");
				sb.append(camps.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}

		return sb.toString();
	}

}
