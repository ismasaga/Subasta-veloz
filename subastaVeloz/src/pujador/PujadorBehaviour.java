package pujador;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	private Float maxPrice;

	public PujadorBehaviour(Float maxPrice) {
		super();
		this.maxPrice = maxPrice;
	}


	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage message = null;
		ACLMessage reply = null;
			message = myAgent.receive(mt);
			if (message != null) {
				Float price = Float.parseFloat(message.getContent());
				reply = message.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setConversationId("Don Quijote");
				if (price != null && price > 0){
					if (price <= maxPrice) {
						reply.setContent("interested");
					}
					else {
						reply.setContent("not interested");
						myAgent.doDelete();
					}
				}
				else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					reply.setContent("Precio no válido");
				}
				reply.setSender(myAgent.getAID());
				myAgent.send(reply);	
			}
			else {
				block();
			}
		
	}
}
