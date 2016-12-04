package linkedinapp;

public class Profile {
	String url;
	String name;
	String currentTitle;
	String currentPosition;
	String summary;
	String[] skills;
	Experience[] experience;
	Education[] education;
	int score;
	
	public Profile(String url, String name, String currentTitle, String currentPosition, 
			String summary, String[] skills, Experience[] experience, Education[] education){
		this.url = url;
		this.name = name;
		this.currentTitle = currentTitle;
		this.currentPosition = currentPosition;
		this.summary = summary;
		this.skills = skills;
		this.experience = experience;
		this.education = education;
		/*
		 * score mechanism can be improved given more time,
		 * each skill can be rated to reflect importance
		 * experience - search for keywords that match job requirements
		 * education - institutions rankings, university vs college etc.
		 */
		score = 0;
		if(skills != null)
			score += skills.length * 2;
		if(skills != null)
			for(Experience exp : experience)
				score += exp.experienceInYears * 20;
	}
}
