package ganesh.entities;

public class PostJobEn {
	
	private int id;
	
	private String name;
	
	private String email;
	
	private String discription;
	
	private String title;
	
	private String skills;
	
	private String salary;
	
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "PostJobEn [id=" + id + ", name=" + name + ", email=" + email + ", discription=" + discription
				+ ", title=" + title + ", skills=" + skills + ", salary=" + salary + ", role=" + role + "]";
	}
	
	  
}


