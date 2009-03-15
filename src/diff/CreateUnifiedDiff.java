package diff;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @brief Unified diff
 *
 * Vytvori diff soubor, ktery je podobny verzi vytvorene pomoci diff -u.
 * Vystup neni shodny 1:1, vzhledem k tomu, ze pouzity algoritmus obcas vybere
 * jinak, pokud je vic moznosti jak diff zapsat. Je vsak zaruceno, ze pokud se
 * pouzije vystup tohoto algoritmu jako patch, pak vysledek bude spravny.
 *
 * Je možné nastavit kolik kontextových řádků má být zahrnuto. Viz volba
 * diff -U pocet. Standardne je stejne jako u diffu pouzito tri radku.
 *
 * Tato implementace nedela zadne presouvani ani kopirovani retezcu, takze 
 * by mela byt rychlejsi nez CreateNormalDiff. Cely seznam zmen projde jen jednou
 * a pocita pritom indexy hranic zmen.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateUnifiedDiff extends CreateDiff {

    /** Retezec, ktery se vypise. */
    protected String stringRepresentation;

    /** Pocitadlo radku u prvniho souboru. */
    protected int fstCounter;
    /** Pocitadlo radku u druheho souboru. */
    protected int sndCounter;

    /** Pocet kontextovych radku, ktere se maji vypsat. */
    protected int context;
    /** Index zacatku zmen v reprezentaci zmen. */
    protected int startDiff;
    /** Index konce zmen v reprezentaci zmen. */
    protected int endDiff;
    /** Index zacatku zmen v prvnim souboru. */
    protected int startFst;
    /** Index konce zmen v prvnim souboru. */
    protected int endFst;
    /** Index zacatku zmen v druhem souboru. */
    protected int startSnd;
    /** Index konce zmen v druhem souboru. */
    protected int endSnd;

    /**
     * @brief Konstruktor vytvarejici unified diff
     *
     * Konstruktor vytvori diff soubor, ktery obsahuje zadany pocet kontextovych
     * radku.
     *
     * @param fst Prvni ze souboru, ktere se maji porovnavat.
     * @param snd Druhy ze souboru, ktere se maji porovnavat.
     * @param con Pocet kontextovych radku.
     * @throws java.io.IOException
     */
    public CreateUnifiedDiff(File fst, File snd, int con) throws IOException {
        super(fst, snd);
        context = con;
        stringRepresentation = new String();
        fstCounter = sndCounter = 0;
        createHeader(fst, snd);
        createStringRepresentation();
    }

	/**
	 * @brief Konstruktor formatujici unified diff
	 * 
	 * Tento konstruktor uz diff nevytvari, pouze vezme predem vytvoreny a 
	 * zformatuje jej.
	 * 
	 * @param fst Prvni soubor, z ktereho diff pochazi, slouzi k ziskani nazvu.
	 * @param snd Druhy soubor, z ktereho diff pochazi, slouzi k ziskani nazvu.
	 * @param d Diff.
	 */
	public CreateUnifiedDiff(File fst, File snd, List<SequenceElement<String>> d) {
		super(d);
		context = 3;
        stringRepresentation = new String();
        fstCounter = sndCounter = 0;
        createHeader(fst, snd);
        createStringRepresentation();
	}

    /**
     * @brief Vytvori hlavicku diff souboru.
     *
     * Vytvori hlavicku diff souboru, ta obsahuje cestu k souborum a timestamp.
     *
     * @param fst Prvni ze souboru, ktere se maji porovnavat.
     * @param snd Druhy ze souboru, ktere se maji porovnavat.
     */
    protected void createHeader(File fst, File snd) {
        stringRepresentation += "--- " + fst.getPath() + "\t" + fst.lastModified() + "\n";
        stringRepresentation += "+++ " + snd.getPath() + "\t" + snd.lastModified() + "\n";
    }

    /**
     * @brief Vytvoreni vystupniho souboru ze seznamu zmen.
     *
     * Premeni seznam zmen zpocitany predkem CreateDiff ve vystupni soubor.
     * Zpocita indexy zacatku a konce oblasti zmen tak, aby je metoda addChanges
     * mohla pouzit k vytvoreni diff souboru.
     */
    protected void createStringRepresentation() {
        boolean changedLines = false;
        int afterContext = 0;
        int beforeContext = 0;

        for (int i = 0; i < diff.size(); i++) {
            // Radek je nezmenen
            if (diff.get(i).getStatus() == SequenceElement.Status.UNTOUCHED) {
                // Nasleduje az po zmenach
                if (changedLines == true) {
                    afterContext++;
                // Pred zmenami
                } else {
                    beforeContext++;
                }
                fstCounter++; sndCounter++;
            // Radek je zmenen
            } else {
                // Prvni zmeneny radek
                if (changedLines == false) {
                    changedLines = true;
                    startDiff = i - (beforeContext < context ? beforeContext : context);
                    startFst = fstCounter - (beforeContext < context ? beforeContext : context);
                    startSnd = sndCounter - (beforeContext < context ? beforeContext : context);
                // Prvni zmeneny radek po prilis dlouhe dobe => novy chunk
                } else if (afterContext > 2*context) {
                    endFst = fstCounter - afterContext + context;
                    endSnd = sndCounter - afterContext + context;
                    endDiff = i - afterContext + context;
                    addChanges();
                    startFst = fstCounter - context;
                    startSnd = sndCounter - context;
                    startDiff = i - context;
                }
                beforeContext = afterContext = 0;

                if (diff.get(i).getStatus() == SequenceElement.Status.ADDED) {
                    sndCounter++;
                } else {
                    fstCounter++;
                }
            }
        }
        // Pokud byla aspon jedna zmena, pak pridat posledni kus zmen
        if (changedLines == true) {
            endFst = fstCounter - (afterContext <= context ? 0 : afterContext - context);
            endSnd = sndCounter - (afterContext <= context ? 0 : afterContext - context);
            endDiff = diff.size() - (afterContext <= context ? 0 : afterContext - context);
            addChanges();
        // Pokud nebyla zadna zmena, pak vubec nic nevypisovat
        } else {
            stringRepresentation = "";
        }
    }

    /**
     * @brief Vytvoreni vystupniho souboru
     *
     * Vytvori ze zadanych indexu zmen diff soubor. Doplni znacky a cisla radku.
     */
    protected void addChanges() {
        if (context == 0 && endFst == startFst) {
            stringRepresentation += "@@ -"+startFst+",0 +"+(startSnd+1)+","+(endSnd-startSnd)+" @@\n";
        } else if (context == 0 && endSnd == startSnd) {
            stringRepresentation += "@@ -"+(startFst+1)+","+(endFst-startFst)+" +"+startSnd+",0 @@\n";
        } else {
            stringRepresentation += "@@ -"+(startFst+1)+","+(endFst-startFst)+" +"+(startSnd+1)+","+(endSnd-startSnd)+" @@\n";
        }

        for (int i = startDiff; i < endDiff; i++) {
            switch (diff.get(i).getStatus()) {
                case UNTOUCHED:
                    stringRepresentation += " ";
                    break;
                case ADDED:
                    stringRepresentation += "+";
                    break;
                case REMOVED:
                    stringRepresentation += "-";
                    break;
            }
            stringRepresentation += diff.get(i).getElement() + "\n";
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
