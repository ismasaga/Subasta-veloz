package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class AcceptProposal implements AgentAction {

	private Book book;

	public AcceptProposal() {

	}

	public AcceptProposal(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
