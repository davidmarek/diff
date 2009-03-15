package diffGui;

import diff.SequenceElement;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Renderer pro bunky tabulky obsahujici diff.
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class DiffLineCellRenderer extends DefaultTableCellRenderer {

	/** 
	 * Konstruktor
	 */
    public DiffLineCellRenderer() {
        super();
    }

	/**
	 * Pouziva zdedeny defaultni renderer, pouze meni barvu pozadi v zavislosti
	 * na statusu radku.
	 *
	 * @param table Tabulka.
	 * @param value Hodnota bunky.
	 * @param isSelected Je bunka vybrana?
	 * @param hasFocus Ma focus?
	 * @param row Cislo radku.
	 * @param column Cislo sloupce.
	 * @return Renderer.
	 */
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setOpaque(true);
        SequenceElement<String> v;

        v = (SequenceElement<String>) value;
        
        switch (v.getStatus()) {
            case ADDED:
                setBackground(Color.GREEN);
                break;
            case REMOVED:
                setBackground(Color.RED);
                break;
            case UNTOUCHED:
                setBackground(Color.WHITE);
                break;
        }

        super.getTableCellRendererComponent(table, v.getElement(), isSelected, hasFocus, row, column);
        return this;
    }
}
