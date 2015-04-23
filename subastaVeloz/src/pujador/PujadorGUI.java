package pujador;

import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import book.Book;

@SuppressWarnings("serial")
public class PujadorGUI extends JFrame {

	private JPanel contentPane;
	private HashMap <Book, String> books;
	private ModeloTabla model;
	private JTable table;

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
		setTitle("Agente Pujador");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(70, 101, 304, 123);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	public PujadorGUI(HashMap<Book, String> books){
		this();
		this.books = books;
		model = new ModeloTabla(books);
		table.setModel(model);
	}
	
	public ModeloTabla getModel() {
		return model;
	}
}
