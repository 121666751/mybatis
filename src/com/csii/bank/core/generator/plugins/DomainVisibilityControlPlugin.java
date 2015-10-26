package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class DomainVisibilityControlPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (interfaze != null
				&& JavaVisibility.PUBLIC.equals(interfaze.getVisibility())) {
			interfaze.setVisibility(JavaVisibility.DEFAULT);
		}
		return super.clientGenerated(interfaze, topLevelClass,
				introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if (topLevelClass != null
				&& JavaVisibility.PUBLIC.equals(topLevelClass.getVisibility())) {
			topLevelClass.setVisibility(JavaVisibility.DEFAULT);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass,
				introspectedTable);
	}

}
