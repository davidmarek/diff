package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** 
 * @brief Zakladni trida CreateDiff, ktera porovnava dva textove soubory.
 *
 * Tato trida se nehodi pro vytvareni opravdovych diffu, neobsahuje zadnou logiku
 * pro jejich vypisovani, slouzi jen k ziskani datove struktury obsahujici diff.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateDiff {

    /** Pocatecni radky, ktere jsou spolecne pro oba soubory. */
    protected LinkedList<SequenceElement<String>> beginOffset;
    /** Koncove radky, ktere jsou spolecne pro oba soubory. */
    protected LinkedList<SequenceElement<String>> endOffset;

    /** Seznam radku prvniho souboru. */
    protected LinkedList<String> firstFile;
    /** Seznam radku druheho souboru. */
    protected LinkedList<String> secondFile;

    /** Vysledny diff. */
    protected ArrayList<SequenceElement<String>> diff;

    /**
     * @brief Konstruktor vytvarejici diff.
     * 
     * Objekt uz pri svem vytvoreni zpocita diff zadanych souboru.
     *
     * @param fst Prvni ze souboru, ktere se maji porovnavat.
     * @param snd Druhy ze souboru, ktere se maji porovnavat.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public CreateDiff(File fst, File snd) throws FileNotFoundException, IOException {

        firstFile = loadLines(fst);
        secondFile = loadLines(snd);

        beginOffset = new LinkedList<SequenceElement<String>>();
        endOffset = new LinkedList<SequenceElement<String>>();

        stripMargin();
        diffFiles();
        
    }

	/**
	 * @brief Konstruktor pro inicializaci jiz hotovym diffem
	 * 
	 * Pouzije uz hotovy diff. Diky nemu mohou byt odvozene tridy pouzity jen k
	 * naformatovani vystupu.
	 * 
	 * @param d Diff.
	 */
	public CreateDiff(List<SequenceElement<String>> d) {
		diff = new ArrayList<SequenceElement<String>>(d);
	}

    /**
     * @brief Nacteni radku souboru.
     *
     * Nacte soubor radek po radku.
     *
     * @param f Soubor, ktery se ma nacist.
     * @return Seznam radku souboru.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
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
     * @brief Odstraneni spolecnych casti.
     *
     * Pro zrychleni algoritmu se odrizne spolecny zacatek a konec obou souboru
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
     * @brief Zpocitani diffu.
     *
     * Vytvori instanci tridy LongestCommonSubsequence a zpocita diff, nakonec
     * prida i odrizle casti ze zacatku a konce.
     */
    protected void diffFiles() {
        LongestCommonSubsequence<String> lcs = new LongestCommonSubsequence<String>(firstFile, secondFile);
        diff = new ArrayList<SequenceElement<String>>(beginOffset);
        diff.addAll(lcs.findDiff());
        diff.addAll(endOffset);
    }

    /**
     * @brief Vrati diff jako strukturu SequenceElementu
     * @return diff
     */
    public ArrayList<SequenceElement<String>> getDiff() {
        return diff;
    }

    /**
     * @brief Diff
     *
     * Vraci vysledny diff soubor, v tomto pripade pouze vypise seznam obsahujici
     * reprezentaci zmen.
     *
     * @return diff
     */
    @Override
    public String toString() {
        return diff.toString()+"\n";
    }
}
