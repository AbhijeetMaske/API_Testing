package in.synergyconnect.platform.model;

public class BookData {
	private Long id;
	private String title;
	private String author;
	private int publishedYear;
	private String genre;
	private String isbn;
	private String publisher;

	// Default constructor (needed for frameworks like Spring)
	public BookData() {
	}

	// Parameterized constructor for easy object creation
	public BookData(Long id, String title, String author, int publishedYear, String genre, String isbn,
			String publisher) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publishedYear = publishedYear;
		this.genre = genre;
		this.isbn = isbn;
		this.publisher = publisher;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(int publishedYear) {
		this.publishedYear = publishedYear;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Override
	public String toString() {
		return "BookData{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' + ", publishedYear="
				+ publishedYear + ", genre='" + genre + '\'' + ", isbn='" + isbn + '\'' + ", publisher='" + publisher
				+ '\'' + '}';
	}
}
