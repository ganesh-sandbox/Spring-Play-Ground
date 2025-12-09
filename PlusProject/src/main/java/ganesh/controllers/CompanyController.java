package ganesh.controllers;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ganesh.dao.ProjectDao;
import ganesh.entities.ApplyJobsEn;
import ganesh.entities.ApplyProjectsEn;
import ganesh.entities.CompanyEn;
import ganesh.entities.FreelancerEn;
import ganesh.entities.PostJobEn;
import ganesh.entities.PostProjectEn;

@Controller
public class CompanyController {
	
	
	@Autowired
	ProjectDao dao;
	
// Register Form	
	@RequestMapping("/registerCompany")
	public String showRegisterForm() {
		
		return "registerCompany";
	}
	
	
// Login Form	
	@RequestMapping("/loginCompany")
	public String showCompanyLoginForm() {
		
		return "loginCompany";
	}
	
//Registration Handler	
	@RequestMapping(value = "/registercompany" , method = RequestMethod.POST)
	public String registerCompany(@ModelAttribute("company") CompanyEn company , @RequestParam("filename") MultipartFile filename, ModelMap mdm ) throws IOException {
		
	// 0. Allow to register compnay only when the password and confirm password are  matched else redirect to the same login page 
		
	if(company.getPassword().equals(company.getConfirmPassword()))	
	{
		// Getting the file 
		
	// FIle Upload code section	
		
		// for file name 
		String file = filename.getOriginalFilename();
		
		// for file storage
		String path = "D:\\Eclipse\\22nd-April-2025\\PlusProject\\src\\main\\webapp\\files\\webImages";
		
		// concatinate file name and the path of the file 
		BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(path +"/"+file));
		
		// get file conversion into byte format 
		
		byte b [] = filename.getBytes();
		
		bf.write(b);
		
		bf.close();
		
		// setting the file to domain field
		
		company.setCompanyProfilePicture(file);
	
	// end of file upload code section	
		
		dao.registerCompany(company);
		
	// To display msg	
		
		mdm.addAttribute("registerMessage", "Registration Successfully Completed.You may Login Now.!");
		
		return "loginCompany";
	}	
		// To disply the error msg 
		
		mdm.addAttribute("registerError","Something went wrong." );
	  
