package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.image.service.ImageTransferService;
import cn.aircas.fileManager.web.service.FileProcessingService;
import cn.aircas.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileProcessingServiceImpl implements FileProcessingService {
    @Value("${sys.rootPath}")
    public String rootPath;

    @Autowired
    ImageMapper imageMapper;
    @Autowired
    ImageTransferService imageTransferService;

    @Override
    public void formatConverter(int fileId, String format) {
        Image srcimage = this.imageMapper.selectById(fileId);
        String inputPath = srcimage.getPath();

        String outputPath = srcimage.isPublic() ? FileUtils.getStringPath("file-data","image", System.currentTimeMillis()) :
                FileUtils.getStringPath("user", srcimage.getUserId(), "file-data", "image",System.currentTimeMillis());

        gdal.AllRegister();
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        Dataset ds = gdal.Open(inputPath, gdalconstConstants.GA_ReadOnly);
        if(ds == null){
            System.err.println("GDALOpen failed-"+gdal.VSIGetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            System.exit(1);
        }

        Driver hDriver = gdal.GetDriverByName(format);
        System.out.println("Driver:"+hDriver.getShortName()+"/"+hDriver.getLongName());
        hDriver.CreateCopy(outputPath,ds);
        ds.delete();
        hDriver.delete();

        String filePath = FileUtils.getStringPath(this.rootPath, outputPath);
        Image image = this.imageTransferService.parseFileInfo(filePath);
        this.imageMapper.insert(image);
        log.info("影像格式转换成功");

    }
}
