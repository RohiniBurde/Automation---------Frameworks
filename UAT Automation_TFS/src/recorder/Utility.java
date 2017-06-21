package recorder;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;

import com.uat.base.TestBase;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;
import java.awt.*;
import java.io.File;

public class Utility extends TestBase
{
	private ScreenRecorder screenRecorder;
	public void startRecording(String location) throws Exception
    {  
		
		String updatedLocation = location.split("Videos")[0]+"..\\"+CONFIG.getProperty("projectName")+"_Videos"+location.split("Videos")[1];
						
    	  File file = new File(updatedLocation);
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          int width = screenSize.width;
          int height = screenSize.height;               
          Rectangle captureSize = new Rectangle(0,0, width, height);
          //Create a instance of GraphicsConfiguration to get the Graphics configuration
          //of the Screen. This is needed for ScreenRecorder class.                    
          GraphicsConfiguration gc = GraphicsEnvironment
           .getLocalGraphicsEnvironment()
           .getDefaultScreenDevice()
           .getDefaultConfiguration();
          this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
          new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
          new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                QualityKey, 1.0f,
                KeyFrameIntervalKey, 15 * 60),
           new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                FrameRateKey, Rational.valueOf(30)),
           null, file, "Screen Recording");
           this.screenRecorder.start();
    }
    public void stopRecording() throws Exception
    {
      this.screenRecorder.stop();
    }
}
