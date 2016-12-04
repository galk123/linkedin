package linkedinapp;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;

import com.google.gson.Gson;

public class DataServiceMySQL implements DataService {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/Linkedin";
	
	//  Database credentials
	static final String USER = "root";
	static final String PASS = "1234";
	
	@Override
	public void add(Profile profile) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		   try{
		      //Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //Open a connection
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //Execute a query
		      //stmt = conn.createStatement();
		      
		      Gson gson = new Gson();
		      String experience = gson.toJson(profile.experience);
		      String education = gson.toJson(profile.education);
		      
		      String sql;
		      sql = "insert into LinkedinProfiles (Url, ProfileName, CurrentTitle, CurrentPosition, "
		      		+ "Summary, Experience, Education, Score) values "
		      		+ "(\""+profile.url+"\", \""+profile.name+"\", \""+profile.currentTitle+"\", \""+profile.currentPosition+"\","
		      				+ " \""+profile.summary+"\", ?, ?, "+profile.score+")";
		      
		      stmt = conn.prepareStatement(sql);
		      stmt.setString(1, experience);
		      stmt.setString(2, education);
		      stmt.executeUpdate();
		      //Clean-up environment
		      stmt.close();
		      
		      sql = "select Id from LinkedinProfiles where Url = \""+profile.url+"\"";
		      stmt = conn.prepareStatement(sql);
		      ResultSet rs = stmt.executeQuery(sql);
		      
		      //Extract data from result set
		      if(rs.next()){
		    	  int id  = rs.getInt("Id");
		    	  //Clean-up environment
		    	  rs.close();
		    	  stmt.close();

		    	  for(String skill : profile.skills){
		    		  sql = "insert into LinkedinProfileSkills (SkillName, ProfileId) values (\""+skill+"\",\""+id+"\")";
		    		  stmt = conn.prepareStatement(sql);
		    		  stmt.executeUpdate();
		    		  stmt.close();
		    	  }
		      }
		      
		      conn.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	}

	@Override
	public Profile[] searchByName(String q) throws Exception {
		return searchByStringField("ProfileName", q, true, null);
	}

	@Override
	public Profile[] searchByCurrentTitle(String q) throws Exception {
		return searchByStringField("CurrentTitle", q, true, null);
	}

	@Override
	public Profile[] searchByCurrentPosition(String q) throws Exception {
		return searchByStringField("CurrentPosition", q, true, null);
	}

	@Override
	public Profile[] searchBySummary(String q) throws Exception {
		return searchByStringField("Summary", q, true, null);
	}

	@Override
	public Profile[] searchBySkills(String[] skills) throws Exception {
		return searchByStringField("SkillName", null, true, skills);
	}

	@Override
	public Profile searchByURL(String url) throws Exception {
		Profile[] profiles = searchByStringField("Url", url, false, null);
		if(profiles != null)
			return profiles[0];
		else
			return null;
	}

	private Profile[] searchByStringField(String fieldName, String value, boolean like, String[] skillNames){
		Connection conn = null;
		PreparedStatement stmt = null;
		   try{
		      //Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //Open a connection
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //Execute a query
		      
		      ResultSet rs = null;
		      
		      String sql;
		      if(skillNames != null){
		    	  sql = "select ProfileId from linkedinprofileskills where SkillName in (\""+String.join("\",\"", skillNames)+"\")";
		    	  stmt = conn.prepareStatement(sql);
			      rs = stmt.executeQuery();
			      
			      HashSet<Integer> ids = new HashSet<Integer>();
			      while(rs.next())
			    	  ids.add(rs.getInt("ProfileId"));
			      
			      rs.close();
			      stmt.close();
			      
			      sql = "select * from linkedinprofiles where Id in (0";
			      for(int id : ids.toArray(new Integer[]{}))
			    	  sql += ","+id;
			      sql += ")";
		    	  stmt = conn.prepareStatement(sql);
		      }
		      else if(like){
		    	  sql = "select * from LinkedinProfiles where "+fieldName+" like \"%"+value+"%\"";
		    	  stmt = conn.prepareStatement(sql);
		      }
		      else{
		    	  sql = "select * from LinkedinProfiles where "+fieldName+" = ?";
		    	  stmt = conn.prepareStatement(sql);
			      stmt.setString(1, value);
		      }

		      rs = stmt.executeQuery();

		      //Extract data from result set
		      LinkedList<Profile> profiles = new LinkedList<Profile>();
		      LinkedList<Integer> ids = new LinkedList<Integer>();
		      while(rs.next()){
		         //Retrieve by column name
		         Gson gson = new Gson();
		         Experience[] exp = gson.fromJson(rs.getString("Experience"), Experience[].class);
		         Education[] edu = gson.fromJson(rs.getString("Education"), Education[].class);
		         
		         Profile profile = new Profile(rs.getString("Url"), rs.getString("ProfileName"), rs.getString("CurrentTitle"), rs.getString("CurrentPosition"), rs.getString("Summary"), null, exp, edu);
		         profile.score = rs.getInt("Score");
		         
		         ids.add(rs.getInt("Id"));
		         
		         profiles.add(profile);
		      }
		      //Clean-up environment
		      rs.close();
		      
		      for(int i = 0; i < ids.size(); i++){
		    	  sql = "select * from LinkedinProfileSkills where ProfileId = "+ids.get(i);
		    	  rs = stmt.executeQuery(sql);
		    	  LinkedList<String> skills = new LinkedList<String>();
		    	  while(rs.next()){
		    		  skills.add(rs.getString("SkillName"));
		    	  }
		    	  rs.close();
		    	  profiles.get(i).skills = skills.toArray(new String[]{});
		      }
		      
		      stmt.close();
		      conn.close();
		      
		      return profiles.toArray(new Profile[]{});
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   return null;
	}
}
