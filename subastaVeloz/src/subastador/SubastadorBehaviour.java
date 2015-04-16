package subastador;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SubastadorBehaviour extends TickerBehaviour {

	public SubastadorBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void onTick() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			AID[] buyerAgents = new AID[result.length];
			int i = 0;
			for (DFAgentDescription ad : result) {
				buyerAgents[i] = ad.getName();
				i++;
			}
			ACLMessage message = new ACLMessage(ACLMessage.CFP);
			message.setContent("holi");
			for (AID buyer : buyerAgents) {
				message.addReceiver(buyer);
			}
			myAgent.send(message);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
	}
}
