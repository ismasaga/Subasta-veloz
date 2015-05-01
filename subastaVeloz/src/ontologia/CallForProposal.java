package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class CallForProposal implements AgentAction {

	private Book book;

	public CallForProposal() {

	}

	public CallForProposal(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
