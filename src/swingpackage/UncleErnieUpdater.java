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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class UncleErnieUpdater implements ActionListener{

	JLabel flavor;
	JLabel topping;
	JComboBox<String> flavors;
	String[] toppingsArray;
	double[] toppingsPrices;
	String[] flavorsArray;
	double[] flavorsPrices;
	JComboBox<String> toppings;
	JButton deleteFlavor;
	JButton addFlavor;
	JButton deleteTopping;
	JButton addTopping;
	JTextField newFlavor;
	JTextField newTopping;
	
	public UncleErnieUpdater() {
	
		updateGUI();
		
		JFrame jfrm = new JFrame("Flavor/Topping Updater");
		jfrm.setLayout(new GridLayout(4, 3, 1, 1));
		jfrm.setSize(400, 300);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setVisible(true);
	
		newFlavor = new JTextField();
		newTopping = new JTextField();
		flavor = new JLabel("Flavors: ");
		topping = new JLabel("Toppings: ");
		deleteFlavor = new JButton("Delete Flavor");
		addFlavor = new JButton("Add Flavor");
		deleteTopping = new JButton("Delete Topping");
		addTopping = new JButton("Add Topping");
		
		
		jfrm.add(flavor);
		jfrm.add(newFlavor);
		jfrm.add(addFlavor);
		jfrm.add( new JLabel(""));
		jfrm.add(flavors);
		jfrm.add(deleteFlavor);
		jfrm.add(topping);
		jfrm.add(newTopping);
		jfrm.add(addTopping);
		jfrm.add(new JLabel(""));
		jfrm.add(toppings);
		jfrm.add(deleteTopping);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UncleErnieUpdater();
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
