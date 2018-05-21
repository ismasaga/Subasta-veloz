package pujador;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ontologia.Libro;

public class PujadorGUI extends JFrame {

	private JPanel contentPane;
	private HashMap<Libro, String> books;
	private ModeloTabla model;
	private JTable table;
	private JLabel lblTtulo;
	private JTextField title;
	private JTextField maxPrice;
	private AgentePujador pujador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PujadorGUI frame = new PujadorGUI();
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
	public PujadorGUI() {

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pujador.takeDown();
			}
		});

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(61, 57, 313, 167);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		lblTtulo = new JLabel("T\u00EDtulo");
		lblTtulo.setBounds(10, 8, 44, 27);
		contentPane.add(lblTtulo);

		title = new JTextField();
		title.setBounds(46, 11, 86, 20);
		contentPane.add(title);
		title.setColumns(10);

		JLabel lblNewLabel = new JLabel("Precio M\u00E1ximo");
		lblNewLabel.setBounds(142, -4, 80, 50);
		contentPane.add(lblNewLabel);

		maxPrice = new JTextField();
		maxPrice.setBounds(218, 11, 86, 20);
		contentPane.add(maxPrice);
		maxPrice.setColumns(10);

		JButton add = new JButton("A\u00F1adir");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!title.getText().equals("")
						&& !maxPrice.getText().equals("")) {
					pujador.addBook(title.getText(),
							Float.parseFloat(maxPrice.getText()));
					title.setText("");
					maxPrice.setText("");
				}
			}
		});
		add.setBounds(335, 10, 89, 23);
		contentPane.add(add);
	}

	public PujadorGUI(HashMap<Libro, String> books, AgentePujador pujador) {
		this();
		this.books = books;
		this.pujador = pujador;
		model = new ModeloTabla(books);
		table.setModel(model);
		setTitle(pujador.getLocalName());
	}

	public ModeloTabla getModel() {
		return model;
	}
}
