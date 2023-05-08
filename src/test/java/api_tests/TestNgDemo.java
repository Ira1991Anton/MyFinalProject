package api_tests;

import org.testng.asserts.SoftAssert;
import pages.SauceDemoPage;
import utilities.Driver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestNgDemo {

	SauceDemoPage page;

	@BeforeTest
	public void beforeTest() {
		System.out.println("Before Test Method");
	}

	@AfterTest
	public void AfterTest() {
		System.out.println("After Test Method");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("Before Method.");
	}
	
	@BeforeMethod
	public void AfterMethod() {
		System.out.println("Before Method.");
	}
	
	@Test(priority = 0, description = "UI test demo with TestNg")
	public void uiTestDemo() {
		page = new SauceDemoPage();
		// Test case 1
		// go to https://saucedemo.com
		Driver.getDriver().get(" https://saucedemo.com");
		// log in with valid username = standard_user password = secret_sauce
		page.username.sendKeys("standard_user");
		page.username.sendKeys("secret_sauze");
		page.loginBtn.click();
		// verify that you are logged in succesfully
		Assert.assertEquals(page.inventoryItems.size(), 6);
	}

	@Test(priority = 10, description = "Hard Assert Demo")
	public void assertionDemo() {
		// Hard assert. if the conditions fails, it stops the execution of the code.
		Assert.assertEquals(true, true);
		System.out.println("//Hard assert. if the conditions fails, it stops the execution of the code.");
	}

	@Test(priority=4, description ="Soft assert Demo", dependsOnMethods="assertionDemo")
	public void softAssertDemo() {
        SoftAssert softassert = new SoftAssert();
		//soft assert. if the condition fails, it does not stop the execution of the code.
		//but it marks the test as fail.
		softassert.assertEquals(true, true);
		System.out.println("//soft assert. if the condition fails, it does not stop the execution of the code.");
		
		// in order for soft assert to mark the test fail with exception/rrors, we need to have this line 
		softassert.assertAll();
		
		
	}
}
