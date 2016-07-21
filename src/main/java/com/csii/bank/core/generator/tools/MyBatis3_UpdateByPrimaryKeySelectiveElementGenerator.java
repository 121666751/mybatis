package com.csii.bank.core.generator.tools;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * UpdateByPrimaryKeySelective的xmlMapper生成器
 * 
 * MyBatis3版本
 * 
 * 针对表中存在的DATELASTMAINT进行同一赋值
 * 
 * @author GaoYu
 * 
 */
public class MyBatis3_UpdateByPrimaryKeySelectiveElementGenerator extends MyBatis3_BaseXmlElementGenerator {
	public MyBatis3_UpdateByPrimaryKeySelectiveElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$

		String parameterType;

		if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
			parameterType = introspectedTable.getRecordWithBLOBsType();
		} else {
			parameterType = introspectedTable.getBaseRecordType();
		}

		answer.addAttribute(new Attribute("parameterType", parameterType)); //$NON-NLS-1$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());

		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("trim"); //$NON-NLS-1$
		dynamicElement.addAttribute(new Attribute("prefix",
				hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns()) ? UPDATE_CLAUSE : "set")); //$NON-NLS-1$
		dynamicElement.addAttribute(new Attribute("suffixOverrides", ",")); //$NON-NLS-1$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			if (isDateLastMainColumn(introspectedColumn)) {
				continue;
			}
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
			sb.append(',');

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}

		boolean and = false;
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			sb.setLength(0);
			if (and) {
				sb.append("  and "); //$NON-NLS-1$
			} else {
				sb.append("where "); //$NON-NLS-1$
				and = true;
			}

			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
			answer.addElement(new TextElement(sb.toString()));
		}

		parentElement.addElement(answer);
	}
}
