package linkedinapp;

public interface DataService {
	public void add(Profile profile) throws Exception;
	
	public Profile[] searchByName(String q) throws Exception;
	
	public Profile[] searchByCurrentTitle(String q) throws Exception;
	
	public Profile[] searchByCurrentPosition(String q) throws Exception;
	
	public Profile[] searchBySummary(String q) throws Exception;
	
	public Profile[] searchBySkills(String[] skills) throws Exception;
	
	public Profile searchByURL(String url) throws Exception;
}
