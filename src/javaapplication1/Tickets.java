package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;
	String curUser;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public Tickets(Boolean isAdmin, String usid) { //UserID is passed to Ticket when logging in. Keeps track of current User.

		chkIfAdmin = isAdmin;
		curUser = usid; // Assign the userid to any request they make.
		createMenu();
		prepareGUI();
		
		try {

			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(TicketsJTable.buildTableModel(dao.readTickets(curUser)));
				jt.setBounds(30, 40, 200, 400);
				jt.setBackground(Color.gray);
		        jt.setForeground(Color.white);
		        jt.getTableHeader().setBackground(Color.BLACK);
		        jt.getTableHeader().setForeground(Color.white);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				System.out.println("Ticket view sucessfully created.");
				chkIfAdmin = true;
		} catch (SQLException e1) {
			System.out.println("Ticket view failed.");
			e1.printStackTrace();
		}
	}
	
	public void remakeJTable() {
		try {

			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(TicketsJTable.buildTableModel(dao.readTickets(curUser)));
				jt.setBounds(30, 40, 200, 400);
				jt.setBackground(Color.gray);
		        jt.setForeground(Color.white);
		        jt.getTableHeader().setBackground(Color.BLACK);
		        jt.getTableHeader().setForeground(Color.white);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				System.out.println("Ticket view sucessfully created.");
				chkIfAdmin = true;
		} catch (SQLException e1) {
			System.out.println("Ticket view failed.");
			e1.printStackTrace();
		}
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);
		
		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu items for Tickets main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Ticket main menu item
		mnuTickets.add(mnuItemUpdate);

		// initialize third sub menu items for Tickets main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Ticket main menu item
		mnuTickets.add(mnuItemDelete);

		// initialize fourth sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI() {

		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if(chkIfAdmin) {
			bar.add(mnuAdmin); //Show admin menu only to admin.
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
		// define a window close operation
		public void windowClosing(WindowEvent wE) {
		    System.exit(0);
		}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String act = "exit"; 
		
		//For switch case since mnuItemOpenTicket can not be used as a parameter for switch case.
		if(e.getSource() == mnuItemOpenTicket){
			act = "openTicket";
		} else if (e.getSource() == mnuItemUpdate) {
			act = "updateTicket";
		} else if (e.getSource() == mnuItemDelete) {
			act = "deleteTicket";
		} else if (e.getSource() == mnuItemViewTicket) {
			act = "viewTicket";
		}
		
		//Switch case instead of if else.
		switch(act) {
			case "openTicket":
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a description for your ticket");
				if(ticketDesc != null) {
					dao.insertTicket(curUser, ticketDesc); //Takes the userID to keep track if user is accessing only their tickets.
					remakeJTable();
				}
				break;
			case "updateTicket":
				String ticketID = JOptionPane.showInputDialog(null, "Enter the ID of the ticket you want to update");
				if(ticketID != null) {
					String newDesc = JOptionPane.showInputDialog(null, "Enter a new description for your ticket");
					if(newDesc != null) {
						dao.updateTicket(curUser, Integer.parseInt(ticketID), newDesc); //User Permission
						remakeJTable();
					}
				}
				break;
			case "deleteTicket":
				String tid2 = JOptionPane.showInputDialog(null, "Which ticket do you want to delete?");
				if(tid2 != null) {
					dao.deleteTicket(curUser, Integer.parseInt(tid2)); //User Permission
					remakeJTable();
				}
				break;
			case "viewTicket":
				String tid3 = JOptionPane.showInputDialog(null, "Which ticket do you want to view?");
				if(tid3 != null) {
					String viewT = dao.readTicket(curUser, Integer.parseInt(tid3)); //User Permission
					JOptionPane.showMessageDialog(null, viewT); //Use dialog to display the searched Ticket
				}
				break;
			default: 
				System.exit(0);
		}

	}

}
