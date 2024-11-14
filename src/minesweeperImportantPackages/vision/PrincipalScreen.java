package minesweeperImportantPackages.vision;

import javax.swing.JFrame;

import minesweeperImportantPackages.model.Board;


@SuppressWarnings("serial")
public class PrincipalScreen extends JFrame{
	public PrincipalScreen() {
	    Board board = new Board(16, 30, 50);
	    BoardPanel boardPanel = new BoardPanel(board); 
	    add(boardPanel);  

	    setTitle("Minesweeper");
	    setSize(650, 450);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    setVisible(true);
	}

	
	public static void main (String[] args) {
		
		new PrincipalScreen();
	}

}
