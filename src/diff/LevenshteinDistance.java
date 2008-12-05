package diff;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class LevenshteinDistance<T> {

    private ArrayList<T> firstList, secondList;

    /**
     *
     * @param firstList
     * @param secondList
     */
    public LevenshteinDistance(List<T> firstList, List<T> secondList) {
        this.firstList = new ArrayList<T>(firstList);
        this.secondList = new ArrayList<T>(secondList);
    }

    /**
     *
     * @return
     */
    public List<SequenceElement<T>> findDiff() {

    }
}
