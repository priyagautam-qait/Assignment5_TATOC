package org.qait.TatocAssignment;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class AutomateTatoc {

	public static void main(String args[]) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "/home/priyagautam/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();

		driver.get("http://10.0.1.86/tatoc/basic");
		driver.findElement(By.className("greenbox")).click();

		driver.switchTo().frame("main");
		WebElement box1 = driver.findElement(By.id("answer"));
		String color1 = box1.getAttribute("class");

		driver.switchTo().frame("child");
		WebElement box2 = driver.findElement(By.id("answer"));
		String color2 = box2.getAttribute("class");

		while (!(color2.equals(color1))) {
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");
			driver.findElement(By.xpath("//a[text()='Repaint Box 2']")).click();
			// driver.switchTo().defaultContent();
			driver.switchTo().frame("child");
			box2 = driver.findElement(By.id("answer"));
			color2 = box2.getAttribute("class");

		}
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();

		Actions action = new Actions(driver);
		WebElement srclocator = driver.findElement(By.id("dragbox"));
		WebElement destlocator = driver.findElement(By.id("dropbox"));
		action.dragAndDrop(srclocator, destlocator).build().perform();
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();

		driver.findElement(By.xpath("//a[text()='Launch Popup Window']")).click();

		String windowhandlerbefore = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles())
			driver.switchTo().window(handle);

		WebElement txt_box = driver.findElement(By.id("name"));
		txt_box.click();
		txt_box.sendKeys("hello");
		driver.findElement(By.id("submit")).click();

		driver.switchTo().window(windowhandlerbefore);
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();

		driver.findElement(By.xpath("//a[text()='Generate Token']")).click();
		
		String token = driver.findElement(By.id("token")).getText().substring(7);
		
		Cookie mycookie = new Cookie("Token",token);
		driver.manage().addCookie(mycookie);
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();

		
		
		}
}
