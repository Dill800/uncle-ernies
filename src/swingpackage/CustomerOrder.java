package swingpackage;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class CustomerOrder {

	private String firstName;
	private String lastName;
	private String flavor;
	private String topping;
	private String coneOrCup;
	private String price;
	private String currentDate;
	
	public CustomerOrder(String firstName, String lastName, String flavor, String topping, String coneOrCup, String price) {
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		currentDate = df.format(date);
		for(int i = 0; i < currentDate.length(); i++) {
			if(currentDate.substring(i, i+1).equals("/")) {
				currentDate = currentDate.substring(0, i) + "-" + currentDate.substring(i+1);
			}
		}

		this.firstName = firstName;
		this.lastName = lastName;
		this.flavor = flavor;
		this.topping = topping;
		this.coneOrCup = coneOrCup;
		this.price = price;
		
		
	}
	
	public CustomerOrder(String firstName, String lastName, String flavor, String topping, String coneOrCup, String price, String date) {
		
		this(firstName, lastName, flavor, topping, coneOrCup, price);
		currentDate = date;
		
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFlavor() {
		return flavor;
	}
	
	public String getTopping() {
		return topping;
	}
	
	public String getConeOrCup() {
		return coneOrCup;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getCurrentDate() {
		return currentDate;
	}
	
}
