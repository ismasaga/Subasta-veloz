package subastador;

import jade.core.Agent;

import java.util.HashMap;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent{
	
	private Float incremento = (float) 5.0;
	private HashMap <String, Float> books;
	
	
	
	
	protected void setup() {
		books.put("Don Quijote", (float)20.0);
		books.put("Eragon", (float)15.0);
		books.put("El nombre de la rosa", (float)12.0);
		
		System.out.println("Hola! " + getAID().getName() + " está listo");
		for (String book : books.keySet()){
			addBehaviour(new SubastadorBehaviour(this, 10000, book, books.get(book), incremento));
		}
		
	}
	
	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}
}
