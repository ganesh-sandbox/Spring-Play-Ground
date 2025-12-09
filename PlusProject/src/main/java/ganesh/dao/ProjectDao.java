package ganesh.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ganesh.entities.ApplyJobsEn;
import ganesh.entities.ApplyProjectsEn;
import ganesh.entities.CompanyEn;
import ganesh.entities.FreelancerEn;
import ganesh.entities.PostJobEn;
import ganesh.entities.PostProjectEn;
import ganesh.entities.ShowJobsDto;

public class ProjectDao {

	// we took this t1 as a - name attribute into configuration file in db code
	// section

	JdbcTemplate t1;

	public JdbcTemplate getT1() {
		return t1;
	}

	public void setT1(JdbcTemplate t1) {
		this.t1 = t1;
	}

	public void registerCompany(CompanyEn company) {

		t1.update("insert into company( company_name, company_email," + " company_phone_number, company_website, "
				+ "company_Profile_Picture, password, confirm_password ) values " + "('" + company.getCompanyName()
				+ "','" + company.getCompanyEmail() + "','" + company.getCompanyPhone() + "','"
				+ company.getCompanyWebSite() + "','" + company.getCompanyProfilePicture() + "','"
				+ company.getPassword() + "','" + company.getConfirmPassword() + "')");

	}

	public List<CompanyEn> checkLoginDetails(String email, String password) {

		// NOTE -
		// When we post(Add or Store) the data to the DB - use t1.update() method
		// When we Get(fetch or get) the data to the DB - use t1.query() method

		return t1.query("SELECT * FROM company WHERE company_email ='" + email + "' and password = '" + password + "'",
				new RowMapper<CompanyEn>() {

					@Override
					public CompanyEn mapRow(ResultSet rs, int rowNum) throws SQLException {

						// Domain obj to set and get the fields which will be usefull for latter

						CompanyEn c1 = new CompanyEn();

						// fetching the data from the resultSet rs obj - which is rertuned by the query
						// into select clause
						// and setting to the domain fields

						c1.setId(rs.getInt(1));
						c1.setCompanyName(rs.getString(2));
						c1.setCompanyEmail(rs.getString(3));
						c1.setCompanyPhone(rs.getString(4));
						c1.setCompanyWebSite(rs.getString(5));
						c1.setCompanyProfilePicture(rs.getString(6));
						c1.setPassword(rs.getString(7));
						c1.setConfirmPassword(rs.getString(8));
						c1.setAbout(rs.getString(9));
						// return this same obj of the domain

						return c1;
					}

				});
	}

	public void updateDataOfCompanyProfie(CompanyEn company) {

		// Update data sql query

		t1.update("UPDATE COMPANY SET COMPANY_NAME='" + company.getCompanyName() + "',COMPANY_EMAIL='"
				+ company.getCompanyEmail() + "',COMPANY_PHONE_NUMBER='" + company.getCompanyPhone()
				+ "',COMPANY_WEBSITE='" + company.getCompanyWebSite() + "',COMPANY_PROFILE_PICTURE='"
				+ company.getCompanyProfilePicture() + "',ABOUT='" + company.getAbout() + "'WHERE COMPANY_ID='"
				+ company.getId() + "'");

	}

	public void registerFreelancer(FreelancerEn freelancer) {
		
		// query
		
	
		t1.update("insert into freelancer (name,email,phone,dob,linkedin,education,profilef,charges,gender,skills,password,cpassword) values ('"+freelancer.getName()+"','"+freelancer.getEmail()+"','"+freelancer.getPhone()+"','"+freelancer.getDob()+"','"+freelancer.getLinkedinName()+"','"+freelancer.getEducation()+"','"+freelancer.getProfilePhotoF() +"','"+freelancer.getCharges()+"','"+freelancer.getGender()+"','"+freelancer.getSkills()+"','"+freelancer.getPassword()+"','"+freelancer.getCpassword()+"')");
		
	}


	// Authenticate freelancer
	
