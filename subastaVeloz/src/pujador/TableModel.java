package pujador;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import book.Book;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {

	private ArrayList<Book> books;

	public TableModel() {
		books = new ArrayList<Book>();
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int column) {
		return "Book";
	}

	@Override
	public int getRowCount() {
		return books.size();
	}

	public Book getBookAt(int rowIndex, int columnIndex) {
		return books.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return books.get(rowIndex).getTitle();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return super.isCellEditable(rowIndex, columnIndex);
	}

}
