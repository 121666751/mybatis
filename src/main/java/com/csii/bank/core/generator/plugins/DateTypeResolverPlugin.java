package com.csii.bank.core.generator.plugins;

import java.sql.Types;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

public class DateTypeResolverPlugin extends PluginAdapter {
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		IntrospectedColumn column = introspectedTable
				.getColumn("DATELASTMAINT");
		if (column != null) {
			column.setJdbcTypeName("TIMESTAMP");
		}

		List<IntrospectedColumn> cList = introspectedTable.getAllColumns();
		for (IntrospectedColumn c : cList) {
			if (!"DATELASTMAINT".equals(c.getActualColumnName())&& Types.DATE == c.getJdbcType()) {
				if(c.getActualColumnName().contains("DATETIME")){
					c.setJdbcTypeName("TIMESTAMP");
				}
			}
		}
		super.initialized(introspectedTable);
	}
}
