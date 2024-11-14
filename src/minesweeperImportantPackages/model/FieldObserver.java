package minesweeperImportantPackages.model;

@FunctionalInterface
public interface FieldObserver {

	public void eventOccurred(Field field, FieldEvent event);
}
