/**
 * 
 */
package com.fssy.shareholder.management.pojo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置
 * 
 * @author Solomon
 */
@ConfigurationProperties(prefix = "file")
public class FileProperties
{
	private String uploadDir;

	public String getUploadDir()
	{
		return uploadDir;
	}

	public void setUploadDir(String uploadDir)
	{
		this.uploadDir = uploadDir;
	}
}
