package ontologia;

import jade.content.AgentAction;

public class RejectProposal implements AgentAction {

    private Libro libro;

    public RejectProposal() {
    }

    public RejectProposal(Libro libro) {
        this.libro = libro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}