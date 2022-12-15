/**
 * ------------------------修改日志---------------------------------
 * 修改人        修改日期       		修改内容
 * 班群蔚        2022-5-8    	因为定时任务不能确定创建人，所以就指定为超级用户(id为0)
 * <p>
 */

package com.fssy.shareholder.management.handler;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * 重写mybatis-plus添加和修改数据时更新两个时间戳
 *
 * @author Solomon
 */
@Component
public class CreatedAndUpdatedAtMetaHandler implements MetaObjectHandler {

    private static final Logger LOGGER = LoggerFactory
			.getLogger(CreatedAndUpdatedAtMetaHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        LOGGER.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdAt",
                () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updatedAt",
                () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本3.3.3(推荐)
        // 添加创建人和修改人id
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            this.strictInsertFill(metaObject, "createdId", () -> user.getId(),
                    Long.class);
            this.strictInsertFill(metaObject, "createdName", () -> user.getName(), String.class);

            this.strictUpdateFill(metaObject, "updatedId", () -> user.getId(),
                    Long.class);
            this.strictInsertFill(metaObject, "updatedName", () -> user.getName(), String.class);
        } catch (Exception e) {
            this.strictInsertFill(metaObject, "createdId", () -> 0l,
                    Long.class);
            this.strictInsertFill(metaObject, "createdName", () -> "超级用户", String.class);

            this.strictUpdateFill(metaObject, "updatedId", () -> 0l,
                    Long.class);
            this.strictInsertFill(metaObject, "updatedName", () -> "超级用户", String.class);
        }

    }

	@Override
	public void updateFill(MetaObject metaObject)
	{
		LOGGER.info("start update fill ....");
		this.setFieldValByName("updatedAt", LocalDateTime.now(), metaObject); // 起始版本3.3.3(推荐)
		// 添加修改人id
		User user;
		if (ObjectUtils.isEmpty(SecurityUtils.getSubject())
				|| ObjectUtils.isEmpty(SecurityUtils.getSubject().getPrincipal()))
		{
			this.setFieldValByName("updatedId", 0L, metaObject);
			this.setFieldValByName("updatedName", "超级用户", metaObject);
		}
		else
		{
			user = (User) SecurityUtils.getSubject().getPrincipal();
			this.setFieldValByName("updatedId", user.getId(), metaObject);
			this.setFieldValByName("updatedName", user.getName(), metaObject);
		}
	}

}
