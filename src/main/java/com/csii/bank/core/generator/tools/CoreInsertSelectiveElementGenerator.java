package com.csii.bank.core.generator.tools;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

public class CoreInsertSelectiveElementGenerator extends
		BaseXmlElementGenerator {

	public CoreInsertSelectiveElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

		answer.addAttribute(new Attribute(
				"id", introspectedTable.getInsertSelectiveStatementId())); //$NON-NLS-1$

		FullyQualifiedJavaType parameterType = introspectedTable.getRules()
				.calculateAllFieldsClass();

		answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
				parameterType.getFullyQualifiedName()));

		context.getCommentGenerator().addComment(answer);

		GeneratedKey gk = introspectedTable.getGeneratedKey();

		if (gk != null && gk.isPlacedBeforeInsertInIbatis2()) {
			IntrospectedColumn introspectedColumn = introspectedTable
					.getColumn(gk.getColumn());
			// if the column is null, then it's a configuration error. The
			// warning has already been reported
			if (introspectedColumn != null) {
				// pre-generated key
				answer.addElement(getSelectKey(introspectedColumn, gk));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("insert into "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement insertElement = new XmlElement("dynamic"); //$NON-NLS-1$
		insertElement.addAttribute(new Attribute("prepend", "(DATELASTMAINT,")); //$NON-NLS-1$ //$NON-NLS-2$
		answer.addElement(insertElement);

		answer.addElement(new TextElement("values")); //$NON-NLS-1$

		XmlElement valuesElement = new XmlElement("dynamic"); //$NON-NLS-1$
		valuesElement.addAttribute(new Attribute("prepend", "("+SYSDATE+",")); //$NON-NLS-1$ //$NON-NLS-2$
		answer.addElement(valuesElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable
				.getAllColumns()) {
			if (introspectedColumn.isIdentity()) {
				// cannot set values on identity fields
				continue;
			}
			if (isDateLastMainColumn(introspectedColumn)) {
				// datelastmaint必须进行插入，luibao
				continue;
			}

			XmlElement insertNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
			insertNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
			insertNotNullElement.addAttribute(new Attribute(
					"property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
			insertNotNullElement.addElement(new TextElement(
					Ibatis2FormattingUtilities
							.getEscapedColumnName(introspectedColumn)));
			insertElement.addElement(insertNotNullElement);

			XmlElement valuesNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
			valuesNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
			valuesNotNullElement.addAttribute(new Attribute(
					"property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
			valuesNotNullElement.addElement(new TextElement(
					Ibatis2FormattingUtilities
							.getParameterClause(introspectedColumn)));
			valuesElement.addElement(valuesNotNullElement);
		}

		insertElement.addElement(new TextElement(")")); //$NON-NLS-1$
		valuesElement.addElement(new TextElement(")")); //$NON-NLS-1$

		if (gk != null && !gk.isPlacedBeforeInsertInIbatis2()) {
			IntrospectedColumn introspectedColumn = introspectedTable
					.getColumn(gk.getColumn());
			// if the column is null, then it's a configuration error. The
			// warning has already been reported
			if (introspectedColumn != null) {
				// pre-generated key
				answer.addElement(getSelectKey(introspectedColumn, gk));
			}
		}

		parentElement.addElement(answer);
	}
}
