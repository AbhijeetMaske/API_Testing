package in.synergyconnect.platform.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.synergyconnect.platform.model.BookData;
import in.synergyconnect.platform.service.BookDataService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookDataService bookDataService;

	@GetMapping("/")
	public String landingPage() {
		// Redirect to the landing page when accessing the root URL
		return "redirect:/landing.html";
	}

	// Retrieve all book data and set a cookie
	@GetMapping("/all") @Description("")
	public ResponseEntity<?> getAllBooks(HttpServletResponse response){
		
		try {
			String encodedRole = URLEncoder.encode("Librarian", StandardCharsets.UTF_8.toString());
			Cookie cookie = new Cookie("userRole", encodedRole);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(60 * 60); // Set cookie expiry to 1 hour
			response.addCookie(cookie);
			return new ResponseEntity<>(bookDataService.getAllBooks(), HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>("Error encoding cookie", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Retrieve book data by ID and get a cookie value
	@GetMapping("/get")
	public ResponseEntity<?> getBookById(@RequestParam Long bookId,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		try {
			String decodedRole = URLDecoder.decode(userRole, StandardCharsets.UTF_8.toString());
			if ("guest".equals(decodedRole)) {
				return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
			}
			BookData bookData = bookDataService.getBookById(bookId);
			if (bookData != null) {
				return new ResponseEntity<>(bookData, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Book data not found.", HttpStatus.NOT_FOUND);
			}
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>("Error decoding cookie", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add new book data and set a cookie
	@PostMapping("/add")
	public ResponseEntity<BookData> addBook(@RequestBody BookData newBookData, HttpServletResponse response) {
		try {
			bookDataService.addBook(newBookData);
			String encodedBookTitle = URLEncoder.encode(newBookData.getTitle(), StandardCharsets.UTF_8.toString());
			Cookie cookie = new Cookie("lastAddedBook", encodedBookTitle);
			response.addCookie(cookie);
			return new ResponseEntity<>(newBookData, HttpStatus.CREATED);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete book data by ID
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBookById(@RequestParam Long bookId,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		boolean isRemoved = bookDataService.deleteBookById(bookId);
		if (isRemoved) {
			return new ResponseEntity<>("Book data deleted successfully.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Book data not found.", HttpStatus.NOT_FOUND);
		}
	}

	// Update book data with PUT (replace entire record)
	@PutMapping("/update")
	public ResponseEntity<?> updateBook(@RequestParam Long bookId, @RequestBody BookData updatedBookData,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		BookData existingBook = bookDataService.getBookById(bookId);
		if (existingBook != null) {
			// Replace the entire book record with new data
			updatedBookData.setId(bookId); // Maintain the same ID
			bookDataService.updateBook(updatedBookData);
			return new ResponseEntity<>(updatedBookData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Book data not found.", HttpStatus.NOT_FOUND);
		}
	}

	// Partially update book data with PATCH (update specific fields)
	@PatchMapping("/updatePartial")
	public ResponseEntity<?> partiallyUpdateBook(@RequestParam Long bookId, @RequestBody BookData partialUpdateData,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		BookData existingBook = bookDataService.getBookById(bookId);
		if (existingBook != null) {
			// Update only the fields that are provided in the request
			if (partialUpdateData.getTitle() != null) {
				existingBook.setTitle(partialUpdateData.getTitle());
			}
			if (partialUpdateData.getAuthor() != null) {
				existingBook.setAuthor(partialUpdateData.getAuthor());
			}
			if (partialUpdateData.getPublishedYear() != 0) {
				existingBook.setPublishedYear(partialUpdateData.getPublishedYear());
			}
			if (partialUpdateData.getGenre() != null) {
				existingBook.setGenre(partialUpdateData.getGenre());
			}
			if (partialUpdateData.getIsbn() != null) {
				existingBook.setIsbn(partialUpdateData.getIsbn());
			}
			if (partialUpdateData.getPublisher() != null) {
				existingBook.setPublisher(partialUpdateData.getPublisher());
			}
			bookDataService.updateBook(existingBook);
			return new ResponseEntity<>(existingBook, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Book data not found.", HttpStatus.NOT_FOUND);
		}
	}
}