package minesweeperImportantPackages;

import minesweeperImportantPackages.model.Board;
import minesweeperImportantPackages.vision.BoardConsole;

public class applicationProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Board board = new Board(6,6,3);
		
		new BoardConsole(board);
		

	}

}
