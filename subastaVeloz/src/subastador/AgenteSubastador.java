package subastador;

import jade.core.Agent;

import java.util.ArrayList;
import book.Book;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent{
	
	private ArrayList <Book> books;
	
	protected void setup() {
		books.add(new Book ("Don Quijote", (float)10.0, (float)2.0));
		
		System.out.println("Hola! " + getAID().getName() + " está listo");
		for (Book book : books){
			addBehaviour(new SubastadorBehaviour(this, 10000, book));
		}
	}
	
	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}
}
