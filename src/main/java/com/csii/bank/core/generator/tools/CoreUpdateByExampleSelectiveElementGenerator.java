package com.csii.bank.core.generator.tools;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

public class CoreUpdateByExampleSelectiveElementGenerator extends BaseXmlElementGenerator{
    public CoreUpdateByExampleSelectiveElementGenerator() {
        super();
    }
    
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                        "id", introspectedTable.getUpdateByExampleSelectiveStatementId())); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("dynamic"); //$NON-NLS-1$
        dynamicElement.addAttribute(new Attribute("prepend", hasDateLastMainColumn(introspectedTable.getNonPrimaryKeyColumns()) ? UPDATE_CLAUSE : "set")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getAllColumns()) {
        	if(isDateLastMainColumn(introspectedColumn)){
        		continue;
        	}
            XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
            isNotNullElement.addAttribute(new Attribute(
                    "property", introspectedColumn.getJavaProperty("record."))); //$NON-NLS-1$ //$NON-NLS-2$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(Ibatis2FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(Ibatis2FormattingUtilities.getParameterClause(
                    introspectedColumn, "record.")); //$NON-NLS-1$

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        XmlElement isParameterPresentElement = new XmlElement(
                "isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresentElement);

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresentElement.addElement(includeElement);
        
        parentElement.addElement(answer);
    }
   
}
