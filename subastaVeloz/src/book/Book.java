package book;

public class Book {
	private String title;
	private float price;
	
	public Book (String title, float price) {
		this.title = title;
		this.price = price;
	}
	public Float getPrice() {
		return price;
	}
	public String getTitle() {
		return title;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
