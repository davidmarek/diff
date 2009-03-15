package diffGui;

import diff.SequenceElement;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class DiffLineCellRenderer extends DefaultTableCellRenderer {

    public DiffLineCellRenderer() {
        super();
    }

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
