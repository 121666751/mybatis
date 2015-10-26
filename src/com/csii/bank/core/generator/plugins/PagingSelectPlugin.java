package com.csii.bank.core.generator.plugins;

import java.text.MessageFormat;
import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class PagingSelectPlugin extends PluginAdapter {
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String value = introspectedTable
				.getTableConfigurationProperty("isNeedPaging");
		if (value != null && !"".equals(value) && "true".equals(value)) {
			for (int i = 0; i < 2; i++) {
				Method m = new Method();
				// 可见性
				m.setVisibility(JavaVisibility.PUBLIC);
				// 返回参数
				FullyQualifiedJavaType returnType = FullyQualifiedJavaType
						.getNewListInstance();
				returnType.addTypeArgument(new FullyQualifiedJavaType(
						introspectedTable.getBaseRecordType()));
				m.setReturnType(returnType);
				// 方法名
				m.setName(i == 0 ? "selectByExample"
						: "selectByExampleWithBLOBs");
				// 方法参数
				m.addParameter(0, new Parameter(new FullyQualifiedJavaType(
						introspectedTable.getExampleType()), "example"));
				m.addParameter(1,
						new Parameter(FullyQualifiedJavaType.getIntInstance(),
								"skip"));
				m.addParameter(2,
						new Parameter(FullyQualifiedJavaType.getIntInstance(),
								"max"));
				// 注释
				context.getCommentGenerator().addGeneralMethodComment(m,
						introspectedTable);
				interfaze.addMethod(m);

				Method method = new Method();
				method.setVisibility(JavaVisibility.PUBLIC);
				method.setReturnType(returnType);
				method.setName(i == 0 ? "selectByExample"
						: "selectByExampleWithBLOBs");
				// 方法参数
				method.addParameter(0,
						new Parameter(new FullyQualifiedJavaType(
								introspectedTable.getExampleType()), "example"));
				method.addParameter(1,
						new Parameter(FullyQualifiedJavaType.getIntInstance(),
								"skip"));
				method.addParameter(2,
						new Parameter(FullyQualifiedJavaType.getIntInstance(),
								"max"));

				context.getCommentGenerator().addGeneralMethodComment(method,
						introspectedTable);

				StringBuilder sb = new StringBuilder();
				sb.append(method.getReturnType().getShortName());
				sb.append(" list = "); //$NON-NLS-1$
				sb.append(MessageFormat
						.format("getSqlMapClientTemplate().queryForList(\"{0}.{1}\",{2},{3},{4});",
								introspectedTable.getIbatis2SqlMapNamespace(),
								i == 0 ? introspectedTable
										.getSelectByExampleStatementId()
										: introspectedTable
												.getSelectByExampleWithBLOBsStatementId(),
								"example", "skip", "max")); //$NON-NLS-1$
				method.addBodyLine(sb.toString());
				method.addBodyLine("return list;"); //$NON-NLS-1$

				topLevelClass.addMethod(method);
			}
		}
		return true;
	}
}
