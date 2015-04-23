package pujador;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import book.Book;

@SuppressWarnings("serial")
public class PujadorGUI extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private ArrayList <Book> books;
	private TableModel model;

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
		
		table = new JTable();
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setBounds(33, 206, 366, -151);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(table);
	}
	
	public PujadorGUI(ArrayList<Book> books){
		this();
		this.books = books;
		model = new TableModel(books);
		table.setModel(model);
	}
}
