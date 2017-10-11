/**
 * 
 */
package com.onechain.fabric.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


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
		
//		stroe();
//		
//		for (String string : arrList) {
//			System.out.println(string);
//		}
		
		//System.setProperty("org.hyperledger.fabric.sdk.configuration", "/project/javaworkspace/fabric-sdk-java-sample/src/org/hyperledger/fabric/sdk/configuration");
		
		
		testfile();
		
		
        
	}
	
	
	
	public  static void stroe(){
		
		Collections.sort(arrList, (String a,String b)->{ return a.compareTo(b);});
	}

	
	
	public static void testfile(){
		
		Properties sdkProperties = new Properties();
		FileInputStream configProps;
		
		String filepath = System.getProperty("org.hyperledger.fabric.sdk.configuration", "config.properties");
		System.out.println( filepath );
		File loadFile;
		loadFile = new File(filepath).getAbsoluteFile();
		
		try {
			
			
			configProps = new FileInputStream(loadFile);
			sdkProperties.load(configProps);
			
			System.out.println( "ddd" );
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	
}
