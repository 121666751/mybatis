package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 缓存插件，为MAPPER文件添加缓存配置
 * 
 * mybatis的缓存机制相对简单：
 * 
 * 1、配置完<cache/>表示该mapper映射文件中，所有的select语句都将被缓存，所有的insert、update和delete语句都将刷新缓存。
 * 2、但是实际中，我们并是希望这样，有些select不想被缓存时，可以添加select的属性useCache=“false”；有些insert、update和delete不想让他刷新缓存时，添加属性flushCache=”false ”。
 * 
 * @author xujin
 * 
 */
public class MyBatis3_CachePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("cache");
		context.getCommentGenerator().addComment(element);

		Attribute id = new Attribute("type", "com.csii.iiap.cache.LoggingRedisCache");
		element.addAttribute(id);

		document.getRootElement().addElement(0, element);

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

}
