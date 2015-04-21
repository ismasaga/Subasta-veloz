
public class Book {
	private String title;
	private Float price;
	private Float increase;
	private Integer quantity;
	
	public Book (String title, Float price, Float increase, Integer quantity) {
		this.title = title;
		this.price = price;
		this.increase = increase;
		this.quantity = quantity;
	}
	
	public Float getIncrease() {
		return increase;
	}
	public Float getPrice() {
		return price;
	}
	public Integer getQuantity() {
		return quantity;
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
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
