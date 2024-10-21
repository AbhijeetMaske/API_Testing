package in.synergyconnect.platform.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import in.synergyconnect.platform.model.BookData;
import in.synergyconnect.platform.service.BookDataService;

@Service
public class BookDataServiceImpl implements BookDataService {

	private List<BookData> bookDataList;
	private Long lastId; // To track the last used ID

	// Constructor initializes sample book data and sets the last used ID
	public BookDataServiceImpl() {
		this.bookDataList = new ArrayList<>();
		this.lastId = 4L; // Assuming 4 books are pre-populated

		bookDataList.add(new BookData(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", "9780743273565",
				"Scribner"));
		bookDataList.add(new BookData(2L, "To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", "9780061120084",
				"J.B. Lippincott & Co."));
		bookDataList
				.add(new BookData(3L, "1984", "George Orwell", 1949, "Dystopian", "9780451524935", "Secker & Warburg"));
		bookDataList.add(
				new BookData(4L, "Pride and Prejudice", "Jane Austen", 1813, "Romance", "9780141439518", "T. Egerton"));
	}

	// Retrieve all books
	@Override
	public List<BookData> getAllBooks() {
		return bookDataList;
	}

	// Retrieve a book by ID
	@Override
	public BookData getBookById(Long bookId) {
		return bookDataList.stream().filter(book -> Objects.equals(book.getId(), bookId)).findFirst().orElse(null);
	}

	// Add a new book (ID is auto-generated)
	@Override
	public void addBook(BookData newBookData) {
		newBookData.setId(++lastId); // Auto-increment the ID
		bookDataList.add(newBookData);
	}

	// Delete a book by ID
	@Override
	public boolean deleteBookById(Long bookId) {
		return bookDataList.removeIf(book -> Objects.equals(book.getId(), bookId));
	}

	// Update book details
	@Override
	public void updateBook(BookData updatedBookData) {
		for (int i = 0; i < bookDataList.size(); i++) {
			if (Objects.equals(bookDataList.get(i).getId(), updatedBookData.getId())) {
				bookDataList.set(i, updatedBookData);
				break;
			}
		}
	}
}