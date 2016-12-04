package linkedinapp;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
 
@Path("/search")
public class SearchService {
	@Path("/{field}/{value}")
	@GET
	@Produces("application/json")
	public Response searchProfiles(@PathParam("field") String field, @PathParam("value") String value) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		try{
			DataService data = ServiceHandler.getDataService();
			Profile[] results = null;
			if(field.equals("name"))
				results = data.searchByName(value);
			else if(field.equals("title"))
				results = data.searchByCurrentTitle(value);
			else if(field.equals("position"))
				results = data.searchByCurrentPosition(value);
			else if(field.equals("summary"))
				results = data.searchBySummary(value);
			else if(field.equals("skills"))
				results = data.searchBySkills(value.split(","));
			String result;
			if(results == null || results.length == 0){
				jsonObject.put("error", "no results");
				result = jsonObject.toString();
			}
			else{
				Gson gson = new Gson();
				result = gson.toJson(results);
			}
			return Response.status(200).entity(result).build();
		}catch(Exception e){
			
			jsonObject.put("error", e.getMessage());
			String result = jsonObject.toString();
			return Response.status(500).entity(result).build();
		}
	}
}

