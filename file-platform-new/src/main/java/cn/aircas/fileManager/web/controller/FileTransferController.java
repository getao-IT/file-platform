package cn.aircas.fileManager.web.controller;


import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import cn.aircas.fileManager.web.entity.FileBackendTransferProgress;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.web.service.FileTransferService;
import cn.aircas.utils.date.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件传输接口
 */
@RestController
@Api(tags = "文件传输接口")
@RequestMapping("/transfer")
@Slf4j
public class FileTransferController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    FileTransferService fileTransferService;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 后台上传文件
     * @param fileTransferInfo 影像上传参数
     * @return
     */
    @Log(value = "后台上传文件")
    //@OperationLog(value = "后台上传文件")
    @ApiOperation("后台上传文件")
    @PostMapping("/backend")
    public CommonResult<String> transferFromBackend(@RequestBody FileTransferInfo fileTransferInfo) {
        String token = request.getHeader("token");
        fileTransferInfo.setToken(token);
        fileTransferService.backendTransfer(fileTransferInfo);
        return new CommonResult<String>().success().message("上传影像成功");
    }

    @Log(value = "提交文件上传信息")
    //@OperationLog("提交影像上传信息")
    @PostMapping(value = "/commit")
    @ApiOperation("提交文件上传信息")
    public CommonResult<Integer> commitTransferInfo(FileTransferInfo fileTransferInfo){
        int fileTransferId = this.fileTransferService.commitTransferInfo(fileTransferInfo);
        return new CommonResult<Integer>().data(fileTransferId).success().message("提交文件上传信息成功");
    }


    @Log(value = "验证文件md5")
    //@OperationLog("验证md5")
    @PostMapping(value = "/checkFileMd5")
    @ApiOperation("验证文件md5")
    public CommonResult<String> checkFileMd5(String fileMD5, int fileTransferId){
        this.fileTransferService.checkFileMd5(fileMD5,fileTransferId);
        return new CommonResult<String>().data(null).success().message("验证文件md5成功");
    }

    @Log(value = "分块上传影像")
    //@OperationLog("分块上传影像")
    @PostMapping(value = "/byChuck")
    @ApiOperation("分块上传影像")
    public CommonResult<FileTransferParam> transferFromWeb(HttpServletRequest request, FileTransferParam fileTransferParam) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
            return new CommonResult<FileTransferParam>().data(null).fail().message("请选择上传文件");

        this.fileTransferService.transferFromWeb(fileTransferParam);
        return new CommonResult<FileTransferParam>().success().message("上传分块结束");
    }

    @Log(value = "更新文件上传状态")
    @PutMapping(value = "/updateUploadStatus")
    @ApiOperation("更新文件上传状态")
    public CommonResult<String> updateUploadStatus(String fileName) throws Exception {
        int fileId = 0;
        if (fileName.equalsIgnoreCase("nigulayefujichang.tif")) {
            fileId = 1977;
        }
        if (fileName.equalsIgnoreCase("JL1KF02B02_200383366_001_L5D_PSH.tif")) {
            fileId = 1997;
        }
        if (fileName.equalsIgnoreCase("hengxuhe_sar.tiff")) {
            fileId = 1968;
        }
        if (fileName.equalsIgnoreCase("songshan_kjg_1.tif")) {
            fileId = 1963;
        }
        if (fileName.equalsIgnoreCase("GF01_WF4_062299_20241116_MY8M2_01_022_L1A_01.browse.tif")) {
            fileId = 1939;
        }
        Image image = imageMapper.selectById(fileId);
        image.setDelete(false);
        image.setCreateTime(DateUtils.nowDate());
        this.imageMapper.updateById(image);
        log.info("文件 {} 自主上传完成", fileName);
        return new CommonResult<String>().success().data(fileName).message("更新文件上传状态成功");
    }

    /**
     * 获取后台上传进度信息
     * @param transferToken
     * @return
     */
    @Log(value = "获取后台上传进度信息")
    @ApiOperation("获取后台上传进度信息")
    @GetMapping("/status")
    public CommonResult<FileBackendTransferProgress> getTransferProgress(String transferToken) {
        FileBackendTransferProgress fileBackendTransferProgress = fileTransferService.getBackendTransferProgress(transferToken);
        return new CommonResult<FileBackendTransferProgress>().data(fileBackendTransferProgress).success().message("获取后台上传进度信息成功");
    }

    /**
     * 文件下载
     * @param
     * @return
     */
    @Log(value = "根据id下载文件")
    //@OperationLog(value = "下载影像")
    @ApiOperation("根据id下载文件")
    @GetMapping("/download/{id}")
    public CommonResult<String> download(@PathVariable("id") int id, FileType fileType) {
        String filePath = this.fileTransferService.download(id,fileType);
        return new CommonResult<String>().success().data(filePath).message("根据id下载文件");
    }
}
