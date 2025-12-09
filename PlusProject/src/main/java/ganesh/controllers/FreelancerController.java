package ganesh.controllers;

import java.io.BufferedOutputStream;
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

import ganesh.dao.ProjectDao;
import ganesh.entities.ApplyJobsEn;
import ganesh.entities.ApplyProjectsEn;
import ganesh.entities.CompanyEn;
import ganesh.entities.FreelancerEn;
import ganesh.entities.PostJobEn;
import ganesh.entities.PostProjectEn;
import ganesh.entities.ShowJobsDto;

@Controller
public class FreelancerController {

	
	@Autowired
	ProjectDao dao;
	private List<ShowJobsDto> allJobsData;
	
	@RequestMapping("/registerfreelancer")
	public String registerPage() {
		
		return "registerFreelancer";
		
	}
	
	
	// login form  freelancer
	
	@RequestMapping("/loginfreelancer")
	public String loginPageforFreelancer() {
		
		return "loginFreelancer";
	}
	
	
	
	// Register Freelancer
	
	@RequestMapping(value = "/registerfreelancerData" , method = RequestMethod.POST)
	public String registerFreelancerData(@ModelAttribute("freelancer") FreelancerEn freelancer, 
			@RequestParam("filenamef") MultipartFile filename, @RequestParam("email") String email , ModelMap mdm) throws IOException {
		
	// To avoid login using same email id - taken email and checked it with already stored email id's   
			
			List<FreelancerEn> freelancerEmailData = dao.checkPresentEMailIds(email);
			
		// check for - login using duplicate email	
			if(freelancer.getPassword().equals(freelancer.getCpassword()) && freelancerEmailData.isEmpty() ) {
			
		
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
				
				freelancer.setProfilePhotoF(file);
			
			// end of file upload code section	
		
				
	// save the data into db
				
				dao.registerFreelancer(freelancer);
				
		// Message Display
				
				mdm.addAttribute("messagelogin", "Registration Successfully Done Login Now!");
				
				return "loginFreelancer";
		
			}
			
			mdm.addAttribute("messageError", "Check Your Password and Confirm Password or Email");
		return "registerFreelancer";
	}
	
	
	
	// Login For Freelancer
	
	
		@RequestMapping(value = "/loginForFreelancer", method = RequestMethod.POST)
		public String loginFreelancerData(@RequestParam("email") String email , @RequestParam("password") String password ,ModelMap mdm , HttpSession h1) {
			
			
			// Now to authenticate the user check this credentials are matched with our credentials or not
			
				// Freelancer all data
			List<FreelancerEn> frLoginData = dao.loginFreelancerDetails(email,password);
			
			if(frLoginData.isEmpty()) {
				
				// msg when login not successful
				mdm.addAttribute("messageloginfreelancer","you Don't Have An Account Register Now !");
				return "loginFreelancer";
			}
			
			h1.setAttribute("freelancerEmail", email);
			
			h1.setAttribute("freelancerAllData", frLoginData);
			// msg when login is successful
			
			mdm.addAttribute("","");
//			return "redirect:/homeFreelancer";
			return "redirect:/freelancerHomePage";
		}
	
	
		// Freelancer home page

