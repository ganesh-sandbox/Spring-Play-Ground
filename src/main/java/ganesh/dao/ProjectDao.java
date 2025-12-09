package ganesh.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ganesh.entities.CompanyEn;

public class ProjectDao {
	
	// we took this t1 as a - name attribute into configuration file in db code section
	
	JdbcTemplate t1;

	public JdbcTemplate getT1() {
		return t1;
	}

	public void setT1(JdbcTemplate t1) {
		this.t1 = t1;
	}

	public void registerCompany(CompanyEn company) {
		

		t1.update("insert into company( company_name, company_email," + " company_phone_number, company_website, "
				+ "company_Profile_Picture, password, confirm_password ) values "
				+ "('"+company.getCompanyName()+"','"+company.getCompanyEmail()+"','"+company.getCompanyPhone()+"','"+company.getCompanyWebSite()
				+"','"+company.getCompanyProfilePicture()+"','"+company.getPassword()+"','"+company.getConfirmPassword()+"')");
		
		
		
	}

	public List<CompanyEn> checkLoginDetails(String email, String password) {
		
		// NOTE - 
		// When we post(Add or Store) the data to the DB - use t1.update() method
		// When we Get(fetch or get) the data to the DB - use t1.query() method 
		
		return	t1.query("SELECT * FROM company WHERE company_email ='"+email+"' and password = '"+password+"'", new RowMapper<CompanyEn>() {

			@Override
			public CompanyEn mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				// Domain obj to set and get the fields which will be usefull for latter
					
				 CompanyEn c1 = new CompanyEn();
				 
				 // fetching the data from the resultSet rs obj - which is rertuned by the query into select clause				 
				 // and setting to the domain fields
				 
				 c1.setId(rs.getInt(1));
				 c1.setCompanyName(rs.getString(2));
				 c1.setCompanyEmail(rs.getString(3));
				 c1.setCompanyPhone(rs.getString(4));
				 c1.setCompanyWebSite(rs.getString(5));
				 c1.setCompanyProfilePicture(rs.getString(6));
				 c1.setPassword(rs.getString(7));
				 c1.setConfirmPassword(rs.getString(8));
				 
				// return this same  obj of the domain
				 
				return c1;
			}
			
		});
	}
	
	

}
