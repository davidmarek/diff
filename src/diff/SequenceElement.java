package diff;

/**
 * Jednotlive prvky vysledne posloupnosti.
 * Trida obsahuje vzdy prvek a pak take jeho stav @see Status.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class SequenceElement<T> {

    /**
     * Kazdy prvek posloupnosti muze byt bud:
     * <ul>
     *  <li>ADDED - Pridan</li>
     *  <li>REMOVED - Odebran</li>
     *  <li>UNTOUCHED - Nezmenen</li>
     * </ul>
     */
    public enum Status {
        ADDED, REMOVED, UNTOUCHED;
    }

    protected T element;
    protected Status status;

    /**
     * Konstruktor.
     *
     * @param e Prvek vysledne posloupnosti
     * @param s Status prvku
     */
    public SequenceElement(T e, Status s) {
        element = e;
        status = s;
    }

    /**
     * Vraci prvek posloupnosti.
     *
     * @return Prvek
     */
    public T getElement() {
        return element;
    }

    /**
     * Vraci status prvku, zda-li byl pridan, odebran nebo zustal nezmenen.
     *
     * @return Status
     */
    public Status getStatus() {
        return status;
    }
}
