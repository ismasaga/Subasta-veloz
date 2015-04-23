package pujador;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;

import book.Book;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	private ArrayList <Book> books;
	private AgentePujador pujador;

	public PujadorBehaviour(AgentePujador pujador, HashMap<Book, String> books) {
		super();
		this.books = new ArrayList<Book> (books.keySet());
		this.pujador = pujador;
	}


	@Override
	public void action() {
		this.books = new ArrayList<>(pujador.getBooks().keySet());
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage message = null;
		ACLMessage reply = null;
		boolean flag = false;
		Float maxPrice = (float)0.0;
			message = myAgent.receive(mt);
			if (message != null) {
				Float price = Float.parseFloat(message.getContent());
				reply = message.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				if (price != null && price > 0){
					pujador.changeStatus(new Book(message.getConversationId()), "En curso");
					flag = false;
					maxPrice = (float)0.0;
					for (Book book : books){
						if (message.getConversationId().equals(book.getTitle())){
							flag = true;
							maxPrice = book.getPrice();
						}
					}
					if (flag == true && Float.parseFloat(message.getContent()) <= maxPrice) {
						reply.setContent("interested");
						System.out.println(myAgent.getName() + " acepta pujar por " + message.getConversationId() + " por " + message.getContent());
					}
					else {
						reply.setContent("not interested");
						System.out.println(myAgent.getName() + " rechaza pujar por " + message.getConversationId() + " por " + message.getContent());
					}
				}
				else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					reply.setContent("Precio no válido");
				}
				reply.setSender(myAgent.getAID());
				myAgent.send(reply);
			}
			
			mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			message = myAgent.receive(mt);
			if (message != null) {
				//Current bidder won desired book
				if (books.contains(new Book(message.getConversationId()))) {
					books.remove(new Book(message.getConversationId()));
				}
				pujador.changeStatus(new Book(message.getConversationId()), "Adquirido");
			}
			else {
				block();
			}
			
			mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			message = myAgent.receive(mt);
			if (message != null) {
				if (books.contains(new Book(message.getConversationId()))) {
					pujador.changeStatus(new Book(message.getConversationId()), "No ganada");
				}
				System.out.println(myAgent.getName() + ": no he ganado la subasta de " + message.getConversationId());
			}
	}
}
