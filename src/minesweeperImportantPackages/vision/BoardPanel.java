package minesweeperImportantPackages.vision;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import minesweeperImportantPackages.model.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	public BoardPanel(Board board) {
		setLayout(new GridLayout(board.getRows(), board.getColumns()));
		board.forEachField(f -> add(new FieldButton(f))); 
		board.registerObserver(e -> {
			SwingUtilities.invokeLater(() -> {
				if (e.isWon()) {
					JOptionPane.showMessageDialog(this, "You Won :)");
				} else {
					JOptionPane.showMessageDialog(this, "You Lost :(");
				}
				board.restart();
			});
		});
	}

}
