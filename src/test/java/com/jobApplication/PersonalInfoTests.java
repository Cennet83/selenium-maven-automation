package com.jobApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.github.bonigarcia.wdm.WebDriverManager;
import static org.testng.Assert.*;

public class PersonalInfoTests {
	WebDriver driver;
	String firstName;
	String lastName;
	int gender;
	String dob;
	String email;
	String phoneNumber;
	String city;
	String state;
	String country;
	int salary;
	List<String> technologies;
	int experience;
	String education;
	String github;
	List<String> certifications;
	String additional;
	Faker faker = new Faker();
	Random rd = new Random();

	@BeforeClass
	public void setUp() {
		System.out.println("setting up WebDriver before class");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	@BeforeMethod // runs before each test
	public void navigateToHomepage() {
		System.out.println("navigating to homepage");
		driver.get(
				"https://forms.zohopublic.com/murodil/form/JobApplicationForm/formperma/kOqgtfkv1dMJ4Df6k4_mekBNfNLIconAHvfdIk3CJSQ");
		firstName = faker.name().firstName();
		lastName = faker.name().lastName();
		//gender = rd.nextInt(2) + 1;// min 1 max 2
		gender=faker.number().numberBetween(1, 3);
		dob = faker.date().birthday().toString();
		email = "jawocur@trimsj.com";
		phoneNumber = faker.phoneNumber().cellPhone();
		city = faker.address().city();
		state = faker.address().city();
		country = faker.address().country();
		//salary = rd.nextInt(150000) + 50000;// min 50000 max 150000
		salary=faker.number().numberBetween(50000, 150000);
		technologies=new ArrayList();
//		technologies.add("Java-Expert");
//		technologies.add("Html-Proficient");
//		technologies.add("Selenium WebDriver-Beginner");
//		technologies.add("TestNG-Expert");
//		technologies.add("Maven-Beginner");
		technologies.add("Java-"+faker.number().numberBetween(1, 4));
		technologies.add("Html-"+faker.number().numberBetween(1, 4));
		technologies.add("Selenium WebDriver-"+faker.number().numberBetween(1, 4));
		technologies.add("TestNG-"+faker.number().numberBetween(1, 4));
		technologies.add("Maven-"+faker.number().numberBetween(1, 4));
		technologies.add("Cucumber"+faker.number().numberBetween(1, 4));
		experience=faker.number().numberBetween(0, 11);
		education=faker.number().numberBetween(1, 4)+"";
		github="https://github.com/CybertekSchool/selenium-maven-automation.git";
		certifications.add("Java OCA");
		certifications.add("AWS");
		additional=faker.job().keySkills();
		
		
		
	}

	@Test
	public void fullNameEmptyTest() {
		// first assert that you are on the correct page
		assertEquals(driver.getTitle(), "SDET Job Application");
		// driver.findElement(By.name("Name_First")).clear();
		// <input type="text" maxlength="255" elname="first" name="Name_First" value=""
		// invlovedinsalesiq="false">
		driver.findElement(By.xpath("//input[@name='Name_First']")).clear();
		;
		driver.findElement(By.xpath("//input[@name='Name_Last']")).clear();
		// <em> Next </em>
		driver.findElement(By.xpath("//em[.=' Next ']")).click();
		// <p class="errorMessage" elname="error" id="error-Name" style="display:
		// block;">Enter a value for this field.</p>
		// write xpath with tagname+id
		// get the text and assert equal
		String errorMessage = driver.findElement(By.xpath("//p[@id='error-Name']")).getText();
		assertEquals(errorMessage, "Enter a value for this field.");
	}
	@Test
	public void submitFullApplication() {
		//driver.findElement(By.name("Name_First")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@name='Name_First']")).sendKeys(firstName);
		driver.findElement(By.name("Name_Last")).sendKeys(lastName);
		setGender(gender);
		setdob(dob);
		driver.findElement(By.xpath("//input[@name='Email']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@name='countrycode']")).sendKeys(phoneNumber);
		driver.findElement(By.xpath("//input[@name='Address_City']")).sendKeys(city);
		driver.findElement(By.xpath("//input[@name='Address_Region']")).sendKeys(state);
		Select countryselect=new Select(driver.findElement(By.xpath("//select[@id='Address_Country']")));
		countryselect.selectByIndex(faker.number().numberBetween(1, countryselect.getOptions().size()));// to select random country
		//driver.findElement(By.xpath("//input[@name='Number']")).sendKeys(""+salary);
		driver.findElement(By.xpath("//input[@name='Number']")).sendKeys(String.valueOf(salary)+Keys.TAB);
		verifySalaryCalculations(salary);
		driver.findElement(By.xpath("//input[@rowvalue='java' and @coloumnvalue='expert']"));
		
		
		
		if(experience>0) {
		driver.findElement(By.xpath("//a[@rating_value='"+ experience+"']")).click();
		}
		Select edu=new Select(driver.findElement(By.xpath("//select[@name='Dropdown']")));
		edu.selectByIndex(Integer.parseInt(education));
		
	}
	public void verifySalaryCalculations(int salary) {
		String monthly=driver.findElement(By.xpath("//input[@name='Formula']")).getAttribute("value");
		String weekly=driver.findElement(By.xpath("//input[@name='Formula1']")).getAttribute("value");
		String hourly=driver.findElement(By.xpath("//input[@name='Formula2']")).getAttribute("value");
		DecimalFormat formatter=new DecimalFormat("#.##");
		
	assertEquals(Double.parseDouble(monthly),Double.parseDouble(formatter.format((double)salary /12.0)));
		assertEquals(Double.parseDouble(weekly),Double.parseDouble(formatter.format((double)salary / 52.0)));
		assertEquals(Double.parseDouble(hourly),Double.parseDouble(formatter.format((double)salary / 52.0 / 40.0)));
//		assertEquals(Double.parseDouble(monthly), (double)salary/(double)12);
//		assertEquals(Double.parseDouble(weekly), (double)salary/(double)52);
//		assertEquals(Double.parseDouble(hourly), (double)salary/(double)52/40.0);
	}
	public void setGender(int n) {
		if(n==1) {
			driver.findElement(By.xpath("//input[@value='Male']")).click();
		}else {
			driver.findElement(By.xpath("//input[@value='Female']")).click();
		}
		
	}
	public void setdob(String dob) {
		String[] str=dob.split(" ");
		String DOB=str[1]+"-"+str[2]+"-"+str[str.length-1];
		driver.findElement(By.xpath("//input[@id='Date-date']")).sendKeys(DOB);
		
	}
}
