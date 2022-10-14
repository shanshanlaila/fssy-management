package com.fssy.shareholder.management.tools.mybatisplus.injector;

import java.util.List;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

/**
 * 自定义数据方法注入
 * 
 * @author Solomon
 */
public class MySqlInjector extends DefaultSqlInjector
{

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo)
	{
		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
		// 批量添加
		methodList.add(new InsertBatchSomeColumn());

		return methodList;
	}

}


