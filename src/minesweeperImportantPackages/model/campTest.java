package minesweeperImportantPackages.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import minesweeperImportantPackages.exception.ExplosionException;

public class campTest {

	private Camp camp = new Camp(3, 3);

	@BeforeEach
	void startingCamp() {
		camp = new Camp(3, 3);
	}

	@Test
	void neighborTestDistance1Left() {

		Camp neighbor = new Camp(3, 2);
		boolean result = camp.addNeighbors(neighbor);
		assertTrue(result);

	}

	@Test
	void neighborTestDistance1Right() {

		Camp neighbor = new Camp(3, 4);
		boolean result = camp.addNeighbors(neighbor);
		assertTrue(result);

	}

	@Test
	void neighborTestDistance1Above() {

		Camp neighbor = new Camp(2, 3);
		boolean result = camp.addNeighbors(neighbor);
		assertTrue(result);

	}

	@Test
	void neighborTestDistance1Under() {

		Camp neighbor = new Camp(4, 3);
		boolean result = camp.addNeighbors(neighbor);
		assertTrue(result);

	}

	@Test
	void neighborTestDistance1Diagonal() {

		Camp neighbor = new Camp(2, 2);
		boolean result = camp.addNeighbors(neighbor);
		assertTrue(result);
	}

	@Test
	void noNeighborTest() {

		Camp neighbor = new Camp(1, 1);
		boolean result = camp.addNeighbors(neighbor);
		assertFalse(result);
	}

	@Test
	void DefaultValueMarkedAtributeTest() {

		assertFalse(camp.isMarked());
	}

	@Test
	void toggleMarkingTest() {
		camp.toggleMarking();
		assertTrue(camp.isMarked());
	}

	@Test
	void toggleMarkingTestTwoCalls() {
		camp.toggleMarking();
		camp.toggleMarking();
		assertFalse(camp.isMarked());
	}

	@Test
	void openNoMinedAndNoMarkedTest() {
		assertTrue(camp.toOpen());
	}

	@Test
	void openNoMineddButMarkedTest() {
		camp.toggleMarking();
		assertFalse(camp.toOpen());
	}

	@Test
	void openMinedAndMinedTest() {
		camp.toggleMarking();
		camp.toMine();
		assertFalse(camp.toOpen());
	}

	@Test
	void openMinedButNoMarkedTest() {
		camp.toMine();

		assertThrows(ExplosionException.class, () -> {
			camp.toOpen();
		});

	}

	@Test
	void openWithNeighborsTest() {

		Camp camp11 = new Camp(1, 1);

		Camp camp22 = new Camp(2, 2);
		camp22.addNeighbors(camp11);

		camp.addNeighbors(camp22);

		camp.toOpen();

		assertTrue(camp22.isOpen() && camp11.isOpen());
	}

	@Test
	void openWithNeighborsTest2() {

		Camp camp11 = new Camp(1, 1);
		Camp camp12 = new Camp(1, 2);
		camp12.toMine();

		Camp camp22 = new Camp(2, 2);
		camp22.addNeighbors(camp11);
		camp22.addNeighbors(camp12);

		camp.addNeighbors(camp22);

		camp.toOpen();

		assertTrue(camp22.isOpen() && camp11.isNotOpen());
	}
}
