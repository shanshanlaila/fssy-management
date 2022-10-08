package com.fssy.shareholder.management;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;


import java.util.Collections;

/**
 * MyBatisPlus代码生成器（新），基本满足使用了。
 * 官方文档-代码生成器配置新：https://baomidou.com/pages/981406/
 *
 */

/**
 * MyBatisPlus代码生成器（新），基本满足使用了。
 * 官方文档-代码生成器配置新：https://baomidou.com/pages/981406/
 *
 */
public class MyBatisPlusCodeGeneratorUtils {


    private static final String url = "jdbc:mysql://localhost:3306/fs-business?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
    private static final String username = "root";
    private static final String password = "123123123";
    //文件夹、数据库配置项、路径配置项
    private static final String outputDir="D://MyBatisPlusCodeGeneratorUtils";
    private static final String packageName="employee";
    private static final String mysqlTableName="bs_performance_events_list";

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("TerryZeng")               //作者
                            .outputDir(outputDir)    //输出路径(写到java目录)
//                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd")
                            .fileOverride();            //开启覆盖之前生成的文件

                })
                .packageConfig(builder -> {
                    builder.parent("com")
                            .moduleName("fssy.shareholder.management")
                            .entity("pojo.system."+packageName)
                            .service("service.system."+packageName)
                            .serviceImpl("service.system.impl."+packageName)
                            .controller("controller.system."+packageName)
                            .mapper("mapper.system."+packageName)
//                            .xml("mapper.xml")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, outputDir+"//main//resources//templates//mapper//system//"+packageName));
                })

                .strategyConfig(builder -> {
                    //数据表名
                    builder.addInclude(mysqlTableName)
                            .addTablePrefix("bs_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            //实体类配置
                            .entityBuilder()
                            .superClass(BaseModel.class)
                            .addSuperEntityColumns()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            //控制器配置
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            //数据接口Mapper配置
                            .mapperBuilder()
                            .enableBaseResultMap()  //生成通用的resultMap
                            .superClass(MyBaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();


    }
}