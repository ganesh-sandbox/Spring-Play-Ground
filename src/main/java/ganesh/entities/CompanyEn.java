package ganesh.entities;

public class CompanyEn {
	
	private int id;
	
	private String companyName;
	
	private String companyEmail;
	
	private String companyPhone;
	
	private String companyWebSite;
	
	private String companyProfilePicture;
	
	private String password;
	
	private String confirmPassword;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyWebSite() {
		return companyWebSite;
	}

	public void setCompanyWebSite(String companyWebSite) {
		this.companyWebSite = companyWebSite;
	}
	
	public String getCompanyProfilePicture() {
		return companyProfilePicture;
	}

	public void setCompanyProfilePicture(String companyProfilePicture) {
		this.companyProfilePicture = companyProfilePicture;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "CompanyEn [id=" + id + ", companyName=" + companyName + ", companyEmail=" + companyEmail
				+ ", companyPhone=" + companyPhone + ", companyWebSite=" + companyWebSite + ", companyProfilePicture="
				+ companyProfilePicture + ", password=" + password + ", confirmPassword=" + confirmPassword + "]";
	}

 
	
	
}
