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

    public CreateNormalDiff(List<String> fstList, List<String> sndList) {
        super(fstList, sndList);
        createStringRepresentation();
    }

    /**
     * TODO: Pokud je vlozen/odebran/zmenen jen jeden radek, tak staci mit jen jedno cislo
     */
    protected void addChanges() {
        String levaStrana, pravaStrana;
        /**
         * Pridane radky
         */
        if (!added.isEmpty() && removed.isEmpty()) {

            levaStrana = Integer.toString(fstCounter);
            if (added.size() > 1) {
                pravaStrana = Integer.toString(sndCounter+1-added.size()) + "," + Integer.toString(sndCounter);
            } else {
                pravaStrana = Integer.toString(sndCounter);
            }

            stringRepresentation += levaStrana + "a" + pravaStrana + "\n";
            for (String s : added) {
                stringRepresentation += "> " + s + "\n";
            }

        /**
         * Odebrane radky
         */
        } else if (added.isEmpty() && !removed.isEmpty()) {

            if (removed.size() > 1) {
                levaStrana = Integer.toString(fstCounter+1-removed.size()) + "," + Integer.toString(fstCounter);
            } else {
                levaStrana = Integer.toString(fstCounter);
            }
            pravaStrana = Integer.toString(sndCounter);

            stringRepresentation += levaStrana + "d" + pravaStrana + "\n";
            for (String s : removed) {
                stringRepresentation += "< " + s + "\n";
            }
        
        /**
         * Zmenene radky
         */
        } else if (!added.isEmpty() && !removed.isEmpty()) {

            if (removed.size() > 1) {
                levaStrana = Integer.toString(fstCounter+1-removed.size()) + "," + Integer.toString(fstCounter);
            } else {
                levaStrana = Integer.toString(fstCounter);
            }

            if (added.size() > 1) {
                pravaStrana = Integer.toString(sndCounter+1-added.size()) + "," + Integer.toString(sndCounter);
            } else {
                pravaStrana = Integer.toString(sndCounter);
            }
            
            stringRepresentation += levaStrana + "c" + pravaStrana + "\n";
            for (String s : removed) {
                stringRepresentation += "< " + s + "\n";
            }
            stringRepresentation += "---\n";
            for (String s : added) {
                stringRepresentation += "> " + s + "\n";
            }
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
            if (se.status == SequenceElement.Status.UNTOUCHED) {
                addChanges();
                fstCounter++;
                sndCounter++;
            } else if (se.status == SequenceElement.Status.ADDED) {
                added.addLast(se.getElement());
                sndCounter++;
            } else if (se.status == SequenceElement.Status.REMOVED) {
                removed.addLast(se.getElement());
                fstCounter++;
            }
        }
        addChanges();
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
