package cn.aircas.fileManager;

import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.elec.service.ElecFileServiceImpl;
import org.apache.commons.math3.complex.Complex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

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



}
