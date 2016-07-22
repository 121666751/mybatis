package com.csii.bank.core.generator.tools;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * UpdateByExample的xmlMapper生成器
 * 
 * MyBatis3版本
 * 
 * 针对表中存在的DATELASTMAINT进行同一赋值
 * 
 * @author GaoYu
 * 
 */
public class MyBatis3_UpdateByExampleWithoutBLOBsElementGenerator extends MyBatis3_BaseXmlElementGenerator {

	public MyBatis3_UpdateByExampleWithoutBLOBsElementGenerator() {
		super();
	}

	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByExampleStatementId()));
		answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$ //$NON-NLS-2$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		// set up for first column
		sb.setLength(0);
		sb.append("set "); //$NON-NLS-1$

		Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonBLOBColumns().iterator();
		while (iter.hasNext()) {
			IntrospectedColumn introspectedColumn = iter.next();

			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(isDateLastMainColumn(introspectedColumn) ? SYSDATE
					: MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$

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

		answer.addElement(getUpdateByExampleIncludeElement());

		parentElement.addElement(answer);
	}
}
