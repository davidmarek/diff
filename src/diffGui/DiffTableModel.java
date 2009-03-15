package diffGui;

import diff.SequenceElement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Model pro tabulku zobrazujici diff.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class DiffTableModel extends AbstractTableModel {

	/** Diff, ktery se ma zobrazit. */
    protected List<SequenceElement<String>> diff;

	/**
	 * Konstruktor
	 */
    public DiffTableModel() {
        diff = new ArrayList<SequenceElement<String>>();
    }

	/**
	 * Konstruktor
	 * @param diff Diff, ktery se ma zobrazit.
	 */
    public DiffTableModel(List<SequenceElement<String>> diff) {
        this.diff = diff;
    }

	/** 
	 * Nastaveni dat, ktera se maji zobrazit.
	 * @param diff Diff, ktery se ma zobrazit.
	 */
    public void setData(List<SequenceElement<String>> diff) {
        this.diff = diff;
        this.fireTableStructureChanged();
    }

	/**
	 * @return Zobrazena data.
	 */
	public List<SequenceElement<String>> getData() {
		return diff;
	}

	/**
	 * Jestli je bunka editovatelna, v Diffu slouzi tabulka pouze pro
	 * zobrazovani, ne k editovani
	 * @param row Radek tabulky.
	 * @param col Sloupec tabulky.
	 * @return Vzdy vraci false, tabulka neni editovatelna.
	 */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

	/**
	 * @return Pocet radku tabulky je roven poctu radku v diffu.
	 */
    public int getRowCount() {
        return diff.size();
    }

	/**
	 * @return Jsou 2 sloupce, jeden pro cislovani radku, druhy pro diff.
	 */
    public int getColumnCount() {
        return 2;
    }

	/**
	 * @param columnIndex Cislo sloupce.
	 * @return Nazev sloupce, sloupce nemaji nazev.
	 */
    @Override
    public String getColumnName(int columnIndex) {
        return "";
    }

	/**
	 * Vraci hodnotu v bunkce, pokud se jedna o prvni sloupce tak vraci cislo 
	 * radku, v druhem sloupci je radek diffu na tomto radku.
	 * 
	 * @param rowIndex Cislo radku.
	 * @param columnIndex Cislo sloupce.
	 * @return Hodnota bunky.
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex+1;
        } else {
            return diff.get(rowIndex);
        }
    }

	/**
	 * Vraci tridu, kterou maji prvky v zadanem sloupci, v prvnim sloupci jsou
	 * cisla radku, tedy Integer. Ve druhem sloupci jsou radky diffu, tedy
	 * SequenceElement.
	 * @param c Cislo sloupce.
	 * @return Trida prvku v sloupci.
	 */
    @Override
    public Class getColumnClass(int c) {
        if (c == 0) {
            return Integer.class;
        } else {
            return String.class;
        }
    }

}
