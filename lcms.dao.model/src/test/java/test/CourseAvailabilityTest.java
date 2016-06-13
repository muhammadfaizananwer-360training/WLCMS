package test;

import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.service.IPublishingService;
import com.softech.ls360.lcms.contentbuilder.test.AbstractLcmsTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class CourseAvailabilityTest extends AbstractLcmsTest{


	private static Logger logger = Logger.getLogger(CourseAvailabilityTest.class);
	
	@Autowired
	IPublishingService publishingService;
	
	@Test
	public void testCourseAvailablitySettingsRecordExist() throws NumberFormatException, SQLException{
		
		Object obj = null;
		long courseId = 113772;
	
			try {
				obj =  publishingService.getCourseAvailability(courseId);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			if(obj == null){
				System.out.println("OBJECT IS NULL");
			}
			assertNotNull(obj);
	}
	
	@Test
	public void testCourseAvailablitySettingsRecordUpdate() throws NumberFormatException, SQLException{
		
		String courseId = "113772";
		String industry = "All";
		String standardCourseGrpId = null;
		String courseExpiration = "02-20-2015";
		String learnerAccessToCourse = "85";
		boolean mobileTablet = true;
		boolean subscription = true;
		boolean varResale = true;
		boolean distributionSCORM = true;
		boolean distributionAICC = true;
		boolean reportingToRegulator = true;
		boolean requireShippable = true;
		boolean thirdPartyCourse = true;
		String userId = "11";
		
		CourseAvailability availability = new CourseAvailability();
		availability.setId(Integer.parseInt(courseId));
		availability.setIndustry(industry);
		availability.setCourseGroupId(standardCourseGrpId);
		availability.setCourseExpiration(courseExpiration);
		availability.setLearnerAccessToCourse((learnerAccessToCourse == null || learnerAccessToCourse.length() == 0) ? "365" : learnerAccessToCourse);
		availability.setEligibleForMobileTablet(mobileTablet);
		availability.setEligibleForSubscription(subscription);
		availability.setEligibleForVARresale(varResale);
		availability.setEligibleForDistrThruSCORM(distributionSCORM);
		availability.setEligibleForDistrThruAICC(distributionAICC);
		availability.setRequireReportToRegulator(reportingToRegulator);
		availability.setRequireShippableItems(requireShippable);
		availability.setIsThirdpartyCourse(thirdPartyCourse);
		availability.setUpdatedBy(String.valueOf(userId));
		Boolean isUpdated = false;
		
		try{
			isUpdated = publishingService.updateCourseAvailability(availability);
		}
		catch (Exception e){
			logger.error(e.toString());
		}
		
		CourseAvailability availabilityNew = new CourseAvailability();
		try {
			availabilityNew =  publishingService.getCourseAvailability(Long.parseLong(courseId));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if(availabilityNew == null){
			System.out.println("OBJECT IS NULL");
		}
		
		logger.error(isUpdated);
		
		assertNotNull(availabilityNew);
		assertTrue(availabilityNew.getIndustry().equals(availability.getIndustry()));
		assertTrue(availabilityNew.getLearnerAccessToCourse().equals(availability.getLearnerAccessToCourse()));
		assertTrue(availabilityNew.getEligibleForMobileTablet() == availability.getEligibleForMobileTablet());
		assertTrue(availabilityNew.getEligibleForDistrThruAICC() == availability.getEligibleForDistrThruAICC());
		assertTrue(availabilityNew.getEligibleForDistrThruSCORM() == availability.getEligibleForDistrThruSCORM());
		assertTrue(availabilityNew.getEligibleForSubscription() == availability.getEligibleForSubscription());
		assertTrue(availabilityNew.getEligibleForVARresale() == availability.getEligibleForVARresale());
		assertTrue(availabilityNew.getIsThirdpartyCourse() == availability.getIsThirdpartyCourse());
		assertTrue(availabilityNew.getRequireReportToRegulator() == availability.getRequireReportToRegulator());
		assertTrue(availabilityNew.getRequireShippableItems() == availability.getRequireShippableItems());
	}
}
