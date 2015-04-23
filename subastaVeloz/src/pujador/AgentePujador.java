package pujador;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.HashMap;

import book.Book;

@SuppressWarnings("serial")
public class AgentePujador extends Agent{

	private HashMap<Book, String> books;
	private PujadorGUI pujadorGUI;
	
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
			for (int i = 0; i < args.length; i+=2)
				books.put(new Book((String)args[i], Float.parseFloat((String) args[i+1])), "Esperando subasta");
		}
		else {
			System.out.println("Número incorrecto de argumentos");
		}
		
		for (Book book : books.keySet()) {
			System.out.println("Pujando por " + book.getTitle() + " por un máximo de " + book.getPrice() + " euros");
		}
		addBehaviour(new PujadorBehaviour(this, books));
		pujadorGUI = new PujadorGUI(books);
		pujadorGUI.setVisible(true);
	}
	
	public void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(getAID().getName() + " ha terminado");
	}
	
	public void changeStatus (Book book, String status) {
		books.put(book, status);
		pujadorGUI.getModel().changeStatus(book, status);
	}
	
}
