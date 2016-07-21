package com.csii.bank.core.generator.tools;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * UpdateByExampleSelective的xmlMapper生成器
 * 
 * MyBatis3版本
 * 
 * 针对表中存在的DATELASTMAINT进行同一赋值
 * 
 * @author GaoYu
 * 
 */
public class MyBatis3_UpdateByExampleSelectiveElementGenerator extends MyBatis3_BaseXmlElementGenerator {

	public MyBatis3_UpdateByExampleSelectiveElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByExampleSelectiveStatementId())); //$NON-NLS-1$

		answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$ //$NON-NLS-2$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("trim"); //$NON-NLS-1$
		dynamicElement.addAttribute(new Attribute("prefix",
				hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns()) ? UPDATE_CLAUSE : "set")); //$NON-NLS-1$
		dynamicElement.addAttribute(new Attribute("suffixOverrides", ",")); //$NON-NLS-1$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			if (isDateLastMainColumn(introspectedColumn)) {
				continue;
			}
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty("record.")); //$NON-NLS-1$
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$
			sb.append(',');

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}

		answer.addElement(super.getUpdateByExampleIncludeElement());

		parentElement.addElement(answer);
	}

}
