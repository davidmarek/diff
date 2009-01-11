package diff;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateNormalDiff extends CreateDiff {

    String stringRepresentation;

    int fstCounter;
    int sndCounter;

    LinkedList<String> removed;
    LinkedList<String> added;

    /**
     *
     * @param fstList
     * @param sndList
     */
    public CreateNormalDiff(List<String> fstList, List<String> sndList) {
        super(fstList, sndList);
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
            if (se.status.equals(SequenceElement.Status.UNTOUCHED)) {
                if (!added.isEmpty() || !removed.isEmpty()) {
                    addChanges();
                }
                fstCounter++;
                sndCounter++;
            } else if (se.status.equals(SequenceElement.Status.ADDED)) {
                added.addLast(se.getElement());
                sndCounter++;
            } else if (se.status.equals(SequenceElement.Status.REMOVED)) {
                removed.addLast(se.getElement());
                fstCounter++;
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
