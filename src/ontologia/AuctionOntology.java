package ontologia;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

public class AuctionOntology extends Ontology {

	// Nome da ontoloxia
	public static final String ONTOLOGY_NAME = "Auction-Ontology";

	// Vocabulario
	public static final String BOOK = "Libro";
	public static final String TITLE = "title";
	public static final String PRICE = "price";
	public static final String INCREASE = "increase";
	public static final String WINNER = "winner";
	public static final String CFP = "CallForProposal";
	public static final String PROPOSE = "Propose";
	public static final String ANSWER = "answer";
	public static final String ACCEPT_PROPOSAL = "AcceptProposal";
	public static final String REJECT_PROPOSAL = "RejectProposal";
	public static final String REQUEST = "Request";
	public static final String INFORM = "Inform";

	// Instancia da ontoloxia
	private static AuctionOntology theInstance = new AuctionOntology();

	// Metodo para obter a referencia a ontoloxia (Singleton)
	public static AuctionOntology getInstance() {
		return theInstance;
	}

	private AuctionOntology() {
		// E unha ontoloxia basica
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
            // Engadimos os libros
			add(new ConceptSchema(BOOK), Libro.class);
			ConceptSchema cs = (ConceptSchema) getSchema(BOOK);
			cs.add(TITLE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(PRICE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
			cs.add(INCREASE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT),
					ObjectSchema.OPTIONAL);
			cs.add(WINNER, (PrimitiveSchema) getSchema(BasicOntology.STRING),
					ObjectSchema.OPTIONAL);

			add(new AgentActionSchema(CFP), CallForProposal.class);
			AgentActionSchema schema = (AgentActionSchema) getSchema(CFP);
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

			add(new AgentActionSchema(PROPOSE), Propose.class);
			schema = (AgentActionSchema) getSchema(PROPOSE);
			schema.add(ANSWER,
					(PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

			add(new AgentActionSchema(ACCEPT_PROPOSAL), AcceptProposal.class);
			schema = (AgentActionSchema) getSchema(ACCEPT_PROPOSAL);
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

			add(new AgentActionSchema(REJECT_PROPOSAL), RejectProposal.class);
			schema = (AgentActionSchema) getSchema(REJECT_PROPOSAL);
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

			add(new AgentActionSchema(REQUEST), Request.class);
			schema = (AgentActionSchema) getSchema(REQUEST);
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

			add(new AgentActionSchema(INFORM), Inform.class);
			schema = (AgentActionSchema) getSchema(INFORM);
			schema.add(BOOK, (ConceptSchema) getSchema(BOOK));

		} catch (OntologyException oe) {

			oe.printStackTrace();

		}

	}
}
