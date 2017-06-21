package com.uat.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;

public class SetAndIterators{
	
	@Test
	public void test()
	{
		
		String[] newArray;
		
		String[] strArray = {"def", "def", "abc"};
		
		Set<String> setArray = new HashSet<String>() ;
		
		for (int i = 0; i < strArray.length; i++) 
		{
			setArray.add(strArray[i]);
		}
		
		newArray = new String[setArray.size()];
		
		Iterator itr = setArray.iterator();
		
		
		int i = 0;
		
		while (itr.hasNext()) 
		{			
			newArray[i] = (String) itr.next();			
			i++;
		}
		
		System.out.println(newArray[0]+newArray[1]);
		
		System.out.println(setArray.size());
		
		TreeSet sortedSet = new TreeSet<String>(setArray);
		
		System.out.println(sortedSet);
		
		setArray = null;
		
	}

}