	public List<FreelancerEn> loginFreelancerDetails(String emailF, String passwordF) {

		return t1.query("SELECT * FROM FREELANCER WHERE EMAIL= '"+emailF+"' AND PASSWORD= '"+passwordF+"'", new RowMapper<FreelancerEn>() {

			@Override
			public FreelancerEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				
				// Now make the Domain obj and set the values which are given by the RowMapper from the resultSet 
				
				FreelancerEn f1 = new FreelancerEn();
					
				f1.setId(rs.getInt(1));
				f1.setName(rs.getString(2));
				f1.setEmail(rs.getString(3));
				f1.setPhone(rs.getString(4));
				f1.setDob(rs.getDate(5));
				f1.setLinkedinName(rs.getString(6));
				f1.setEducation(rs.getString(7));
				f1.setProfilePhotoF(rs.getString(8));
				f1.setCharges(rs.getString(9));
				f1.setGender(rs.getString(10));
				f1.setSkills(rs.getString(11));
				
				// No need of pass and cpassword so not taken
				
				return f1;
			}
 			
		});
  
	}
	
	// To avoid the login using same email id - check the mail id while registration is already present or not

	public List<FreelancerEn> checkPresentEMailIds(String email) {
		
		return t1.query("SELECT * FROM FREELANCER WHERE EMAIL = '"+email+"'", new RowMapper<FreelancerEn> () {

			@Override
			public FreelancerEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				FreelancerEn f1 = new FreelancerEn();
				
				f1.setEmail(rs.getString(3));
				
				return f1;
			}
			
			
		});
		
	}

	public void updateFreelancerProfileData(FreelancerEn freelancer) {
		
		t1.update("UPDATE FREELANCER SET NAME = '"+freelancer.getName()+"',email = '"+freelancer.getEmail()+"', phone= '"+freelancer.getPhone()+"'"
				+ ",linkedin= '"+freelancer.getLinkedinName()+"',education = '"+freelancer.getEducation()+"',charges='"+freelancer.getCharges()+"',skills='"+freelancer.getSkills()+"'");
		
	}


	// JOB Detials
	
	public void postJobDetails(PostJobEn jobPost) {
		
		t1.update("INSERT INTO postjob (name,email,discription,title,skills,salary,role) values ('"+jobPost.getName()+"','"+jobPost.getEmail()+"','"+jobPost.getDiscription()+"','"+jobPost.getTitle()+"','"+jobPost.getSkills()+"','"+jobPost.getSalary()+"','"+jobPost.getRole()+"')");
 
		
	}

	// get the job data
	
	public List<ShowJobsDto> getAllJobsData() {
 
		return t1.query("SELECT * FROM POSTJOB PJ RIGHT JOIN COMPANY CP ON PJ.NAME = CP.COMPANY_NAME WHERE PJ.NAME IS NOT NULL ORDER BY RANDOM();", new RowMapper<ShowJobsDto>() {

			@Override
			public ShowJobsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				ShowJobsDto s1 = new ShowJobsDto();
				
				s1.setId(rs.getInt(1));
				s1.setCompanyName(rs.getString(2));
				s1.setCompanyProfilePic(rs.getString(14));
				s1.setSkills(rs.getString(6));
				s1.setPosition(rs.getString(5));
				
				
				
				return s1;
			}
			
		});
	}

	
	// Store the post Project Data
	
	public void postProjectData(PostProjectEn postProject) {
		
		t1.update("INSERT INTO POSTPROJECT (PROJECT_DESCRIPTION,PROJECT_TITLE,PROJECT_PDF,PROJECT_SKILLS,PROJECT_BUDGET,COMPANY_NAME,COMPANY_EMAIL) VALUES ('"+postProject.getProjectDescription()+"','"+postProject.getProjectTitle()+"','"+postProject.getProjectPdf()+"','"+postProject.getProjectSkills()+"','"+postProject.getProjectBudget()+"','"+postProject.getCname()+"','"+postProject.getCemail()+"')");
		
	}


	// Get all the project Details 
	
	public List<PostProjectEn> showAllProjectDetails() {
 
		
		return t1.query("SELECT * FROM POSTPROJECT", new RowMapper<PostProjectEn>() {
			
			
			@Override
			public PostProjectEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				PostProjectEn p1 = new PostProjectEn();
				
				p1.setId(rs.getInt(1));
				p1.setProjectTitle(rs.getString(3));
				p1.setProjectPdf(rs.getString(4));
				p1.setProjectSkills(rs.getString(5));
				p1.setCname(rs.getString(7));
								
				return p1;
			}
			
			
		});
		
	}
	
	// Get the JOB DATA

	public List<PostJobEn> getJobData(int id) {
 
		return t1.query("SELECT * FROM POSTJOB WHERE ID= '"+id+"'", new RowMapper<PostJobEn>() {


			@Override
			public PostJobEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PostJobEn p1 = new PostJobEn();
				
				p1.setId(rs.getInt(1));
				p1.setName(rs.getString(2));
				p1.setEmail(rs.getString(3));
				p1.setDiscription(rs.getString(4));
				p1.setTitle(rs.getString(5));
				p1.setSkills(rs.getString(6));
				p1.setSalary(rs.getString(7));
				p1.setRole(rs.getString(8));
				 
				return p1;
			}
			
		});
	}

	
	// Get the project Data
	
	public List<PostProjectEn> getAllProjectDataById(int id) {
 
		return t1.query("SELECT * FROM POSTPROJECT WHERE ID= '"+id+"'", new RowMapper<PostProjectEn>() {

			
			@Override
			public PostProjectEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PostProjectEn p1 = new PostProjectEn();
				
				p1.setId(rs.getInt(1));
				p1.setProjectDescription(rs.getString(2));
				p1.setProjectTitle(rs.getString(3));
				p1.setProjectPdf(rs.getString(4));
				p1.setProjectSkills(rs.getString(5));
				p1.setProjectBudget(rs.getString(6));
				p1.setCname(rs.getString(7));
				p1.setCemail(rs.getString(8));
				
				return p1;
			}
			
			
			
		});
		
	}

	// Apply for the Job By Freelancer
	
	public void getAppliedJobData(ApplyJobsEn applyJobFr) {
		
		t1.update("INSERT INTO APPLYJOB (company_name,position,freelancer_Name,freelancer_email,resume,status) VALUES('"+applyJobFr.getCompanyName()+"','"+applyJobFr.getPosition()+"','"+applyJobFr.getFreelancerName()+"','"+applyJobFr.getFreelancerEmail()+"','"+applyJobFr.getResumePdf()+"','"+applyJobFr.getStatus()+"')");
	}
	
	
	
	// Apply for the project By Freelancer

	public void applyForProject(ApplyProjectsEn applyForProjectByFr) {
		
		t1.update("INSERT INTO APPLYPROJECT (project_Title,project_Company,project_CompanyEmail,freelancer_Name,freelancer_Email,freelancer_Resume,status) VALUES ('"+applyForProjectByFr.getProjectTitle()+"','"+applyForProjectByFr.getProjectCompany()+"','"+applyForProjectByFr.getProjectCompanyEmail()+"','"+applyForProjectByFr.getFreelancerName()+"','"+applyForProjectByFr.getFreelancerEmail()+"','"+applyForProjectByFr.getFreelancerResume()+"','"+applyForProjectByFr.getStatus()+"')");
 
		
	}

	public List<ApplyJobsEn> getMyApplicatonData(String email) {

		return t1.query("SELECT * FROM APPLYJOB WHERE FREELANCER_EMAIL LIKE '%"+email+"%'", new RowMapper<ApplyJobsEn>() {
			
			
			@Override
			public ApplyJobsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				
				ApplyJobsEn a1 = new ApplyJobsEn();
				
				a1.setId(rs.getInt(1));
				a1.setCompanyName(rs.getString(2));
				a1.setPosition(rs.getString(3));
				a1.setFreelancerName(rs.getString(4));
				a1.setFreelancerEmail(rs.getString(5));
				a1.setResumePdf(rs.getString(6));
				a1.setStatus(rs.getString(7));
				
				return a1;
			}
			
		});
	}


	// Get the project Application Data
	
	public List<ApplyProjectsEn> getProjectApplicationData(String email) {
 
		return t1.query("SELECT * FROM APPLYPROJECT WHERE FREELANCER_EMAIL LIKE '%"+email+"%'", new RowMapper<ApplyProjectsEn>() {

			@Override
			public ApplyProjectsEn mapRow(ResultSet rs, int rowNum) throws SQLException {

					ApplyProjectsEn a1 = new ApplyProjectsEn();
					
					a1.setId(rs.getInt(1));
					a1.setProjectTitle(rs.getString(2));
					a1.setProjectCompany(rs.getString(3));
					a1.setProjectCompanyEmail(rs.getString(4));
					a1.setFreelancerName(rs.getString(5));
					a1.setFreelancerEmail(rs.getString(6));
					a1.setFreelancerResume(rs.getString(7));
					a1.setStatus(rs.getString(8));
					
				
				return a1;
			}
			
		});
	}
	
	// Get the specific application data based of name 
	

	public List<ApplyJobsEn> getApplicationData(String name) {
		
		return t1.query("SELECT * FROM APPLYJOB WHERE COMPANY_NAME LIKE '%"+name+"%' and status= 'send'", new RowMapper<ApplyJobsEn>() {

			@Override
			public ApplyJobsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ApplyJobsEn a1 = new ApplyJobsEn();
				
				a1.setId(rs.getInt(1));
				a1.setCompanyName(rs.getString(2));
				a1.setPosition(rs.getString(3));
				a1.setFreelancerName(rs.getString(4));
				a1.setFreelancerEmail(rs.getString(5));
				a1.setResumePdf(rs.getString(6));
				a1.setStatus(rs.getString(7));
				
				return a1;
			}
			
			
			
		});
		
	}

	// Get The Applied Project Data 	
	
	public List<ApplyProjectsEn> getProjectApplicationsToCompany(String name) {
		
		return t1.query("SELECT * FROM APPLYPROJECT WHERE PROJECT_COMPANY LIKE '%"+name+"%' and status = 'send'", new RowMapper<ApplyProjectsEn>() {

			@Override
			public ApplyProjectsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ApplyProjectsEn a1 = new ApplyProjectsEn();
				
				a1.setId(rs.getInt(1));
				a1.setProjectTitle(rs.getString(2));
				a1.setProjectCompany(rs.getString(3));
				a1.setProjectCompanyEmail(rs.getString(4));
				a1.setFreelancerName(rs.getString(5));
				a1.setFreelancerEmail(rs.getString(6));
				a1.setFreelancerResume(rs.getString(7));
				a1.setStatus(rs.getString(8));
				
			
			return a1;
			}
			
			
			
		});
		 
	}
	
	
	// Update Job Application Status applied by Freelancer  by Company 

	public void updateJobApplicationStatus(ApplyJobsEn jobAppByFr) {
		
		t1.update("UPDATE APPLYJOB SET STATUS = '"+jobAppByFr.getStatus()+"' WHERE ID = '"+jobAppByFr.getId()+"'");
		
	}
	
	
	// Update the Project Status applied by Freelancer by Company

	public void updateProjectStatus(ApplyProjectsEn projectApp) {
 
		t1.update("UPDATE APPLYPROJECT SET STATUS = '"+projectApp.getStatus()+"' WHERE ID = '"+projectApp.getId()+"'");
		
	}
	
	
	// Accepted Job Applications Data By Company

	public  List<ApplyJobsEn> getAcceptedJobApplications(String companyName) {
		
		return t1.query("SELECT * FROM APPLYJOB WHERE COMPANY_NAME= '"+companyName+"' AND STATUS= 'Accepted'", new RowMapper<ApplyJobsEn>() {

			
			@Override
			public ApplyJobsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 

				ApplyJobsEn a1 = new ApplyJobsEn();
				
				a1.setId(rs.getInt(1));
				a1.setCompanyName(rs.getString(2));
				a1.setPosition(rs.getString(3));
				a1.setFreelancerName(rs.getString(4));
				a1.setFreelancerEmail(rs.getString(5));
				a1.setResumePdf(rs.getString(6));
				a1.setStatus(rs.getString(7));
				
				return a1;
			}
			
			
			
		});
 
		
	}
	
	
	// Accepted Project Applications Data by Company

	public List<ApplyProjectsEn> getAcceptedProjectApplications(String companyName) {
		
		return t1.query("SELECT * FROM APPLYPROJECT WHERE PROJECT_COMPANY='"+companyName+"' AND STATUS = 'Accepted'", new RowMapper<ApplyProjectsEn>() {

			
			@Override
			public ApplyProjectsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				ApplyProjectsEn a1 = new ApplyProjectsEn();

				a1.setId(rs.getInt(1));
				a1.setProjectTitle(rs.getString(2));
				a1.setProjectCompany(rs.getString(3));
				a1.setProjectCompanyEmail(rs.getString(4));
				a1.setFreelancerName(rs.getString(5));
				a1.setFreelancerEmail(rs.getString(6));
				a1.setFreelancerResume(rs.getString(7));
				a1.setStatus(rs.getString(8));
				
				return a1;
			}
			
			
			
		});
 
		
	}
	
	// Rejected Job Applications 

	
	public List<ApplyJobsEn> getRejectedJobApplications(String companyName) {
		
		return t1.query("SELECT * FROM APPLYJOB WHERE COMPANY_NAME= '"+companyName+"' AND STATUS='Rejected'", new RowMapper<ApplyJobsEn>() {
			
			
			@Override
			public ApplyJobsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				ApplyJobsEn a1 = new ApplyJobsEn();

				a1.setId(rs.getInt(1));
				a1.setCompanyName(rs.getString(2));
				a1.setPosition(rs.getString(3));
				a1.setFreelancerName(rs.getString(4));
				a1.setFreelancerEmail(rs.getString(5));
				a1.setResumePdf(rs.getString(6));
				a1.setStatus(rs.getString(7));

				return a1;
			}
			
			
		});
		
	}
	
	// Rejected Project Applications 
	
	public List<ApplyProjectsEn> getRejectedProjectApplications(String companyName) {
		
		return t1.query("SELECT * FROM APPLYPROJECT WHERE PROJECT_COMPANY = '"+companyName+"' and STATUS = 'Rejected'", new RowMapper<ApplyProjectsEn>() {
			
			
			@Override
			public ApplyProjectsEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				ApplyProjectsEn a1 = new ApplyProjectsEn();

				a1.setId(rs.getInt(1));
				a1.setProjectTitle(rs.getString(2));
				a1.setProjectCompany(rs.getString(3));
				a1.setProjectCompanyEmail(rs.getString(4));
				a1.setFreelancerName(rs.getString(5));
				a1.setFreelancerEmail(rs.getString(6));
				a1.setFreelancerResume(rs.getString(7));
				a1.setStatus(rs.getString(8));
				
				return a1;
			}
			
			
			
		});
		
	}
	
	// History of JOb Data
	

	public List<PostJobEn> getJobHistoryData(String companyName) {
		
		return t1.query("SELECT * FROM POSTJOB WHERE NAME= '"+companyName+"'", new RowMapper<PostJobEn>() {

			@Override
			public PostJobEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PostJobEn p1 = new PostJobEn();

				p1.setId(rs.getInt(1));
				p1.setName(rs.getString(2));
				p1.setEmail(rs.getString(3));
				p1.setDiscription(rs.getString(4));
				p1.setTitle(rs.getString(5));
				p1.setSkills(rs.getString(6));
				p1.setSalary(rs.getString(7));
				p1.setRole(rs.getString(8));

				return p1;
			}
			
			
			
			
		});
		
	}
	
		// History of Project Data 
		
	
	public List<PostProjectEn> getProjectHistoryData(String companyName) {
		
		return t1.query("SELECT * FROM POSTPROJECT WHERE COMPANY_NAME = '"+companyName+"'", new RowMapper<PostProjectEn>() {
			
			
			@Override
			public PostProjectEn mapRow(ResultSet rs, int rowNum) throws SQLException {

				PostProjectEn p1 = new PostProjectEn();

				p1.setId(rs.getInt(1));
				p1.setProjectDescription(rs.getString(2));
				p1.setProjectTitle(rs.getString(3));
				p1.setProjectPdf(rs.getString(4));
				p1.setProjectSkills(rs.getString(5));
				p1.setProjectBudget(rs.getString(6));
				p1.setCname(rs.getString(7));
				p1.setCemail(rs.getString(8));

				return p1;
			}
			
			
			
			
		} );
		
	}
	
	// Job Data of company for Edit Job Details
	 
	public List<PostJobEn> getJobDataOfCompany(String jobId) {
		
		return t1.query("SELECT * FROM POSTJOB WHERE id = '"+jobId+"'", new RowMapper<PostJobEn>() {

			@Override
			public PostJobEn mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				PostJobEn p1 = new PostJobEn();

				p1.setId(rs.getInt(1));
				p1.setName(rs.getString(2));
				p1.setEmail(rs.getString(3));
				p1.setDiscription(rs.getString(4));
				p1.setTitle(rs.getString(5));
				p1.setSkills(rs.getString(6));
				p1.setSalary(rs.getString(7));
				p1.setRole(rs.getString(8));

				return p1;
			}
			
			
	
		});
		
	}
	
	// Update Job Data 

	public void editJobData(PostJobEn postJob) {
		
		t1.update("UPDATE POSTJOB SET DISCRIPTION = '"+postJob.getDiscription()+"', TITLE='"+postJob.getTitle()+"',SKILLS='"+postJob.getSkills()+"',SALARy='"+postJob.getSalary()+"',ROLE='"+postJob.getRole()+"',NAME='"+postJob.getName()+"',EMAIL='"+postJob.getEmail()+"' WHERE ID='"+postJob.getId()+"'");
		
	}
	
	// Delete Project By Company
	
	
	public void removeProjectByCompany(int projectId) {
		
		t1.update("DELETE FROM POSTPROJECT WHERE ID = '"+projectId+"'");
		
	}
	
	
	// Delete Job by Company
	
	public void removeJobByCompany(int jobId) {
	 
		t1.update("DELETE FROM POSTJOB WHERE ID = '"+jobId+"'");
		}
 	
	
	// Get all freelancers
	
	public List<FreelancerEn> getAllFreelancers() {
 
		return t1.query("SELECT * FROM FREELANCER", new RowMapper<FreelancerEn>() {
			
			
			@Override
			public FreelancerEn mapRow(ResultSet rs, int rowNum) throws SQLException {

				FreelancerEn f1 = new FreelancerEn();
				
				f1.setId(rs.getInt(1));
				f1.setName(rs.getString(2));
				f1.setEmail(rs.getString(3));
				f1.setPhone(rs.getString(4));
				f1.setDob(rs.getDate(5));
				f1.setLinkedinName(rs.getString(6));
				f1.setEducation(rs.getString(7));
				f1.setProfilePhotoF(rs.getString(8));
				f1.setCharges(rs.getString(9));
				f1.setGender(rs.getString(10));
				f1.setSkills(rs.getString(11));
				
				// No need of pass and cpassword so not taken
				
				return f1;
			}
			
			
			
		});
	}
	
	// Get Totle Count of Freelancer 
	
	public int getCountOfFreelancer() {
		
		return t1.queryForObject("SELECT COUNT(*) FROM FREELANCER", Integer.class);
	}
	
	
	// Get Totle Job Count
	
	public int getTotleJobsCount() {
		
		return t1.queryForObject("SELECT COUNT(*) FROM POSTJOB", Integer.class);
		
	}
	
		
	
	
	// Get Totle Project Counts

	public int getCountOfProjects() {
		
		return t1.queryForObject("SELECT COUNT(*) FROM POSTPROJECT", Integer.class);
		
	}	
	
	
	
	// Get Count of Companiew

	public int getCountOfComapnies() {
		
		return t1.queryForObject("SELECT COUNT(*) FROM COMPANY", Integer.class);
		
	}

	
	
	
	
	
	
	
	// show Job Info on Freelancer Home Page

	public List<ShowJobsDto> getJobInfo() {
		
		return t1.query("SELECT * FROM POSTJOB PJ RIGHT JOIN COMPANY CP ON PJ.NAME = CP.COMPANY_NAME WHERE PJ.NAME IS NOT NULL ORDER BY RANDOM();",new RowMapper<ShowJobsDto>() {

			
			@Override
			public ShowJobsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
 
				ShowJobsDto s1 = new ShowJobsDto();

				s1.setId(rs.getInt(1));
				s1.setCompanyName(rs.getString(2));
				s1.setCompanyProfilePic(rs.getString(14));
				s1.setSkills(rs.getString(6));
				s1.setPosition(rs.getString(5));

				return s1;
			}
			
			
		});
 
		
	}
	
	// Get Project Info on Freelancer Homepage

	public List<PostProjectEn> getProjectInfo() {
 
		return t1.query("SELECT * FROM POSTPROJECT", new RowMapper<PostProjectEn>() {

			
			@Override
			public PostProjectEn mapRow(ResultSet rs, int rowNum) throws SQLException {

				PostProjectEn p1 = new PostProjectEn();

				p1.setId(rs.getInt(1));
				p1.setProjectDescription(rs.getString(2));
				p1.setProjectTitle(rs.getString(3));
				p1.setProjectPdf(rs.getString(4));
				p1.setProjectSkills(rs.getString(5));
				p1.setProjectBudget(rs.getString(6));
				p1.setCname(rs.getString(7));
				p1.setCemail(rs.getString(8));

				return p1;
			}
			
			
			
		});
	}
	
	
	//  Show all Listed companies to FR

	public List<CompanyEn> getAllListedCompaniesToFr() {
 
		return t1.query("SELECT * FROM COMPANY", new RowMapper<CompanyEn>() {
			
			
			@Override
			public CompanyEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				CompanyEn c1 = new CompanyEn();
				
				c1.setId(rs.getInt(1));
				c1.setCompanyName(rs.getString(2));
				c1.setCompanyEmail(rs.getString(3));
				c1.setCompanyPhone(rs.getString(4));
				c1.setCompanyWebSite(rs.getString(5));
				c1.setCompanyProfilePicture(rs.getString(6));
				c1.setPassword(rs.getString(7));
				c1.setConfirmPassword(rs.getString(8));
				c1.setAbout(rs.getString(9));

				return c1;
			}
			
			
			
			
		});
	}
	
	
	


}
