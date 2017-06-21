package com.uat.listener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.uat.util.ErrorUtil;

public class TestsListenerAdapter implements IInvokedMethodListener {
	
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		Reporter.setCurrentTestResult(result);

		if (method.isTestMethod()) {
			List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
			//if there are verification failures...
			if (verificationFailures.size() != 0) {
				//set the test to failed
				result.setStatus(ITestResult.FAILURE);
				
				//if there is an assertion failure add it to verificationFailures
				if (result.getThrowable() != null) {
					verificationFailures.add(result.getThrowable());
				}
 
				int size = verificationFailures.size();
				//if there's only one failure just set that
				if (size == 1) 
				{
					result.setThrowable(verificationFailures.get(0));
				} 
				else 
				{
					//int bufferSize = 0;
					File uatTestFailureFile = null;
					try 
					{
						uatTestFailureFile = File.createTempFile("UATTestResult", ".txt");
					
					
						FileWriter fw = new FileWriter(uatTestFailureFile);
						
						BufferedWriter writer = new BufferedWriter(fw);
						//List<String> sBuffer = new ArrayList<String>();
						String content = null;
						
						
						
						content = "Multiple failures ("+size+"):\n\n";
					//	bufferSize += content.length();
						writer.write(content);
						//sBuffer.add("Multiple failures ("+size+"):\n\n");
						//create a failure message with all failures and stack traces (except last failure)
						//StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append(")\n\n");
						for (int i = 0; i < size-1; i++) 
						{
							
							content = "Failure "+(i+1)+" of "+size+": ";
							//bufferSize += content.length();
							writer.write(content);
							//sBuffer.add("Failure "+(i+1)+" of "+size+": ");
							//failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(": ");
							Throwable t = verificationFailures.get(i);
							String fullStackTrace = Utils.stackTrace(t, false)[1];
						//	bufferSize += fullStackTrace.length();
							writer.write(fullStackTrace);
							t = null;
							fullStackTrace = null;
							//sBuffer.add(fullStackTrace+"\n");
							//failureMessage.append(fullStackTrace).append("\n");
						}
	 
						//final failure
						Throwable last = verificationFailures.get(size-1);
						content = "Failure "+size+" of"+size+": ";
						//sBuffer.add("Failure "+size+" of"+size+": ");
						writer.write(content);
					//	bufferSize += content.length();
						
						content = last.toString();
						writer.write(content);
					//	bufferSize += content.length();
						
						writer.close();
						
						//sBuffer.add(last.toString());
						//failureMessage.append("Failure ").append(size).append(" of ").append(size).append(": ");
						//failureMessage.append(last.toString());
						FileReader fr = new FileReader(uatTestFailureFile);
						BufferedReader reader = new BufferedReader(fr);
						
						//StringBuilder sBuilder = new StringBuilder(bufferSize+1000);
						
						String line = null;
						content = "";
						while ((line = reader.readLine())!= null) 
						{
							content += line;
							
						}
						reader.close();
						
						
						//set merged throwable
						Throwable merged = new Throwable(content);
						content = null;
						//sBuilder = null;
						//Throwable merged = new Throwable(failureMessage.toString());
						merged.setStackTrace(last.getStackTrace());
						last = null;
	 
						result.setThrowable(merged);
						merged = null;
						
					} 
					catch (IOException e) 
					{						
						e.printStackTrace();
					}
					
				}
			}
		
		}
		
	}
	
	
	//a utility to handle String Buffer
	private String concatenateStrings(List<String> items)
    {
        if (items == null)
            return null;
        if (items.size() == 0)
            return "";
        int expectedSize = 0;
        for (String item: items)
            expectedSize += item.length();
        StringBuffer result = new StringBuffer(expectedSize);
        for (String item: items)
            result.append(item);
        return result.toString();
    }
 
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {}
 
}
