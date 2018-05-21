package subastador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Iterator;

import ontologia.*;

public class SubastadorBehaviour extends TickerBehaviour {

    private Libro libro;
    private static final long serialVersionUID = 1L;
    private ArrayList<AID> buyerAgents;
    private AID winner;
    private AgenteSubastador subastador;

    public SubastadorBehaviour(Agent agent, int period, Libro libro) {
        // Pasaselle o punteiro do axente que executa o comportamento a clase pai xuto co intervalo no que se executara
        super(agent, period);
        this.libro = libro;
        this.subastador = (AgenteSubastador) agent;
    }

    // Metodo que se autoexecuta cada intervalo de tempo (period)
    @Override
    protected void onTick() {
        ACLMessage msg, message;
        MessageTemplate mt;
        DFAgentDescription pantillaAxente;
        ServiceDescription sd;
        DFAgentDescription[] result = new DFAgentDescription[0];
        boolean haveWinner = false;
        int replyCount = 0;
        int interestedCount = 0;
        Action action = null;


        // Definimos un axente a buscar (non fai falla nome)
        pantillaAxente = new DFAgentDescription();
        // Definimos un servicio a buscar
        sd = new ServiceDescription();
        // Daselle tipo ao servicio
        sd.setType("puxador");
        // Engadese o servicio ao axente definido
        pantillaAxente.addServices(sd);
        try {
            // Buscanse todos os axentes que concordan coa plantilla de busca
            result = DFService.search(myAgent, pantillaAxente);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        // Imprimense todos os puxadores atopados cos servizos que ofrecen
        System.out.println("BUSQUEDA REALIZADA\n\tOs puxadores atopados foron:");
        buyerAgents = new ArrayList<>();
        for (DFAgentDescription ad : result) {
            // Metese nunha lista os AID dos axentes atopados (para engadir como receptores das nosas mensaxes)
            buyerAgents.add(ad.getName());
            System.out.println("\t\t - " + ad.getName().getLocalName() + " ofrece os servizos:");
            Iterator servicios = ad.getAllServices();
            while (servicios.hasNext()) {
                sd = (ServiceDescription) servicios.next();
                System.out.println("\t\t\t - Servicio de tipo: '" + sd.getType() + "' con descripcion: '" + sd.getName() + "'");
            }
        }
        // Si hay compradores
        if (!buyerAgents.isEmpty()) {
            System.out.println(); // Mellora a visibilidade dos datos en consola
            // Por si se quere que o ganhador sexa aleatorio no caso de que queden varios
            //Collections.shuffle(buyerAgents);
            // Instanciamos este obxeto para
            CallForProposal cfp = new CallForProposal(libro);
            // Instanciase unha mensaxe de tipo CFP
            message = new ACLMessage(ACLMessage.CFP);
            // Meteselle a ontoloxia e a linguaxe que se vai usar nesta mensaxe
            message.setOntology(subastador.getOntology().getName());
            message.setLanguage(subastador.getCodec().getName());
//
            try {
                myAgent.getContentManager().fillContent(message, new Action(myAgent.getAID(), cfp));
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            // Isto xa o fai o send(), non fai falla
            //message.setSender(myAgent.getAID());
            // Engadense como receptores da mensaxe a todos os puxadores
            for (AID buyer : buyerAgents) {
                message.addReceiver(buyer);
            }
            // Metese de identificador da mensaxe o titulo do libro
            message.setConversationId(libro.getTitle());
            // O receptor tera que responder con este texto
            message.setReplyWith("interested" + System.currentTimeMillis());

            // Enviase a mensaxe engadindo o valor correspondente ao sender da mensaxe (campo interno)
            myAgent.send(message);

// Preparase un filtro para seleccionar as mensaxes da cola
            mt = MessageTemplate.and(MessageTemplate.MatchConversationId(libro.getTitle()),
                    MessageTemplate.MatchInReplyTo(message.getReplyWith()));
            mt = MessageTemplate.and(mt, MessageTemplate
                    .MatchOntology(subastador.getOntology().getName()));
            do {
                // Collese a seguinte mensaxe, previamente filtrada, da cola (FIFO) e xa se elimina dela
                msg = myAgent.receive(mt);
                // Si hay mensaxes na cola
                if (msg != null) {
                    try {
                        // Extraemos a accion
                        action = (Action) subastador.getContentManager().extractContent(msg);
                    } catch (CodecException | OntologyException e) {
                        e.printStackTrace();
                    }
                    // Extraemos a proposta
                    Propose propose = (Propose) action.getAction();
// Para executar co -ea comprobando se o parametro e null
//assert propose.getAnswer() != null : " propose.getAnswer() returns null ";
                    // Se a proposta foi aceptada
                    if (propose.getAnswer() == true) {
                        // S inda non temos posible ganhador
                        if (!haveWinner) {
                            // Marcamolo como posible
                            haveWinner = true;
                            // Gardamolo como tal
                            winner = action.getActor();
                        }
                        // Imprimese info para validar que se puxou
                        System.out.println(action.getActor().getLocalName()
                                + "acepta pujar por "
                                + message.getConversationId() + " por "
                                + libro.getPrice() + " euros");
                        // Incrementamos o contador de interesados
                        interestedCount++;
                    }
                    // De todas formas marcamos o posible interesado como xa revisado
                    replyCount++;
                }
                // Mentres non se revisen as mensaxes de todos os posibles interesados
            } while (replyCount < buyerAgents.size());

            AcceptProposal acceptPropose = new AcceptProposal(libro);
            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            message.setOntology(subastador.getOntology().getName());
            message.setLanguage(subastador.getCodec().getName());
            message.setSender(myAgent.getAID());
            message.addReceiver(winner);
            try {
                myAgent.getContentManager().fillContent(message,
                        new Action(myAgent.getAID(), acceptPropose));
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            message.setConversationId(libro.getTitle());
            myAgent.send(message);

            RejectProposal rejectProposal = new RejectProposal(libro);
            message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
            message.setOntology(subastador.getOntology().getName());
            message.setLanguage(subastador.getCodec().getName());
            message.setSender(myAgent.getAID());
            message.setConversationId(libro.getTitle());
            for (AID buyer : buyerAgents) {
                if (!buyer.equals(winner)) {
                    message.addReceiver(buyer);
                }
            }
            try {
                myAgent.getContentManager().fillContent(message,
                        new Action(myAgent.getAID(), rejectProposal));
            } catch (CodecException | OntologyException e) {
                e.printStackTrace();
            }
            myAgent.send(message);

            boolean ended = false;

            if (interestedCount == 0) {
                // No hay interesados en la ronda actual
                if (winner != null) {
                    System.out.println("Subasta de " + libro
                            + " ganada por " + winner.getLocalName()
                            + " por haber ganado la ronda anterior");
                    ended = true;
                }
            } else if (interestedCount == 1) {
                // Sólo hay un interesado en la ronda actual
                System.out
                        .println("Subasta de "
                                + libro
                                + " ganada por "
                                + winner.getLocalName()
                                + " por ser el único interesado en la ronda actual");
                ended = true;
            } else {
                // hay más interesados en la ronda actual
                libro.updatePrice();
                subastador.updatePrice(libro);
            }

            if (ended == true) {
                //Informar a todos los participantes que no han ganado la subasta
                Inform inform = new Inform(libro);
                message = new ACLMessage(ACLMessage.INFORM);
                message.setOntology(subastador.getOntology().getName());
                message.setLanguage(subastador.getCodec().getName());
                message.setSender(myAgent.getAID());
                message.setConversationId(libro.getTitle());
                for (AID buyer : buyerAgents) {
                    if (!buyer.equals(winner)) {
                        message.addReceiver(buyer);
                    }
                }
                try {
                    myAgent.getContentManager().fillContent(message,
                            new Action(myAgent.getAID(), inform));
                } catch (CodecException | OntologyException e) {
                    e.printStackTrace();
                }
                myAgent.send(message);

                // Solicitar pago al ganador de la subasta
                Request request = new Request(libro);
                message = new ACLMessage(ACLMessage.REQUEST);
                message.setOntology(subastador.getOntology().getName());
                message.setLanguage(subastador.getCodec().getName());
                message.setSender(myAgent.getAID());
                message.setConversationId(libro.getTitle());
                message.addReceiver(winner);
                try {
                    myAgent.getContentManager().fillContent(message,
                            new Action(myAgent.getAID(), request));
                } catch (CodecException | OntologyException e) {
                    e.printStackTrace();
                }
                myAgent.send(message);

                subastador.updateWinner(libro, winner.getLocalName());
                // Parase a execucion do comportamento
                this.stop();
            }
        } else {
            // Informase de que non hay puxadores
            System.out.println("\t\t - Non hay puxadores neste momento");
            // Non fai falla bloquear o comportamento xa que este tipo executase cada x tempo,
            // non seguido, e dicir, non desperdiciamos CPU igualmente
        }
    }
}
