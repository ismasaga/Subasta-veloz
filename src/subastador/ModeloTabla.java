package subastador;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ontologia.Libro;

public class ModeloTabla extends AbstractTableModel {

	public ArrayList<Libro> libros;

	public ModeloTabla(ArrayList<Libro> libros) {
		this.libros = libros;
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 3;
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
		case 2:
			tmp = "Ganador";
			break;
		}
		return tmp;
	}

	@Override
	public int getRowCount() {
		return libros.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return libros.get(rowIndex).getTitle();
		case 1:
			return libros.get(rowIndex).getPrice();
		case 2:
			return libros.get(rowIndex).getWinner();
		}
		return "default";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return super.isCellEditable(rowIndex, columnIndex);
	}

	public void changeStatus(Libro libro) {
		int i = 0;
		int temp = 0;
		for (Libro b : libros) {
			if (b.equals(libro)) {
				temp = i;
			}
			i++;
		}
		libros.set(temp, libro);
		fireTableDataChanged();
	}
}
