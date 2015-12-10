package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.UpdateByExampleSelectiveElementGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.UpdateByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

import com.csii.bank.core.generator.tools.CoreInsertElementGenerator;
import com.csii.bank.core.generator.tools.CoreInsertSelectiveElementGenerator;
import com.csii.bank.core.generator.tools.CoreUpdateByExampleSelectiveElementGenerator;
import com.csii.bank.core.generator.tools.CoreUpdateByExampleWithoutBLOBsElementGenerator;
import com.csii.bank.core.generator.tools.CoreUpdateByPrimaryKeySelectiveElementGenerator;
import com.csii.bank.core.generator.tools.CoreUpdateByPrimaryKeyWithoutBLOBsElementGenerator;

public class DatelastmaintPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			XmlElement parentElement = document.getRootElement();

			if (introspectedTable.getRules().generateInsert()) {
				AbstractXmlElementGenerator elementGenerator = new CoreInsertElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}

			if (introspectedTable.getRules().generateInsertSelective()) {
				AbstractXmlElementGenerator elementGenerator = new CoreInsertSelectiveElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}

			if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
				AbstractXmlElementGenerator elementGenerator = new CoreUpdateByExampleSelectiveElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}

			if (introspectedTable.getRules()
					.generateUpdateByExampleWithoutBLOBs()) {
				AbstractXmlElementGenerator elementGenerator = new CoreUpdateByExampleWithoutBLOBsElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}
			if (introspectedTable.getRules()
					.generateUpdateByPrimaryKeySelective()) {
				AbstractXmlElementGenerator elementGenerator = new CoreUpdateByPrimaryKeySelectiveElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}
			if (introspectedTable.getRules()
					.generateUpdateByPrimaryKeyWithoutBLOBs()) {
				AbstractXmlElementGenerator elementGenerator = new CoreUpdateByPrimaryKeyWithoutBLOBsElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement,
						introspectedTable);
			}
		}
		return true;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByExampleSelectiveElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean modelSetterMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		if ("DATELASTMAINT".equals(introspectedColumn.getActualColumnName())) {
			method.setVisibility(JavaVisibility.PROTECTED);
		}
		return true;
	}

	protected void initializeAndExecuteGenerator(
			AbstractXmlElementGenerator elementGenerator,
			XmlElement parentElement, IntrospectedTable introspectedTable) {
		elementGenerator.setContext(context);
		elementGenerator.setIntrospectedTable(introspectedTable);
		elementGenerator.setProgressCallback(null);
		elementGenerator.setWarnings(null);
		elementGenerator.addElements(parentElement);
	}

	private boolean hasDateLastMainColumn(List<IntrospectedColumn> columns) {
		boolean findcolumn = false;

		for (IntrospectedColumn cd : columns) {
			if (isDateLastMainColumn(cd)) {
				findcolumn = true;
				break;
			}
		}

		return findcolumn;
	}

	private boolean isDateLastMainColumn(IntrospectedColumn cd) {
		return "DATELASTMAINT".equalsIgnoreCase(cd.getActualColumnName());
	}
}
