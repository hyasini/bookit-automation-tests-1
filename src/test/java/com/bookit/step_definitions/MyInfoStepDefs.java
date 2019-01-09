package com.bookit.step_definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;

import com.bookit.pages.SelfPage;
import com.bookit.pages.SignInPage;
import com.bookit.pages.TeamPage;
import com.bookit.utilities.BrowserUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import com.bookit.utilities.Driver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MyInfoStepDefs {

  @Given("user logs in using {string} {string}")
  public void user_logs_in_using(String email, String password) {
      Driver.getDriver().get(ConfigurationReader.getProperty("url"));
      Driver.getDriver().manage().window().maximize();
      SignInPage signInPage = new SignInPage();
      signInPage.email.sendKeys(email);
      signInPage.password.sendKeys(password);
      signInPage.signInButton.click();
      BrowserUtils.waitFor(2);
  }

  @When("user is on the my self page")
  public void user_is_on_the_my_self_page() {
      SelfPage selfPage = new SelfPage();
      selfPage.goToSelf();
  }

  @Then("user info should match with the database records for {string}")
  public void user_info_should_match_with_the_database_records_for(String email) {
      
	  //writing our query
	  String query = "SELECT firstname,lastname,role FROM users WHERE email = '"+email+"'";
	  
	  //get value from database and assign to map
	  Map<String,Object> result = DBUtils.getRowMap(query);
	  
	  //assigning database values to variables
	  String expectedFirstName = (String) result.get("firstname");
	  String expectedLastName = (String) result.get("lastname");
	  String expectedFullName = expectedFirstName + " " + expectedLastName;
	  String expectedRole = (String) result.get("role");
	  
	  //---UI---
	  SelfPage selfPage = new SelfPage();
	  
	  //wait until the information table is loaded
	  BrowserUtils.waitFor(2);
	  
	  //getting values from UI and assigning them to variables
	  String actualFullName = selfPage.name.getText();
	  String actualRole = selfPage.role.getText();
	  
	  System.out.println(actualFullName);
	  System.out.println(actualRole);
	  
	  //compare UI and Database values
	  assertEquals(actualFullName, expectedFullName);
	  assertEquals(actualRole,expectedRole);
	  
  }
  
  @When("user is on the my team page")
  public void user_is_on_the_my_team_page() {
	  SelfPage selfPage = new SelfPage();
	  selfPage.goToTeam();
	  BrowserUtils.waitFor(2);
  }

  @Then("team info should match with the database records {string}")
  public void team_info_should_match_with_the_database_records(String email) {
      
	  //writing the query
	  String query = "SELECT firstname, role FROM users WHERE team_id =(SELECT team_id FROM users WHERE email = '"+email+"')";
	  
	  //Assign query result to list of maps
	  List<Map<String,Object>> result =DBUtils.getQueryResultMap(query);
	  
	  //---UI---
	  //Import team page and create team page object to reach names and roles
	  TeamPage teamPage = new TeamPage();
	  
	  //Empty list for actual names
	  List<String> actualNames = new ArrayList<>();
	  
	  for (WebElement el:teamPage.teamMemberNames) {
		  //get each webelement role and add to actual names list
		  actualNames.add(el.getText());
	  }
	  
	  //empty list for actual roles
	  List<String> actualRoles = new ArrayList<>();
	  
	  for (WebElement el:teamPage.teamMemberRoles) {
		  //get each webelement role and add to actual roles list
		  actualRoles.add(el.getText());
	  }
	  
	  //before one by one comparison, check the number of results from DB and UI
	  assertEquals(result.size(), actualNames.size());
	  
	  //compare DB names against UI names (list to list)
	  for (Map<String,Object> row: result) {
		  String firstName = (String) row.get("firstname");
		  Assert.assertTrue(actualNames.contains(firstName));
	  }
	  
	//compare DB roles against UI roles (list to list)
	  for(Map<String, Object> row : result) {
          String role = (String) row.get("role");
          
          Assert.assertTrue(actualRoles.contains(role));
      }
		  
	  
  }
  
}