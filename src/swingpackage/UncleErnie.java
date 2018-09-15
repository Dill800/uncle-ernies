package swingpackage;

import java.awt.Dimension;     
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.FlowLayout;
import java.awt.GridLayout; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class UncleErnie implements ActionListener {

	JTextField firstName;
	JTextField lastName;
	JComboBox<String> flavors;
	String[] flavorsArray;
	double[] flavorsPrices;
	JComboBox<String> toppings;
	String[] toppingsArray;
	double[] toppingsPrices;
	JRadioButton jrbCone;
	JRadioButton jrbCup;
	ButtonGroup bg;
	JButton add;
	JButton showTable;
	JLabel priceLabel;
	ArrayList<CustomerOrder> customerOrders = new ArrayList<>();
	JTable table;
	JScrollPane scrollpane;
	boolean tableCreated = false;
	JFrame tableFrame = new JFrame("Table");
	String[] headings = {"First Name", "Last Name", "Flavor", "Topping", "Cone/Cup", "Price", "Date"};
	boolean tableSeen = false;
	JButton deleteFlavor;
	JButton addFlavor;
	JButton deleteTopping;
	JButton addTopping;
	JTextField newFlavor;
	JTextField newTopping;
	JFrame alterFrame = new JFrame("Edit Flavors/Toppings");
	JButton alterChoices;
	boolean alterShowing = false;
	JTextField newFlavorPrice;
	JTextField newToppingPrice;
	JComboBox<String> editFlavors;
	JComboBox<String> editToppings;
	JButton checkMenu;
	JTable flavorTable;
	JTable toppingTable;
	JScrollPane flavorScroll;
	JScrollPane toppingScroll;
	JFrame menuFrame = new JFrame("Menu");
	boolean menuSeen = false;
	boolean menuCreated = false;
	JFrame jfrm = new JFrame("Uncle Ernie's Ice Cream");
	boolean continueEdit = true;
	JLabel invalidTopping = new JLabel("");
	JLabel invalidFlavor = new JLabel("");
	
	public UncleErnie() {
		initializeTable();
		updateGUI();
		
		jfrm.setLayout(new GridLayout(8, 2, 1, 1));
		jfrm.setSize(400, 300);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setVisible(true);
		
		firstName = new JTextField();
		lastName = new JTextField();
		JLabel label1 = new JLabel("First Name: ");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.add(label1);
		jfrm.add(firstName);
		JLabel label2 = new JLabel("Last Name: ");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.add(label2);
		jfrm.add(lastName);
		
		flavors = new JComboBox<String>(flavorsArray);
		flavors.setEditable(false);
		
		toppings = new JComboBox<String>(toppingsArray);
		toppings.setEditable(false);
		
		JLabel label3 = new JLabel("Flavor: ");
		label3.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.add(label3);				
		jfrm.add(flavors);
		JLabel label4 = new JLabel("Topping: ");
		label4.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.add(label4);
		jfrm.add(toppings);
		JLabel label5 = new JLabel("Cone or Cup: ");
		label5.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.add(label5);
		
		jrbCone = new JRadioButton("Cone", true);
		jrbCup = new JRadioButton("Cup");
		
		alterChoices = new JButton("Edit Flavors/Toppings");
		
		bg = new ButtonGroup();
		bg.add(jrbCone);
		bg.add(jrbCup);
		
		priceLabel = new JLabel("");
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jfrm.add(jrbCone);
		jfrm.add(priceLabel);
		jfrm.add(jrbCup);
		
		add = new JButton("Add order");
		showTable = new JButton("Show table");
		
		checkMenu = new JButton("View Flavors/Toppings");
		
		add.addActionListener(this);
		showTable.addActionListener(this);
		alterChoices.addActionListener(this);
		checkMenu.addActionListener(this);
		
		jfrm.add(add);
		jfrm.add(showTable);
		jfrm.add(alterChoices);
		jfrm.add(checkMenu);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UncleErnie();
			}
		});

	}

	public void actionPerformed(ActionEvent ae) {

		switch(ae.getActionCommand()) {
		case "Add order" :
			if(firstName.getText().equals("") || lastName.getText().equals("")) {
				priceLabel.setText("Please fill in all fields");
			}
			else {
				// Pricing
				double price = flavorsPrices[flavors.getSelectedIndex()] + toppingsPrices[toppings.getSelectedIndex()];
				String priceString = price + "";
				if(priceString.length() != 4) {
					priceString += "0";
				}
				priceLabel.setText("Price: $" + priceString);
				
				String coneCup = "Cup";
				if(jrbCone.isSelected()) {
					coneCup = "Cone";
				}
				
				CustomerOrder order = new CustomerOrder(firstName.getText(), lastName.getText(), (String)flavors.getSelectedItem(), (String)toppings.getSelectedItem(), coneCup, priceString);
				customerOrders.add(order);
				
				try {
					
					Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
					stmt.executeUpdate("Insert into CUSTOMER_ORDERS values ('" + firstName.getText() + "', '" + lastName.getText() + "', '" + (String)flavors.getSelectedItem() + "', '" + (String)toppings.getSelectedItem() + "', '" + coneCup + "', '" + priceString + "', '" + order.getCurrentDate() + "')");
					
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				firstName.setText("");
				lastName.setText("");
				flavors.setSelectedIndex(0);
				toppings.setSelectedIndex(0);
				jrbCone.setSelected(true);
				
			}
			
			if(tableFrame.isShowing()) {
				updateTable();
			}
			
			break;
		case "Show table":
			tableSeen = true;
			updateTable();
			break;
		
		case "Edit Flavors/Toppings": 
			alterChoices();
			break;

		case "Add Flavor":
			
			if(!newFlavor.getText().equals("")) {
				try {
					Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
					stmt.executeUpdate("insert into flavors values ('" + newFlavor.getText() + "', " + newFlavorPrice.getText() + ")");
					invalidFlavor.setText("");
				} catch(Exception e) {
					System.out.println(e);
					continueEdit = false;
					invalidFlavor.setText("Invalid Input");
				}
			}
			updateGUI();
			if(continueEdit) {
				flavors.addItem(flavorsArray[flavorsArray.length -  1]);
				editFlavors.addItem(flavorsArray[flavorsArray.length - 1]);
			}
			continueEdit = true;
			newFlavor.setText("Flavor Name");
			newFlavorPrice.setText("Price");
			break;
			
		case "Delete Flavor":
			try {
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Do you really want to delete " + editFlavors.getSelectedItem() + "?"))
				DBConnectionManager.getinstance().getConnection().createStatement().executeUpdate("delete from flavors where flavor = '" + editFlavors.getSelectedItem() + "'");
			} catch (Exception e) {
				System.out.println(e);
			}
			updateGUI();
			flavors.removeItemAt(editFlavors.getSelectedIndex());
			editFlavors.removeItemAt(editFlavors.getSelectedIndex());
			editFlavors.setSelectedIndex(0);
			break;
			
			
		case "Add Topping":
			
			if(!newTopping.getText().equals("")) {
				try {
					Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
					stmt.executeUpdate("insert into toppings values ('" + newTopping.getText() + "', " + newToppingPrice.getText() + ")");
					invalidTopping.setText("");
				} catch(Exception e) {
					System.out.println(e);
					continueEdit = false;
					invalidTopping.setText("Invalid Input");
				}
			}
			updateGUI();
			if(continueEdit) {
				toppings.addItem(toppingsArray[toppingsArray.length - 1]);
				editToppings.addItem(toppingsArray[toppingsArray.length - 1]);
			}
			continueEdit = true;
			newTopping.setText("Topping Name");
			newToppingPrice.setText("Price");
			break;
			
		case "Delete Topping":
			try {
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Do you really want to delete " + editToppings.getSelectedItem() + "?"))
				DBConnectionManager.getinstance().getConnection().createStatement().executeUpdate("delete from toppings where topping = '" + editToppings.getSelectedItem() + "'");
			} catch (Exception e) {
				System.out.println(e);
			}
			updateGUI();
			toppings.removeItemAt(editToppings.getSelectedIndex());
			editToppings.removeItemAt(editToppings.getSelectedIndex());
			editToppings.setSelectedIndex(0);
			break;
			
		case "View Flavors/Toppings":
			menuSeen = true;
			updateMenu();
			break;
		}
			
		
		
	}
	
	public void updateTable() {
		if(tableCreated) {
			tableFrame.remove(scrollpane);
		}
		tableFrame.getContentPane().setLayout(new FlowLayout());
		tableFrame.setSize(600, 160);
		tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		if(tableSeen) 
		tableFrame.setVisible(true);
		
		tableCreated = true;
		Object[][] data = new Object[customerOrders.size()][7];
		for(int i = 0; i < customerOrders.size(); i++) {
			data[i][0] = customerOrders.get(i).getFirstName();
			data[i][1] = customerOrders.get(i).getLastName();
			data[i][2] = customerOrders.get(i).getFlavor();
			data[i][3] = customerOrders.get(i).getTopping();
			data[i][4] = customerOrders.get(i).getConeOrCup();
			data[i][5] = customerOrders.get(i).getPrice();
			data[i][6] = customerOrders.get(i).getCurrentDate();
		}
		
		table = new JTable(data, headings) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		scrollpane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(550, 80));
		tableFrame.getContentPane().add(scrollpane);
		if(tableFrame.isVisible()) {
			tableFrame.setVisible(true);
		}
		
		
	}
	
	
	public void initializeTable() {
		
		try {
			Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("Select * from CUSTOMER_ORDERS");
			while(rs.next()) {
				String firstName = rs.getString("First_Name");
				String lastName = rs.getString("Last_Name");
				String flavor = rs.getString("Flavor");
				String topping = rs.getString("Topping");
				String coneOrCup = rs.getString("Cone_or_Cup");
				String price = rs.getString("Price");
				String date = rs.getString("Order_Date");
				customerOrders.add(new CustomerOrder(firstName, lastName, flavor, topping, coneOrCup, price, date));
			}
			updateTable();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public void updateGUI() {
		
		
		// creates names of flavors from db
		try {
			
			Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
			int numToppings = 0;
			int numFlavors = 0;
			
			ResultSet rs1 = stmt.executeQuery("select count(*) from TOPPINGS");
			rs1.next();
			numToppings = rs1.getInt(1);
			
			ResultSet toppingRS = stmt.executeQuery("select * from TOPPINGS");

			toppingsArray = new String[numToppings];
			for(int i = 0; toppingRS.next(); i++) {
				toppingsArray[i] = toppingRS.getString("topping");
			}
			
			ResultSet rs2 = stmt.executeQuery("select count(*) from FLAVORS");
			rs2.next();
			numFlavors = rs2.getInt(1);
			
			ResultSet flavorRS = stmt.executeQuery("select * from FLAVORS");
			flavorsArray = new String[numFlavors];
			for(int i = 0; flavorRS.next(); i++) {
				flavorsArray[i] = flavorRS.getString("flavor");
			} 
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			continueEdit = false;
		}
		
		
		// prices from table in db
		try {
			Statement stmt = DBConnectionManager.getinstance().getConnection().createStatement();
			toppingsPrices = new double[toppingsArray.length];
			flavorsPrices = new double[flavorsArray.length];
			
			ResultSet toppingRS = stmt.executeQuery("select * from TOPPINGS");
			
			for(int i = 0; toppingRS.next(); i++) {
				toppingsPrices[i] = toppingRS.getFloat("price");
			}
			
			ResultSet flavorRS = stmt.executeQuery("select * from FLAVORS");
			
			for(int i = 0; flavorRS.next(); i++) {
				flavorsPrices[i] = flavorRS.getFloat("price");
			} 
			
			for(int i = 0; i < toppingsPrices.length; i++) {
				if(Double.toString(toppingsPrices[i]).length() >= 5) {
					String temp = Double.toString(toppingsPrices[i]).substring(0, 4);
					toppingsPrices[i] = Double.parseDouble(temp);
				}
			}
			
			for(int i = 0; i < flavorsPrices.length; i++) {
				if(Double.toString(flavorsPrices[i]).length() >= 5) {
					String temp = Double.toString(flavorsPrices[i]).substring(0, 4);
					flavorsPrices[i] = Double.parseDouble(temp);
				}
			}
			
			
			if(menuFrame.isShowing()) {
				updateMenu();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			continueEdit = false;
		}

	}
	
	public void alterChoices() {
		
		alterFrame.setVisible(true);
		editFlavors = new JComboBox<String>(flavorsArray);
		editToppings = new JComboBox<String>(toppingsArray);
		
		if(!alterShowing) {
			alterFrame.setLayout(new GridLayout(4, 4, 1, 1));
			alterFrame.setSize(500,  200);
			alterFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			newFlavorPrice = new JTextField("Price");
			newToppingPrice = new JTextField("Price");
			deleteFlavor = new JButton("Delete Flavor");
			addFlavor = new JButton("Add Flavor");
			deleteTopping = new JButton("Delete Topping");
			addTopping = new JButton("Add Topping");
			newFlavor = new JTextField("Flavor Name");
			newTopping = new JTextField("Topping Name");
			
			editFlavors.setEditable(false);
			editToppings.setEditable(false);
			
			alterFrame.add(new JLabel("Flavors: "));
			alterFrame.add(newFlavor);
			alterFrame.add(newFlavorPrice);			
			alterFrame.add(addFlavor);
			alterFrame.add(new JLabel(""));
			alterFrame.add(invalidFlavor);
			alterFrame.add(editFlavors);
			alterFrame.add(deleteFlavor);
			alterFrame.add(new JLabel("Toppings: "));
			alterFrame.add(newTopping);
			alterFrame.add(newToppingPrice);
			alterFrame.add(addTopping);
			alterFrame.add(new JLabel(""));
			alterFrame.add(invalidTopping);
			alterFrame.add(editToppings);
			alterFrame.add(deleteTopping);
			alterShowing = true;
		}
		
		addFlavor.addActionListener(this);
		deleteFlavor.addActionListener(this);
		addTopping.addActionListener(this);
		deleteTopping.addActionListener(this);
		
		if(alterFrame.isVisible()) {
			alterFrame.setVisible(true);
		}

	}
	
	public void updateMenu() {
		
		if(menuCreated) {
			menuFrame.remove(flavorScroll);
			menuFrame.remove(toppingScroll);
		}
		menuFrame.getContentPane().setLayout(new FlowLayout());
		menuFrame.setSize(500, 200);
		menuFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		if(menuSeen) 
		menuFrame.setVisible(true);
		
		menuCreated = true;
		
		// flavor table
		Object[][] flavorData = new Object[flavorsArray.length][2];
		for(int i = 0; i < flavorsArray.length; i++) {
			flavorData[i][0] = flavorsArray[i];
			flavorData[i][1] = flavorsPrices[i];
		}
		String[] flavorHeading = {"Flavor", "Price"};
		flavorTable = new JTable(flavorData, flavorHeading) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		flavorScroll = new JScrollPane(flavorTable);
		flavorTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
		menuFrame.getContentPane().add(flavorScroll);
		
		//topping table
		Object[][] toppingData = new Object[toppingsArray.length][2];
		for(int i = 0; i < toppingsArray.length; i++) {
			toppingData[i][0] = toppingsArray[i];
			toppingData[i][1] = toppingsPrices[i];
		}
		String[] toppingHeading = {"Topping", "Price"};
		toppingTable = new JTable(toppingData, toppingHeading) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		toppingScroll = new JScrollPane(toppingTable);
		toppingTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
		menuFrame.getContentPane().add(toppingScroll);
		
		
		if(menuFrame.isVisible()) {
			menuFrame.setVisible(true);
		}
		
	}
	
	
}
