package ontologia;

import jade.content.AgentAction;

public class Inform implements AgentAction {

    private Libro libro;

    public Inform() {
    }

    public Inform(Libro libro) {
        this.libro = libro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

}
