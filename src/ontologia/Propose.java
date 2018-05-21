package ontologia;

import jade.content.AgentAction;

public class Propose implements AgentAction {

    private Boolean answer;
    private Libro libro;

    public Propose() {
    }

    public Propose(Boolean answer, Libro libro) {
        this.answer = new Boolean(answer);
        this.libro = libro;
    }

    public Libro getLibro() {
        return libro;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

}
