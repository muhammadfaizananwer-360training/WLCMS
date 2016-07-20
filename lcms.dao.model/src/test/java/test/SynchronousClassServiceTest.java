package test;

import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousSession;
import com.softech.ls360.lcms.contentbuilder.service.ISynchronousClassService;
import com.softech.ls360.lcms.contentbuilder.test.AbstractLcmsTest;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;



public class SynchronousClassServiceTest extends AbstractLcmsTest{
	
	
	@Autowired
	protected ISynchronousClassService synchronousClassService;
	
	
	/*@Test
	public void testUpdateSynchronousClassandSessions(){
		
		
		SynchronousClass synchronousClassPers = synchronousClassService.getSynchronousClassById(new Long(18736));
		synchronousClassPers.setStatus("U");
		
		//locationPers.setLocationname("Junit Testing....");
		
		// commented code using for invalid case.
		 List<SynchronousSession> list = new ArrayList<SynchronousSession>(synchronousClassPers.getSyncSession());
		 
		 for(SynchronousSession  synchronousSession : list){
			 if(synchronousSession.getId().equals(new Long(30617))){
				 synchronousSession.setStatus("U");
				 synchronousSession.setUpdateDate(new Date());
			 }
		 }
		 
		 synchronousClassService.saveSynchronousClass(synchronousClassPers);
		 
	     Assert.assertNotNull(synchronousClassPers);
		
		
	}*/
	
	
	@Test
	public void testDeleteSynchronousClassandSessions(){
		
		
		SynchronousClass synchronousClassPers = null;//synchronousClassService.getSynchronousClassById(new Long(18736));
		
		//locationPers.setLocationname("Junit Testing....");
		
		// commented code using for invalid case.
		//synchronousClassPers.setStatus("U");
		if(synchronousClassPers!=null) {
            synchronousClassPers.setDeleted(true);
            //synchronousClassPers.setUpdateDate(new Date());

            List<SynchronousSession> list = new ArrayList<SynchronousSession>(synchronousClassPers.getSyncSession());

            for (SynchronousSession synchronousSession : list) {
                if (synchronousSession.getId().equals(new Long(30614))) {
                    synchronousSession.setStatus("D");
                    //synchronousSession.setUpdateDate(new Date());
                }
            }

            //synchronousClassService.saveSynchronousClass(synchronousClassPers);

            Assert.assertNotNull(synchronousClassPers);
        }
		
	}
	
	

}
