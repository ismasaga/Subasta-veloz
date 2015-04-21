package subastador;

import jade.core.Agent;

import java.util.ArrayList;
import book.Book;

@SuppressWarnings("serial")
public class AgenteSubastador extends Agent{
	
	private ArrayList <Book> books;
	private Float increase = (float)5.0;
	protected void setup() {
		books = new ArrayList<>();
		books.add(new Book ("Don Quijote", (float)10.0));
		books.add(new Book ("El perfume", (float)12.0));
		books.add(new Book ("El nombre de la rosa", (float)15.0));
		
		System.out.println("Hola! " + getAID().getName() + " está listo");
		for (Book book : books){
			addBehaviour(new SubastadorBehaviour(this, 10000, book, increase));
		}
	}
	
	protected void takeDown() {
		System.out.println(getAID().getName() + " ha terminado");
	}
}
