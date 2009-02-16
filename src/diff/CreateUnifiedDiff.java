package diff;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class CreateUnifiedDiff extends CreateDiff {

    protected String stringRepresentation;

    protected int fstCounter;
    protected int sndCounter;

    protected int context;
    protected int startDiff;
    protected int endDiff;
    protected int startFst;
    protected int endFst;
    protected int startSnd;
    protected int endSnd;
    
    {
        stringRepresentation = new String();
        context = 3;
        fstCounter = sndCounter = 0;
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
        endFst = fstCounter - (afterContext <= context ? 0 : afterContext - context);
        endSnd = sndCounter - (afterContext <= context ? 0 : afterContext - context);
        endDiff = diff.size() - (afterContext <= context ? 0 : afterContext - context);
        addChanges();
    }


    protected void addChanges() {
        stringRepresentation += "@@ -"+(startFst+1)+","+(endFst-startFst)+" +"+(startSnd+1)+","+(endSnd-startSnd)+" @@\n";
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
     *
     * @return
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }
}
