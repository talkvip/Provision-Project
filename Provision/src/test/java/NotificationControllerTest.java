import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.cpn.vsp.provision.notify.NotificationController;


public class NotificationControllerTest {

	@Test
	public void testNotificationControllerChange() throws Exception{
		assertNotNull(NotificationController.getEventForToken("https://www.appdirect.com/rest/api/events/dummyChange"));
	}
	
	@Test
	public void testNotificationControllerOrder() throws Exception{
		assertNotNull(NotificationController.getEventForToken("https://www.appdirect.com/rest/api/events/dummyOrder"));
	}
	
	@Test
	public void testNotificationControllerCancel() throws Exception{
		assertNotNull(NotificationController.getEventForToken("https://www.appdirect.com/rest/api/events/dummyCancel"));
	}
	
	@Test
	public void testNotificationControllerUserAssign() throws Exception{
		assertNotNull(NotificationController.getEventForToken("https://www.appdirect.com/rest/api/events/dummyAssign"));
	}
	
	@Test
	public void testNotificationControllerUserUnassign() throws Exception{
		assertNotNull(NotificationController.getEventForToken("https://www.appdirect.com/rest/api/events/dummyUnassign"));
	}
	
	

}
