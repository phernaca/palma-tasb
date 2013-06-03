/*
 * Copyright (c) 2009 THALES
 * 18 avenue du Marechal Juin, Meudon La Foret (92), FRANCE.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * THALES. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with THALES.
 */
package ext.thales.palma.load.imp;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import wt.doc.WTDocument;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.litedefinition.BooleanDefView;
import wt.iba.definition.litedefinition.StringDefView;
import wt.iba.definition.litedefinition.TimestampDefView;
import wt.iba.definition.service.IBADefinitionHelper;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.litevalue.BooleanValueDefaultView;
import wt.iba.value.litevalue.StringValueDefaultView;
import wt.iba.value.litevalue.TimestampValueDefaultView;
import wt.load.LoadServerHelper;
import wt.util.WTException;
import ext.thales.palma.enterprise.DocumentType;
import ext.thales.palma.enterprise.InstanceBasedAttributeIF;
import ext.thales.palma.util.service.PalmaIBAHelper;

/**
 * Engineering Change Proposal loader.
 * 
 * <br><br>
 * @author		Nicolas
 * @version		3.0		4 janv. 2010
 * <br><br>
 * <pre>
 * +-----------------+---------------------------+------------------+
 * | PR Number       | Method Name               | Build Number(*)  |
 * +-----------------+---------------------------+------------------+
 * |                 |                           |                  |
 * |                 |                           |                  |
 *
 * (C) Create     (U) Update     (D) Delete
 * (*) Build Number is mandatory for delivery purpose.
 * </pre>
 */
public class LoadPalmaECP extends LoadPalmaDocument {
	/* TAS BEGIN SDD-TASB.CHG.0140
	Use LoadPalmaDocument as SuperClass  in order to take into account "11version" attribute
 	TAS END */
	
	//CHECKSTYLE:OFF-BEGIN PALMA_CORE CODE
	/**
	 * FULL LOG PATH VARIABLE
	 */
	private static String szFullLogPath;	
	/**
	 * FULL FAILURES PATH VARIABLE
	 */
	private static String szFullFailuresPath;
	/**
	 * KEYWORD FOR ECP LOADER	
	 */
	private static final String KEYWORD = "CreatePalmaECP";
	
	
	/**
	 * CSV column name for ECP Class
	 */
	private static final String ECP_CLASS_CVS_NAME = "21ECPClass";	
		
	/**
	 * CSV column name for askingForCustomerApproval
	 */
	private static final String ASKING_CUSTOMER_APPROVAL_CVS_NAME = "22askingForCustomerApproval";
	
	/**
	 * CSV column name for customerName
	 */
	private static final String CUSTOMER_NAME_CVS_NAME = "23customerName";	
	
	/**
	 * CSV column name for customerApprovalDate
	 */
	private static final String CUSTOMER_APPROVAL_DATE_CVS_NAME = "24customerApprovalDate";	
	
	/**
	 * CSV column name for askingForOfficialServicesApproval
	 */
	private static final String ASKING_OFFICIAL_SERVICE_APPROVAL_CVS_NAME = "25askingForOfficialServicesApproval";
	
	/**
	 * CSV column name for officialServicesApprovalName
	 */
	private static final String OFFICIAL_SERVICE_APPROVAL_NAME_CVS_NAME = "26officialServicesApprovalName";	
	
	/**
	 * CSV column name for officialServicesApprovalDate
	 */
	private static final String OFFICIAL_SERVICE_APPROVAL_DATE_CVS_NAME = "27officialServicesApprovalDate";		
	
	
	static {
		try {
			szFullLogPath = getLogFilePath(KEYWORD);
			szFullFailuresPath = getFailuresFilePath(KEYWORD);
			
		}
		// CHECKSTYLE:OFF-IllegalCatchCheck - extends
		catch (Exception e) {
			e.printStackTrace(); // NOPMD
			LoadServerHelper.printMessage("Error while initializing LoadPalmaECP : "
					+ e.getLocalizedMessage());
		}
	}
	// CHECKSTYLE:ON-IllegalCatchCheck - extends		

