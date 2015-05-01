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
	public static final String BOOK = "book";
	public static final String TITLE = "title";
	public static final String PRICE = "price";
	public static final String INCREASE = "increase";
	public static final String WINNER = "winner";
	public static final String CFP = "CallForProposal";

	// The singleton instance of this ontology
	private static AuctionOntology theInstance = new AuctionOntology();

	// This is the method to access the singleton music shop ontology object
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
		} catch (OntologyException oe) {

			oe.printStackTrace();

		}

	}
}
