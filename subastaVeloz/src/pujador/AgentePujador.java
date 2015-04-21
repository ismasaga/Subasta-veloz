package pujador;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

import book.Book;

@SuppressWarnings("serial")
public class AgentePujador extends Agent{

	private ArrayList<Book> books;
	
	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		books = new ArrayList<>();
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		Object[] args = getArguments();
		if (args != null && args.length % 2 == 0 && args.length >= 2) {
			for (int i = 0; i < args.length; i+=2)
				books.add(new Book((String)args[i], Float.parseFloat((String) args[i+1])));
		}
		else {
			System.out.println("Número incorrecto de argumentos");
		}
		
		for (Book book : books) {
			System.out.println("Pujando por " + book.getTitle() + "por un máximo de " + book.getPrice());
		}
		addBehaviour(new PujadorBehaviour(books));
	}
	
	public void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(getAID().getName() + " ha terminado");
	}
	
}
