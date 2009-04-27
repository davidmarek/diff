package diff;

import java.util.List;

/**
 * Statistiky pro diff soubor
 * @author David Marek <david at davidmarek.cz>
 */
public class DiffStatistics {

	/** Pocet pridanych radku. */
	protected int addedLines;

	/** Pocet odebranych radku. */
	protected int removedLines;

	/**
	 * Konstruktor
	 * @param d Diff, jehoz statistiky maji byt zpocitany.
	 */
	public DiffStatistics(List<SequenceElement<String>> d) {
		addedLines = removedLines = 0;
		computeStatistics(d);
	}

	/**
	 * Zpocita statistiky pro diff.
	 * @param d Diff, jehoz statistiky maji byt zpocitany.
	 */
	protected void computeStatistics(List<SequenceElement<String>> d) {
		for (SequenceElement<String> s : d) {
			switch (s.getStatus()) {
				case REMOVED:
					removedLines++;
					 break;
				case ADDED:
					addedLines++;
					break;
				case UNTOUCHED:
					break;
			}
		}
	}

	/**
	 * @return Pocet pridanych radku.
	 */
	public int getAddedLines() {
		return addedLines;
	}

	/**
	 * @return Pocet odebranych radku.
	 */
	public int getRemovedLines() {
		return removedLines;
	}

}
