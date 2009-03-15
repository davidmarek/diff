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

	/** Pocet zmenenych radku. */
	protected int changedLines;

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
		int removed = 0;
		int added = 0;
		for (SequenceElement<String> s : d) {
			switch (s.getStatus()) {
				case REMOVED:
					removedLines++;
					removed++;
					 break;
				case ADDED:
					addedLines++;
					added++;
					break;
				case UNTOUCHED:
					if (removed > 0 && added > 0) {
						int changed = Math.min(added, removed);
						changedLines += changed;
						removedLines -= changed;
						addedLines -= changed;
					}
					removed = added = 0;
					break;
			}
		}
		if (removed > 0 && added > 0) {
			int changed = Math.min(added, removed);
			changedLines += changed;
			removedLines -= changed;
			addedLines -= changed;
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

	/**
	 * @return Pocet zmenenych radku.
	 */
	public int getChangedLines() {
		return changedLines;
	}
}
