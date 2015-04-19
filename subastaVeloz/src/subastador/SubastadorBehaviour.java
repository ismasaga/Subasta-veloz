package subastador;

import jade.core.AID;
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
	
	private Float price;

	public SubastadorBehaviour(Agent a, int period, float price) {
		super(a, period);
		this.price = price;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <AID> buyerAgents;

	@Override
	protected void onTick() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		template.addServices(sd);
		boolean haveWinner = false;
		AID winner = null;
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
				message.setConversationId("book-trade");
				message.setReplyWith("interested" + System.currentTimeMillis());
				myAgent.send(message);
				MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), MessageTemplate.MatchInReplyTo(message.getReplyWith()));
				
				int replyCount = 0; 
				
				do {
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null) {
						String content = reply.getContent();
						if (!haveWinner && content.equals("interested")) {
							haveWinner = true;
							winner = reply.getSender();
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
				message.setContent("Ronda ganada. Precio: " + price + " euros");
				message.setConversationId("book-trade");
				myAgent.send(message);
				
				message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
				message.setSender(myAgent.getAID());
				message.setContent("No has ganado esta ronda");
				message.setConversationId("book-trade");
				for (AID buyer : buyerAgents) {
					if (!buyer.equals(winner)){
						message.addReceiver(buyer);
					}
				}
				myAgent.send(message);
			}	
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
