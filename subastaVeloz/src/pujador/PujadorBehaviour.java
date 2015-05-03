package pujador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;

import ontologia.AcceptProposal;
import ontologia.Book;
import ontologia.CallForProposal;
import ontologia.Propose;
import ontologia.RejectProposal;
import ontologia.Request;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	private ArrayList<Book> books;
	private AgentePujador pujador;

	public PujadorBehaviour(AgentePujador pujador, HashMap<Book, String> books) {
		super();
		this.books = new ArrayList<Book>(books.keySet());
		this.pujador = pujador;
	}

	@Override
	public void action() {
		Action action = null;
		this.books = new ArrayList<>(pujador.getBooks().keySet());
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.CFP),
				MessageTemplate.MatchOntology(pujador.getOntology().getName()));
		ACLMessage message = null;
		ACLMessage reply = null;
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
			reply.setOntology(pujador.getOntology().getName());
			reply.setLanguage(pujador.getCodec().getName());

			Propose proposal = new Propose();
			proposal.setBook(cfp.getBook());
			if (price != null && price > 0) {
				pujador.changeStatus(new Book(title), "En curso");
				maxPrice = (float) 0.0;
				Boolean flag = false;
				for (Book book : books) {
					if (book.getTitle().equals(title)) {
						maxPrice = book.getPrice();
						flag = true;
					}
				}
				if (flag == true && price <= maxPrice) {
					proposal.setAnswer(new Boolean(true));
					System.out.println(myAgent.getName()
							+ ": acepto pujar por " + title + " por " + price);
				} else {
					proposal.setAnswer(new Boolean(false));
					System.out.println(myAgent.getName()
							+ ": rechazo pujar por " + title + " por " + price);
					pujador.changeStatus(cfp.getBook(), "Precio superado");
				}

			} else {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				reply.setContent("Precio no válido");
			}
			try {
				pujador.getContentManager().fillContent(reply,
						new Action(myAgent.getAID(), proposal));
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			reply.setSender(myAgent.getAID());
			myAgent.send(reply);
		}

		mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
		message = myAgent.receive(mt);
		if (message != null) {
			try {
				action = (Action) pujador.getContentManager().extractContent(
						message);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			AcceptProposal acceptProposal = (AcceptProposal) action.getAction();
			Book book = acceptProposal.getBook();
			pujador.changeStatus(book, "Ronda ganada");
		} else {
			block();
		}

		mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
		message = myAgent.receive(mt);
		if (message != null) {
			try {
				action = (Action) pujador.getContentManager().extractContent(
						message);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			RejectProposal rejectProposal = (RejectProposal) action.getAction();
			Book book = rejectProposal.getBook();
			pujador.changeStatus(book, "Ronda no ganada");
		} else {
			block();
		}

		mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		message = myAgent.receive(mt);
		if (message != null) {
			// Current bidder won desired book
			try {
				action = (Action) pujador.getContentManager().extractContent(
						message);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			Request request = (Request) action.getAction();
			if (books.contains(request.getBook())) {
				books.remove(request.getBook());
			}
			pujador.changeStatus(request.getBook(), "Adquirido");
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
