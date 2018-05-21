package subastador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

import java.util.ArrayList;

import ontologia.AuctionOntology;
import ontologia.Libro;

//********************************AXENTE*******************************
public class AgenteSubastador extends Agent {
    private ArrayList<Libro> libros;
    private ArrayList<SubastadorBehaviour> listaComprotamentos;
    private SubastadorGUI subastadorGUI;
    // Usase o codec SL e non o LEAP para usar codificacion en strings e non en byte-encoded
    private Codec codec = new SLCodec();
    private Ontology ontology = AuctionOntology.getInstance();

    // METODO QUE FAI AS INICIALIZACIONS DO AXENTE
    protected void setup() {
        SubastadorBehaviour comportamento;

        // Informase de que non se manexan os argumentos
        if (getArguments().length > 0) {
            System.out.println("ERRO no subastador, os argumentos non seran tidos en conta!!");
        }
        // Crease a lista coas subastas (libros)
        libros = new ArrayList<>();
        libros.add(new Libro("Don Quijote", (float) 10.0, (float) 2.0, "En curso"));
        //libros.add(new Libro("El perfume", (float) 12.0, (float) 2.0, "En curso"));
        //libros.add(new Libro("El nombre de la rosa", (float) 15.0, (float) 2.0, "En curso"));
        // Informase de que o subastador se iniciou correctamente
        System.out.println("Subastador: " + getAID().getLocalName() + " iniciandose....");
        // Mostrase a interfaz grafica
		subastadorGUI = new SubastadorGUI(libros, this);
		subastadorGUI.setVisible(true);
		// Definese o codec a usar e a ontoloxia rexistrandoos no content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        // Engadese un comportamento por libro e gardamos a referencia
        for (Libro libro : libros) {
            comportamento = new SubastadorBehaviour(this, 10000, libro);
            //listaComprotamentos.add(comportamento);
            addBehaviour(comportamento);
        }
        // Terminase a execucion
        //doDelete();
    }

    // METODO QUE SE EXECUTA XUSTO ANTES DE QUE O AXENTE SE CERRE
    protected void takeDown() {
        // Eliminanse os comportamentos que queden
        /*for (SubastadorBehaviour comportamento : listaComprotamentos) {
            removeBehaviour(comportamento);
        }*/
        // Informase do fin do axente
        System.out.println("Subastador: " + getAID().getLocalName() + " finalizado");
    }

    public void AddBook(Libro libro) {
        boolean repe = false;
        for (Libro b : libros) {
            // Se o libro xa estaba antes
            if (b.getTitle().equals(libro.getTitle())) {
                // Marcamos como repetido
                repe = true;
                System.err.println("O libro xa existia previamente");
            }
        }
        // Se o libro non existia previamente
        if (!repe) {
            // Engadimolo na lista
            libros.add(libro);
            // Engadimos o comportamento que o trata
            addBehaviour(new SubastadorBehaviour(this, 10000, libro));
        }
    }

    public ArrayList<Libro> getListaSubastas() {
        return this.libros;
    }

    public void updatePrice(Libro libro) {
        subastadorGUI.getModel().changeStatus(libro);
    }

    public void updateWinner(Libro libro, String winner) {
        int i = 0;
        int temp = 0;
        for (Libro b : libros) {
            if (b.equals(libro)) {
                temp = i;
            }
            i++;
        }
        libros.get(temp).setWinner(winner);
        subastadorGUI.getModel().changeStatus(libro);
    }

    public Ontology getOntology() {
        return ontology;
    }

    public Codec getCodec() {
        return codec;
    }

}
