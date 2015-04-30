package book;

public class Book {
	private String title;
	private float price;
	private float increase;
	private String winner;

	public Book() {

	}

	public Book(String title, float price) {
		this.title = title;
		this.price = price;
	}

	public Book(String title, float price, float increase, String winner) {
		this.title = title;
		this.price = price;
		this.increase = increase;
		this.winner = winner;
	}

	public Book(String title) {
		this.title = title;
	}

	public Float getPrice() {
		return price;
	}

	public String getTitle() {
		return title;
	}

	public float getIncrease() {
		return increase;
	}

	public String getWinner() {
		return winner;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIncrease(float increase) {
		this.increase = increase;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void updatePrice() {
		price += increase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
