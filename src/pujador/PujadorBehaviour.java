package pujador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;

import ontologia.*;

public class PujadorBehaviour extends CyclicBehaviour {

    private HashMap<Libro, String> books;
    private AgentePujador pujador;

    public PujadorBehaviour(AgentePujador pujador, HashMap<Libro, String> books) {
        super();
        this.books = new HashMap<>(books);
        this.pujador = pujador;
    }

    @Override
    public void action() {
        Action action = null;
        this.books = new HashMap<>(pujador.getBooks());
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.MatchOntology(pujador.getOntology().getName()));
        ACLMessage message = null;
        ACLMessage reply = null;
        Float maxPrice = (float) 0.0;
        String estado = "Adquirido";

        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) pujador.getContentManager().extractContent(message);
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            CallForProposal cfp = (CallForProposal) action.getAction();
            String title = cfp.getLibro().getTitle();
            Float price = cfp.getLibro().getPrice();
            reply = message.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            reply.setOntology(pujador.getOntology().getName());
            reply.setLanguage(pujador.getCodec().getName());

            Propose proposal = new Propose();
            proposal.setLibro(cfp.getLibro());
            if (price != null && price > 0) {
                pujador.changeStatus(new Libro(title), "En curso");
                maxPrice = (float) 0.0;
                Boolean flag = false;
                for (Libro libro : books.keySet()) {
                    if (libro.getTitle().equals(title)) {
                        maxPrice = libro.getPrice();
                        estado = books.get(libro);
                        flag = true;
                    }
                }
                if (flag == true && price <= maxPrice && estado != "Adquirido") {
                    proposal.setAnswer(new Boolean(true));
                    System.out.println(myAgent.getName()
                            + ": acepto pujar por " + title + " por " + price);
                } else {
                    proposal.setAnswer(new Boolean(false));
                    System.out.println(myAgent.getName()
                            + ": rechazo pujar por " + title + " por " + price);
                    pujador.changeStatus(cfp.getLibro(), "Precio superado");
                }

            } else {
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("Precio no válido");
            }
            try {
                pujador.getContentManager().fillContent(reply,
                        new Action(myAgent.getAID(), proposal));
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            reply.setSender(myAgent.getAID());
            myAgent.send(reply);
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) pujador.getContentManager().extractContent(
                        message);
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            AcceptProposal acceptProposal = (AcceptProposal) action.getAction();
            Libro libro = acceptProposal.getLibro();
            pujador.changeStatus(libro, "Ronda ganada");
        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) pujador.getContentManager().extractContent(
                        message);
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            RejectProposal rejectProposal = (RejectProposal) action.getAction();
            Libro libro = rejectProposal.getLibro();
            pujador.changeStatus(libro, "Ronda no ganada");
        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        message = myAgent.receive(mt);
        if (message != null) {
            // Current bidder won desired book
            try {
                action = (Action) pujador.getContentManager().extractContent(
                        message);
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            Request request = (Request) action.getAction();
            if (books.keySet().contains(request.getLibro())) {
                books.remove(request.getLibro());
            }
            pujador.changeStatus(request.getLibro(), "Adquirido");
        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) pujador.getContentManager().extractContent(
                        message);
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            Inform inform = (Inform) action.getAction();
            if (books.keySet().contains(inform.getLibro())) {
                pujador.changeStatus(new Libro(message.getConversationId()),
                        "No ganada");
            }
            System.out.println(myAgent.getName()
                    + ": no he ganado la subasta de "
                    + message.getConversationId());
        }
    }
}
