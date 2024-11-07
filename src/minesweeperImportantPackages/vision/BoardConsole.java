package minesweeperImportantPackages.vision;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import minesweeperImportantPackages.exception.ExitException;
import minesweeperImportantPackages.exception.ExplosionException;
import minesweeperImportantPackages.model.Board;

public class BoardConsole {

	private Board board;
	private Scanner sc = new Scanner(System.in);

	public BoardConsole(Board board) {
		this.board = board;

		toExecuteGame();

	}

	private void toExecuteGame() {
		try {

			boolean toContinue = true;
			while (toContinue) {

				circleOfTheGame();

				System.out.println("Do you want to play again? (Y/n) ");
				String answer = sc.nextLine();
				if ("n".equalsIgnoreCase(answer)) {
					toContinue = false;
				} else {
					board.reStart();
				}
			}

		} catch (ExitException e) {
			System.out.println("Bye! :)");
		} finally {
			sc.close();
		}
	}

	private void circleOfTheGame() {

		try {
			while (!board.goalAchieved()) {
				System.out.println(board);
				String typed = captureTypedValue("Type (x , y): ");

				Iterator<Integer> xy = Arrays.stream(typed.split(",")).map(e -> Integer.parseInt(e.trim())).iterator();

				typed = captureTypedValue(" Type 1 to OPEN or 2 to UNMARK");
				if ("1".equals(typed)) {
					board.opening(xy.next(), xy.next());
				} else if ("2".equals(typed)) {
					board.changingMark(xy.next(), xy.next());
				}

			}
			System.out.println(board);
			System.out.println(" You are the Winner! ");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println(" You lost!");

		}
	}

	private String captureTypedValue(String text) {
		System.out.print(text);
		String typed = sc.nextLine();
		if ("leave".equalsIgnoreCase(typed)) {
			throw new ExitException();
		}
		return typed;
	}

}