		// if password miss-matched redirect to Registration. 
		return "registerCompany";

	}
	
	
	// Login Company
	// Maintainin session using - HttpSession

	@RequestMapping(value = "/loginCompany", method = RequestMethod.POST)
	public String loginCompany(@RequestParam("email") String email, @RequestParam("password") String password,
			ModelMap mdm, HttpSession session) {

		// For login - Check credentials with credentials we have stored in DB

		// List contains the all data about the company stored in DB

		List<CompanyEn> companyData = dao.checkLoginDetails(email, password);
		
		System.out.println("Company Data list in login compnay at 108: " +companyData);
		// IF no data found into list - redirect to the login page
		if (companyData.isEmpty()) {

			// Here we can display the msg to the user when the credentials not matched

			mdm.put("msgKey", "Something went Wrong.Check Your Login Credentials !");

			return "loginCompany";
		}

		// at this place the login is successful.So maintain here session

		session.setAttribute("userSession", email);

		// Setting the complete company Data list obj into session

		session.setAttribute("companydata", companyData);
		System.out.println("Company Data list in login compnay at 126: " +companyData);

		// If data is present - It means login credentilas are matched and present into
		// db
		// so redireect to - company Home page
		return "redirect:/homeCompany";

	}

	
	
	
	// Company Home page
	// On successful login we redirected here user - so maintaining - the same
	// session here also

	@RequestMapping("/homeCompany")
	public String companyHomePage(HttpSession session, ModelMap mdm) {

		// Getting the session attribute - to persist the user

		String sessionKey = (String) session.getAttribute("userSession");

		// Maintaning the session conditionally

		if (sessionKey == null) {

			// session is null - user can be logged out
			return "loginCompany";
		}
		
		
		// Display count section on company Home Page at 'Work Freelaner Has Done' section
		// 0. Freelancer count
		int countOfFreelancer = dao.getCountOfFreelancer();
		
			mdm.addAttribute("freelancerCount", countOfFreelancer);
		
			
		// 1. Totle Jobs count
			
		int countOfJobs = dao.getTotleJobsCount();
			
			mdm.addAttribute("totleJobsCount",countOfJobs);
			
			
		// 2. Totle freelancing Projects Count
			
			int countOfProjects = dao.getCountOfProjects();
			
			mdm.addAttribute("totleProjectsCount", countOfProjects);
			
			
		// 3. Totle Partner Companies Count	
			
			
			int countOfComapnies = dao.getCountOfComapnies();
			
			mdm.addAttribute("totleCompaniesCount",countOfComapnies);
			
			
			
		// Getting the company data obj from session - companyDataList
		// Company Data 

		List<CompanyEn> companyDataList = (List<CompanyEn>) session.getAttribute("companydata"); 
		
		System.out.println("List into homeCompany at 160:" +companyDataList);
		// forwarding it to the jsp using ModelMap  - key of DataList

		mdm.addAttribute("companydatakey", companyDataList);

		// if session key is present user is in active session
		System.out.println("In homeCompany at After 167:" +companyDataList);
		return "homeCompany";
	}
	
	
	
	// Logout Handler
	@RequestMapping("/logoutCompany")
	public String logoutCompany(HttpSession session) {

		// Invalidate the session when logout
		session.invalidate();

		return "loginCompany";
	}
	
	
	
	// Company Profile
	
	@RequestMapping("/profileCompany")
	public String companyProfile(HttpSession session, ModelMap mdm) {
		
		
		// Takint the dta 
		
		// Company Data 

		List<CompanyEn> companyDataList = (List<CompanyEn>) session.getAttribute("companydata"); 
		
		System.out.println("List into homeCompany at 160:" +companyDataList);
		// forwarding it to the jsp using ModelMap  - key of DataList

		mdm.addAttribute("companyInfoKey", companyDataList);
		
		
		
		return "companyProfile";
	}
	
	
	
	// Update Company Profile
	
	@RequestMapping(value =  "/updateprofilecompany", method = RequestMethod.POST)
	public String updateProfile(@ModelAttribute("company") CompanyEn company, @RequestParam("filename") MultipartFile filename,ModelMap mdm ) throws IOException {
		
		
		// File Upload code
		
		String file = filename.getOriginalFilename();
		
		// for file storage
		String path = "D:\\Eclipse\\22nd-April-2025\\PlusProject\\src\\main\\webapp\\files\\webImages";
		
		// concatinate file name and the path of the file 
		BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(path +"/"+file));
		
		// get file conversion into byte format 
		
		byte b [] = filename.getBytes();
		
		bf.write(b);
		
		bf.close();
		
		// setting the file to domain field
		
		company.setCompanyProfilePicture(file);
	
	// end of file upload code section	
		
		
		// Update the data 
		
		dao.updateDataOfCompanyProfie(company);
		
		
	// To display the msg 
		
		mdm.addAttribute("msgkeyforProfile","Profile is Successfully Updated Login Now.");
		
		return "loginCompany";
	}
	
	
	
	// Post job - show form 
	
	@RequestMapping("/postjob")
	public String postJobForm(HttpSession session, ModelMap mdm) {

		// Company Data

		List<CompanyEn> companyDataList = (List<CompanyEn>) session.getAttribute("companydata");

		System.out.println("List into homeCompany at 160:" + companyDataList);
		// forwarding it to the jsp using ModelMap - key of DataList

		mdm.addAttribute("companyInfoKey", companyDataList);

		return "postJobByCompany";

	}
	
	
	// Post Job 
	
	@RequestMapping(value = "/postjobdata", method = RequestMethod.POST)
	public String postJobRequirements(@ModelAttribute("jobPost") PostJobEn jobPost) {
		
		
		// Save details to db 
		
		dao.postJobDetails(jobPost);
		
		
		return "postJobByCompany";
	}
	
	
	// Post Project page
	
	@RequestMapping("/postproject")
	public String postProjectPage(HttpSession session,ModelMap mdm) {
		
		// Company Data

		List<CompanyEn> companyDataList = (List<CompanyEn>) session.getAttribute("companydata");

		System.out.println("List into homeCompany at 160:" + companyDataList);
		// forwarding it to the jsp using ModelMap - key of DataList

		mdm.addAttribute("companyInfoKey", companyDataList);
		
		return "postProject";
	}
	
	
	// save the post project data to db
	
	@RequestMapping(value = "/postprojectdata", method = RequestMethod.POST)
	public String savePostProjectData(@ModelAttribute("postProject") PostProjectEn postProject,
			@RequestParam("projectfile") MultipartFile filename) throws IOException {

		// File Upload code

		String file = filename.getOriginalFilename();

		// for file storage
		String path = "D:\\Eclipse\\22nd-April-2025\\PlusProject\\src\\main\\webapp\\files\\webImages";

		// concatinate file name and the path of the file
		BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(path + "/" + file));

		// get file conversion into byte format

		byte b[] = filename.getBytes();

		bf.write(b);

		bf.close();

		// setting the file to domain field

		postProject.setProjectPdf(file);

		// end of file upload code section

		// Save the data into db

		dao.postProjectData(postProject);

		return "postProject";
	}
	
	
	// Candidates Applications 
	
	@RequestMapping("/viewcandidatesapplications/{name}")
	public String showCandidateApplications(@PathVariable String name,ModelMap mdm,HttpSession session) {
		
		// Get the specific application data
		
		 List<ApplyJobsEn> candidatesAllData = dao.getApplicationData(name);
		
		 mdm.addAttribute("applicationData", candidatesAllData);
		 
		 
	//  Show Project Application applied by the candidates 
		 
		 List<ApplyProjectsEn> projectApplicationData= dao.getProjectApplicationsToCompany(name);
		 
		 mdm.addAttribute("projectAppData", projectApplicationData);
		 
		 
	// Company Data 
 
			List<CompanyEn> companyDataList = (List<CompanyEn>) session.getAttribute("companydata"); 
			
			mdm.addAttribute("companydatakey", companyDataList);
		 
		 
		 
		
		return "viewCandidatesApplications";
	}
	
	
	// Update Job Application Status
	
	
	@RequestMapping(value =  "/updatestatusOfJobApplication", method = RequestMethod.POST)
	public String updateJobApplicationStatus(@ModelAttribute("jobAppByFr") ApplyJobsEn jobAppByFr,ModelMap mdm ) {
		
		
	// Update JOB App Status
		
		dao.updateJobApplicationStatus(jobAppByFr);
		
		mdm.addAttribute("messageUpdateJob", "Job Status Updated.");
		
		return "messageCompany";
	}
	
	
	
	// Update Project Status
	
	@RequestMapping(value = "/updateprojectstatus", method = RequestMethod.POST)
	public String updateProjectStatus(@ModelAttribute("projectApp") ApplyProjectsEn projectApp,ModelMap mdm) {
		
		
	// update the status of project applied by the Fr
		
		dao.updateProjectStatus(projectApp);
		
		mdm.addAttribute("messageUpdateProject","Project Status Updated.");
		
		return "messageCompany";
	}
	
	
	// Messge company page 

		// Catching the Flash Attribute using - @ModelAttribute("key")
	
	@RequestMapping("/messageFromCompany")
	public String showMessageFromCompanyPage(@ModelAttribute("deleteProjectByCompany") String msgForDeleteProject,@ModelAttribute("deleteJobByCompany") String msgForDeleteJob,ModelMap mdm) {
		 
//		shows the message after redirection on Removal of project from  removeProject()
		
		mdm.addAttribute("deleteProjectKeyUsingFlashScope", msgForDeleteProject);
		
		mdm.addAttribute("deleteJObKeyUsingFlshScope", msgForDeleteJob);
		return "messageCompany";
	}
	
	
	// Display Accepted Job and Project Applications under Accepted Tag
	
	@RequestMapping("/acceptedApplications/{companyName}")
	public String showAcceptedJobProjectApplications(@PathVariable String companyName, ModelMap mdm) {
		  
	// Accepted Job Applications Data	
		List<ApplyJobsEn> acceptedJobApplicationsData =  dao.getAcceptedJobApplications(companyName);
		
		mdm.addAttribute("acceptedJobApps", acceptedJobApplicationsData);
		
		
	//	Accepted Project Applications Data
		
		List<ApplyProjectsEn> acceptedProjectApplicationsData =  dao.getAcceptedProjectApplications(companyName);
		
		mdm.addAttribute("acceptedProjectApps", acceptedProjectApplicationsData);
		
		
		return "acceptedJobProjectApplications";
	}
	
	
	// Display Rejected Job and Project Applications under the Rejected Tag
	
		@RequestMapping("/rejectedApplications/{companyName}")
		public String showRejectedJobProjectApplications(@PathVariable String companyName,ModelMap mdm) {
			
		// Rejected Job Applications Data	
			List<ApplyJobsEn> rejectedJobApplicationsData = dao.getRejectedJobApplications(companyName);
			
			mdm.addAttribute("rejectedJobApps", rejectedJobApplicationsData);
			
			
		// Rejected Project Application Data
			
			List<ApplyProjectsEn> rejectedJProjectApplicationsData =  dao.getRejectedProjectApplications(companyName);
				
				mdm.addAttribute("rejectedProjectApps", rejectedJProjectApplicationsData);
			
			return "rejectedJobProjectApplications";
		}
	
		
	//  Manage Jobs And Projects - for logged in company
		
		
		@RequestMapping("/historyofJobandprojects/{companyName}")
		public String showHistoryOfJobAndProjects(@PathVariable String companyName,ModelMap mdm) {
			
			// Job Data History
			
			List<PostJobEn> postJobDataHistory= dao.getJobHistoryData(companyName);
				
			mdm.addAttribute("postJobHistory", postJobDataHistory);
			
			
			// Project Data History
			
		List<PostProjectEn> postProjectDataHistory = dao.getProjectHistoryData(companyName);
			
			mdm.addAttribute("postProjectHistory", postProjectDataHistory);
			
			return "historyOfJobAndProjects";
		}
		
		
	// Edit Job Details 
		
		@RequestMapping("/editJob/{id}")
		public String showEditJobDetailsForm(@PathVariable("id") String jobId,ModelMap mdm) {
			
			// Job data of company 
			
			List<PostJobEn> jobDetails = dao.getJobDataOfCompany(jobId);
			
			mdm.addAttribute("jobData", jobDetails);
			
			return "editJobDetails";
		}
		
	
	// Update Job Data Company
		
		@RequestMapping(value = "/updatejobdatacompany", method = RequestMethod.POST)
		public String updateJobData(@ModelAttribute("postJob") PostJobEn postJob,ModelMap mdm) {
			
			dao.editJobData(postJob);
			
			mdm.addAttribute("messageAfterJobD", "Job Details Updated.");
			return "messageCompany";
		}
		
		
	// Remove Project
		// Use of RedirectAttributes to redirection for another handler with paramere in Flash Scope
		@RequestMapping(value = "/removeproject/{id}", method = RequestMethod.GET)
		public String removeProject(@PathVariable("id") int projectId, RedirectAttributes redirectAttributes) {
			
			
			dao.removeProjectByCompany(projectId);
			
			redirectAttributes.addFlashAttribute("deleteProjectByCompany", "Project Deleted From Records.");
			
			return "redirect:/messageFromCompany";
		}
		
		// Do the same Operation for  
		// Remove Jobs
		
		
		@RequestMapping(value = "/removejob/{id}" , method = RequestMethod.GET)
		public String removeJob(@PathVariable("id") int jobId,RedirectAttributes redirectedAttributes) {
			
			dao.removeJobByCompany(jobId);
			
			redirectedAttributes.addFlashAttribute("deleteJobByCompany", "Job Deleted From Records.");
			
			return "redirect:/messageFromCompany";
		}
		
		
		
		
		
		
		
		
		
	// View All Freelancers for Company
		
		
		@RequestMapping("/viewFreelancer")
		public String showViewAllFreelancersPage(ModelMap mdm) {
			
			// Get all registered Freelancers
			
			List<FreelancerEn> allFreelancers = dao.getAllFreelancers();
			
			mdm.addAttribute("allFreelancersKey", allFreelancers);
			
			return "viewAllFreelancer" ;
		}
		
		
		
}
