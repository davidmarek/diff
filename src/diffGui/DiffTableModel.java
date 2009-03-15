package diffGui;

import diff.SequenceElement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class DiffTableModel extends AbstractTableModel {

    protected List<SequenceElement<String>> diff;

    public DiffTableModel() {
        diff = new ArrayList<SequenceElement<String>>();
    }

    public DiffTableModel(List<SequenceElement<String>> diff) {
        this.diff = diff;
    }

    public void setData(List<SequenceElement<String>> diff) {
        this.diff = diff;
        this.fireTableStructureChanged();
    }

	public List<SequenceElement<String>> getData() {
		return diff;
	}

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    public int getRowCount() {
        return diff.size();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex+1;
        } else {
            return diff.get(rowIndex);
        }
    }

    @Override
    public Class getColumnClass(int c) {
        if (c == 0) {
            return Integer.class;
        } else {
            return String.class;
        }
    }

}
