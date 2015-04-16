package subastador;

import jade.core.Agent;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent{
	protected void setup() {
		System.out.println("Hola! " + getAID().getName() + " está listo");
		addBehaviour(new SubastadorBehaviour(this, 10000));
	}
	
	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}
}
