package api_tests;

import java.util.HashMap;

import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

import io.restassured.response.Response;
import utilities.BrowserUtils;
import utilities.DataReader;


public class CraterItems {
	
	
	int itemId;
	String itemName;
	Response response;
	BrowserUtils utils;
	String token;
	
	@BeforeClass
	public void setup(){
	RestAssured.baseURI="http://crater.primetech-apps.com/";
	
	}
	
	@Test
	public void login() {
		String endpoint = "/api/v1/auth/login";
		//setting the request body to map
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("username",DataReader.getProperty("username"));
		requestBody.put("password",DataReader.getProperty("password"));
		requestBody.put("device_name",DataReader.getProperty("device_name"));
		
		//making the request token 
		response = RestAssured.given()
		.contentType("application/json")
		.accept("application/json")
		.body(requestBody)
		.post(endpoint)
		.thenReturn();
		
		response.prettyPrint();
		token = response.jsonPath().getString("token");
		
	}

	@Test(dependsOnMethods="login")
	public void createItem() {
		
		String endpoint = "api/v1/items";
		
		utils = new BrowserUtils();
	    itemName = "Standing Desk" + utils.randomNumber();
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", itemName);
		requestBody.put("price", 20000);
		requestBody.put("unit_id", 11);
		requestBody.put("description", "Nice standing desk");
		
				response = RestAssured.given()
			    .contentType("application/json")
				.accept("application/json")
			    .auth().oauth2(("Bearer "+token+""))
				.body(requestBody)
				.post(endpoint)
				.thenReturn();
				
				itemId = response.jsonPath().getInt("data.id");
				
			response.then().statusCode(200)
			.and().assertThat().contentType("application/json")
			.and().assertThat().body("data.name", Matchers.equalTo(itemName))
			.and().assertThat().body("data.price",Matchers.equalTo(20000))
			.and().assertThat().body("data.unit_id", Matchers.equalTo(11))
			.and().assertThat().body("data.description", Matchers.equalTo("Nice standing desk"));
			
			
		}
	
	@Test(dependsOnMethods = "createItem")
	public void getItem() {
		String endpoint = "/api/v1/items/" + itemId;
		
		response = RestAssured.given()
			    .contentType("application/json")
				.accept("application/json")
				.auth().oauth2("Bearer "+token+"")
				.get(endpoint).thenReturn();
				  
		
		//response.prettyPrint();
		response.then().statusCode(200)
		.and().assertThat().contentType("application/json")
		.and().assertThat().body("data.name", Matchers.equalTo(itemName))
		.and().assertThat().body("data.price",Matchers.equalTo(20000))
		.and().assertThat().body("data.unit_id", Matchers.equalTo(11))
		.and().assertThat().body("data.description", Matchers.equalTo("Nice standing desk"));
		
		
		
	}
	@Test(dependsOnMethods="getItem")
	public void updateItem() {
		
		String endpoint = "/api/v1/items/"+ itemId;
		
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", itemName);
		requestBody.put("price", 60000);
		requestBody.put("unit_id", 11);
		requestBody.put("description", "Nice standing desk");
		
				response = RestAssured.given()
			    .contentType("application/json")
				.accept("application/json")
				.auth().oauth2("Bearer " +token+"")
				.body(requestBody)
				.put(endpoint)
				.thenReturn();
				
			
				
			response.then().statusCode(200)
			.and().assertThat().contentType("application/json")
			.and().assertThat().body("data.name", Matchers.equalTo(itemName))
			.and().assertThat().body("data.price",Matchers.equalTo(60000))
			.and().assertThat().body("data.unit_id", Matchers.equalTo(11))
			.and().assertThat().body("data.description", Matchers.equalTo("Nice standing desk"));
	
	
}
	@Test(dependsOnMethods="updateItem")
	public void deleteItem() {
		
		String endpoint = "/api/v1/items/delete";
		String id= String.valueOf(itemId);
		String[] itemIds = {id};
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("ids", itemIds);
		
		
			   response= RestAssured.given()
			    .contentType("application/json")
				.accept("application/json")
				.auth().oauth2("Bearer "+token+"")
				.body(requestBody)
				.put(endpoint)
				.thenReturn();
			   
			   //Another option of validation.
				//.then().statusCode(200).contentType("application/json")
				//.body("success", Matchers.equalTo(true));
				
			
				
			
}
}