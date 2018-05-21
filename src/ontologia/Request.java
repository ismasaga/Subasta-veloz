package ontologia;

import jade.content.AgentAction;

public class Request implements AgentAction {

    private Libro libro;

    public Request() {
    }

    public Request(Libro libro) {
        this.libro = libro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

}
