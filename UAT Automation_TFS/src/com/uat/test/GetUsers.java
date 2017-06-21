package com.uat.test;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.base.TestBase;

public class GetUsers extends TestBase {

	Credentials user;
	@Test
	public void getUserss() throws Exception
	{
		/*Credentials c =new Credentials("username", "password");
		System.out.println(c.username);
		System.out.println(c.password);*/
		initialize();
		
		//user = getUserToLogin("Admin");
		
		System.out.println(user.username);
		System.out.println(user.password);
		/*ArrayList<Credentials> arrList= getUsers("Stakeholder", 5);
		
		for (int i = 0; i < arrList.size(); i++) 
		{
			System.out.println(arrList.get(i).username+"                "+arrList.get(i).password);
			
		}*/
		
	}

}
