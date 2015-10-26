package com.csii.bank.core.generator.plugins;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getSetterMethodName;

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
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

public class LockStatmentPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String value = introspectedTable.getTableConfigurationProperty("lock");
		if (value != null && "true".equals(value)) {
			interfaze.addMethod(getMethodShell(introspectedTable));

			Method method = getMethodShell(introspectedTable);
			StringBuilder sb = new StringBuilder();
			if (!introspectedTable.getRules().generatePrimaryKeyClass()) {
				// no primary key class, but primary key is enabled. Primary
				// key columns must be in the base class.
				FullyQualifiedJavaType keyType = new FullyQualifiedJavaType(
						introspectedTable.getBaseRecordType());
				topLevelClass.addImportedType(keyType);

				sb.setLength(0);
				sb.append(keyType.getShortName());
				sb.append(" _key = new "); //$NON-NLS-1$
				sb.append(keyType.getShortName());
				sb.append("();"); //$NON-NLS-1$
				method.addBodyLine(sb.toString());

				for (IntrospectedColumn introspectedColumn : introspectedTable
						.getPrimaryKeyColumns()) {
					sb.setLength(0);
					sb.append("_key."); //$NON-NLS-1$
					sb.append(getSetterMethodName(introspectedColumn
							.getJavaProperty()));
					sb.append('(');
					sb.append(introspectedColumn.getJavaProperty());
					sb.append(");"); //$NON-NLS-1$
					method.addBodyLine(sb.toString());
				}
			}

			sb.setLength(0);
			sb.append("getSqlMapClientTemplate().queryForObject(\"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + ".addLock\"" + ",_key);"); //$NON-NLS-1$
			method.addBodyLine(sb.toString());

			topLevelClass.addMethod(method);
		}
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		String value = introspectedTable.getTableConfigurationProperty("lock");
		if (value != null && "true".equals(value)) {
			XmlElement e = new XmlElement("select");
			e.addAttribute(new Attribute("id", "addLock"));

			String parameterType;
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				parameterType = introspectedTable.getPrimaryKeyType();
			} else {
				parameterType = introspectedTable.getBaseRecordType();
			}
			e.addAttribute(new Attribute("parameterClass", parameterType));
			context.getCommentGenerator().addComment(e);
			StringBuilder sb = new StringBuilder();
			sb.append("select 1 from "
					+ introspectedTable.getFullyQualifiedTableNameAtRuntime());
			e.addElement(new TextElement(sb.toString()));

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
						.getAliasedEscapedColumnName(introspectedColumn));
				sb.append(" = "); //$NON-NLS-1$
				sb.append(Ibatis2FormattingUtilities
						.getParameterClause(introspectedColumn));
				e.addElement(new TextElement(sb.toString()));
			}
			e.addElement(new TextElement(" for update"));
			document.getRootElement().addElement(e);
		}
		return true;
	}

	private Method getMethodShell(IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("void"));
		method.setName("addLock");
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
