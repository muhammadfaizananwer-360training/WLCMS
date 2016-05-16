package com.softech.ls360.lcms.contentbuilder.service.test;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	
	public static boolean wasRun;	
	
    public AppTest( String testName )
    {
        super( testName );
    }

 
  public static Test suite()
    {
    	TestSuite suite = new TestSuite("");
    	    	
    	//suite.addTest(new TestJunit1("AbdusTest"));
    	//suite.addTestSuite(TestJunit2.class);
    	suite.addTest(new  AppTest("notObviouslyATest"));
    	
    	return suite;
    }
    
  public void notObviouslyATest() {
	                wasRun = true;
	           }
 }

