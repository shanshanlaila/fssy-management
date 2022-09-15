package com.fssy.shareholder.management.tools.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * 口令认证器
 * 
 * @author Solomon
 */
public class ShiroMatcher extends HashedCredentialsMatcher
{
	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info)
	{
		// 找到token中的用户和密码
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String name = usernamePasswordToken.getUsername();
		String password = String.valueOf(usernamePasswordToken.getPassword());
		String host = usernamePasswordToken.getHost();
		
		// 找出user对象
		User user = (User) info.getPrincipals().getPrimaryPrincipal();

		// 获取盐值
		String salt = user.getSalt();
		String inputPassword;
		// 如果请求头包含wx，也就是微信小程序请求的，就不去做MD5加密
		if (!host.equals("wx")) {
			int count = 20;
			inputPassword = new SimpleHash("MD5", password, salt, count).toString();
		} else {
			inputPassword = password;
		}
		// 获取realm中管理的用户口令
		String realPassword = info.getCredentials().toString();
		return this.equals(inputPassword, realPassword);
	}
	
}
