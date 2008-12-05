package diff;

import java.util.List;
import java.util.LinkedList;

/**
 * Implementace algoritmu nejdelsi spolecne podposloupnosti
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class LongestCommonSubsequence<T> {

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
     * Algoritmus projiti tabulky a ziskani diffu. Algoritmus projde tabulku
     * vytvorenou pomoci metody computeLength. Vlevo tabulky je prvni
     * posloupnost, nahore je druha posloupnost. Pokud se tedy pohybujeme
     * doleva, pak zahazujeme prvek z druhe posloupnosti, pokud se pohybujeme
     * nahoru, pak opoustime prvek z prvni posloupnosti.
     *
     * Zacneme pravo dole a postupujeme nahoru a doleva. Pokud zjistime, ze
     * prvky na souradnicich i,j jsou stejne, pak je pridame do vysledne
     * posloupnosti jako UNTOUCHED(nezmenene) a posuneme se doleva a nahoru.
     * Pokud se nerovnaji, pak se vydame na sousedni prvek, ktery ma vetsi
     * hodnotu. Pritom pridame bud prvek z druhe posloupnosti, pokud se v
     * tabulce pohybujeme doleva, protoze tento prvek se urcite nerovna zadnemu
     * z prvni posloupnosti. Oznacime jej jako ADDED(pridan). Pokud se presuneme
     * na horni prvek pak pridame prvek z prvni posloupnosti, take uz se nemuze
     * rovnat zadnemu z druhe. Tento bude REMOVED(odebran).
     *
     * @return Vysledna posloupnost
     */
    public List<SequenceElement<T>> findDiff() {
        LinkedList<SequenceElement<T>> sequence = new LinkedList<SequenceElement<T>>();
        int[][] table = computeLength();

        int i = table.length-1;
        int j = table[0].length-1;
        while (i > 0 && j > 0)  {

            if (firstList.get(i-1).equals(secondList.get(j-1))) {
                SequenceElement<T> newElement = new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.UNTOUCHED);
                sequence.addFirst(newElement);
                i--; j--;
            } else if (table[i][j-1] >= table[i-1][j]) {
                SequenceElement<T> newElement = new SequenceElement<T>(secondList.get(j-1), SequenceElement.Status.ADDED);
                sequence.addFirst(newElement);
                j--;
            } else {
                SequenceElement<T> newElement = new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.REMOVED);
                sequence.addFirst(newElement);
                i--;
            }
        }

        while (i > 0) {
            SequenceElement<T> newElement = new SequenceElement<T>(firstList.get(i-1), SequenceElement.Status.REMOVED);
            sequence.addFirst(newElement);
            i--;
        }
        while (j > 0) {
             SequenceElement<T> newElement = new SequenceElement<T>(secondList.get(j-1), SequenceElement.Status.ADDED);
             sequence.addFirst(newElement);
             j--;
        }

        return sequence;
    }

}
