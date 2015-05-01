package pujador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;

import ontologia.AuctionOntology;
import ontologia.Book;

@SuppressWarnings("serial")
public class AgentePujador extends Agent {

	private HashMap<Book, String> books;
	private PujadorGUI pujadorGUI;
	private Ontology ontology = AuctionOntology.getInstance();
	private Codec codec = new SLCodec();;

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		books = new HashMap<>();
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		Object[] args = getArguments();
		if (args != null && args.length % 2 == 0 && args.length >= 2) {
			for (int i = 0; i < args.length; i += 2)
				books.put(
						new Book((String) args[i], Float
								.parseFloat((String) args[i + 1])),
						"Esperando subasta");

			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			for (Book book : books.keySet()) {
				System.out.println("Pujando por " + book.getTitle()
						+ " por un máximo de " + book.getPrice() + " euros");
			}
			addBehaviour(new PujadorBehaviour(this, books));
			pujadorGUI = new PujadorGUI(books, this);
			pujadorGUI.setVisible(true);
		} else {
			System.out.println("Número incorrecto de argumentos");
		}

	}

	public void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(getAID().getName() + " ha terminado");
	}

	public void changeStatus(Book book, String status) {
		if (books.containsKey(book)) {
			books.put(book, status);
			pujadorGUI.getModel().changeStatus(book, status);
		}
	}

	public void addBook(String title, Float maxPrice) {
		Book book = new Book(title, maxPrice);
		books.put(book, "Esperando subasta");
		pujadorGUI.getModel().changeStatus(book, books.get(book));
	}

	public HashMap<Book, String> getBooks() {
		return books;
	}

	public Codec getCodec() {
		return codec;
	}

	public Ontology getOntology() {
		return ontology;
	}
}
