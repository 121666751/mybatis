package com.csii.bank.core.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.csii.bank.core.generator.tools.MyBatis3_UpdateByExampleSelectiveElementGenerator;
import com.csii.bank.core.generator.tools.MyBatis3_UpdateByExampleWithoutBLOBsElementGenerator;
import com.csii.bank.core.generator.tools.MyBatis3_UpdateByPrimaryKeySelectiveElementGenerator;
import com.csii.bank.core.generator.tools.MyBatis3_UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

/**
 * DATELASTMAINT统一赋值插件
 * 
 * MyBatis3版本
 * 
 * 已知问题：
 * <li>没有针对context的defaultModelType属性做相应的处理，不支持MyBatis3Simple设置</li>
 * <li>没有针对javaClientGenerator的type属性做相应的处理，只支持XMLMAPPER，即SQL完全依赖XML</li>
 * 
 * @author GaoYu
 * 
 */
public class MyBatis3_DatelastmaintPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			XmlElement parentElement = document.getRootElement();

			// insert情况不再考虑，由数据库的default默认

			if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
				AbstractXmlElementGenerator elementGenerator = new MyBatis3_UpdateByExampleSelectiveElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement, introspectedTable);
			}
			if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
				AbstractXmlElementGenerator elementGenerator = new MyBatis3_UpdateByExampleWithoutBLOBsElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement, introspectedTable);
			}
			if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
				AbstractXmlElementGenerator elementGenerator = new MyBatis3_UpdateByPrimaryKeySelectiveElementGenerator();
				initializeAndExecuteGenerator(elementGenerator, parentElement, introspectedTable);
			}
			if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
				AbstractXmlElementGenerator elementGenerator = new MyBatis3_UpdateByPrimaryKeyWithoutBLOBsElementGenerator(false);
				initializeAndExecuteGenerator(elementGenerator, parentElement, introspectedTable);
			}
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		if ("DATELASTMAINT".equalsIgnoreCase(introspectedColumn.getActualColumnName())) {
			method.setVisibility(JavaVisibility.PROTECTED);
		}
		return true;
	}

	protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement,
			IntrospectedTable introspectedTable) {
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
