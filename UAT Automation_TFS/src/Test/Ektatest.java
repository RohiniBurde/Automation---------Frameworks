package Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class Ektatest{
	WebDriver dr;
	@Test
	public void start() throws InterruptedException, AWTException
	{
		System.setProperty("webdriver.ie.driver", "D:/Projects/New folder/UAT Automation/src/com/uat/config/IEDriverServer.exe");
		dr = new InternetExplorerDriver();
		dr.get("http://srvsps6/test/uat/SitePages/Report.aspx");
		login("sapan.vaswani","savasw1989%");
		Thread.sleep(2500);
		Select sel = new Select(dr.findElement(By.id("projSelect")));
		sel.selectByIndex(1);
		dr.findElement(By.xpath("//img[@title='Export To Excel']")).click();
		
		Thread.sleep(10000);
		
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_S);
		Thread.sleep(5000);
		Alert a = dr.switchTo().alert();
		a.sendKeys("C:/Users/ekta.choudhary.RSINNGP/Desktop/Test.xlsx");
		a.accept();
	}
	
	

	public void login(String username, String password) throws InterruptedException, AWTException{
		

		Alert a = dr.switchTo().alert();
        a.sendKeys(username);   
        
       	Robot r = new Robot();
        r.keyPress(KeyEvent.VK_ENTER);                    
        r.keyPress(KeyEvent.VK_ENTER);   
        
        Thread.sleep(2000);
        Alert b = dr.switchTo().alert();
        b.sendKeys(password);
    
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyPress(KeyEvent.VK_ENTER);
	
	}
}
