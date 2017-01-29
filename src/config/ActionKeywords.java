package config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static executionEngine.DriverScript.OR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import executionEngine.DriverScript;
import utility.ExcelUtils;
import utility.Log;

public class ActionKeywords {
	
		public static WebDriver driver;
			
	public static void openBrowser(String object,String data){		
		Log.info("Opening Browser");
		try{				
			if(data.equals("Mozilla")){
				driver=new FirefoxDriver();
				Log.info("Mozilla browser started");				
				}
			else if(data.equals("IE")){
				//Dummy Code, Implement you own code
				driver=new InternetExplorerDriver();
				Log.info("IE browser started");
				}
			else if(data.equals("Chrome")){
				//Dummy Code, Implement you own code
				driver=new ChromeDriver();
				Log.info("Chrome browser started");
				}
			
			int implicitWaitTime=(10);
			driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		}catch (Exception e){
			Log.info("Not able to open the Browser --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}
	
	public static void navigate(String object, String data){
		try{
			Log.info("Navigating to URL");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(Constants.URL);
		}catch(Exception e){
			Log.info("Not able to navigate --- " + e.getMessage());
			DriverScript.bResult = false;
			}
		}
	
	public static void click(String object, String data){
		try{
			Log.info("Clicking on Webelement "+ object);
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		 }catch(Exception e){
 			Log.error("Not able to click --- " + e.getMessage());
 			DriverScript.bResult = false;
         	}
		}
	
	public static void input(String object, String data){
		try{
			Log.info("Entering the text in " + object);
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		 }catch(Exception e){
			 Log.error("Not able to Enter UserName --- " + e.getMessage());
			 DriverScript.bResult = false;
		 	}
		}
	

	public static void waitFor(String object, String data) throws Exception{
		try{
			Log.info("Wait for 5 seconds");
			Thread.sleep(5000);
		 }catch(Exception e){
			 Log.error("Not able to Wait --- " + e.getMessage());
			 DriverScript.bResult = false;
         	}
		}

	public static void closeBrowser(String object, String data){
		try{
			Log.info("Closing the browser");
			driver.quit();
		 }catch(Exception e){
			 Log.error("Not able to Close the Browser --- " + e.getMessage());
			 DriverScript.bResult = false;
         	}
		}
	public static void GET(String url1,String itemMatch,String expected,int iTestStep) throws Exception{
		try{
			URL url = new URL(url1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			String responseCode1=Integer.toString(conn.getResponseCode());
			ExcelUtils.setCellData(responseCode1, iTestStep, Constants.Col_ResponseCode, Constants.Sheet_TestSteps);
			if (conn.getResponseCode() != 200) {
			throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
			}

			Scanner scan = new Scanner(url.openStream());
			String entireResponse = new String();
			while (scan.hasNext())
			entireResponse += scan.nextLine();

			System.out.println("Response : "+entireResponse);

			scan.close();
			ExcelUtils.setCellData(entireResponse, iTestStep, Constants.Col_Response, Constants.Sheet_TestSteps);
			JSONObject obj = new JSONObject(entireResponse );
			String responseCode = obj.getString("status");
			System.out.println("status : " + responseCode);


			JSONArray arr = obj.getJSONArray("results");
			for (int i = 0; i < arr.length(); i++) {
		
			
			String formatAddress = arr.getJSONObject(i).getString(itemMatch);
			System.out.println("Address : " + formatAddress);

			//validating Address as per the requirement
			if(formatAddress.equalsIgnoreCase(expected))
			{
			System.out.println("Address is as Expected");
			}
			else
			{
			System.out.println("Address is not as Expected");
			}
			}

			conn.disconnect();

		 }catch (MalformedURLException e) {
			 DriverScript.bResult = false;

		 } catch (IOException e) {

			 DriverScript.bResult = false;

		 }
		}
	}