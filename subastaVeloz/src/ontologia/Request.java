package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class Request implements AgentAction {

	private Book book;

	public Request() {

	}

	public Request(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
