package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class RejectProposal implements AgentAction {

	private Book book;

	public RejectProposal() {

	}

	public RejectProposal(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}