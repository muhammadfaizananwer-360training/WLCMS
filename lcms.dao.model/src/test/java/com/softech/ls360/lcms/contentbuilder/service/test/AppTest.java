package com.softech.ls360.lcms.contentbuilder.service.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	/*String GUID = UUID.randomUUID().toString().replaceAll("-", "");
    	CourseDAO courseDao = new CourseDAOImpl();
    	courseDao.addScene("name", 1, GUID, 111466, 11, 117970);*/
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}

