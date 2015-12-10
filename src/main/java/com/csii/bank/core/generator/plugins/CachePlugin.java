package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.plugins.CachePlugin.CacheProperty;

/**
 * 缓存插件，为sqlmap文件添加缓存配置
 * 
 * ibatis的缓存机制相对简单：
 * 
 * 1、它不是以对象为key，而是以sql语句为key，即：即使两个不同的sql查询出的对象相同，框架也不会只缓存一个对象。
 * 2、缓存的刷新也不是以对象为单位，而是以表为单位的，当针对某个表执行了C、U、D操作时，该表上的所有缓存都会失效
 * 
 * @author lubiao
 * 
 */
public class CachePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		Attribute att = new Attribute("cacheModel", "cacheConfig");
		element.addAttribute(att);
		return super.sqlMapSelectByPrimaryKeyElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		Attribute att = new Attribute("cacheModel", "cacheConfig");
		element.addAttribute(att);
		return super.sqlMapSelectByPrimaryKeyElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		String namespace = introspectedTable
				.getFullyQualifiedTableNameAtRuntime();

		XmlElement element = new XmlElement("cacheModel");
		context.getCommentGenerator().addComment(element);

		Attribute id = new Attribute("id", "cacheConfig");
		element.addAttribute(id);
		Attribute type = new Attribute("type", "LRU");
		element.addAttribute(type);
		Attribute readOnly = new Attribute("readOnly", "true");
		element.addAttribute(readOnly);

		XmlElement e1 = new XmlElement("flushOnExecute");
		e1.addAttribute(new Attribute("statement", namespace
				+ ".deleteByPrimaryKey"));
		element.addElement(e1);

		XmlElement e2 = new XmlElement("flushOnExecute");
		e2.addAttribute(new Attribute("statement", namespace
				+ ".deleteByExample"));
		element.addElement(e2);

		XmlElement e3 = new XmlElement("flushOnExecute");
		e3.addAttribute(new Attribute("statement", namespace
				+ ".updateByExampleSelective"));
		element.addElement(e3);

		XmlElement e4 = new XmlElement("flushOnExecute");
		e4.addAttribute(new Attribute("statement", namespace
				+ ".updateByExample"));
		element.addElement(e4);

		XmlElement e5 = new XmlElement("flushOnExecute");
		e5.addAttribute(new Attribute("statement", namespace
				+ ".updateByPrimaryKeySelective"));
		element.addElement(e5);

		XmlElement e6 = new XmlElement("flushOnExecute");
		e6.addAttribute(new Attribute("statement", namespace
				+ ".updateByPrimaryKey"));
		element.addElement(e6);

		XmlElement e7 = new XmlElement("flushOnExecute");
		e7.addAttribute(new Attribute("statement", namespace + ".insert"));
		element.addElement(e7);

		XmlElement e8 = new XmlElement("flushOnExecute");
		e8.addAttribute(new Attribute("statement", namespace
				+ ".insertSelective"));
		element.addElement(e8);

		document.getRootElement().addElement(0, element);

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

}
