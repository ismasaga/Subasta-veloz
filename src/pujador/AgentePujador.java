package pujador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;

import ontologia.AuctionOntology;
import ontologia.Libro;

public class AgentePujador extends Agent {

    private HashMap<Libro, String> books;
    private PujadorGUI pujadorGUI;
    private Ontology ontology = AuctionOntology.getInstance();
    private Codec codec = new SLCodec();

    public void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("puxador");
        sd.setName("JADE-book-trading");
        dfd.addServices(sd);
        books = new HashMap<>();
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.out.println(e.getMessage());
        }

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        for (Libro libro : books.keySet()) {
            System.out.println("Pujando por " + libro.getTitle()
                    + " por un máximo de " + libro.getPrice() + " euros");
        }
        addBehaviour(new PujadorBehaviour(this, books));
        pujadorGUI = new PujadorGUI(books, this);
        pujadorGUI.setVisible(true);

    }

    public void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(getAID().getName() + " ha terminado");
    }

    public void changeStatus(Libro libro, String status) {
        if (books.containsKey(libro) && books.get(libro) != "Adquirido") {
            books.put(libro, status);
            pujadorGUI.getModel().changeStatus(libro, status);
        }
    }

    public void addBook(String title, Float maxPrice) {
        Libro libro = new Libro(title, maxPrice);
        books.put(libro, "Esperando subasta");
        pujadorGUI.getModel().changeStatus(libro, books.get(libro));
    }

    public HashMap<Libro, String> getBooks() {
        return books;
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntology() {
        return ontology;
    }
}
