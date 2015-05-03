package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class Inform implements AgentAction {

	private Book book;

	public Inform() {

	}

	public Inform(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
