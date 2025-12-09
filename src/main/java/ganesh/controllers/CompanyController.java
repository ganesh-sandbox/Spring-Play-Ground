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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ganesh.dao.ProjectDao;
import ganesh.entities.CompanyEn;

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

		// Getting the company data obj from session - companyDataList

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
	
	
	
}
