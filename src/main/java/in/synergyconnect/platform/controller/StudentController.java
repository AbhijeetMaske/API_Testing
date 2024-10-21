package in.synergyconnect.platform.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
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

import in.synergyconnect.platform.model.StudentData;
import in.synergyconnect.platform.service.StudentDataService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	@Autowired
	private StudentDataService studentDataService;

	@GetMapping("/")
	public String landingPage() {
		// Redirect to the landing page when accessing the root URL
		return "redirect:/landing.html";
	}

	// Retrieve all student data and set a cookie
	@GetMapping("/all")
	public ResponseEntity<?> getAllStudents(HttpServletResponse response) {
		try {
			String encodedRole = URLEncoder.encode("Admin", StandardCharsets.UTF_8.toString());
			Cookie cookie = new Cookie("userRole", encodedRole);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(60 * 60); // Set cookie expiry to 1 hour
			response.addCookie(cookie);
			return new ResponseEntity<>(studentDataService.getAllStudents(), HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>("Error encoding cookie", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Retrieve student data by ID and get a cookie value
	@GetMapping("/get")
	public ResponseEntity<?> getStudentById(@RequestParam Long studentId,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		try {
			String decodedRole = URLDecoder.decode(userRole, StandardCharsets.UTF_8.toString());
			if ("guest".equals(decodedRole)) {
				return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
			}
			StudentData studentData = studentDataService.getStudentById(studentId);
			if (studentData != null) {
				return new ResponseEntity<>(studentData, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Student data not found.", HttpStatus.NOT_FOUND);
			}
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>("Error decoding cookie", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add new student data and set a cookie
	@PostMapping("/add")
	public ResponseEntity<StudentData> addStudent(@RequestBody StudentData newStudentData,
			HttpServletResponse response) {
		try {
			studentDataService.addStudent(newStudentData);
			String encodedStudentName = URLEncoder.encode(newStudentData.getName(), StandardCharsets.UTF_8.toString());
			Cookie cookie = new Cookie("lastAddedStudent", encodedStudentName);
			response.addCookie(cookie);
			return new ResponseEntity<>(newStudentData, HttpStatus.CREATED);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete student data by ID
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteStudentById(@RequestParam Long studentId,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		boolean isRemoved = studentDataService.deleteStudentById(studentId);
		if (isRemoved) {
			return new ResponseEntity<>("Student data deleted successfully.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Student data not found.", HttpStatus.NOT_FOUND);
		}
	}

	// Update student data with PUT (replace entire record)
	@PutMapping("/update")
	public ResponseEntity<?> updateStudent(@RequestParam Long studentId, @RequestBody StudentData updatedStudentData,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		StudentData existingStudent = studentDataService.getStudentById(studentId);
		if (existingStudent != null) {
			// Replace the entire student record with new data
			updatedStudentData.setId(studentId); // Maintain the same ID
			studentDataService.updateStudent(updatedStudentData);
			return new ResponseEntity<>(updatedStudentData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Student data not found.", HttpStatus.NOT_FOUND);
		}
	}

	// Partially update student data with PATCH (update specific fields)
	@PatchMapping("/updatePartial")
	public ResponseEntity<?> partiallyUpdateStudent(@RequestParam Long studentId,
			@RequestBody StudentData partialUpdateData,
			@CookieValue(value = "userRole", defaultValue = "guest") String userRole) {
		if ("guest".equals(userRole)) {
			return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
		}

		StudentData existingStudent = studentDataService.getStudentById(studentId);
		if (existingStudent != null) {
			// Update only the fields that are provided in the request
			if (partialUpdateData.getName() != null) {
				existingStudent.setName(partialUpdateData.getName());
			}
			if (partialUpdateData.getAge() != 0) {
				existingStudent.setAge(partialUpdateData.getAge());
			}
			if (partialUpdateData.getGrade() != null) {
				existingStudent.setGrade(partialUpdateData.getGrade());
			}
			if (partialUpdateData.getEmail() != null) {
				existingStudent.setEmail(partialUpdateData.getEmail());
			}
			if (partialUpdateData.getLocation() != null) {
				existingStudent.setLocation(partialUpdateData.getLocation());
			}
			if (partialUpdateData.getSubject() != null) {
				existingStudent.setSubject(partialUpdateData.getSubject());
			}
			studentDataService.updateStudent(existingStudent);
			return new ResponseEntity<>(existingStudent, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Student data not found.", HttpStatus.NOT_FOUND);
		}
	}
}
