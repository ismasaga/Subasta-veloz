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
import java.util.Collections;

import ontologia.AcceptProposal;
import ontologia.Book;
import ontologia.CallForProposal;
import ontologia.Propose;
import ontologia.RejectProposal;
import ontologia.Request;

public class SubastadorBehaviour extends TickerBehaviour {

	private Book book;
	private static final long serialVersionUID = 1L;
	private ArrayList<AID> buyerAgents;
	private AID winner;
	private AgenteSubastador subastador;

	public SubastadorBehaviour(Agent a, int period, Book book) {
		super(a, period);
		this.book = book;
		this.subastador = (AgenteSubastador) a;
	}

	/**
	 * 
	 */

	@Override
	protected void onTick() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-buying");
		template.addServices(sd);
		boolean haveWinner = false;
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			System.out.println("BUSQUEDA REALIZADA");
			buyerAgents = new ArrayList<>();
			for (DFAgentDescription ad : result) {
				buyerAgents.add(ad.getName());
			}

			if (!buyerAgents.isEmpty()) {
				Collections.shuffle(buyerAgents);
				CallForProposal cfp = new CallForProposal(book);

				ACLMessage message = new ACLMessage(ACLMessage.CFP);
				message.setOntology(subastador.getOntology().getName());
				message.setLanguage(subastador.getCodec().getName());
				try {
					myAgent.getContentManager().fillContent(message,
							new Action(myAgent.getAID(), cfp));
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
				message.setSender(myAgent.getAID());
				for (AID buyer : buyerAgents) {
					message.addReceiver(buyer);
				}
				message.setConversationId(book.getTitle());
				message.setReplyWith("interested" + System.currentTimeMillis());
				myAgent.send(message);
				MessageTemplate mt = MessageTemplate.and(
						MessageTemplate.MatchConversationId(book.getTitle()),
						MessageTemplate.MatchInReplyTo(message.getReplyWith()));
				mt = MessageTemplate.and(mt, MessageTemplate
						.MatchOntology(subastador.getOntology().getName()));

				int replyCount = 0;
				int interestedCount = 0;

				do {
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null) {
						Action action = null;
						try {
							action = (Action) subastador.getContentManager()
									.extractContent(reply);
						} catch (CodecException | OntologyException e) {
							e.printStackTrace();
						}
						Propose propose = (Propose) action.getAction();

						assert propose.getAnswer() != null : " propose.getAnswer() returns null ";

						if (propose.getAnswer() == true) {
							if (!haveWinner) {
								haveWinner = true;
								winner = action.getActor();
							}
							System.out.println(action.getActor().getLocalName()
									+ "acepta pujar por "
									+ message.getConversationId() + " por "
									+ book.getPrice() + " euros");
							interestedCount++;
						}
						replyCount++;
					} else {
						block();
					}
				} while (replyCount < buyerAgents.size());

				AcceptProposal acceptPropose = new AcceptProposal(book);
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
				message.setConversationId(book.getTitle());
				myAgent.send(message);

				RejectProposal rejectProposal = new RejectProposal(book);
				message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
				message.setOntology(subastador.getOntology().getName());
				message.setLanguage(subastador.getCodec().getName());
				message.setSender(myAgent.getAID());
				message.setConversationId(book.getTitle());
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
						System.out.println("Subasta de " + book
								+ " ganada por " + winner.getLocalName()
								+ " por haber ganado la ronda anterior");
						ended = true;
					}
				} else if (interestedCount == 1) {
					// Sólo hay un interesado en la ronda actual
					System.out
							.println("Subasta de "
									+ book
									+ " ganada por "
									+ winner.getLocalName()
									+ " por ser el único interesado en la ronda actual");
					ended = true;
				} else {
					// hay más interesados en la ronda actual
					book.updatePrice();
					subastador.updatePrice(book);
				}

				if (ended == true) {
					/*
					 * Informar a todos los participantes que no han ganado la
					 * subasta
					 */
					message = new ACLMessage(ACLMessage.INFORM);
					message.setSender(myAgent.getAID());
					message.setConversationId(book.getTitle());
					message.setContent("Subasta de " + book
							+ " finalizada. El libro se vende por "
							+ String.valueOf(book.getPrice()));
					for (AID buyer : buyerAgents) {
						if (!buyer.equals(winner)) {
							message.addReceiver(buyer);
						}
					}
					myAgent.send(message);

					/***************************************************************/

					/* Solicitar pago al ganador de la subasta */
					Request request = new Request(book);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setOntology(subastador.getOntology().getName());
					message.setLanguage(subastador.getCodec().getName());
					message.setSender(myAgent.getAID());
					message.setConversationId(book.getTitle());
					message.addReceiver(winner);
					try {
						myAgent.getContentManager().fillContent(message,
								new Action(myAgent.getAID(), request));
					} catch (CodecException | OntologyException e) {
						e.printStackTrace();
					}
					myAgent.send(message);
					/******************************************/

					subastador.updateWinner(book, winner.getLocalName());
					this.stop();
				}
			}
		} catch (FIPAException e) {
			System.out.println(e.getMessage());
		}

	}
}
