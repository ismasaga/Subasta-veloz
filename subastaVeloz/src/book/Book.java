package book;

public class Book {
	private String title;
	private float price;
	private float increase;
	
	public Book (String title, float price, float increase) {
		this.title = title;
		this.price = price;
		this.increase = increase;
	}
	
	public Float getIncrease() {
		return increase;
	}
	public Float getPrice() {
		return price;
	}
	public String getTitle() {
		return title;
	}
	public void setIncrease(Float increase) {
		this.increase = increase;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
