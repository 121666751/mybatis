package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * 将sqlmap文件中的 表名前缀  替换为大写,主要是为了兼容mysql 
 * 
 * @author lulu
 *
 */
public class ToUpperCasePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		/*
		 * List<IntrospectedColumn> list = introspectedTable.getAllColumns();
		 * for (IntrospectedColumn c : list) { String s =
		 * c.getActualColumnName(); c.setActualColumnName(s.toUpperCase()); }
		 * 
		 * String qualifiedTableName = introspectedTable
		 * .getFullyQualifiedTableNameAtRuntime(); introspectedTable
		 * .setSqlMapFullyQualifiedRuntimeTableName(qualifiedTableName
		 * .toUpperCase());
		 * 
		 * String aliasedTableName = introspectedTable
		 * .getAliasedFullyQualifiedTableNameAtRuntime(); introspectedTable
		 * .setSqlMapAliasedFullyQualifiedRuntimeTableName(aliasedTableName
		 * .toUpperCase());
		 */

		String aliasedTableName = introspectedTable
				.getAliasedFullyQualifiedTableNameAtRuntime();
		String fileName = introspectedTable.getIbatis2SqlMapFileName();
		introspectedTable.setIbatis2SqlMapFileName(fileName.replace(
				aliasedTableName, aliasedTableName.toUpperCase()));

		String nameSpace = introspectedTable.getIbatis2SqlMapNamespace();
		introspectedTable.setIbatis2SqlMapNamespace(nameSpace.toUpperCase());
	}

}
