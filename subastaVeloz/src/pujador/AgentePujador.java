package pujador;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class AgentePujador extends Agent{

	private Float maxPrice;
	
	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}
		Object[] args = getArguments();
		if (args != null && args.length == 2) {
			maxPrice = Float.parseFloat((String) args[1]);
			System.out.println(String.valueOf(maxPrice));
		}
		else {
			System.out.println("Número incorrecto de argumentos");
		}
		addBehaviour(new PujadorBehaviour(maxPrice));
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
