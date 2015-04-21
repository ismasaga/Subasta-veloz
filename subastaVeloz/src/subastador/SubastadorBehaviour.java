package subastador;

import jade.core.AID;
import book.Book;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class SubastadorBehaviour extends TickerBehaviour {
	
	private String book;
	private Float price;
	private Float increase;

	public SubastadorBehaviour(Agent a, int period, Book book, Float increase) {
		super(a, period);
		this.book = book.getTitle();
		this.price = book.getPrice();
		this.increase = increase;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <AID> buyerAgents;
	private AID winner;

	@Override
	protected void onTick() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		template.addServices(sd);
		boolean haveWinner = false;
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			System.out.println("BUSQUEDA REALIZADA"); 
			buyerAgents = new ArrayList<>();
			for (DFAgentDescription ad : result) {
				buyerAgents.add(ad.getName());
			}
			
			if (!buyerAgents.isEmpty()){
				ACLMessage message = new ACLMessage(ACLMessage.CFP);
				message.setSender(myAgent.getAID());
				message.setContent(String.valueOf(price));
				for (AID buyer : buyerAgents) {
					message.addReceiver(buyer);
				}
				message.setConversationId(book);
				message.setReplyWith("interested" + System.currentTimeMillis());
				myAgent.send(message);
				MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId(book), MessageTemplate.MatchInReplyTo(message.getReplyWith()));
				
				int replyCount = 0; 
				int interestedCount = 0;
				
				do {
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null) {
						String content = reply.getContent();
						if (content.equals("interested")){
							if (!haveWinner) {
								haveWinner = true;
								winner = reply.getSender();
							}
							System.out.println(reply.getSender().getName() + "acepta pujar por " + message.getConversationId() + " por " + price + " euros");
							interestedCount ++;
						}
						replyCount ++;
					}
					else {
						block();
					}
				} while (replyCount < buyerAgents.size());
				
				
				message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				message.setSender(myAgent.getAID());
				message.addReceiver(winner);
				message.setContent("Ronda ganada de " + book + ". Precio: " + price + " euros");
				message.setConversationId(book);
				myAgent.send(message);
				
				message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
				message.setSender(myAgent.getAID());
				message.setContent("No has ganado esta ronda de " + book);
				message.setConversationId(book);
				for (AID buyer : buyerAgents) {
					if (!buyer.equals(winner)){
						message.addReceiver(buyer);
					}
				}
				myAgent.send(message);
				
				boolean ended = false;
				
				if (interestedCount == 0) {
					//No hay interesados en la ronda actual
					if (winner != null) {
						System.out.println("Subasta de " + book + " ganada por " + winner.getLocalName() + " por haber ganado la ronda anterior");
						ended = true;
					}
				}
				else if (interestedCount == 1) {
					//Sólo hay un interesado en la ronda actual
					System.out.println("Subasta de " + book + " ganada por " + winner.getLocalName() + " por ser el único interesado en la ronda actual");
					ended = true;
				}
				else {
					//hay más interesados en la ronda actual
					price += increase;
				}
				
				if (ended == true) {
					/*Informar a todos los participantes que no han ganado la subasta*/
					message = new ACLMessage(ACLMessage.INFORM);
					message.setSender(myAgent.getAID());
					message.setConversationId(book);
					message.setContent("Subasta de " + book + " finalizada. El libro se vende por " + String.valueOf(price));
					for (AID buyer : buyerAgents) {
						if (!buyer.equals(winner)) {
							message.addReceiver(buyer);
						}
					}
					myAgent.send(message);
					
					/***************************************************************/
					
					/*Solicitar pago al ganador de la subasta*/
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setSender(myAgent.getAID());
					message.setConversationId(book);
					message.setContent("Has ganado la subasta de " + book + " por " + String.valueOf(price) + " por favor, realiza el pago");
					message.addReceiver(winner);
					myAgent.send(message);
					/******************************************/
					this.stop();
				}
			}	
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
