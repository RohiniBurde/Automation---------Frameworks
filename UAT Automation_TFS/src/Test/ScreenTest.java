package Test;

import org.testng.annotations.Test;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;
import recorder.Utility;

public class ScreenTest extends TestBase{
	
	Utility utilRecorder = new Utility();
	
	@Test
	public void test() throws Exception
	{
		initialize();
		openBrowser();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
		TestUtil.takeScreenShot("test", "test");
		closeBrowser();
	}

}
