package cn.aircas.fileManager.commons.aspect;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.web.dao.UserUploadAuthMapper;
import cn.aircas.fileManager.web.entity.database.UserUploadAuthInfo;
import cn.aircas.utils.file.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.security.auth.message.AuthException;

@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private UserUploadAuthMapper authMapper;

    @Pointcut("@annotation(cn.aircas.fileManager.commons.annnotation.AuthLog)")
    public void AuthPointcut() {
    }

    @Before("AuthPointcut()")
    public void authBefore(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String adminLevel = ((StandardMultipartHttpServletRequest) args[0]).getRequest().getParameter("adminLevel");
        String userId = ((StandardMultipartHttpServletRequest) args[0]).getRequest().getParameter("userId");

        if ("1".equals(adminLevel)) {
            String size = ((StandardMultipartHttpServletRequest) args[0]).getRequest().getParameterMap().get("size")[0];
            if (Double.parseDouble(size) > 500 * 1014 * 1024) {
                throw new AuthException("当前上传文件过大，上传文件不得大于500MB");
            }

            QueryWrapper<UserUploadAuthInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", Integer.parseInt(userId));

            UserUploadAuthInfo userUploadAuthInfo = authMapper.selectOne(wrapper);
            if (userUploadAuthInfo != null) {
                int fileCount = userUploadAuthInfo.getFileCount();
                if (fileCount >= 5) {
                    throw new AuthException("抱歉,您当前权限只能上传5景原始文件数据，请先删除再上传");
                }
                double fileSize = userUploadAuthInfo.getFileSize();
                if (fileSize + Double.parseDouble(size) > 500 * 1024 * 1024) {
                    throw new AuthException(String.format("抱歉,您当前上传原始文件总大权限为500MB,当前已上传 %s 请合理安排空间使用", FileUtils.fileSizeToString(fileSize)));
                }
            }
        }

        if ("2".equals(adminLevel)) {
            throw new AuthException("抱歉,您当前只有阅读权限");
        }

    }
}
