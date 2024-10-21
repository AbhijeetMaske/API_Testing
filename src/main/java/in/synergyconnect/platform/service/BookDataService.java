package in.synergyconnect.platform.service;

import in.synergyconnect.platform.model.BookData;

public interface BookDataService {

    BookData getBookById(Long bookId);  // Retrieve a book by its ID

    void updateBook(BookData existingBook);  // Update the details of an existing book

    boolean deleteBookById(Long bookId);  // Delete a book by its ID

    Object getAllBooks();  // Retrieve all books

    void addBook(BookData newBookData);  // Add a new book
}
