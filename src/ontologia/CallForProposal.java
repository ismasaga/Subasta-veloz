package ontologia;

import jade.content.AgentAction;

public class CallForProposal implements AgentAction {

    private Libro libro;

    public CallForProposal() {
    }

    public CallForProposal(Libro libro) {
        this.libro = libro;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
