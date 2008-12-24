package diff;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateDiff {

    protected LinkedList<SequenceElement<String>> beginOffset;
    protected LinkedList<SequenceElement<String>> endOffset;

    protected LinkedList<String> firstFile;
    protected LinkedList<String> secondFile;

    protected LinkedList<SequenceElement<String>> diff;

    /**
     *
     * @param fstList
     * @param sndList
     */
    public CreateDiff(List<String> fstList, List<String> sndList) {
        firstFile = new LinkedList<String>(fstList);
        secondFile = new LinkedList<String>(sndList);

        beginOffset = new LinkedList<SequenceElement<String>>();
        endOffset = new LinkedList<SequenceElement<String>>();

        stripMargin();
        diffFiles();
        
    }

    /**
     *
     */
    protected void stripMargin() {
        while (!firstFile.isEmpty() && !secondFile.isEmpty() && firstFile.getFirst().equals(secondFile.getFirst())) {
            beginOffset.addLast(new SequenceElement<String>(firstFile.pollFirst(), SequenceElement.Status.UNTOUCHED));
            secondFile.pollFirst();
        }

        while (!firstFile.isEmpty() && !secondFile.isEmpty() && firstFile.getLast().equals(secondFile.getLast())) {
            endOffset.addFirst(new SequenceElement<String>(firstFile.pollLast(), SequenceElement.Status.UNTOUCHED));
            secondFile.pollLast();

        }
    }

    /**
     *
     */
    protected void diffFiles() {
        LongestCommonSubsequence<String> lcs = new LongestCommonSubsequence<String>(firstFile, secondFile);
        diff = new LinkedList<SequenceElement<String>>(beginOffset);
        diff.addAll(lcs.findDiff());
        diff.addAll(endOffset);
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return diff.toString()+"\n";
    }
}
