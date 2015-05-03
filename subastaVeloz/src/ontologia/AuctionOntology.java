package ontologia;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

@SuppressWarnings("serial")
public class AuctionOntology extends Ontology {

	// The name identifying this ontology
	public static final String ONTOLOGY_NAME = "Auction-Ontology";

	// Vocabulary
	public static final String BOOK = "Book";
	public static final String TITLE = "title";
	public static final String PRICE = "price";
	public static final String INCREASE = "increase";
	public static final String WINNER = "winner";
	public static final String CFP = "CallForProposal";
	public static final String PROPOSE = "Propose";
	public static final String ANSWER = "answer";
	public static final String ACCEPT_PROPOSAL = "AcceptProposal";
	public static final String REJECT_PROPOSAL = "RejectProposal";

	// The singleton instance of this ontology
	private static AuctionOntology theInstance = new AuctionOntology();

	// This is the method to access the singleton auction ontology object
	public static AuctionOntology getInstance() {
		return theInstance;
	}

	private AuctionOntology() {

		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new ConceptSchema(BOOK), Book.class);
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

		} catch (OntologyException oe) {

			oe.printStackTrace();

		}

	}
}
