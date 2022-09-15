package com.fssy.shareholder.management.tools.common;

import com.fssy.shareholder.management.mapper.system.config.AttachmentSecretMapper;
import com.fssy.shareholder.management.pojo.properties.FileProperties;
import com.fssy.shareholder.management.pojo.system.config.AttachmentSecret;
import com.fssy.shareholder.management.pojo.common.Module;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author TerryZeng
 * @title: FileAttachmentSecretTool
 * @description: 文件/附件操作工具类
 * @date 2021/10/30
 */
@Component
public class FileAttachmentSecretTool {
    private final Path fileStorageLocation; // 文件在本地存储的地址

    @Autowired
    private AttachmentSecretMapper attachmentSecretMapper;
    /**
     * 构造器设置文件目录
     *
     * @param fileProperties
     */
    @Autowired
    public FileAttachmentSecretTool(FileProperties fileProperties) {
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ServiceException("上传文件目录创建失败", ex);
        }
    }

    /**
     * 存储文件到系统（弃用）
     *
     * @param file 文件
     * @return 文件名
     */
    public String storeFile(MultipartFile file) {
        // 格式化文件名，针对斜杠
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 判断文件名格式
            if (fileName.contains("..")) {
                throw new ServiceException("文件名格式不正确" + fileName);
            }

            // 保存文件（存在同名文件则覆盖）
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试",
                    ex);
        }
    }

    /**
     * 按模块分开保存文件/附件
     *
     * @param file   文件对象
     * @param module 模块枚举类
     * @return
     */
    public AttachmentSecret storeFileToModule(MultipartFile file, Module module,
                                              AttachmentSecret attachmentSecret) {
        // 格式化文件名，针对斜杠
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // 生成UUID盐值
        String saltString = UUID.randomUUID().toString();
        // 用于查询的条件
        String queryPath = DigestUtils.md5Hex(saltString);
        // 获取md5文件名
        String md5FileName = randomFileName(fileName, saltString);
        // 文件路径
        // 从模块开始到月份的相对路径
        String relativeFilePathStr = joinModuleAndDate(module.getName());
        String relativePathStr = relativeFilePathStr + File.separator
                + md5FileName;
        try {
            // 判断文件名格式
            if (fileName.contains("..")) {
                throw new ServiceException("文件名格式不正确" + fileName);
            }

            // 获取文件所在目录信息
            Path targetDirLocation = this.fileStorageLocation
                    .resolve(relativeFilePathStr);
            // 判断目录存在与否，不存在创建目录或者存在但是为文件
            File dir = new File(targetDirLocation.toString());
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
            // 解析目标文件名
            Path targetLocation = targetDirLocation.resolve(md5FileName);
            // 保存文件（存在同名文件则覆盖）
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);

            // 保存附件表
            attachmentSecret.setFilename(fileName);
            // 默认就是正在导入
            attachmentSecret.setMd5Path(queryPath);
            attachmentSecret.setModule(module.getValue());
            attachmentSecret.setPath(relativePathStr);
            // 默认就是上载成功
            attachmentSecretMapper.insert(attachmentSecret);
            return attachmentSecret;
        } catch (IOException ex) {
            throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试",
                    ex);
        }
    }

    /**
     * 加载文件
     *
     * @param path     文件路径名
     * @param fileName 文件名
     * @return 文件
     */
    public Resource loadFileAsResource(String path, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(path)
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ServiceException("文件不存在，名字为：" + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ServiceException("文件不存在，名字为：" + fileName, ex);
        }
    }

    /**
     * 删除文件
     *
     * @param path     文件路径名
     * @param fileName 文件名
     * @return
     */
    public boolean deleteFileAsResource(String path, String fileName) {
        Path filePath = this.fileStorageLocation.resolve(path).normalize();
        File file = new File(filePath.toString());
        if (file.exists()) {
            return file.delete();
        } else {
            throw new ServiceException("文件不存在，名字为：" + fileName);
        }
    }

    /**
     * 重新根据MD5拼凑文件名
     *
     * @param fileName   原文件名
     * @param saltString UUID字符串
     * @return
     */
    protected String randomFileName(String fileName, String saltString) {
        // 获取后缀名
        String extension = FilenameUtils.getExtension(fileName);
        String result = DigestUtils.md5Hex(saltString + fileName) + "."
                + extension;
        return result;
    }

    /**
     * 获取模块，年，月的目录路径
     *
     * @param moduleName 模块名字
     * @return
     */
    protected String joinModuleAndDate(String moduleName) {
        // 添加年和月，默认为当地时区
        Calendar calendar = Calendar.getInstance();
        String yearString = String.valueOf(calendar.get(Calendar.YEAR));
        // 月份0代表1月份，需要加1
        String monthString = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String tempString = String.format("%s/%s", yearString, monthString);
        // 相对路径需要保存
        return ObjectUtils.isEmpty(moduleName) ? tempString
                : moduleName + File.separator + tempString;

    }
}
