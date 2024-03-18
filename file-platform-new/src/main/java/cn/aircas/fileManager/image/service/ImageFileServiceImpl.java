package cn.aircas.fileManager.image.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.image.entity.ImageSearchParam;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.web.service.UserInfoService;
import cn.aircas.fileManager.web.service.impl.AuthServiceImpl;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import cn.aircas.utils.image.ImageInfo;
import cn.aircas.utils.image.ParseImageInfo;
import cn.aircas.utils.image.slice.CreateThumbnail;
import cn.aircas.utils.image.slice.SliceGenerateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.imageio.ImageIO;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;



@Service("IMAGE-SERVICE")
@Slf4j
public class ImageFileServiceImpl extends ServiceImpl<ImageMapper, Image> implements FileTypeService {

    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    private ImageTransferService imageTransferService;

    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    HttpServletRequest request;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public String downloadFileById(int fileId) {
        Image image = this.imageMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath, image.getPath());
    }

    /**
     * 根据id批量删除影像文件
     *
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) throws AuthException {
        List<Image> imageInfoList = this.imageMapper.selectBatchIds(idList);
        Assert.notEmpty(imageInfoList, String.format("影像的路径列表:%s为空", imageInfoList.toString()));
        imageInfoList.forEach(image -> {
            image.setDelete(true);
            authService.deleteToUpdateUserFileAuth(image);
        });

        this.updateBatchById(imageInfoList);
//        for (Image imageInfo : imageInfoList) {
//            String filePath = FileUtils.getStringPath(this.rootPath, imageInfo.getPath());
//            FileUtils.forceDelete(filePath);
//        }
//        List<Integer> imageIdList = imageInfoList.stream().map(Image::getId).collect(Collectors.toList());
//        this.imageMapper.deleteBatchIds(imageIdList);
    }

    /**
     * 根据id批量获取文件信息
     *
     * @param fileIdList
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList, boolean content) {
        List<Image> imageList = this.imageMapper.selectBatchIds(fileIdList);
        return imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    @Override
    public int getFileUserId(int fileId) {
        Image image = this.imageMapper.selectById(fileId);
        return image.getUserId();
    }

    /**
     * 更新文件信息
     *
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<Image> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getDescription() != null, "description", fileInfo.getDescription())
                .set(fileInfo.getKeywords() != null, "keywords", fileInfo.getKeywords())
                .set(fileInfo.getIsPublic() != null, "is_public", fileInfo.getIsPublic())
                .set(fileInfo.getSource() != null, "source", fileInfo.getSource()).in("id", fileIdList);
        this.update(updateWrapper);

    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
        return this.imageMapper.listImageIdBySearchParam(imageSearchParam);
    }

    /**
     * 分页查询影像信息-真实查询
     * @param fileSearchParam
     * @return
     */
    /*@Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
        Page<Image> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        IPage<Image> imageIPage = this.imageMapper.listImageInfosByPage(page, imageSearchParam);
        List<Image> imageList = imageIPage.getRecords();
        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(imageIPage.getCurrent(), result, imageIPage.getTotal());
    }*/

    /**
     * 分页查询影像信息-真实查询
     *
     * @param fileSearchParam
     * @return
     */
    public PageResult<JSONObject> relateFind(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
        Page<Image> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        JSONObject userInfo = this.userInfoService.getUserInfoByToken(request.getHeader("token")).getData();
        imageSearchParam.setUserId(userInfo.getInteger("id"));
        IPage<Image> imageIPage = this.imageMapper.listImageInfosByPage(page, imageSearchParam , userInfo.getString("admin_level"));
        List<Image> imageList = imageIPage.getRecords();
        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(imageIPage.getCurrent(), result, imageIPage.getTotal());
    }

    /**
     * 分页查询影像信息-j查询
     *
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        IPage<Image> imageIPage = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        List<JSONObject> result = new ArrayList<>();
        if (!fileSearchParam.isIstest()) {
            return this.relateFind(fileSearchParam);
        } else {
            long totalCount = 10000000030l;
            Integer selectCount = this.imageMapper.selectCount(new QueryWrapper<Image>()
                    .select("id").eq("delete", false).and(qw -> {
                        qw.eq("user_id", fileSearchParam.getUserId()).or().eq("is_public", true).or(fileSearchParam.getAdminLevel().equals("0"));
                    }));
            if (selectCount == 0) {
                return new PageResult<>(0, result, 0);
            }
            int pages = selectCount % fileSearchParam.getPageSize() == 0 ? selectCount / fileSearchParam.getPageSize()
                    : selectCount / fileSearchParam.getPageSize() + 1;
            if (fileSearchParam.getPageNo() > pages) {
                fileSearchParam.setPageNo(fileSearchParam.getPageNo() % pages == 0 ? pages : fileSearchParam.getPageNo() % pages);
            }
            String adminLevel = request.getParameter("adminLevel");
            ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
            Page<Image> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
            imageIPage = this.imageMapper.listImageInfosByPage(page, imageSearchParam , adminLevel);
            List<Image> imageList = imageIPage.getRecords();
            result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
            if (imageIPage.getTotal() == selectCount) {
                imageIPage.setTotal(totalCount);
            }
        }

        return new PageResult<>(imageIPage.getCurrent(), result, imageIPage.getTotal());
    }

    /**
     * 转换查询参数
     *
     * @param fileSearchParam
     * @return
     */
    private ImageSearchParam convertSearchParam(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = new ImageSearchParam();
        BeanUtils.copyProperties(fileSearchParam, imageSearchParam);
        //imageSearchParam.setAdminLevel(Integer.parseInt(fileSearchParam.getAdminLevel()));
        imageSearchParam.setImageIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)) {
            List<String> params = Arrays.asList(searchParam.split(" "));
            imageSearchParam.setSearchParamList(params);
        }

        imageSearchParam.setImageName(fileSearchParam.getFileName());
        return imageSearchParam;
    }

    /**
     * 列出所有影像的中心坐标点
     *
     * @return
     */
    public JSONArray listImageCoordinate() {
        JSONArray result = new JSONArray();
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("min_lon", "min_lat", "max_lon", "max_lat", "id", "image_name");
        List<Image> imageList = this.list(queryWrapper);

        for (Image image : imageList) {
            JSONObject imageInfo = new JSONObject();
            double centerLat = (image.getMinLat() + image.getMaxLat()) / 2;
            double centerLon = (image.getMinLon() + image.getMaxLon()) / 2;

            imageInfo.put("id", image.getId());
            imageInfo.put("centerLat", centerLat);
            imageInfo.put("centerLon", centerLon);
            imageInfo.put("name", image.getImageName());
            result.add(imageInfo);
        }

        return result;
    }

    /**
     * 不保留经纬度信息，单纯裁切影像得到切片图片
     * @param fileType
     * @param path
     * @param beginX
     * @param beginY
     * @param width
     * @param height
     * @return
     */
    public Boolean makeImageSlice(FileType fileType, String path, int beginX, int beginY, int width, int height) {
        String filePath = FileUtils.getStringPath(this.rootPath, path);
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedImage imageRead = ImageIO.read(fileInputStream);
            BufferedImage bufferedImage = new BufferedImage(width, height, imageRead.getType());
            Graphics2D graphics = bufferedImage.createGraphics();
            int dx1 = 0;
            int dy1 = 0;
            int dx2 = beginX + width;
            int dy2 = beginY + height;
            int sx1 = 1000;
            int sy1 = 1000;
            int sx2 = width;
            int sy2 = height;
            graphics.drawImage(imageRead, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            graphics.dispose();
            String extend = file.getName().substring(file.getName().lastIndexOf("."));
            String savePath1 = FileUtils.getStringPath(file.getParentFile().getPath(), "slice1") + extend;
            ImageIO.write(bufferedImage, extend.replace(".", ""), new File(savePath1));
            System.out.println("Slice ok : " + savePath1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保留经纬度信息，裁切影像所有位置得到切片图片
     * @param fileType
     * @param id
     * @param width
     * @param height
     * @param sliceInsertPath
     * @return
     */
    @Async
    public void makeImageAllGeoSlice(FileType fileType, int id, int width, int height, String sliceInsertPath, int step, Boolean storage) {
        List<String> slicePathList = new ArrayList<>();
        Image image = this.getById(id);
        String filePath = FileUtils.getStringPath(this.rootPath, image.getPath());
        //this.rootPath = "C:\\Users\\dell\\Desktop";
        //String filePath = "C:\\Users\\dell\\Desktop\\image\\3.tiff";
        File file = new File(filePath);
        String savePath = FileUtils.getStringPath(this.rootPath, sliceInsertPath, file.getName()+"_all", System.currentTimeMillis()) + "/";
        String extend = file.getName().substring(file.getName().lastIndexOf("."));
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        /*int imageWidth = 10000;
        int imageHeight = 10000;*/
        int currentWidth = 0;
        int currentHeight = 0;
        int nextWidth = 0;
        int nextHeight = 0;
        step = (step == 0 || step < width) ? width : step;
        for (int i = 0; i < imageWidth; i+=step) {
            nextWidth = (nextWidth + width) > imageWidth ? imageWidth : (nextWidth + width);
            for (int j = 0; j < imageHeight; j+=height) {
                nextHeight = (nextHeight + height) > imageHeight ? imageHeight : (nextHeight + height);
                sliceInsertPath = savePath + file.getName() + "_" + currentWidth + "-" + currentHeight + extend;
                double[] range = new double[]{currentWidth, currentHeight, nextWidth, nextHeight};
                SliceGenerateUtil.generateSlice(range, filePath, sliceInsertPath, true);
                slicePathList.add(sliceInsertPath);
                currentHeight = nextHeight;
                // 切片入库
                if (storage) {
                    Image sliceImage = this.parseFileInfo(sliceInsertPath, image);
                    this.save(sliceImage);
                }
                sliceInsertPath = savePath;
            }
            currentWidth = nextWidth;
            currentHeight = 0;
            nextHeight = 0;
        }
        log.info("生成切片成功，路径：{}", savePath);
    }

    /**
     * 保留经纬度信息，裁切影像得到切片图片
     * @param fileType
     * @param id
     * @param minLon
     * @param minLat
     * @param width
     * @param height
     * @param sliceInsertPath
     * @return
     */
    @Async
    public void makeImageGeoSlice(FileType fileType, int id, double minLon, double minLat, int width, int height, String sliceInsertPath, Boolean storage) {
        Image image = this.getById(id);
        String filePath = FileUtils.getStringPath(this.rootPath, image.getPath());
        //String filePath = "C:\\Users\\dell\\Desktop\\image\\3.tiff";
        File file = new File(filePath);
        double minX = minLon;
        double minY = minLat;
        double maxX = minLon + width;
        double maxY = minLat + height;
        String extend = file.getName().substring(file.getName().lastIndexOf("."));
        sliceInsertPath = FileUtils.getStringPath(this.rootPath, sliceInsertPath, file.getName(), System.currentTimeMillis()) + "/"
                + file.getName() + ".sub" + extend;
        //sliceInsertPath = "C:\\Users\\dell\\Desktop\\image\\3-slice1.tiff";
        double[] range = new double[]{minX, minY, maxX, maxY};
        SliceGenerateUtil.generateSlice(range, filePath, sliceInsertPath, true);

        // 切片入库
        if (storage) {
            Image sliceImage = this.parseFileInfo(sliceInsertPath, image);
            this.save(sliceImage);
        }
        log.info("生成切片成功，路径：{}, ", sliceInsertPath);
    }

    private Image parseFileInfo(String filePath, Image sourceImage) {
        File imageFile = new File(filePath);

        String thumbnailPath = FileUtils.getStringPath(imageFile.getParentFile().getAbsolutePath(),
                "thumbnail_" + FilenameUtils.removeExtension(imageFile.getName()) + ".jpg");
        String fileSize = FileUtils.fileSizeToString(imageFile.length());
        String thumbnail = CreateThumbnail.createBase64Thumbnail(filePath, thumbnailPath, 256);

        String relativeFilePath = filePath.substring(this.rootPath.length());
        if (relativeFilePath.startsWith("/"))
            relativeFilePath = relativeFilePath.substring(1);
        ImageInfo imageInfo = ParseImageInfo.parseInfo(filePath);
        Image image = null;
        try {
            image = Image.builder().imageName(imageFile.getName()).createTime(DateUtils.nowDate()).path(relativeFilePath)
                    .thumb(thumbnail).size(fileSize).fileLength(imageFile.length()).minProjectionX(imageInfo.getProjectionRange()[0])
                    .minProjectionY(imageInfo.getProjectionRange()[1]).maxProjectionX(imageInfo.getProjectionRange()[2])
                    .maxProjectionY(imageInfo.getProjectionRange()[3]).isPublic(sourceImage.isPublic()).userId(sourceImage.getUserId())
                    .userName(sourceImage.getUserName()).keywords(sourceImage.getKeywords()).delete(false).build();
        } catch (Exception e) {
            log.error("文件 {} 影像信息解析失败");
            return null;
        }
        BeanUtils.copyProperties(imageInfo,image);

        return image;
    }


    public static void main(String[] args) {
        /*String dateTime="2020-01-13T16:00:00.000Z";
        dateTime=dateTime.replace("Z","UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = format.parse(dateTime);
            //Date time = format.parse(dateTime);
            String result = dateFormat.format(parse);
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        ImageFileServiceImpl service = new ImageFileServiceImpl();
        String path = "image\\3.tiff";
        service.makeImageGeoSlice(FileType.IMAGE, 1, 500, 500, 2000, 2000, "", true);
        //service.makeImageAllGeoSlice(FileType.IMAGE, 1, 2000, 2000, "image\\slice\\", 2000);
        System.out.println("后台处理中。。。");
    }

}

