package Test;

import org.testng.SkipException;

public class testSkipExcption {

	public static void main(String[] args) 
	{

		try
		  {
		  throw new SkipException("Test");
		  //throw new NullPointerException();
		  
		  }
		  catch(SkipException t)
		  {
		  // skip = true; 
		   System.out.println(" skip = true;");
		  }
		  catch(Throwable t)
		  {
		   //fail = true;
		   System.out.println("fail = true;");
		  }
		
		System.out.println("OUT");
	}

}
