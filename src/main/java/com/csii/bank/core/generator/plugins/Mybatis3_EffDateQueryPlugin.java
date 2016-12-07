package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
//import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 自动生成带Effdate和Inactive的sql代码
 * 
 */
public class Mybatis3_EffDateQueryPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		if (isNeedEffectiveQuery(introspectedTable)) {
			XmlElement e = new XmlElement("select");
			e.addAttribute(new Attribute("id", "getEffectiveEntryByDate"));
			e.addAttribute(new Attribute("resultMap", introspectedTable
					.getBaseResultMapId()));

			String parameterType;
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				parameterType = introspectedTable.getPrimaryKeyType();
			} else {
				parameterType = introspectedTable.getBaseRecordType();
			}
			e.addAttribute(new Attribute("parameterType", parameterType));

			context.getCommentGenerator().addComment(e);

			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			e.addElement(new TextElement(sb.toString()));
			e.addElement(getBaseColumnListElement(introspectedTable));

			sb.setLength(0);
			sb.append("from "); //$NON-NLS-1$
			sb.append(introspectedTable
					.getAliasedFullyQualifiedTableNameAtRuntime());
			e.addElement(new TextElement(sb.toString()));

			IntrospectedColumn effdateColumn = null;
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

				if (!"EFFDATE".equals(introspectedColumn.getActualColumnName())) {
					sb.append(MyBatis3FormattingUtilities
							.getAliasedEscapedColumnName(introspectedColumn));
					sb.append(" = "); //$NON-NLS-1$
					sb.append(MyBatis3FormattingUtilities
							.getParameterClause(introspectedColumn));
				}

				if ("EFFDATE".equals(introspectedColumn.getActualColumnName())) {
					effdateColumn = introspectedColumn;
					if (isInactiveColumnExist(introspectedTable)) {
						sb.append("<![CDATA[ "
								+ MyBatis3FormattingUtilities
										.getAliasedEscapedColumnName(introspectedColumn));
						sb.append(" <= ");
						sb.append(MyBatis3FormattingUtilities
								.getParameterClause(introspectedColumn) + "]]>");
					} else {
						sb.append("<![CDATA[ "
								+ MyBatis3FormattingUtilities
										.getAliasedEscapedColumnName(introspectedColumn));
						sb.append(" = ");
						sb.append("(select max(EFFDATE) from ");
						sb.append(introspectedTable
								.getAliasedFullyQualifiedTableNameAtRuntime());
						boolean isAnd = false;
						for (IntrospectedColumn item : introspectedTable
								.getPrimaryKeyColumns()) {
							if (isAnd) {
								sb.append("  and "); //$NON-NLS-1$
							} else {
								sb.append(" where "); //$NON-NLS-1$
								isAnd = true;
							}
							if (!"EFFDATE".equals(item.getActualColumnName())) {
								sb.append(MyBatis3FormattingUtilities
										.getAliasedEscapedColumnName(item));
								sb.append(" = "); //$NON-NLS-1$
								sb.append(MyBatis3FormattingUtilities
										.getParameterClause(item));
							}
							if ("EFFDATE".equals(item.getActualColumnName())) {
								sb.append(MyBatis3FormattingUtilities
										.getAliasedEscapedColumnName(item));
								sb.append(" <= ");
								sb.append(MyBatis3FormattingUtilities
										.getParameterClause(item));
							}
						}
						sb.append(")]]>");
					}
				}

				e.addElement(new TextElement(sb.toString()));
			}

			if (isInactiveColumnExist(introspectedTable)) {
				sb.setLength(0);
				sb.append("  and ");
				sb.append("<![CDATA[ (inactivedate is null or inactivedate >"
						+ MyBatis3FormattingUtilities
								.getParameterClause(effdateColumn) + ")");
				sb.append("]]>");
				e.addElement(new TextElement(sb.toString()));
			}

			if("sysdata".equals(introspectedTable.getContext().getId())){
				Attribute att = new Attribute("cacheModel", "cacheConfig");
				e.addAttribute(att);
			}
			document.getRootElement().addElement(e);
		}
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isNeedEffectiveQuery(introspectedTable)) {
			interfaze.addMethod(getMethodShell(introspectedTable));
		}
		return true;
	}

	protected XmlElement getBaseColumnListElement(
			IntrospectedTable introspectedTable) {
		XmlElement answer = new XmlElement("include");
		answer.addAttribute(new Attribute("refid",introspectedTable.getBaseColumnListId())); //$NON-NLS-1$
		return answer;
	}

	private boolean isNeedEffectiveQuery(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> cList = introspectedTable
				.getPrimaryKeyColumns();
		boolean isEffDateColumnExist = false;
		for (IntrospectedColumn c : cList) {
			if ("EFFDATE".equals(c.getActualColumnName())) {
				isEffDateColumnExist = true;
			}
		}
		return isEffDateColumnExist;
	}

	private boolean isInactiveColumnExist(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> cList = introspectedTable.getAllColumns();
		boolean isInactiveColumnExist = false;
		for (IntrospectedColumn c : cList) {
			if ("INACTIVEDATE".equals(c.getActualColumnName())) {
				isInactiveColumnExist = true;
			}
		}
		return isInactiveColumnExist;
	}

	private Method getMethodShell(IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		FullyQualifiedJavaType returnType = introspectedTable.getRules()
				.calculateAllFieldsClass();
		method.setReturnType(returnType);
		method.setName("getEffectiveEntryByDate");
		if (introspectedTable.getRules().generatePrimaryKeyClass()) {
			FullyQualifiedJavaType type = new FullyQualifiedJavaType(
					introspectedTable.getPrimaryKeyType());
			method.addParameter(new Parameter(type, "_key")); //$NON-NLS-1$
		} else {
			for (IntrospectedColumn introspectedColumn : introspectedTable
					.getPrimaryKeyColumns()) {
				FullyQualifiedJavaType type = introspectedColumn
						.getFullyQualifiedJavaType();
				method.addParameter(new Parameter(type, introspectedColumn
						.getJavaProperty()));
			}
		}
		context.getCommentGenerator().addGeneralMethodComment(method,
				introspectedTable);
		return method;
	}
}