		@RequestMapping("/freelancerHomePage")
		public String freelancerHome(HttpSession h1 ,ModelMap mdm) {

			// retriving the value from session

			String freelancerSession = (String) h1.getAttribute("freelancerEmail");

			if (freelancerSession == null) {

				return "loginFreelancer";
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
				
				
				
				
				
				
				
			// Get the info on HomePage
				
			List<ShowJobsDto> showJobsData = dao.getJobInfo();
			
			mdm.addAttribute("jobsInfo", showJobsData);
			
			
			// Get the project Info on freelancer home page
			
				List<PostProjectEn> showProjectData = dao.getProjectInfo();
				
				mdm.addAttribute("projectInfo", showProjectData);
			
				// All freelancer Data
			 List<FreelancerEn> freeLancerDataList =  (List<FreelancerEn>) h1.getAttribute("freelancerAllData");
			 
			 // Pass this to the view using mdm
			 
			 mdm.addAttribute("freelancerData",freeLancerDataList);
			 
			return "homeFreelancer";
		}
		
		
	// Logout freelancer 
		
		@RequestMapping("/logoutfreelancer")
		public String logoutFreelancer(HttpSession h1) {
			
			
			// Invalidate the session after the logout
			
//			String freelancerSessione = (String) h1.getAttribute("freelancerEmail");
			
//			if(freelancerSessione == null)
			
			h1.invalidate();
			
			return "loginFreelancer";
		}
		
		
	// header freelancer
		
		@RequestMapping("/headerfreelancer")
		public String headerFreelancer() {
			
			return "headerFreelancer";
		}
		
		
	// profile Freelancer	
		
		@RequestMapping("/profilefreelancer")
		public String profileFreelancer(HttpSession h1 , ModelMap mdm) {
			
			// All freelancer Data
			 List<FreelancerEn> freeLancerDataList =  (List<FreelancerEn>) h1.getAttribute("freelancerAllData");
			 
			 // Pass this to the view using mdm
			 
			 mdm.addAttribute("freelancerData",freeLancerDataList);
			 
			return "profilefreelancer";
		}
		
		
	// Update Freelancer profile
		
		@RequestMapping(value =  "/updateFreelancerProfile" , method = RequestMethod.POST)
		public String updateFreelancerProfile(@ModelAttribute("freelancer") FreelancerEn freelancer , @RequestParam("filename") MultipartFile filename) throws IOException {
											
			
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
			
			freelancer.setProfilePhotoF(file);
		
		// end of file upload code section	
			
			
	// Update freelancer profile data
			
			dao.updateFreelancerProfileData(freelancer);
			
			
			return "loginFreelancer";
		}
		
		
	//	Expore Jobs section for the freelancer section
		
		@RequestMapping("/Explorejobs")
		public String showexploreJobs(ModelMap mdm) {
			
			
		// Get the jobs data
			
		List<ShowJobsDto> showJobsData = dao.getAllJobsData();
			
		// Use this key in jsp for each
			mdm.addAttribute("jobsdata",showJobsData);
		
			return "exploreJobs";
		}
		
	// Explore Project 
		
		@RequestMapping("/exploreprojects")
		public String showExploreProjectPage(ModelMap mdm ) {
			
			
			// Get project Details
			
			List<PostProjectEn> projectData = dao.showAllProjectDetails();
			
				// key
			mdm.addAttribute("projectInfo", projectData);
			
			return "exploreProjects";
		}
		
		
		
	//  View and apply
		
		@RequestMapping(value ="/viewandapplyjob/{id}",method = RequestMethod.GET)
		public String viewAndApplyPage(@PathVariable int id, ModelMap mdm,HttpSession h1) {			
			// get data
			
			List<PostJobEn> allDataofJobs = dao.getJobData(id);
			
			mdm.addAttribute("alljobData",allDataofJobs);
			
			
			// All freelancer Data
			 List<FreelancerEn> freeLancerDataList =  (List<FreelancerEn>) h1.getAttribute("freelancerAllData");
			 
			 // Pass this to the view using mdm  - freelancer data key
			 
			 mdm.addAttribute("freelancerData",freeLancerDataList);	
			
			
			return "viewAndApplyJob";
		}
		
		
	
	// Project Details 
		
		@RequestMapping(value =  "/viewandapplyproject/{id}",method = RequestMethod.GET)            
		public String veiwAllProjectDetails(@PathVariable int id,ModelMap mdm,HttpSession h1) {
			
			
			List<PostProjectEn> allProjectDataById = dao.getAllProjectDataById(id);
			
			mdm.addAttribute("projectData", allProjectDataById);
			
			
			// All freelancer Data
			 List<FreelancerEn> freeLancerDataList =  (List<FreelancerEn>) h1.getAttribute("freelancerAllData");
			 
			 // Pass this to the view using mdm  - freelancer data key
			 
			 mdm.addAttribute("freelancerData",freeLancerDataList);		
			
			
			
			return "vieAndApplyProject";
		}
		
	
	// Apply JOb by Freelancer 
		
		@RequestMapping(value = "/applyforjob" , method = RequestMethod.POST)
		public String applyforJobByFreelancer(@ModelAttribute("applyJobFr") ApplyJobsEn applyJobFr, @RequestParam("resume") MultipartFile filename,ModelMap mdm) throws IOException {
			
			// consist one file - which is resume pdf - perform file upload first
			
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
						
						applyJobFr.setResumePdf(file);
					
					// end of file upload code section	
			 
			// store applied job data to DB 
			
			dao.getAppliedJobData(applyJobFr);
			
		// msg
			
			mdm.addAttribute("msgAfterSubmitJob", "Congratulations Your Application Submitted Successfully.!");
			
			
			return "exploreJobs";
		}
		
		
	//  Apply for project by freelancer
		
		
		@RequestMapping(value =  "/applyforproject", method = RequestMethod.POST)
		public String applyProjectByFreelancer(@ModelAttribute("applyForProjectByFr") ApplyProjectsEn applyForProjectByFr,@RequestParam("resumeFile")MultipartFile filename, ModelMap mdm) throws IOException {
			
		
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
			
			applyForProjectByFr.setFreelancerResume(file);
		
		// end of file upload code section		
			
			
			
			
			
		// post the data to db
			
			dao.applyForProject(applyForProjectByFr);
			
		// msg
			
			mdm.addAttribute("msgAfterSubmitProjectApp", "Congratulations Your Project Application Submitted Successfully.!");	
			
			return "exploreProjects";
		}
		
		
// My Applications
		
//		@PathVariable - if name of a varible is same in jsp and method parameter - then no need to provide the varible name in "" in @pathvariable
		
		@RequestMapping(value = "/myapplications/{email}")
		public String showApplications(@PathVariable String email,ModelMap mdm) {
			
	// Get the application data
			
			List<ApplyJobsEn> myApplicatonData = dao.getMyApplicatonData(email);
			
			mdm.addAttribute("myJobAppDetails",myApplicatonData);
			
	// Get the project Data 
			
			
			List<ApplyProjectsEn> projectApplicationData = dao.getProjectApplicationData(email);
			
			mdm.addAttribute("myProjectApplications", projectApplicationData);
			
			return "myApplications";
		}
		
		
	// Display all listed companies to freelancer 
		
		@RequestMapping("/listedCompanies")
		public String showAllListedCompaniesToFr(ModelMap mdm) {
			
			List<CompanyEn> allListedCompaniesToFr = dao.getAllListedCompaniesToFr();
			
			mdm.addAttribute("allListedCompanies", allListedCompaniesToFr);
			return "listOfAllCompanies";
		}
		
		
}
