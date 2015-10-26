package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ExtendInterfacePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if ("bizdata".equals(context.getId())) {
			String value = introspectedTable
					.getTableConfigurationProperty("DaoType");
			if (value != null && !"".equals(value)) {
				String daoName = context.getProperty(value);
				if (daoName != null && !"".equals(daoName)) {
					interfaze.addSuperInterface(new FullyQualifiedJavaType(
							daoName));
					interfaze.addImportedType(new FullyQualifiedJavaType(
							daoName));
				}
			}
		} else {
			String s = "com.csii.bank.core.base.support.dao.ISysDataDAO";
			interfaze.addSuperInterface(new FullyQualifiedJavaType(s));
			interfaze.addImportedType(new FullyQualifiedJavaType(s));
		}
		return true;
	}
}
