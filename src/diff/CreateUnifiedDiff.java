package diff;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateUnifiedDiff extends CreateDiff {

    protected String stringRepresentation;

    protected LinkedList<String> removed;
    protected LinkedList<String> added;

    protected int fstCounter;
    protected int sndCounter;

    protected int context;
    
    {
        stringRepresentation = new String();
        context = 3;
        fstCounter = 0;
        sndCounter = 0;
        removed = new LinkedList<String>();
        added = new LinkedList<String>();
    }

    /**
     *
     * @param fst
     * @param snd
     * @throws java.io.IOException
     */
    public CreateUnifiedDiff(File fst, File snd) throws IOException {
        super(fst,snd);
        createHeader(fst, snd);
        createStringRepresentation();
    }

    /**
     *
     * @param fst
     * @param snd
     * @param con
     * @throws java.io.IOException
     */
    public CreateUnifiedDiff(File fst, File snd, int con) throws IOException {
        super(fst, snd);
        context = con;
        createHeader(fst, snd);
        createStringRepresentation();
    }

    /**
     *
     * @param fst
     * @param snd
     */
    protected void createHeader(File fst, File snd) {
        stringRepresentation += "--- " + fst.getPath() + "\t" + fst.lastModified() + "\n";
        stringRepresentation += "+++ " + snd.getPath() + "\t" + snd.lastModified() + "\n";
    }

    /**
     *
     */
    protected void createStringRepresentation() {
        Iterator<SequenceElement<String>> it = diff.iterator();
        LinkedList<String> lastLines = new LinkedList<String>();
        int writeContextLines = 0;

        SequenceElement<String> se;
        while (it.hasNext()) {
            se = it.next();
            switch (se.getStatus()) {
                case UNTOUCHED:

                    /*
                     * Pokud se jedna o prvni spolecny radek po rozdilnych, pak
                     * musime vypsat nejprve kontextove radky, pote rozdil a
                     * nakonec musime nastavit kolik kontextovych radku chceme
                     * po vypisu.
                     */
                    if (!added.isEmpty() || !removed.isEmpty()) {
                        for (String l : lastLines) {
                            stringRepresentation += lastLines;
                        }
                        addChanges(it);
                        lastLines.clear();
                        writeContextLines = context;
                    }

                    /*
                     * Mozne jsou dva pripady, bud mame nezmeneny radek, ktery
                     * patri do kontextu minuleho rozdilu, nebo radek, ktery by
                     * mohl teoreticky patrit do kontextu dalsiho.
                     */
                    if (writeContextLines > 0) {
                        stringRepresentation += se.getElement();
                        writeContextLines--;
                    } else {
                        lastLines.push(se.getElement());
                        if (lastLines.size() > context) { lastLines.pollLast(); }
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
            se = it.next();
        }
    }

    /**
     *
     */
    protected void addChanges(Iterator<SequenceElement<String>> it) {

    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }
}
