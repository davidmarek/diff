package diff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class ConsoleApp {

    /**
     * Povolene prepinace
     */
    protected enum Option {
        NORMAL, UNIFIED, UNIFIED_CONTEXT;
    }

    /**
     * Neznamy prepinac.
     */
    public class UnknownOptionException extends Exception {

        /**
         * @brief Vytvori vyjimku
         * @param opt Prepinac, ktery nebyl nalezen.
         */
        public UnknownOptionException(String opt) {
            super("Unknown option: "+opt+" .");
        }
    }

    /**
     * Spatny parametr.
     */
    public class MissingParameterException extends Exception {

        /**
         * @brief Vytvori vyjimku.
         */
        public MissingParameterException() {
            super("Missing parameter.");
        }
    }

    /**
     * Prilis mnoho parametru
     */
    public class TooManyParametersException extends Exception {

        /**
         * @brief Vytvori vyjimku.
         */
        public TooManyParametersException() {
            super("Too many parameters.");
        }
    }

    /** Struktura pro prevadeni z retezce parametru na vstupu na enum Option */
    TreeMap<String, Option> shortOpt;

    /** Prepinac urcujici, ktera trida se ma pouzit pro vytvareni diffu. */
    Option style;

    /** U unified diffu muze byt definovan pocet kontextovych radku. */
    int context;
    
    /** Seznam souboru, ktere se maji porovnavat. */
    ArrayList<File> files;

    /**
     * @brief Konstruktor
     */
    public ConsoleApp() {
        shortOpt = new TreeMap<String, Option>();
        shortOpt.put("-u", Option.UNIFIED);
        shortOpt.put("-U", Option.UNIFIED_CONTEXT);
        style = Option.NORMAL;
        context = 3;
        files = new ArrayList<File>();
    }

    /**
     * @brief Rozpoznani prepinace
     *
     * Snazi se zadany prepinac najit v tabulce prepinacu. Pokud jej nenajde tak
     * vyhodi vyjimku.
     *
     * @param opt
     * @throws diff.ConsoleApp.UnknownOptionException
     * @return Zadany prepinac.
     */
    protected Option getOption(String opt) throws UnknownOptionException {
        Option o;
        if ((o = shortOpt.get(opt)) == null) {
            throw new UnknownOptionException(opt);
        }
        return o;
    }

    /**
     * @brief Zpracovani vstupu od uzivatele
     *
     * Projde zadane parametry, vyhodnoti podle nich prepinace, pripravi program
     * k vytvareni diffu. V pripade, ze nalezne chybu v prepinacich nebo ve vstupnich
     * souborech, vyhodi vyjimku.
     *
     * @param args Argumenty, zadane v konzoli
     * @throws diff.ConsoleApp.UnknownOptionException
     * @throws diff.ConsoleApp.TooManyParametersException
     */
    public void parseArgs(String[] args) throws UnknownOptionException, MissingParameterException, TooManyParametersException {
        Option opt;
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                opt = getOption(args[i]);
                switch(opt) {

                    case UNIFIED:
                        style = Option.UNIFIED;
                        break;

                    case UNIFIED_CONTEXT:
                        style = Option.UNIFIED;
                        try {
                            context = Integer.parseInt(args[i+1]);
                            i++;
                        } catch (Exception e) {
                            throw new MissingParameterException();
                        }
                        break;
                }
            } else {
                files.add(new File(args[i]));
            }
        }
        if (files.size() > 2) {
            throw new TooManyParametersException();
        } else if (files.size() < 2) {
            throw new MissingParameterException();
        }
    }

    /**
     * @brief Vytvori diff a vypise jej
     *
     * Vytvori potomka tridy CreateDiff, necha jej vytvorit diff a vypise jej.
     *
     * @throws java.io.IOException
     */
    void run() throws IOException {
        CreateDiff cd;
        switch (style) {
            case NORMAL:
                cd = new CreateNormalDiff(files.get(0), files.get(1));
                break;
            case UNIFIED:
                cd = new CreateUnifiedDiff(files.get(0), files.get(1), context);
                break;
            default:
                cd = new CreateNormalDiff(files.get(0), files.get(1));
                break;
        }
        System.out.print(cd);
    }
}
