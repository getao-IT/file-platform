package cn.aircas.fileManager;

import cn.aircas.fileManager.elec.service.ElecFileServiceImpl;
import me.walkerknapp.devolay.*;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class Recv1 {

    @Test
    public void testNDIRecv1() throws IOException {
        Devolay.loadLibraries();

        DevolayReceiver receiver = new DevolayReceiver(); // 14520320

        // Create a finder
        try(DevolayFinder finder = new DevolayFinder()) { // 14398992
            // Query for sources
            DevolaySource[] sources;
            while ((sources = finder.getCurrentSources()).length == 0) {
                // If none found, wait until the list changes
                System.out.println("Waiting for sources...");
                finder.waitForSources(5000);
            }

            // Connect to the first source found
            System.out.println("Connecting to source: " + sources[0].getSourceName());
            receiver.connect(sources[0]);
        }

        // Create initial frames to be used for capturing
        DevolayVideoFrame videoFrame = new DevolayVideoFrame();
        DevolayAudioFrame audioFrame = new DevolayAudioFrame();
        DevolayMetadataFrame metadataFrame = new DevolayMetadataFrame();

        // Run for one minute
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 1000 * 60 * 60) {
            // Capture with a timeout of 5000 milliseconds
            // This method now explicitly clears data from videoFrame, audioFrame, and metadataFrame that was previously allocated
            switch (receiver.receiveCapture(videoFrame, audioFrame, metadataFrame, 5000)) {
                case NONE:
                    System.out.println("No data received.");
                    break;
                case VIDEO:
                    System.out.println("Video data received (" + videoFrame.getXResolution() + "x" + videoFrame.getYResolution() + ", " +
                            videoFrame.getFrameRateN() + "/" + videoFrame.getFrameRateD() + ").");
                    ByteBuffer data = videoFrame.getData();
                    byte[] dst = new byte[data.capacity()];
                    data.get(dst);
                    String dstPath = "C:\\Users\\Administrator\\Desktop\\temp\\泰山.png";
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(dst);
                    FileOutputStream outputStream = new FileOutputStream(dstPath);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    System.out.println("接受成功：" + dstPath);
                    break;
                case AUDIO:
                    System.out.println("Audio data received (" + audioFrame.getSamples() + ", " + audioFrame.getChannelStride() + ").");
                    break;
                case METADATA:
                    System.out.println("Metadata received (" + metadataFrame.getData() + ").");
                    break;
            }

            if(receiver.getConnectionCount() < 1) {
                System.out.println("Lost connection.");
                break;
            }
        }

        try(DevolayPerformanceData performanceData = new DevolayPerformanceData()) {
            receiver.queryPerformance(performanceData);
            System.out.println("Dropped Video: " + performanceData.getDroppedVideoFrames() + "/" + performanceData.getTotalVideoFrames());
            System.out.println("Dropped Audio: " + performanceData.getDroppedAudioFrames() + "/" + performanceData.getTotalAudioFrames());
        }

        // Destroy the references to each. Not necessary, but can free up the memory faster than Java's GC by itself
        videoFrame.close();
        audioFrame.close();
        metadataFrame.close();
        receiver.close();
    }
}
