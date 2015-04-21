package book;

public class Book {
	private String title;
	private float price;
	
	public Book (String title, float price) {
		this.title = title;
		this.price = price;
	}
	
	public Book (String title) {
		this.title = title;
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
