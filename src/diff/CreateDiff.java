package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

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
    public CreateDiff(File fst, File snd) throws FileNotFoundException, IOException {

        firstFile = loadLines(fst);
        secondFile = loadLines(snd);

        beginOffset = new LinkedList<SequenceElement<String>>();
        endOffset = new LinkedList<SequenceElement<String>>();

        stripMargin();
        diffFiles();
        
    }

    protected LinkedList<String> loadLines(File f) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        LinkedList<String> lines = new LinkedList<String>();
        String l;
        while ((l = reader.readLine()) != null) {
            lines.addLast(l);
        }
        return lines;
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
