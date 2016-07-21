package com.csii.bank.core.generator.tools;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * UpdateByPrimaryKey的xmlMapper生成器
 * 
 * MyBatis3版本
 * 
 * 针对表中存在的DATELASTMAINT进行同一赋值
 * 
 * @author GaoYu
 * 
 */
public class MyBatis3_UpdateByPrimaryKeyWithoutBLOBsElementGenerator extends MyBatis3_BaseXmlElementGenerator {
	private boolean isSimple;

	public MyBatis3_UpdateByPrimaryKeyWithoutBLOBsElementGenerator(boolean isSimple) {
		super();
		this.isSimple = isSimple;
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
		answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));//$NON-NLS-1$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		// set up for first column
		sb.setLength(0);
		sb.append(hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns()) ? UPDATE_CLAUSE : "set"); //$NON-NLS-1$

		Iterator<IntrospectedColumn> iter;
		if (isSimple) {
			iter = introspectedTable.getNonPrimaryKeyColumns().iterator();
		} else {
			iter = introspectedTable.getBaseColumns().iterator();
		}
		while (iter.hasNext()) {
			IntrospectedColumn introspectedColumn = iter.next();

			if (isDateLastMainColumn(introspectedColumn)) {
				continue;
			}

			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)); // $NON-NLS-1$

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
