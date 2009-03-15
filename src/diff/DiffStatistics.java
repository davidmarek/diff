package diff;

import java.util.List;

/**
 *
 * @author david
 */
public class DiffStatistics {

	protected int addedLines;
	protected int removedLines;
	protected int changedLines;
	protected int length;

	public DiffStatistics(List<SequenceElement<String>> d) {
		addedLines = removedLines = 0;
		length = d.size();
		computeStatistics(d);
	}

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

	public int getAddedLines() {
		return addedLines;
	}

	public int getRemovedLines() {
		return removedLines;
	}

	public int getChangedLines() {
		return changedLines;
	}
}
