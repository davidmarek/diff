package diff;

import java.util.ArrayList;
import java.util.LinkedList;
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

    protected int[][] computeTable() {
        int[][] table = new int[firstList.size()+1][secondList.size()+1];

        for (int i = 0; i < firstList.size()+1; i++) {
            table[i][0] = i;
        }
        for (int i = 0; i < secondList.size()+1; i++) {
            table[0][i] = i;
        }

        int cost;
        for (int i = 0; i < firstList.size(); i++) {
            for (int j = 0; j < secondList.size(); j++) {

                if (firstList.get(i).equals(secondList.get(j))) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                table[i+1][j+1] = Math.min(Math.min(table[i][j]+cost, table[i+1][j]+1), table[i][j+1]+1);
            }
        }

        return table;
    }

    /**
     *
     * @return
     */
    public List<SequenceElement<T>> findDiff() {
        int[][] table = computeTable();
        LinkedList<SequenceElement<T>> ret = new LinkedList<SequenceElement<T>>();

        int i = table.length-1;
        int j = table[0].length-1;
        while (i > 0 && j > 0) {
            if (firstList.get(i-1).equals(secondList.get(j-1))) {
                ret.addFirst(new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.UNTOUCHED));
                i--;
                j--;
            } else if (table[i-1][j] < table[i][j-1]) {
                ret.addFirst(new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.REMOVED));
                i--;
            } else {
                ret.addFirst(new SequenceElement<T>(secondList.get(j-1), SequenceElement.Status.ADDED));
                j--;
            }
        }

        while (i > 0) {
            SequenceElement<T> newElement = new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.REMOVED);
            ret.addFirst(newElement);
            i--;
        }
        while (j > 0) {
             SequenceElement<T> newElement = new SequenceElement<T>(secondList.get(j-1), SequenceElement.Status.ADDED);
             ret.addFirst(newElement);
             j--;
        }

        return ret;
    }
}
