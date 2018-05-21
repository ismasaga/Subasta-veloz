package pujador;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import ontologia.Libro;

public class ModeloTabla extends AbstractTableModel {

    private HashMap<Libro, String> books;

    public ModeloTabla(HashMap<Libro, String> books) {
        this.books = books;
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        String tmp = "";
        switch (column) {
            case 0:
                tmp = "Libro";
                break;
            case 1:
                tmp = "Estado";
                break;
        }
        return tmp;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<Libro> tmp = new ArrayList<>(books.keySet());
        switch (columnIndex) {
            case 0:
                return tmp.get(rowIndex).getTitle();
            case 1:
                return books.get(tmp.get(rowIndex));
        }
        return "default";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return super.isCellEditable(rowIndex, columnIndex);
    }

    public void changeStatus(Libro libro, String status) {
        books.put(libro, status);
        fireTableDataChanged();
    }
}
