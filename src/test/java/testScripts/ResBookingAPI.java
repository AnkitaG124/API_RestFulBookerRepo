package testScripts;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import POJO.Request.createBooking.BookingDates;
import POJO.Request.createBooking.CreateBooking;
import constants.StatusCodes;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ResBookingAPI {
	String token;
	int bookingId;
	CreateBooking book;
	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}")
				.when().post("/auth");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		// System.out.println(res.getBody());
		token = res.jsonPath().getString("token");
		System.out.println(token);
	}

	@Test(priority = 1,enabled = false)
	public void getSpecificBookingById() {
		Response res = RestAssured.given().log().all().header("Accept", "application/json").when()
				.get("/booking/" + bookingId);

		Assert.assertTrue(res instanceof Object);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.statusCode(), StatusCodes.OK);
		validateResponse(res, book,"");
	}
	
	
	@Test(enabled = false)
	public void updateBooking()
	{
		book.setFirstname("Piyush");
		Response res = RestAssured.given().log().all().header("Accept", "application/json").
				header("Content-Type","application/json").header("Cookie","token="+token)
				.when()
				.body(book)
				.put("/booking/" + bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.statusCode(), StatusCodes.OK);
		CreateBooking resBody=res.as(CreateBooking.class);
		Assert.assertTrue(book.equals(resBody));
	}
	
	@Test(enabled = false)
	public void partialUpdateBooking()
	{
		book.setFirstname("Piyush");
		Response res = RestAssured.given().log().all().header("Accept", "application/json").
				header("Content-Type","application/json").header("Cookie","token="+token)
				.when()
				.body(book)
				.patch("/booking/" + bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.statusCode(), StatusCodes.OK);
		CreateBooking resBody=res.as(CreateBooking.class);
		Assert.assertTrue(book.equals(resBody));
	}
	
	@Test
	public void deleteBooking()
	{
		book.setFirstname("Piyush");
		Response res = RestAssured.given().log().all().
				header("Content-Type","application/json").header("Cookie","token="+token)
				.when()
				.body(book)
				.delete("/booking/" + bookingId);
		System.out.println(res.asPrettyString());
		//Assert.assertEquals(res.statusCode(), StatusCodes.OK);
//		CreateBooking resBody=res.as(CreateBooking.class);
//		Assert.assertTrue(book.equals(resBody));
	}
	
	@Test(enabled = false)
	
	public void getSpecificBookingByIdWithDeserialization() {
		Response res = RestAssured.given().log().all().header("Accept", "application/json").when()
				.get("/booking/" + bookingId);

		CreateBooking resBody=res.as(CreateBooking.class);
		Assert.assertTrue(book.equals(resBody));
		
		
		
	}

	@Test(priority = 0)
	public void createBookingwithPoJo() {
		// RestAssured.baseURI="https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";

		BookingDates date = new BookingDates();
		date.setCheckin("2019-01-01");
		date.setCheckout("2019-01-01");

		 book = new CreateBooking();
		book.setFirstname("Ankita");
		book.setLastname("gupta");
		book.setTotalprice(123);
		book.setDepositpaid(true);
		book.setBookingdates(date);
		book.setAdditionalneeds("breakfast");

		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(book).when().post("/booking");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), StatusCodes.OK);
		System.out.println(res.asPrettyString());
		System.out.println(res.statusLine());
		bookingId = res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookingId > 0);
		Integer bookingId1 = Integer.valueOf(bookingId);		
		Assert.assertTrue(bookingId1 instanceof Integer);
		
		validateResponse(res, book,"booking.");
	

	}

	@Test(enabled = false)
	public void getAllBookings() {
		//int bookingId=2208;
		Response res = RestAssured.
				given().
				header("Accept", "application/json").
				when().get("/booking");

		//System.out.println(res.asPrettyString());
		
		List<Integer> list=res.jsonPath().getList("bookingid");
		System.out.println(list.size());
		Assert.assertTrue(list.contains(bookingId));
		Assert.assertEquals(res.statusCode(),StatusCodes.OK);
		//Assert.assertTrue(res instanceof Object);

	}

	@Test(enabled = false)
	public void createBooking() {
		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.headers("Accept", "application/json")
				.body("{\r\n" + "    \"firstname\" : \"Jim\",\r\n" + "    \"lastname\" : \"Brown\",\r\n"
						+ "    \"totalprice\" : 111,\r\n" + "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n" + "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n" + "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n" + "}")
				.when().post("/booking");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), StatusCodes.OK);
		System.out.println(res.asPrettyString());
		System.out.println(res.statusLine());
		int bookingId = res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookingId > 0);
		Integer bookingId1 = Integer.valueOf(bookingId);
		Assert.assertTrue(bookingId1 instanceof Integer);
		// System.out.println(bookingId);

	}

public void validateResponse(Response res,CreateBooking book, String object)
{
	Assert.assertEquals(res.jsonPath().getString(object+"firstname"), book.getFirstname());
	Assert.assertEquals(res.jsonPath().getString(object+"lastname"), book.getLastname());
	Assert.assertEquals(res.jsonPath().getInt(object+"totalprice"), book.getTotalprice());
	Assert.assertEquals(res.jsonPath().getBoolean(object+"depositpaid"), book.getDepositpaid());
	Assert.assertEquals(res.jsonPath().getString(object+"additionalneeds"), book.getAdditionalneeds());
	Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkin"),
			book.getBookingdates().getCheckin());
	Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkout"),
			book.getBookingdates().getCheckout());
}

@Test(enabled = false)
public void demoForResponseWithArrayAlso_togetType()
{
	RestAssured.baseURI="https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
	Response res=RestAssured.given().
	header("Accept", "application/json").
	when().get("/test");
	
	System.out.println(res.asPrettyString());
	List<String> list=res.jsonPath().getList("phoneNumbers.type");
	
	System.out.println(list);	
}

@Test(enabled = false)
public void demoForResponseWithArrayAlso_togetNumbers()
{
	RestAssured.baseURI="https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
	Response res=RestAssured.given().
	header("Accept", "application/json").
	when().get("/test");
	
	System.out.println(res.asPrettyString());
	List<Object> listOfPhoneNumber=res.jsonPath().getList("phoneNumbers");
	
	System.out.println(listOfPhoneNumber.size());
	
	for(Object obj:listOfPhoneNumber)
	{
	
	Map<String,String> map=(Map<String,String>)obj;
	
	System.out.println(map.get("type")+"----"+ map.get("number"));
	
	if(map.get("type").equals("iPhone"))
	{
		Assert.assertTrue(map.get("number").startsWith("3456") );
	
	}
	
	else if(map.get("type").equals("home"))
		
	{
		Assert.assertTrue(map.get("number").startsWith("0123") );
	}
		
			}
	
}

}
