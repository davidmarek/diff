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
    }

    /**
     *
     * @return
     */
    public List<SequenceElement<T>> findDiff() {
        int[][] table = computeTable();
    }
}
