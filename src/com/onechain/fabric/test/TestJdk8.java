/**
 * 
 */
package com.onechain.fabric.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @author fengxiang
 *
 */
public class TestJdk8 {

	
	private static List<String> arrList = Arrays.asList("mark","jike","tom","kate","rober","aliex","phaha");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		stroe();
		
		for (String string : arrList) {
			System.out.println(string);
		}
        
	}
	
	
	
	public  static void stroe(){
		
		Collections.sort(arrList, (String a,String b)->{ return a.compareTo(b);});
	}

}
