package amazonSearch;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class AmazonSearch {
	
	public static WebDriver driver;
	
	public static void main(String[] args) {
		
		String category = "";
		String searchTerm = "";
		
		driver = new ChromeDriver();
		driver.get("https://www.amazon.in");
		
		driver.manage().window().maximize();
		
		
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce","root","root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * From Amazon Where Id = 1");
			
			while (rs.next()) {				
				category = rs.getString(2);
				searchTerm = rs.getString(3);
				
				System.out.println("\nCategory: " + category);
				System.out.println("Search Keyword: " + searchTerm);
			}
			
			
			WebElement categoryDropDown = driver.findElement(By.xpath("//select[@id='searchDropdownBox']"));
			
			Select categorySelect = new Select(categoryDropDown);
			categorySelect.selectByVisibleText(category);
			
			Thread.sleep(5000);
			
			WebElement searchBox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
			searchBox.sendKeys(searchTerm);
			
			Thread.sleep(5000);
			
			WebElement searchButton = driver.findElement(By.xpath("//input[@id='nav-search-submit-button']"));
			searchButton.click();
			
			List<WebElement> resultList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']"));
			System.out.println("\nTotal number of results found on the page: " + resultList.size());
			
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			try {
				FileUtils.copyFile(screenshot, new File("/home/appusharishgmai/eclipse-workspace/PhaseOneProject/screenshots/amazon-search.png"));
				
			} catch (IOException ex) {
				ex.printStackTrace();
				
			}
			
			
			List<WebElement> productNameList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']//span[@class='a-size-medium a-color-base a-text-normal']"));
			List<WebElement> productPriceList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']//span[@class='a-price-whole']"));
			
			System.out.println("\nProduct List for Search Keyword [" + searchTerm + "] from Category [" + category + "] \n");
		
			
			System.out.println("#" + "\t" + "Price" + "\t \t" + "Product Name \n");
			System.out.println("---------------------------------------------------------------------------------------- \n");

			int count = 1;

			for ( int i = 0; i < productNameList.size(); i++ ) {
				System.out.println((count++) + "\t" + productPriceList.get(i).getText() + "\t \t" + productNameList.get(i).getText());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
			driver.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

}
