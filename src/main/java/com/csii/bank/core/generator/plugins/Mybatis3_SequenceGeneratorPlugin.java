package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Sequence取值插件
 * 
 * MyBatis3版本
 * 
 * @author GaoYu
 * 
 */
public class Mybatis3_SequenceGeneratorPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		String value = introspectedTable
				.getTableConfigurationProperty("sequence");
		if (value != null && !"".equals(value)) {
			XmlElement e = new XmlElement("select");
			e.addAttribute(new Attribute("id", "getNext_" + value));
			e.addAttribute(new Attribute("resultClass", "java.lang.Long"));
			context.getCommentGenerator().addComment(e);
			StringBuilder sb = new StringBuilder();
			//目前统一使用dual作为伪表，各数据库将伪表的别名定位dual即可
			sb.append("select csii_sequencenextvalue('"+value.toLowerCase()+"') from dual");
			e.addElement(new TextElement(sb.toString()));
			document.getRootElement().addElement(e);
		}
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String value = introspectedTable
				.getTableConfigurationProperty("sequence");
		if (value != null && !"".equals(value)) {
			Method m = new Method();
			m.setVisibility(JavaVisibility.PUBLIC);
			m.setReturnType(new FullyQualifiedJavaType("java.lang.Long"));
			m.setName("getNext_" + value);
			context.getCommentGenerator().addGeneralMethodComment(m,
					introspectedTable);
			interfaze.addMethod(m);

		}
		return true;
	}
}
