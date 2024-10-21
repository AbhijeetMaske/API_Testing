package in.synergyconnect.platform.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import in.synergyconnect.platform.model.StudentData;
import in.synergyconnect.platform.service.StudentDataService;

@Service
public class StudentDataServiceImpl implements StudentDataService {

	private List<StudentData> studentDataList;
	private Long lastId; // To track the last used ID

	// Constructor initializes sample student data and sets last used ID
	public StudentDataServiceImpl() {
		this.studentDataList = new ArrayList<>();
		this.lastId = 4L; // Assuming 4 students are pre-populated

		studentDataList
				.add(new StudentData(1L, "John Doe", 20, "A", "john.doe@example.com", "New York, USA", "Mathematics"));
		studentDataList
				.add(new StudentData(2L, "Jane Smith", 22, "B", "jane.smith@example.com", "London, UK", "Physics"));
		studentDataList.add(new StudentData(3L, "Michael Johnson", 21, "A", "michael.johnson@example.com",
				"Toronto, Canada", "Chemistry"));
		studentDataList.add(new StudentData(4L, "Emily Davis", 20, "B+", "emily.davis@example.com", "Sydney, Australia",
				"Biology"));
	}

	// Retrieve all students
	public List<StudentData> getAllStudents() {
		return studentDataList;
	}

	// Retrieve a student by ID
	public StudentData getStudentById(Long studentId) {
		return studentDataList.stream().filter(student -> Objects.equals(student.getId(), studentId)).findFirst()
				.orElse(null);
	}

	// Add a new student (ID is auto-generated)
	public void addStudent(StudentData newStudentData) {
		newStudentData.setId(++lastId); // Auto-increment the ID
		studentDataList.add(newStudentData);
	}

	// Delete a student by ID
	public boolean deleteStudentById(Long studentId) {
		return studentDataList.removeIf(student -> Objects.equals(student.getId(), studentId));
	}

	// Update student details
	public void updateStudent(StudentData updatedStudentData) {
		for (int i = 0; i < studentDataList.size(); i++) {
			if (Objects.equals(studentDataList.get(i).getId(), updatedStudentData.getId())) {
				studentDataList.set(i, updatedStudentData);
				break;
			}
		}
	}
}
