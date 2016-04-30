package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * @author lubiao
 *
 */
public class DaoAutoScanPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addAnnotation("@Repository");
		topLevelClass
				.addImportedType("org.springframework.stereotype.Repository");

		topLevelClass.setSuperClass("BaseDaoSupport");
		topLevelClass
				.addImportedType("com.csii.bank.core.base.support.dao.BaseDaoSupport");
		return true;
	}
}
