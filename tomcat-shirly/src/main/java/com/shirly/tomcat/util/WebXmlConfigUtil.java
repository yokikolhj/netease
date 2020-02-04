package com.shirly.tomcat.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.Servlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/2/4 15:14
 * @Description 工具类，解析xml文件，转化为java对象
 */
public class WebXmlConfigUtil extends DefaultHandler {

    /**
     * servlet集合
     */
    Map<String, Object> servlets = new HashMap<>();
    /**
     * servlet参数
     */
//    Map<String, Map<String, String>> servletParam = new HashMap<>();
    /**
     * servlet映射
     */
    Map<String, String> servletMapping = new HashMap<>();
    /**
     * servlet实例集合
     */
//    Map<String, Servlet> servletInstances = new HashMap<>();
    /**
     * xml文件地址
     */
    private String xmlPath;

    /**
     * 读取xml文件中的信息
     * @param xmlPath
     * @return
     * @throws Exception
     */
    public WebXml loadXml(String xmlPath) throws Exception{
        this.xmlPath = xmlPath;
        // 创建一个解析xml的工厂对象
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        // 创建一个解析xml的对象
        SAXParser parser = parserFactory.newSAXParser();
        // 创建一个解析助手类
        parser.parse(this.xmlPath, this);

        WebXml webXml = new WebXml();
        webXml.servlets = this.servlets;
        webXml.servletMapping = this.servletMapping;
        return webXml;
    }

    String currentServlet = null;
    String currentServletMapping = null;
    String currentParam = null;
    String qName = null;

    // 开始解析xml根元素时调用该方法
    @Override
    public void startDocument() throws SAXException {
        System.out.println("一开始解析：" + this.xmlPath);
    }

    // 开始解析每个元素时都会调用该方法
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 判断正在解析的元素是不是开始解析的元素
        this.qName = qName;
    }

    // 解析到每个元素内容时会调用此方法
    @Override
    public void characters (char ch[], int start, int length) throws SAXException {
        String currentValue = new String(ch, start, length);
        if (currentValue == null || currentValue.trim().equals("") || currentValue.trim().equals("\r\n")) {
            return;
        }
        if ("servlet-name".equals(qName)) {
            currentServlet = currentValue;
            currentServletMapping = currentValue;
        } else if ("servlet-class".equals(qName)) {
            // servlet信息
            String servletClass = currentValue;
            servlets.put(currentServlet, servletClass);
        } else if ("param-name".equals(qName)) {
            currentParam = currentValue;
        } else if ("param-value".equals(qName)) {
            String paramValue = currentValue;
            Map<String, String> params = new HashMap<>();
            params.put(currentParam, paramValue);
        } else if ("url-pattern".equals(qName)) {
            String urlPattern = currentValue;
            servletMapping.put(urlPattern, currentServletMapping);
        }
    }

    // 每个元素结束时会调用此方法
    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException {
    }

    // 解析xml根元素结束标签时调用该方法
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public Map<String, Object> getServlets() {
        return servlets;
    }

    public void setServlets(Map<String, Object> servlets) {
        this.servlets = servlets;
    }

    public Map<String, String> getServletMapping() {
        return servletMapping;
    }

    public void setServletMapping(Map<String, String> servletMapping) {
        this.servletMapping = servletMapping;
    }
}
