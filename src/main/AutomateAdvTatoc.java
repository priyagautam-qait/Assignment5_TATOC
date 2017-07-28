package org.qait.TatocAssignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

public class AutomateAdvTatoc {

	public static String readFile() throws IOException {
		String path = "/home/priyagautam/Downloads/file_handle_test.dat";
		FileReader file = new FileReader(path);
		BufferedReader br = new BufferedReader(file);
		String line = br.readLine();
		String sign = "";
		while (line != null) {

			if (line.split(": ")[0].equals("Signature")) {
				sign = line.split(":")[1];
				break;
			}
			line = br.readLine();

		}
		return sign;
	}

	public static void main(String args[])
			throws InterruptedException, ClassNotFoundException, SQLException, IOException {

		System.setProperty("webdriver.chrome.driver", "/home/priyagautam/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();

		driver.get("http://10.0.1.86/tatoc");
		driver.findElement(By.xpath("//a[text()='Advanced Course']")).click();
		driver.findElement(By.xpath("//span[text()='Menu 2']")).click();
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Go Next']")));
        driver.findElement(By.xpath("//span[text()='Go Next']")).click();

		String query = "select * from identity ";
		String query1 = "select * from credentials";
		String fetched_id = null;
		String username = null;
		String password = null;
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.1.86:3306/tatoc", "tatocuser", "tatoc01");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		String symbol = driver.findElement(By.id("symboldisplay")).getText();
		while (rs.next()) {
			if (rs.getString("symbol").equalsIgnoreCase(symbol)) {
				fetched_id = rs.getString("id");
			}
		}
		ResultSet rs1 = stmt.executeQuery(query1);
		while (rs1.next()) {
			if (rs1.getString("id").equalsIgnoreCase(fetched_id)) {
				username = rs1.getString("name");
				password = rs1.getString("passkey");
			}
		}

		driver.findElement(By.id("name")).sendKeys(username);
		driver.findElement(By.id("passkey")).sendKeys(password);
		driver.findElement(By.id("submit")).click();

		driver.get("http://10.0.1.86/tatoc/advanced/rest/#");
		String sessionId = driver.findElement(By.id("session_id")).getText().substring(12);
		JsonPath jpath = RestAssured.when().get(" http://10.0.1.86/tatoc/advanced/rest/service/token/" + sessionId)
				.then().extract().jsonPath();
		String token = jpath.getString("token");
		RestAssured.given().parameters("id", sessionId, "signature", token, "allow_access", "1")
				.post("http://10.0.1.86/tatoc/advanced/rest/service/register");
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Download File']")));

		driver.findElement(By.xpath("//a[text()='Download File']")).click();
		Thread.sleep(3000);
		String signature = AutomateAdvTatoc.readFile();
		
		driver.findElement(By.id("signature")).sendKeys(signature);
		driver.findElement(By.className("submit")).click();


	}
}





































