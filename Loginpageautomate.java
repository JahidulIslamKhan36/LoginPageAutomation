package Loginpageautomate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;



public class Loginpageautomate {

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String file = "F:\\autosearch\\Log_info_file.xlsx";
		FileInputStream inputstream=new FileInputStream(file);
		XSSFWorkbook wb=new XSSFWorkbook(inputstream);
		XSSFSheet sheet=wb.getSheet("LoginDetails");
		
		System.setProperty("webdriver.gecko.driver", "E:\\geckodriver.exe");

        
        WebDriver driver = new FirefoxDriver();
		
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		driver.get("http://demowebshop.tricentis.com/login");
		
		XSSFRow row=null;
		XSSFCell cell=null;
		String userName=null;
		String password=null;
		for (int i=1; i<=sheet.getLastRowNum();i++)
		{
			row=sheet.getRow(i);
			for ( int j=0;j<row.getLastCellNum();j++)
			{
				cell=row.getCell(j);
				
				if(j==0) 
				{
					userName=cell.getStringCellValue();
				}
				if(j==1) 
				{
					password=cell.getStringCellValue();	
				}		
			}
			
			driver.findElement(By.id("Email")).sendKeys(userName);
			driver.findElement(By.id("Password")).sendKeys(password);
			driver.findElement(By.xpath("//*[@value='Log in']")).click();
			String result=null;
			try 
			{	
				Boolean isLoggedIn=driver.findElement(By.xpath("//a[text()='Log out']")).isDisplayed();
				if(isLoggedIn==true)
				{
					result="PASS";
					
					 cell = row.createCell(2);
					 cell.setCellType(CellType.STRING);
					 cell.setCellValue(result);
				}
				System.out.println("User Name : " + userName + " ---- > " + "Password : "  + password + "-----> Login success ? ------> " + result);
				
				driver.findElement(By.xpath("//a[text()='Log out']")).click();
			}
			catch(Exception e)
			{
				
				Boolean isError=driver.findElement(By.xpath("//*[text()='The credentials provided are incorrect']")).isDisplayed();
				if(isError==true)
				{
					result="FAIL";
					cell = row.createCell(2);
					 cell.setCellType(CellType.STRING);
					 cell.setCellValue(result);
					 
				}
				System.out.println("User Name : " + userName + " ---- > " + "Password : "  + password + "-----> Login success ? ------> " + result);
				
				
			}
			Thread.sleep(1000);
			driver.findElement(By.xpath("//a[text()='Log in']")).click();
		}
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		fos.close();
		wb.close();
		
	}

	
	
	
}
