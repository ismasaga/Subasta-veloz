package subastador;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import book.Book;

@SuppressWarnings("serial")
public class ModeloTabla extends AbstractTableModel {

	public ArrayList<Book> books;

	public ModeloTabla(ArrayList<Book> books) {
		this.books = books;
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
		return books.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return books.get(rowIndex).getTitle();
		case 1:
			return books.get(rowIndex).getPrice();
		case 2:
			return books.get(rowIndex).getWinner();
		}
		return "default";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return super.isCellEditable(rowIndex, columnIndex);
	}

	public void changeStatus(Book book) {
		int i = 0;
		int temp = 0;
		for (Book b : books) {
			if (b.equals(book)) {
				temp = i;
			}
			i++;
		}
		books.set(temp, book);
		fireTableDataChanged();
	}
}