	/**
	 * LoadPalmaECP Constructor
	 * @param attrTable
	 * @param cmdLine
	 */
	public LoadPalmaECP(
			Hashtable<String, String> attrTable, // NOPMD
			Hashtable<String, String> cmdLine) { // NOPMD
		super(attrTable, cmdLine);
	}

	// CHECKSTYLE:OFF-IllegalTypeCheck	
	/**
	 * method called to load ECP in csvmapfile
	 * @param attrTable
	 * @param cmd_line
	 * @param return_objects
	 * @return
	 */
	public static boolean createECP(
			Hashtable<String, String> attrTable, // NOPMD
			Hashtable<String, String> cmd_line,  // NOPMD
			Vector return_objects) 					
	{
		LoadPalmaECP loader = 
			new LoadPalmaECP(attrTable, cmd_line);
		
		return loader.createECP();
	}
	// CHECKSTYLE:ON-IllegalTypeCheck	

	/**
	 * Create a new ECP with characteristics specified in {@link this#attr_table}.
	 * 
	 * @return	creation status: <tt>false</tt> means failure.
	 */
	protected boolean createECP() {

    	boolean success = false;
    	
    	try {
    		// Set the soft type
    		attr_table.put(SOFT_TYPE, DocumentType.ECP.getDisplay());
    		
    		success = super.createDocument();
    		
		} 
		// CHECKSTYLE:OFF-IllegalCatchCheck - extends
    	catch (Exception e) {
			String szNumber = LoadServerHelper.getValue(
	    			NUMBER, attr_table, cmd_line, LoadServerHelper.REQUIRED);
			logError(e, szNumber, attr_table, 
					szFullLogPath, szFullFailuresPath, KEYWORD);
    	}
    	// CHECKSTYLE:ON-IllegalCatchCheck - extends		

    	return success;
	}

	/**
	 * Method to return the attrContainer
	 * @param attrContainer
	 * @param ecpDoc
	 * @return
	 * @throws WTException
	 */
	private static DefaultAttributeContainer getAttrContainer (DefaultAttributeContainer attrContainer, WTDocument doc) throws WTException{
		DefaultAttributeContainer szAttrContainer = attrContainer ;
		WTDocument ecpDoc = null ;
		if (szAttrContainer == null){
			ecpDoc = (WTDocument) PalmaIBAHelper.service.refresh(doc);
			szAttrContainer = (DefaultAttributeContainer) ecpDoc.getAttributeContainer();
		}
		return szAttrContainer ;
	}
	
	
	/**
	 * @return the szFullLogPath
	 */
	public String getSzFullLogPath() {
		return szFullLogPath;
	}

	/**
	 * @return the szFullFailuresPath
	 */
	public String getSzFullFailuresPath() {
		return szFullFailuresPath;
	}
	
