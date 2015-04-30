package subastador;

import jade.core.Agent;

import java.util.HashMap;

import book.Book;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent {

	private HashMap<Book, Float> books;
	private Float increase = (float) 5.0;

	protected void setup() {
		books = new HashMap<>();
		books.put(new Book("Don Quijote", (float) 10.0), increase);
		books.put(new Book("El perfume", (float) 12.0), increase);
		books.put(new Book("El nombre de la rosa", (float) 15.0), increase);

		System.out.println("Hola! " + getAID().getName() + " está listo");
		for (Book book : books.keySet()) {
			addBehaviour(new SubastadorBehaviour(this, 10000, book,
					books.get(book)));
		}
	}

	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}

	public void AddBook(Book book, Float increase) {
		if (!books.containsKey(book)) {
			books.put(book, increase);
			addBehaviour(new SubastadorBehaviour(this, 10000, book, increase));
		}
	}

	public HashMap<Book, Float> getBooks() {
		return this.books;
	}
}
