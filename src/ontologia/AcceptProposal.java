package ontologia;

import jade.content.AgentAction;

public class AcceptProposal implements AgentAction {

    private Libro libro;

    public AcceptProposal() {
    }

    public AcceptProposal(Libro libro) {
        this.libro = libro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