	// CHECKSTYLE:OFF-IllegalTypeCheck	
	@Override
	protected void setSpecificIBAs(WTDocument doc) throws Exception { // NOPMD
				DefaultAttributeContainer attrContainer = 
	    	   (DefaultAttributeContainer) doc.getAttributeContainer();
		attrContainer = getAttrContainer(attrContainer, doc);
		String szCtd = LoadServerHelper.getValue(
				CTD, attr_table, cmd_line, LoadServerHelper.REQUIRED);
		
		/*String szCage = LoadServerHelper.getValue(
				CAGE, attr_table, cmd_line, LoadServerHelper.REQUIRED);*/
		
		// ECP Class
		String szECPClass = LoadServerHelper.getValue(
				ECP_CLASS_CVS_NAME, attr_table, cmd_line, LoadServerHelper.REQUIRED);		
				
		// Asking Customer Aproval
		String szAskingCustomerApproval = LoadServerHelper.getValue(
				ASKING_CUSTOMER_APPROVAL_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		// Customer Name
		String szCustomerName = LoadServerHelper.getValue(
				CUSTOMER_NAME_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		// Expected customer approval date
		String szCustomerApprovalDate = LoadServerHelper.getValue(
				CUSTOMER_APPROVAL_DATE_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		// Asking Offcicial Services Aproval
		String szAskingOfficialServiceApproval = LoadServerHelper.getValue(
				ASKING_OFFICIAL_SERVICE_APPROVAL_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		// Official Services Approval Name
		String szOfficialServiceApprovalName = LoadServerHelper.getValue(
				OFFICIAL_SERVICE_APPROVAL_NAME_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		// Expected customer approval date
		String szOfficialServiceApprovalDate = LoadServerHelper.getValue(
				OFFICIAL_SERVICE_APPROVAL_DATE_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);		
		
		AbstractValueView attrValueView = null;
		AttributeDefDefaultView attrDefinizerView = null;
		
		// CTD
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(
				InstanceBasedAttributeIF.CTD_IBA_NAME);
		attrValueView = new StringValueDefaultView(
				(StringDefView) attrDefinizerView, szCtd);
		attrContainer.deleteAttributeValues(attrDefinizerView);
		attrContainer.addAttributeValue(attrValueView);
		
		// ECP Class
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(
				InstanceBasedAttributeIF.ECP_CLASS_IBA_NAME);
		AbstractValueView attrValueViewECP = new StringValueDefaultView(
				(StringDefView) attrDefinizerView, szECPClass);
		attrContainer.deleteAttributeValues(attrDefinizerView);
		attrContainer.addAttributeValue(attrValueViewECP);	
				
		// Asking Customer Aproval
		if(szAskingCustomerApproval != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.ASKING_FOR_CUSTOMER_APPROVAL_IBA_NAME);
			attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szAskingCustomerApproval));
			attrContainer.addAttributeValue(attrValueView);
		}

		// Customer Name
		if(szCustomerName != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.CUSTOMER_NAME_IBA_NAME);
			attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szCustomerName);
			attrContainer.addAttributeValue(attrValueView);
		}		
		
		// Customer Aproval Date
		if (szCustomerApprovalDate != null) {
			Timestamp createTimestamp = new Timestamp(sdf.parse(szCustomerApprovalDate).getTime());
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.CUSTOMER_APPROVAL_DATE_IBA_NAME);
			attrValueView = new TimestampValueDefaultView((TimestampDefView) attrDefinizerView, createTimestamp);
			attrContainer.addAttributeValue(attrValueView);
		}		
		
		
		// Asking Official Service Aproval
		if(szAskingOfficialServiceApproval != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.ASKING_FOR_OFFICIAL_SERVICES_APPROVAL_IBA_NAME);
			attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szAskingOfficialServiceApproval));
			attrContainer.addAttributeValue(attrValueView);
		}

		// Official Services Approval Name
		if(szOfficialServiceApprovalName != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.OFFICIAL_SERVICES_APPROVAL_NAME_IBA_NAME);
			attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szOfficialServiceApprovalName);
			attrContainer.addAttributeValue(attrValueView);
		}		
		
		// Official Service Aproval Date
		if (szOfficialServiceApprovalDate != null) {
			Timestamp createTimestamp = new Timestamp(sdf.parse(szOfficialServiceApprovalDate).getTime());
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.OFFICIAL_SERVICES_APPROVAL_DATE_IBA_NAME);
			attrValueView = new TimestampValueDefaultView((TimestampDefView) attrDefinizerView, createTimestamp);
			attrContainer.addAttributeValue(attrValueView);
		}				
		
	}
	// CHECKSTYLE:ON-IllegalTypeCheck	
	/**
	 * Trace error into log file and rewrite failed line
	 * into failures file.
	 * 
	 * @param nv
	 * @param cmd_line
	 * @param return_objects
	 * @return
	 */
	@Override
	//CHECKSTYLE:OFF-IllegalType
	protected void logError(
			Exception e, 
			String szIdentifier, 
			Hashtable<String, String> attrTable,//NOPMD
			String szLogPath, 
			String szFailuresPath, 
			String szClassName) {
		//CHECKSTYLE:ON-IllegalType	
		super.logError(e, szIdentifier, attrTable, getSzFullLogPath(), getSzFullFailuresPath(), KEYWORD);
	}
	
}
