package com.csii.bank.core.generator.tools;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

public class CoreUpdateByPrimaryKeySelectiveElementGenerator extends
		BaseXmlElementGenerator {
	public CoreUpdateByPrimaryKeySelectiveElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute(
				"id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$

		String parameterType;

		if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
			parameterType = introspectedTable.getRecordWithBLOBsType();
		} else {
			parameterType = introspectedTable.getBaseRecordType();
		}

		answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
				parameterType));

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();

		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("dynamic"); //$NON-NLS-1$
		dynamicElement
				.addAttribute(new Attribute(
						"prepend", hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns()) ? UPDATE_CLAUSE : "set")); //$NON-NLS-1$ //$NON-NLS-2$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable
				.getNonPrimaryKeyColumns()) {
			if (isDateLastMainColumn(introspectedColumn)) {
				continue;
			}
			XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
			isNotNullElement.addAttribute(new Attribute(
					"property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(Ibatis2FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(Ibatis2FormattingUtilities
					.getParameterClause(introspectedColumn));

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}

		boolean and = false;
		for (IntrospectedColumn introspectedColumn : introspectedTable
				.getPrimaryKeyColumns()) {
			sb.setLength(0);
			if (and) {
				sb.append("  and "); //$NON-NLS-1$
			} else {
				sb.append("where "); //$NON-NLS-1$
				and = true;
			}

			sb.append(Ibatis2FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(Ibatis2FormattingUtilities
					.getParameterClause(introspectedColumn));
			answer.addElement(new TextElement(sb.toString()));
		}

		parentElement.addElement(answer);
	}
}
