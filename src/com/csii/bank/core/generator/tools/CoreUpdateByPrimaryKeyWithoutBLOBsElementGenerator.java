package com.csii.bank.core.generator.tools;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

public class CoreUpdateByPrimaryKeyWithoutBLOBsElementGenerator extends
		BaseXmlElementGenerator {
	public CoreUpdateByPrimaryKeyWithoutBLOBsElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute(
				"id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
		answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
				introspectedTable.getBaseRecordType()));

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		// set up for first column
		sb.setLength(0);
		sb.append("set "); //$NON-NLS-1$

		Iterator<IntrospectedColumn> iter = introspectedTable.getBaseColumns()
				.iterator();
		while (iter.hasNext()) {
			IntrospectedColumn introspectedColumn = iter.next();

			sb.append(Ibatis2FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(isDateLastMainColumn(introspectedColumn) ? SYSDATE
					: Ibatis2FormattingUtilities
							.getParameterClause(introspectedColumn));

			if (iter.hasNext()) {
				sb.append(',');
			}

			answer.addElement(new TextElement(sb.toString()));

			// set up for the next column
			if (iter.hasNext()) {
				sb.setLength(0);
				OutputUtilities.xmlIndent(sb, 1);
			}
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
