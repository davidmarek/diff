package diff;

import java.util.List;
import java.util.LinkedList;

/**
 * Implementace algoritmu nejdelsi spolecne podposloupnosti
 * @author David Marek <david at davidmarek.cz>
 */
public class LongestCommonSubsequence<T> {

    /**
     *
     */
    public enum StatusElement {
        ADDED, REMOVED, UNTOUCHED;
    }

    /**
     *
     */
    public class SequenceElement {
        protected T element;
        protected StatusElement status;

        /**
         *
         * @param e
         * @param s
         */
        public SequenceElement(T e, StatusElement s) {
            element = e;
            status = s;
        }

        /**
         *
         * @return
         */
        public T getElement() {
            return element;
        }

        /**
         *
         * @return
         */
        public StatusElement getStatus() {
            return status;
        }
    }
    
    List<T> firstList, secondList;
    
    /**
     * Algoritmus pracuje nad dvema seznamy prvku. Prvky mohou byt jakehokoli 
     * typu, ale musi byt porovnatelne.
     * 
     * @param first Prvni seznam prvku.
     * @param second Druhy seznam prvku.
     */
    public LongestCommonSubsequence(List<T> first, List<T> second) {
        this.firstList = first;
        this.secondList = second;
    }
    
    /**
     * Pouzijeme dynamickeho programovani a zpocitame nejdelsi podposloupnost.
     * Algoritmus ma slozitost O(N*M), kazdy prvek z prvniho seznamu porovname
     * s kazdym prvkem druheho seznamu. Vysledky si zapisujeme do tabulky o
     * velikosti O(N*M). N je velikost prvniho seznamu, M je velikost druheho.
     * 
     * Pokud najdeme dva prvky na indexech i a j, pak do tabulky na pozici i,j
     * zapiseme maximalni velikost spolecne posloupnosti, ktera je na pozici 
     * i-1, j-1, a zvetsime ji o jedna. Pokud se prvky na indexech i a j 
     * nerovnaji, pak do tabulky zapiseme maximum z leve a horni bunky. Timto 
     * zpusobem si zarucime, ze maximalni hodnota (velikost podposloupnosti) nam
     * propadne az do praveho dolniho rohu.
     * 
     * Pro zjisteni maximalni delky nam tedy staci podivat se na pravy dolni roh
     * tabulky, tuto tabulku si vsak uchovavame, protoze z ni ziskame posleze 
     * nejen velikost, ale i celou spolecnou podposloupnost.
     * 
     * @return Tabulka delky spolecne podposloupnosti pro zadane seznamy.
     */
    protected int[][] computeLength() {
        int table[][] = new int[this.firstList.size()+1][this.secondList.size()+1];
        
        for (int i = 0; i < firstList.size(); i++) {
            for (int j = 0; j < secondList.size(); j++) {
                if (firstList.get(i).equals(secondList.get(j))) {
                    table[i+1][j+1] = table[i][j] + 1;
                } else {
                    table[i+1][j+1] = Math.max(table[i+1][j], table[i][j+1]);
                }
            }
        }
        
        return table;
    }
    
    /**
     * Ziskani nejdelsi spolecne podposloupnosti. Algoritmus pouziva
     * predpocitanou tabulku od metody computeLength(). Zacneme v pravem dolnim
     * rohu, kde zjistime delku spolecne podposloupnosti. Pokud maji prvky na
     * pozicich [i-1][j] a [i][j-1] nizsi hodnoty pak vime, ze jsme nasli prvek,
     * ktery je soucasti podposloupnosti. Kdyz jsme totiz vytvareli tabulku tak
     * jsme nemohli zkopirovat hodnotu od sousedniho prvku, ale museli jsme
     * inkrementovat, coz znamena, ze tento prvek je spolecny pro obe
     * posloupnosti. Jeho indexy tedy pridame do seznamu indexu pro prvni i
     * druhou posloupnost.
     *
     * V opacnem pripade, kdy nektery ze sousedu(levy nebo horni) ma stejnou
     * hodnotu, pak se presuneme na nej. Vzdy se tedy aspon jeden z indexu
     * zmensi o jedna.
     *
     * Algoritmus konci ve chvili kdy je hodnota prvku rovna nule. Musi skoncit
     * vzdy, protoze pri vytvareni tabulky jsme nechali prvni radek a prvni
     * sloupec nulovy.
     *
     * @return Velikost nejdelsi spolecne podposloupnosti.
     */
    /*
    protected int findSubsequence() {
        int table[][] = computeLength();

        int i = table.length-1;
        int j = table[0].length-1;
        while (table[i][j] > 0) {
            if (table[i-1][j] == table[i][j]) {
                i--;
            } else if (table[i][j-1] == table[i][j]) {
                j--;
            } else {
                firstListIndexes.add(0, i-1);
                secondListIndexes.add(0, j-1);
                i--; j--;
            }
        }
        return table[table.length-1][table[0].length-1];
    }
    */

    public List<SequenceElement> findDiff() {
        LinkedList<SequenceElement> sequence = new LinkedList<SequenceElement>();
        int[][] table = computeLength();

        int i = table.length-1;
        int j = table[0].length-1;
        while (i > 0 && j > 0)  {

            if (firstList.get(i-1).equals(secondList.get(j-1))) {
                SequenceElement newElement = new SequenceElement(firstList.get(i-1), StatusElement.UNTOUCHED);
                sequence.addFirst(newElement);
                i--; j--;
            } else if (table[i][j-1] >= table[i-1][j]) {
                SequenceElement newElement = new SequenceElement(secondList.get(j-1), StatusElement.ADDED);
                sequence.addFirst(newElement);
                j--;
            } else {
                SequenceElement newElement = new SequenceElement(firstList.get(i-1), StatusElement.REMOVED);
                sequence.addFirst(newElement);
                i--;
            }
        }

        while (i > 0) {
            SequenceElement newElement = new SequenceElement(firstList.get(i-1), StatusElement.REMOVED);
            sequence.addFirst(newElement);
            i--;
        }
        while (j > 0) {
             SequenceElement newElement = new SequenceElement(secondList.get(j-1), StatusElement.ADDED);
             sequence.addFirst(newElement);
             j--;
        }

        return sequence;
    }

}
