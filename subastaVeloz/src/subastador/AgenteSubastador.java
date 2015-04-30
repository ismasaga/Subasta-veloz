package subastador;

import jade.core.Agent;

import java.util.ArrayList;

import ontologia.Book;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent {

	private ArrayList<Book> books;
	private Float increase = (float) 5.0;
	private SubastadorGUI subastadorGUI;

	protected void setup() {
		books = new ArrayList<>();
		books.add(new Book("Don Quijote", (float) 10.0, (float) 2.0, "En curso"));
		books.add(new Book("El perfume", (float) 12.0, (float) 2.0, "En curso"));
		books.add(new Book("El nombre de la rosa", (float) 15.0, (float) 2.0,
				"En curso"));

		System.out.println("Hola! " + getAID().getName() + " est� listo");
		subastadorGUI = new SubastadorGUI(books, this);
		subastadorGUI.setVisible(true);
		for (Book book : books) {
			addBehaviour(new SubastadorBehaviour(this, 10000, book));
		}

	}

	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}

	public void AddBook(Book book) {
		boolean repe = false;
		for (Book b : books) {
			if (b.equals(book)) {
				repe = true;
			}
		}
		if (repe == false) {
			books.add(book);
			addBehaviour(new SubastadorBehaviour(this, 10000, book));
		}
	}

	public ArrayList<Book> getBooks() {
		return this.books;
	}

	public void updatePrice(Book book) {
		int i = 0;
		int temp = 0;
		for (Book b : books) {
			if (b.equals(book)) {
				temp = i;
			}
			i++;
		}
		books.get(temp).updatePrice();
		subastadorGUI.getModel().changeStatus(book);
	}

	public void updateWinner(Book book, String winner) {
		int i = 0;
		int temp = 0;
		for (Book b : books) {
			if (b.equals(book)) {
				temp = i;
			}
			i++;
		}
		books.get(temp).setWinner(winner);
		subastadorGUI.getModel().changeStatus(book);
	}

}
