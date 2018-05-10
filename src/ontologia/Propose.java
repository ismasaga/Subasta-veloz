package ontologia;

import jade.content.AgentAction;

@SuppressWarnings("serial")
public class Propose implements AgentAction {

	private Boolean answer;
	private Book book;

	public Propose() {

	}

	public Propose(Boolean answer, Book book) {
		this.answer = new Boolean(answer);
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public Boolean getAnswer() {
		return answer;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setAnswer(Boolean answer) {
		this.answer = answer;
	}

}
