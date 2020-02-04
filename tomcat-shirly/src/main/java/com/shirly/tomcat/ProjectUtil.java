package com.shirly.tomcat;

import com.shirly.tomcat.util.WebXml;
import com.shirly.tomcat.util.WebXmlConfigUtil;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author shirly
 * @Date 2020/2/4 14:22
 * @Description 项目加载【部署】
 */
public class ProjectUtil {

    public static Map<String, WebXml> load() throws Exception {
        Map<String, WebXml> projectInfos = new HashMap<>();

        // 扫描对应的部署文件夹
        String webapps = "C:\\workspace\\MyProject\\shirly\\netease\\tomcat-shirly\\webapps";

        // TODO war包部署之前，自动解压（java操作压缩包的api）【war包本质就是压缩包zip/rar】
        File[] wars = new File(webapps).listFiles(pathname -> pathname.isFile());
        for (File war: wars) {
            String warPath = webapps + "\\" + war.getName();
            System.out.println("war包路径：" + warPath);
            if (warPath.endsWith(".war")) {
                // TODO 解压
            }
        }
        // 文件夹形式 TODO 省略部分验证过程
        File[] projects = new File(webapps).listFiles(pathname -> pathname.isDirectory());
        for (File project: projects) {
            // 发现项目里面所有的servlet（通过web.xml/注解）
            WebXml webXml = new WebXmlConfigUtil().loadXml(project.getPath() + "\\WEB-INF\\web.xml"); // 规范的重要性
            webXml.projectPath = project.getPath();
            webXml.loadService();

            projectInfos.put(project.getName(), webXml);
        }
        return projectInfos;
    }

}
