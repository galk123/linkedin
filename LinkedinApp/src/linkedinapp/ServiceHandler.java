package linkedinapp;

public class ServiceHandler {
	private static ScrapperService scrapperService = null;
	private static DataService dataService = null;
	
	public static ScrapperService getScrapperService(){
		if(scrapperService == null)
			scrapperService = new ScrapperServiceDOM();
		return scrapperService;
	}
	
	public static DataService getDataService(){
		if(dataService == null)
			dataService = new DataServiceMySQL();
		return dataService;
	}
}
