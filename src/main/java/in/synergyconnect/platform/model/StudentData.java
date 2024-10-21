package in.synergyconnect.platform.model;

public class StudentData {
	private Long id;
	private String name;
	private int age;
	private String grade;
	private String email;
	private String location;
	private String subject;

	// Default constructor (needed for frameworks like Spring)
	public StudentData() {
	}

	// Parameterized constructor for easy object creation
	public StudentData(Long id, String name, int age, String grade, String email, String location, String subject) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.grade = grade;
		this.email = email;
		this.location = location;
		this.subject = subject;
	}

	// Getters and Setters for all fields
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "StudentData{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + ", grade='" + grade + '\''
				+ ", email='" + email + '\'' + ", location='" + location + '\'' + ", subject='" + subject + '\'' + '}';
	}
}
