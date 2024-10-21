package in.synergyconnect.platform.service;

import in.synergyconnect.platform.model.StudentData;

public interface StudentDataService {

	StudentData getStudentById(Long studentId);

	void updateStudent(StudentData existingStudent);

	boolean deleteStudentById(Long studentId);

	Object getAllStudents();

	void addStudent(StudentData newStudentData);

}
