package pujador;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class PujadorBehaviour extends CyclicBehaviour {

	
	
	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null) {
			String content = message.getContent();
			System.out.println(content);
		}
	}
}
