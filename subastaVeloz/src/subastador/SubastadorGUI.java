package subastador;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ontologia.Book;

@SuppressWarnings("serial")
public class SubastadorGUI extends JFrame {

	private JPanel contentPane;
	private JTextField titulo;
	private JTextField precio;
	private JTextField incremento;
	private AgenteSubastador subastador;
	private JLabel lblTitulo;
	private ModeloTabla model;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubastadorGUI frame = new SubastadorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SubastadorGUI() {

		setTitle("Agente Subastador");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(10, 50, 41, 14);
		getContentPane().add(lblPrecio);

		precio = new JTextField();
		precio.setBounds(49, 47, 41, 20);
		getContentPane().add(precio);
		precio.setColumns(10);

		JLabel lblIncremento = new JLabel("Incremento:");
		lblIncremento.setBounds(153, 50, 64, 14);
		getContentPane().add(lblIncremento);

		incremento = new JTextField();
		incremento.setColumns(10);
		incremento.setBounds(220, 47, 41, 20);
		getContentPane().add(incremento);

		JButton btnAadir = new JButton("A\u00F1adir");
		btnAadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (titulo.getText() != "" && precio.getText() != ""
						&& incremento.getText() != "") {
					Book book = new Book(titulo.getText(), Float
							.parseFloat(precio.getText()), Float
							.parseFloat(incremento.getText()), "En curso");
					subastador.AddBook(book);
					titulo.setText("");
					precio.setText("");
					incremento.setText("");
				}
			}
		});
		btnAadir.setBounds(335, 46, 89, 23);
		getContentPane().add(btnAadir);

		lblTitulo = new JLabel("Titulo:");
		lblTitulo.setBounds(10, 22, 46, 14);
		contentPane.add(lblTitulo);

		titulo = new JTextField();
		titulo.setBounds(49, 19, 375, 20);
		contentPane.add(titulo);
		titulo.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 86, 414, 164);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

	}

	public SubastadorGUI(ArrayList<Book> books, AgenteSubastador subastador) {
		this();
		this.subastador = subastador;
		model = new ModeloTabla(books);
		table.setModel(model);
	}

	public ModeloTabla getModel() {
		return this.model;
	}
}
