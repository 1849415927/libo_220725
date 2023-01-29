package mabatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junji
 * @date 2023/1/28
 */
public class MybatisPlusCodeGenerator {

    public static void main(String[] args) {

        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = "F:\\Workspace\\libo_220725";

        gc.setOutputDir(projectPath + "\\src\\main\\java")//文件输出路径
                .setAuthor("junji") //作者
                .setDateType(DateType.ONLY_DATE)
                .setOpen(false) //生成完成是否打开文件夹
                .setFileOverride(true) //是否覆写
                .setIdType(IdType.INPUT) //主键策略
                .setEnableCache(false) //是否启用缓存
                .setServiceName("%sService") //生成service前缀不带I
                .setServiceImplName("%sServiceImpl")
                .setEntityName("%s")
                .setMapperName("%sMapper")
                .setBaseResultMap(true) //生成result 映射
                .setSwagger2(true) //是否启动swagger
                .setBaseColumnList(true); //生成公众的sql字段

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL)
//                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/libo_220725?servierTimezone=UTC")
                .setSchemaName("public")
                .setUsername("root")
                .setPassword("123456");

        //3、策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setCapitalMode(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setInclude("luo_jiaoyuess")   //数据库表名称
                .setEntityLombokModel(true)
//                .setTablePrefix("cux_hr_")
//                .setFieldPrefix("cux_hr_")
                .setRestControllerStyle(false);

        //4、包名策略配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.libo")
                .setController("controller")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setEntity("entity")
                .setXml("xml");

        // 配置模板, 不生成 controller
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
//                .setController(null)
//                .setMapper(null)
//                .setService(null)
//                .setServiceImpl(null)
//                .setEntity(null)
                .setXml(null); // xml又自定义配置删除

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//         如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "\\src\\main\\resources\\mapper\\primary\\" + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        //5、 整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(gc)
                .setCfg(cfg)
                .setTemplate(templateConfig)
                .setDataSource(dsc)
                .setStrategy(sc)
                .setPackageInfo(pc)
                .execute();

    }
}
