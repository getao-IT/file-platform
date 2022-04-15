package cn.aircas.fileManager.web.controller;

import cn.aircas.fileManager.elec.service.ElecFileServiceImpl;
import cn.aircas.fileManager.image.service.ImageFileServiceImpl;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.fileManager.web.service.FileService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "文件管理接口")
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    ImageFileServiceImpl imageFileService;

    /**
     * 查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Log(value = "分页查询文件信息")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping
    @ApiOperation("分页查询文件信息")
    public CommonResult<PageResult<JSONObject>> getFileInfoByPage(FileSearchParam fileSearchParam) {
        PageResult<JSONObject> pageResult = this.fileService.listFileByPage(fileSearchParam);
        return new CommonResult<PageResult<JSONObject>>().success().data(pageResult).message("分页查询文件信息成功");
    }

    /**
     * 查询文件详情信息
     * @param pageSize
     * @param pageNo
     * @param fileType
     * @param fileId
     * @return
     */
    @Log(value = "查询文件详情信息")
    @GetMapping("/getFileContent")
    @ApiOperation("查询文件详情信息")
    public CommonResult<PageResult<JSONObject>> getFileContent(int pageSize, int pageNo,FileType fileType, int fileId) {
        PageResult<JSONObject> pageResult = this.fileService.getContent(pageSize, pageNo, fileType, fileId);
        return new CommonResult<PageResult<JSONObject>>().success().data(pageResult).message("查询文件详情信息成功");
    }

    /**
     * 根据id批量删除文件
     * @param idList
     * @param fileType
     * @return
     */
    @Log(value = "根据id批量删除文件")
    //@OperationLog(value = "分页查询影像信息")
    @DeleteMapping
    @ApiOperation("根据id批量删除文件")
    public CommonResult<String> deleteFileByIds(@RequestParam("idList") List<Integer> idList, FileType fileType) {
        this.fileService.deleteFilesByIds(idList,fileType);
        return new CommonResult<String>().success().message("根据id批量删除文件成功");
    }

    /**
     * 根据id单个或者批量获取文件信息
     * @param fileIdList
     * @param fileType
     * @return
     */
    @Log(value = "根据id单个或者批量获取文件信息")
    //@OperationLog(value = "根据id单个或者批量获取影像信息")
    @GetMapping("/{fileIdList}")
    @ApiOperation("根据id单个或者批量获取文件信息")
    public CommonResult<List<JSONObject>> getFileInfoByIdList(@PathVariable("fileIdList") List<Integer> fileIdList, FileType fileType, @RequestParam(defaultValue = "false") boolean content) {
        List<JSONObject> imageList = fileService.getFileInfoByIds(fileIdList,fileType,content);
        return new CommonResult<List<JSONObject>>().data(imageList).success().message("根据id获取影像数据成功");
    }



    @Log(value = "根据条件查询全部影像ID")
    @GetMapping("/listId")
    @ApiOperation("根据条件查询全部影像ID")
    public CommonResult<List<Integer>> listFileInfoBySearchParam(FileSearchParam fileSearchParam) {
        List<Integer> fileIdList = this.fileService.listFileIdBySearchParam(fileSearchParam);
        return new CommonResult<List<Integer>>().data(fileIdList).success().message("根据条件查询全部文件ID");
    }



    /**
     * 文件下载
     * @param
     * @return
     */
    @Log(value = "根据id修改文件信息")
    //@OperationLog(value = "下载影像")
    @ApiOperation("根据id修改文件信息")
    @PutMapping("/{fileIdList}")
    public CommonResult<String> updateFileInfo(@PathVariable("fileIdList")List<Integer> fileIdList, FileType fileType, FileInfo fileInfo) {
        this.fileService.updateFileInfo(fileIdList,fileType,fileInfo);
        return new CommonResult<String>().success().message("根据id修改文件信息成功");
    }

    /**
     * 获取子文件夹list
     * @param
     * @return
     */
    @ApiOperation("获取子文件夹list")
    @Log(value = "获取子文件夹list")
    //@OperationLog("获取子文件夹list")
    @GetMapping("/folder")
    public CommonResult<List<String>> getFolderList(String path) {
        List<String> folderList = fileService.listFolderFiles(path);
        return new CommonResult<List<String>>().data(folderList).success().message("获取子文件夹数据成功");
    }

    /**
     * 获取子文件夹list
     * @param
     * @return
     */
    @ApiOperation("获取支持的文件类型列表")
    @Log(value = "获取支持的文件类型列表")
    //@OperationLog("获取子文件夹list")
    @GetMapping("/type")
    public CommonResult<List<String>> getFileType(){
        List<String> fileTypeList = this.fileService.getFileType();
        return new CommonResult<List<String>>().data(fileTypeList).success().message("获取支持的文件类型列表");
    }

    /**
     * 查询所有影像坐标信息
     * @param
     * @return
     */
    @Log(value = "查询所有影像坐标信息")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping("/image/coordinate")
    @ApiOperation("查询所有影像坐标信息")
    public CommonResult<JSONArray> listImageCoordinate() {
        JSONArray result = this.imageFileService.listImageCoordinate();
        return new CommonResult<JSONArray>().success().data(result).message("查询所有影像坐标信息信息成功");
    }

}
