package linkedinapp;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * for handling high-volume of requests, multiple AWS instances should be set-up
 * 	with a load balancer.
 */
@Path("/add")
public class AddService {
	@Path("/url")
	@PUT
	@Produces("application/json")
	public Response addProfile(String url) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		try{
			ScrapperService scrapper = ServiceHandler.getScrapperService();
			Profile profile = scrapper.getPublicProfile(url);
			DataService data = ServiceHandler.getDataService();
			data.add(profile);
			Gson gson = new Gson();
			String result = gson.toJson(profile);
			return Response.status(200).entity(result).build();
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			jsonObject.put("error", e.getMessage());
			String result = jsonObject.toString();
			return Response.status(400).entity(result).build();
		}
		catch(Exception e){
			e.printStackTrace();
			jsonObject.put("error", e.getMessage());
			String result = jsonObject.toString();
			return Response.status(500).entity(result).build();
		}
	}
}
