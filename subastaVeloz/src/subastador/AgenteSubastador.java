package subastador;

import jade.core.Agent;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent{
	
	private Float price = (float) 30.0;
	protected void setup() {
		System.out.println("Hola! " + getAID().getName() + " está listo");
		addBehaviour(new SubastadorBehaviour(this, 10000, price));
	}
	
	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}
}
