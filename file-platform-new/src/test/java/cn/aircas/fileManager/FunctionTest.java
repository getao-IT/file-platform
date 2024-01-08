package cn.aircas.fileManager;

import cn.aircas.fileManager.elec.service.ElecFileServiceImpl;
import me.walkerknapp.devolay.*;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.List;



@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FunctionTest {


    @Test
    public void fourierTransFormDataTest() {
        ElecFileServiceImpl elecFileService = new ElecFileServiceImpl();
        double[] doubles = new double[8];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Double.parseDouble(new DecimalFormat("#.##").format(((Math.random()-0.5) * 100)));
        }
        List<Double> fourierTransformData = elecFileService.getFourierTransformData(doubles);
        System.out.println(fourierTransformData);
    }


    @Test
    public void buildOverviews() {

        gdal.SetConfigOption("GDAL_PAM_ENABLED", "FALSE");
        String imagePath = "C:\\Users\\Administrator\\Desktop\\temp\\target\\format\\Beijing_rural_copy.tif";
        int[] overviewLists = new int[]{2,4,8};
        Dataset dataset = gdal.Open(imagePath, gdalconstConstants.GA_Update);
        dataset.BuildOverviews(overviewLists);
        dataset.delete();
        System.out.println("构建成功");
    }


    @Test
    public void testSend() throws IOException, InterruptedException {
        Devolay.loadLibraries();

        // Create the sender using the default settings, other than setting a name for the source.
        DevolaySender sender = new DevolaySender("Devolay Example Video");

        final int width = 1919;
        final int height = 1021;

        String filePath = "C:\\Users\\Administrator\\Desktop\\泰山.png";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] fileByte = new byte[fileInputStream.available()];
        fileInputStream.read(fileByte);

        // BGRX has a pixel depth of 4
        final ByteBuffer data = ByteBuffer.allocateDirect(height * width * 24);
        //final ByteBuffer data = ByteBuffer.wrap(fileByte);
        data.put(fileByte);

        byte[] dst = new byte[fileByte.length];
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
        System.out.println("发送接受时-接受成功：" + dstPath);

        // Create a video frame
        DevolayVideoFrame videoFrame = new DevolayVideoFrame();
        videoFrame.setResolution(width, height);
        videoFrame.setFourCCType(DevolayFrameFourCCType.BGRX);
        videoFrame.setData(data);
        videoFrame.setFrameRate(1, 1);

        int expSendFrame = 1;
        int frameCounter = 0;
        long fpsPeriod = System.currentTimeMillis();

        // Run for ten minutes
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 1000 * 60 * 10 && frameCounter < expSendFrame) {

            //Fill in the buffer for one frame.
            //fillFrame(width, height, frameCounter, data);

            // Submit the frame. This is clocked by default, so it will be submitted at <= 60 fps.
            sender.sendVideoFrame(videoFrame);

            // Give an FPS message every 30 frames submitted
            if(frameCounter % 30 == 29) {
                long timeSpent = System.currentTimeMillis() - fpsPeriod;
                System.out.println("Sent 30 frames. Average FPS: " + 30f / (timeSpent / 1000f));
                fpsPeriod = System.currentTimeMillis();
            }

            frameCounter++;
            System.out.println("已发送数据：" + frameCounter + " / " + expSendFrame);
        }

        /*// BGRX has a pixel depth of 4
        String filePath = "C:\\Users\\Administrator\\Desktop\\虹之间.mp4";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] fileByte = new byte[fileInputStream.available()];
        fileInputStream.read(fileByte);
        ByteBuffer data = ByteBuffer.wrap(fileByte);

        // Create a video frame
        DevolayVideoFrame videoFrame = new DevolayVideoFrame();
        videoFrame.setResolution(width, height);
        videoFrame.setFourCCType(DevolayFrameFourCCType.BGRX);
        videoFrame.setData(data);
        videoFrame.setFrameRate(60, 1);
        sender.sendVideoFrame(videoFrame);*/

        // Destroy the references to each. Not necessary, but can free up the memory faster than Java's GC by itself
        Thread.sleep(6000);
        videoFrame.close();
        sender.close();
    }

    private static void fillFrame(int width, int height, int frameCounter, ByteBuffer data) {
        data.position(0);
        double frameOffset = Math.sin(frameCounter / 120d);
        for(int i = 0; i < width * height; i++) {
            double xCoord = i % width;
            double yCoord = i / (double)width;

            double convertedX = xCoord/width;
            double convertedY = yCoord/height;

            double xWithFrameOffset = convertedX + frameOffset;
            double xWithScreenOffset = xWithFrameOffset - 1;
            double yWithScreenOffset = convertedY + 1;

            double squaredX = xWithFrameOffset * xWithFrameOffset;
            double offsetSquaredX = xWithScreenOffset * xWithScreenOffset;
            double squaredY = convertedY * convertedY;
            double offsetSquaredY = yWithScreenOffset * yWithScreenOffset;

            byte r = (byte) (Math.min(255 * Math.sqrt(squaredX + squaredY), 255));
            byte g = (byte) (Math.min(255 * Math.sqrt(offsetSquaredX + squaredY), 255));
            byte b = (byte) (Math.min(255 * Math.sqrt(squaredX + offsetSquaredY), 255));

            data.put(b).put(g).put(r).put((byte)255);
        }
        data.flip();
    }
}
