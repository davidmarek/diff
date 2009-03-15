package diff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @brief Normalni verze diffu
 *
 * Vytvori diff soubor, ktery je podobny verzi vytvorene pomoci diff --normal.
 * Vystup neni shodny 1:1, vzhledem k tomu, ze pouzity algoritmus obcas vybere
 * jinak, pokud je vic moznosti jak diff zapsat. Je vsak zaruceno, ze pokud se
 * pouzije vystup tohoto algoritmu jako patch, pak vysledek bude spravny.
 *
 * Toto je prvni naivni verze jak sestavit diff soubor. Lepsi reseni je pouzito
 * u CreateUnifiedDiff.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateNormalDiff extends CreateDiff {

    /** Retezec, ktery se vypise. */
    protected String stringRepresentation;

    /** Pocitadlo radku u prvniho souboru. */
    protected int fstCounter;
    /** Pocitadlo radku u druheho souboru. */
    protected int sndCounter;

    /** Odebrane radky */
    protected LinkedList<String> removed;
    /** Pridane radky */
    protected LinkedList<String> added;

    /**
     * @brief Konstruktor vytvarejici diff.
     *
     * Konstruktor uz rovnou zpocita diff. K tomu pouzije predka CreateDiff, pouze
     * formatuje jeho vystup.
     *
     * @param fst Prvni ze souboru, ktere se maji porovnavat.
     * @param snd Druhy ze souboru, ktere se maji porovnavat.
     */
    public CreateNormalDiff(File fst, File snd) throws FileNotFoundException, IOException {
        super(fst, snd);
        createStringRepresentation();
    }

	public CreateNormalDiff(List<SequenceElement<String>> diff) {
		super(diff);
		createStringRepresentation();
	}

    /**
     * @brief Pridani zmen do diffu
     *
     * Zmeny jsou v diff souboru po kusech. Metoda prida vzdy celistvy kus zmen
     * do vystupniho retezce. Stara se take o pridani dalsich informaci do vystupu
     * jako jsou napriklad znacky nebo cisla radku.
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

    /**
     * @brief Vytvoreni vystupniho souboru ze seznamu zmen.
     *
     * Premeni seznam zmen zpocitany predkem CreateDiff ve vystupni soubor.
     */
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

    /**
     * @brief Diff
     *
     * Vraci vysledny diff
     *
     * @return Vysledny diff soubor
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }
}
