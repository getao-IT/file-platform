package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.dao.UserUploadAuthMapper;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.database.UserUploadAuthInfo;
import cn.aircas.fileManager.web.utils.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class AuthServiceImpl {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private UserUploadAuthMapper authMapper;


    public void saveOrUpdateUserFileAuth (FileTransferParam fileTransferParam) {
        String userId = request.getParameter("userId");
        String adminLevel = request.getParameter("adminLevel");
        QueryWrapper<UserUploadAuthInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id" , Integer.parseInt(userId));
        UserUploadAuthInfo userUploadAuthInfo = authMapper.selectOne(queryWrapper);
        double size = Double.parseDouble(request.getParameter("size"));
        if (userUploadAuthInfo == null) {
            log.info("上传权限记录中 {}", userId);
            assert false;
            userUploadAuthInfo = new UserUploadAuthInfo();
            userUploadAuthInfo.setUserId(Integer.parseInt(userId));
            userUploadAuthInfo.setAdminLevel(adminLevel);
            userUploadAuthInfo.setFileCount(1);
            userUploadAuthInfo.setFileSize(size);
            authMapper.insert(userUploadAuthInfo);
        }else {
            userUploadAuthInfo.setFileCount(userUploadAuthInfo.getFileCount() + 1);
            userUploadAuthInfo.setFileSize(userUploadAuthInfo.getFileSize() + size);
            authMapper.updateById(userUploadAuthInfo);
        }
    }

    public void deleteToUpdateUserFileAuth (Image image) {
        String userId = request.getParameter("userId");
        String adminLevel = request.getParameter("adminLevel");
        if (Integer.parseInt(adminLevel) == 1) {
            QueryWrapper<UserUploadAuthInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", Integer.parseInt(userId));
            UserUploadAuthInfo userUploadAuthInfo = authMapper.selectOne(queryWrapper);

            userUploadAuthInfo.setFileCount(userUploadAuthInfo.getFileCount() - 1);
            if (userUploadAuthInfo.getFileCount() == 0) {
                userUploadAuthInfo.setFileSize(0);
            }else {
                userUploadAuthInfo.setFileSize(userUploadAuthInfo.getFileSize() - FileUtils.fileStringToSize(image.getSize()));
            }
            authMapper.updateById(userUploadAuthInfo);
        }
    }

    public void checkDeleteAuth () throws AuthException {
        String adminLevel = request.getParameter("adminLevel");
        if (Integer.parseInt(adminLevel) == 2) {
            throw new AuthException("抱歉，您当前没有删除权限");
        }
    }
}
