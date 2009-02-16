package diff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateNormalDiff extends CreateDiff {

    protected String stringRepresentation;

    protected int fstCounter;
    protected int sndCounter;

    protected LinkedList<String> removed;
    protected LinkedList<String> added;

    /**
     *
     * @param fstList
     * @param sndList
     */
    public CreateNormalDiff(File fst, File snd) throws FileNotFoundException, IOException {
        super(fst, snd);
        createStringRepresentation();
    }

    /**
     * 
     */
    protected void addChanges() {
        String leftSide, rightSide;

        if (removed.size() > 1) {
            leftSide = Integer.toString(fstCounter+1-removed.size()) + "," + Integer.toString(fstCounter);
        } else {
            leftSide = Integer.toString(fstCounter);
        }

        if (added.size() > 1) {
            rightSide = Integer.toString(sndCounter+1-added.size()) + "," + Integer.toString(sndCounter);
        } else {
            rightSide = Integer.toString(sndCounter);
        }

        String symbol;
        if (!added.isEmpty() && removed.isEmpty()) {
            symbol = "a";
        } else if (added.isEmpty() && !removed.isEmpty()) {
            symbol = "d";
        } else {
            symbol = "c";
        }

        stringRepresentation += leftSide + symbol + rightSide + "\n";
        for (String s : removed) {
            stringRepresentation += "< " + s + "\n";
        }
        if (symbol.equals("c")) { stringRepresentation += "---\n"; }
        for (String s : added) {
            stringRepresentation += "> " + s + "\n";
        }

        added.clear();
        removed.clear();
    }
    
    protected void createStringRepresentation() {
        stringRepresentation = new String();        
        fstCounter = 0;
        sndCounter = 0;
        removed = new LinkedList<String>();
        added = new LinkedList<String>();

        Iterator<SequenceElement<String>> it = diff.iterator();
        while (it.hasNext()) {
            SequenceElement<String> se = it.next();
            switch(se.getStatus()) {
                case UNTOUCHED:
                    if (!added.isEmpty() || !removed.isEmpty()) {
                        addChanges();
                    }
                    fstCounter++;
                    sndCounter++;
                    break;

                case ADDED:
                    added.addLast(se.getElement());
                    sndCounter++;
                    break;

                case REMOVED:
                    removed.addLast(se.getElement());
                    fstCounter++;
                    break;
            }
        }
        
        if (!added.isEmpty() || !removed.isEmpty()) {
            addChanges();
        }
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
