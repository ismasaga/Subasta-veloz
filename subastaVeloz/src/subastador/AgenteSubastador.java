package subastador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

import java.util.ArrayList;

import ontologia.AuctionOntology;
import ontologia.Book;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent {

	private ArrayList<Book> books;
	private SubastadorGUI subastadorGUI;
	Codec codec = new SLCodec();
	Ontology ontology = AuctionOntology.getInstance();

	protected void setup() {

		books = new ArrayList<>();
		books.add(new Book("Don Quijote", (float) 10.0, (float) 2.0, "En curso"));
		books.add(new Book("El perfume", (float) 12.0, (float) 2.0, "En curso"));
		books.add(new Book("El nombre de la rosa", (float) 15.0, (float) 2.0,
				"En curso"));

		System.out.println("Hola! " + getAID().getName() + " está listo");
		subastadorGUI = new SubastadorGUI(books, this);
		subastadorGUI.setVisible(true);

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		for (Book book : books) {
			addBehaviour(new SubastadorBehaviour(this, 10000, book));
		}

	}

	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
		// TODO kill GUI
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
