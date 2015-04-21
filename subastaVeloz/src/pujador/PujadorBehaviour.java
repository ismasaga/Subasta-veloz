package pujador;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

import book.Book;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	private ArrayList <Book> books;

	public PujadorBehaviour(ArrayList <Book>books) {
		super();
		this.books = books;
	}


	@Override
	public void action() {
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
						books.remove(new Book(message.getConversationId()));
					}
				}
				else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					reply.setContent("Precio no válido");
				}
				reply.setSender(myAgent.getAID());
				myAgent.send(reply);
			}
			if (books.isEmpty()){
				myAgent.doDelete();
			}
		//TODO kill agent if no more books to buy
	}
}
