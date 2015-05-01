package pujador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;

import ontologia.Book;
import ontologia.CallForProposal;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	private ArrayList<Book> books;
	private AgentePujador pujador;
	private Ontology ontology;

	public PujadorBehaviour(AgentePujador pujador, HashMap<Book, String> books) {
		super();
		this.books = new ArrayList<Book>(books.keySet());
		this.pujador = pujador;
	}

	@Override
	public void action() {
		Action action = null;
		ontology = pujador.ontology;
		this.books = new ArrayList<>(pujador.getBooks().keySet());
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.CFP),
				MessageTemplate.MatchOntology(ontology.getName()));
		ACLMessage message = null;
		ACLMessage reply = null;
		boolean flag = false;
		Float maxPrice = (float) 0.0;
		message = myAgent.receive(mt);
		if (message != null) {
			try {
				action = (Action) pujador.getContentManager().extractContent(
						message);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			CallForProposal cfp = (CallForProposal) action.getAction();
			String title = cfp.getBook().getTitle();
			Float price = cfp.getBook().getPrice();
			reply = message.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			reply.setOntology(ontology.getName());
			if (price != null && price > 0) {
				pujador.changeStatus(new Book(title), "En curso");
				flag = false;
				maxPrice = (float) 0.0;
				for (Book book : books) {
					if (title.equals(book.getTitle())) {
						flag = true;
						maxPrice = book.getPrice();
					}
				}
				if (flag == true && price <= maxPrice) {
					reply.setContent("interested");
					System.out.println(myAgent.getName() + " acepta pujar por "
							+ title + " por " + price);
				} else {
					reply.setContent("not interested");
					System.out.println(myAgent.getName()
							+ " rechaza pujar por " + title + " por " + price);
				}
			} else {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				reply.setContent("Precio no válido");
			}
			reply.setSender(myAgent.getAID());
			myAgent.send(reply);
		}

		mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		message = myAgent.receive(mt);
		if (message != null) {
			// Current bidder won desired book
			if (books.contains(new Book(message.getConversationId()))) {
				books.remove(new Book(message.getConversationId()));
			}
			pujador.changeStatus(new Book(message.getConversationId()),
					"Adquirido");
		} else {
			block();
		}

		mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		message = myAgent.receive(mt);
		if (message != null) {
			if (books.contains(new Book(message.getConversationId()))) {
				pujador.changeStatus(new Book(message.getConversationId()),
						"No ganada");
			}
			System.out.println(myAgent.getName()
					+ ": no he ganado la subasta de "
					+ message.getConversationId());
		}
	}
}
