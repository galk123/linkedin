package linkedinapp;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapperServiceDOM implements ScrapperService {

	@Override
	public Profile getPublicProfile(String url) throws Exception {
		//check to see if the URL had a valid host, throw IllegalArgumentException if needed
		URL aURL = new URL(url);
		if(!aURL.getHost().equals("www.linkedin.com"))
			throw new IllegalArgumentException("not a valid linkedin profile public URL");
		
		DataService data = ServiceHandler.getDataService();
		if(data.searchByURL(url) != null)
			throw new IllegalArgumentException("URL already exists");
		/*
		 * for high-volume of requests, multiple proxy servers
		 * 	should be used when retrieving data from linkedin.com
		 */
		String profileName = null;
		String profileCurrentTitle = null;
		String profileCurrentPosition = null;
		String profileSummary = null;
		String[] profileSkills = null;
		Experience[] profileExperience = null;
		Education[] profileEducation = null;
		
		Document doc = Jsoup.connect(url).get();
		Element name = doc.getElementById("name");		
		
		if(name != null)
			profileName = name.text();
		else
			throw new IllegalArgumentException("not a valid linkedin profile public URL");
		
		Element topcard = doc.getElementById("topcard");
		if(topcard != null){
			profileCurrentTitle = topcard.getElementsByAttributeValue("data-section", "headline").text();
			Elements currentPositionsDetails = topcard.getElementsByAttributeValue("data-section", "currentPositionsDetails");
			if(!currentPositionsDetails.isEmpty()){
				Elements currentPositions = currentPositionsDetails.get(0).getElementsByTag("ol");
				if(!currentPositions.isEmpty())
					profileCurrentPosition = currentPositions.text();
			}
		}
		else
			throw new IllegalArgumentException("not a valid linkedin profile public URL");
		
		Element summary = doc.getElementById("experience");
		if(summary != null)
			profileSummary = summary.getElementsByClass("description").text();
		Element skills = doc.getElementById("skills");
		if(skills != null){
			Elements skillList = skills.getElementsByTag("a");
			if(!skillList.isEmpty()){
				profileSkills = new String[skillList.size()];
				for(int i = 0; i < skillList.size(); i++)
					profileSkills[i] = skillList.get(i).text();
			}
		}
		
		Element experience = doc.getElementById("experience");
		if(experience != null){
			Elements positions = experience.getElementsByClass("position");
			if(!positions.isEmpty()){
				profileExperience = new Experience[positions.size()];
				for(int i = 0; i < positions.size(); i++){
					Element position = positions.get(i);
					profileExperience[i] = new Experience(
							position.getElementsByClass("item-title").text(), 
							position.getElementsByClass("item-subtitle").text(), 
							position.getElementsByClass("date-range").text(), 
							position.getElementsByClass("description").text());
				}
			}
		}
		
		Element education = doc.getElementById("education");
		if(education != null){
			Elements schools = education.getElementsByClass("school");
			if(!schools.isEmpty()){
				profileEducation = new Education[schools.size()];
				for(int i = 0; i < schools.size(); i++){
					Element school = schools.get(i);
					profileEducation[i] = new Education(
							school.getElementsByClass("item-title").text(), 
							school.getElementsByClass("item-subtitle").text(), 
							school.getElementsByClass("date-range").text(), 
							school.getElementsByClass("description").text());
				}
			}
		}
			
		Profile profile = new Profile(url, profileName, profileCurrentTitle, profileCurrentPosition, profileSummary, profileSkills, profileExperience, profileEducation);
		return profile;
	}

}
