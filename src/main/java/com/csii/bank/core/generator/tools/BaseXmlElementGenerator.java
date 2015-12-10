package com.csii.bank.core.generator.tools;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.AbstractXmlElementGenerator;

public abstract class BaseXmlElementGenerator extends
		AbstractXmlElementGenerator {
	protected final static String DATELASTMAINT = "DATELASTMAINT";
	protected final static String SYSDATE = "CSII_CURRENTDATETIME()";
	protected final static String UPDATE_CLAUSE = "set DATELASTMAINT = "
			+ SYSDATE + ",";

	public BaseXmlElementGenerator() {
		super();
	}

	protected boolean hasDateLastMainColumn(List<IntrospectedColumn> columns) {
		boolean findcolumn = false;

		for (IntrospectedColumn cd : columns) {
			if (isDateLastMainColumn(cd)) {
				findcolumn = true;
				break;
			}
		}

		return findcolumn;
	}

	protected boolean isDateLastMainColumn(IntrospectedColumn cd) {
		return DATELASTMAINT.equalsIgnoreCase(cd.getActualColumnName());
	}
}
