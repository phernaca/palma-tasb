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
import java.util.List;
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
import ext.thales.palma.enterprise.DocumentType;
import ext.thales.palma.enterprise.InstanceBasedAttributeIF;
import ext.thales.palma.util.service.PalmaIBAHelper;

/**
 * Request For Deviation loader (RFD).
 *
 * <br><br>
 * @author		Nicolas
 * @version		3.0		6 janv. 2010
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
public class LoadPalmaRFD extends LoadPalmaDocument {
	/* TAS BEGIN SDD-TASB.CHG.0130
	Use LoadPalmaDocument as SuperClass  in order to take into account "11version" attribute
 	TAS END */
	//CHECKSTYLE:OFF-BEGIN PALMA_CORE CODE
	
	/**
	 * Log Path
	 */
	private static String szFullLogPath;
	
	/**
	 * Failure Path
	 */
	private static String szFullFailuresPath;

	/**
	 * Keyword
	 */
	private static final String KEYWORD = "CreatePalmaRFD";

	/**
	 * Visibility values set
	 */
	private static List <String> visibilityValues = null;
	
	/**
	 * Severity values set
	 */
	private static List <String> severityValues = null;
	
	/**
	 * Classification values set
	 */
	private static List <String> classificationValues = null;

	/**
	 * CSV column name for visibility
	 */
	private static final String VISIBILITY_CVS_NAME = "21visibility";
	
	/**
	 * CSV column name for severity
	 */
	private static final String SEVERITY_CVS_NAME = "23severity";
	
	/**
	 * CSV column name for expectedClosureDate
	 */
	private static final String EXPECTED_CLOSURE_DATE_CVS_NAME = "24expectedClosureDate";
	
	/**
	 * CSV column name for usageRestriction
	 */
	private static final String USAGE_RESTRICTION_CVS_NAME = "25usageRestriction";
	
	/**
	 * CSV column name for closureCriterion
	 */
	private static final String CLOSURE_CRITERION_CVS_NAME = "26closureCriterion";
	
	/**
	 * CSV column name for safetyImpactDerog
	 */
	private static final String SAFETY_IMPACT_CVS_NAME = "27safetyImpactDerog";
	
	/**
	 * CSV column name for performanceImpact
	 */
	private static final String PERFORMANCE_IMPACT_CVS_NAME = "28performanceImpact";
	
	/**
	 * CSV column name for functionalImpact
	 */
	private static final String FUNCTIONAL_IMPACT_CVS_NAME = "29functionalImpact";
	
	/**
	 * CSV column name for logisticImpact
	 */
	private static final String LOGISTIC_IMPACT_CVS_NAME = "30logisticImpact";
	
	/**
	 * CSV column name for reliabilityImpact
	 */
	private static final String RELIABILITY_IMPACT_CVS_NAME = "31reliabilityImpact";
	
	/**
	 * CSV column name for visualImpact
	 */
	private static final String VISUAL_IMPACT_CVS_NAME = "32visualImpact";
	
	/**
	 * CSV column name for sizeImpact
	 */
	private static final String SIZE_IMPACT_CVS_NAME = "33sizeImpact";
	
	/**
	 * CSV column name for weightImpact
	 */
	private static final String WEIGHT_IMPACT_CVS_NAME = "34weightImpact";
	
	/**
	 * CSV column name for otherImpact
	 */
	private static final String OTHER_IMPACT_CVS_NAME = "35otherImpact";
	
	/**
	 * CSV column name for classification
	 */
	private static final String CLASSIFICATION_CVS_NAME = "36classification";

	static {
		try {
			szFullLogPath = getLogFilePath(KEYWORD);
			szFullFailuresPath = getFailuresFilePath(KEYWORD);

		} catch (Exception e) {
			e.printStackTrace(); // NOPMD
			LoadServerHelper.printMessage("Error while initializing LoadPalmaRFD : "
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * Constructor
	 */
	public LoadPalmaRFD(
			Hashtable<String, String> attrTable, // NOPMD
			Hashtable<String, String> cmdLine) { // NOPMD
		super(attrTable, cmdLine);
	}

	/**
	 * Create a RFD
	 * 
	 * @param attrTable
	 * @param cmd_line
	 * @param return_objects
	 * @return
	 */
	public static boolean createRFD(
			Hashtable<String, String> attrTable, // NOPMD
			Hashtable<String, String> cmd_line, // NOPMD
			Vector<?> return_objects)
	{
		LoadPalmaRFD loader =
			new LoadPalmaRFD(attrTable, cmd_line);

		return loader.createRFD();
	}

	/**
	 * Create a new RFD with characteristics specified in {@link this#attr_table}.
	 *
	 * @return	creation status: <tt>false</tt> means failure.
	 */
	protected boolean createRFD() {

    	boolean success = false;

    	try {
    		// Set the soft type
    		attr_table.put(SOFT_TYPE, DocumentType.RFD.getDisplay());
    		
    		success = super.createDocument();
            
		} catch (Exception e) {
			String szNumber = LoadServerHelper.getValue(
	    			NUMBER, attr_table, cmd_line, LoadServerHelper.REQUIRED);
			logError(e, szNumber, attr_table,
					szFullLogPath, szFullFailuresPath, KEYWORD);
    	}

    	return success;
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
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void setSpecificIBAs(WTDocument doc) throws Exception // NOPMD
	{
		if (visibilityValues == null)
		{
			visibilityValues = getValuesSet(doc, InstanceBasedAttributeIF.VISIBILITY_IBA_NAME);
		}

		if (severityValues == null)
		{
			severityValues = getValuesSet(doc, InstanceBasedAttributeIF.SEVERITY_IBA_NAME);
		}
		
		if (classificationValues == null)
		{
			classificationValues = getValuesSet(doc, InstanceBasedAttributeIF.CLASSIFICATION_IBA_NAME);
		}

        DefaultAttributeContainer attrContainer = (DefaultAttributeContainer) doc.getAttributeContainer();
        if (attrContainer == null)
        {
        	doc = (WTDocument) PalmaIBAHelper.service.refresh(doc);
			attrContainer = (DefaultAttributeContainer) doc.getAttributeContainer();
		}

        // --- Get value from file
		// CTD
		String szCtd = LoadServerHelper.getValue(
				CTD, attr_table, cmd_line, LoadServerHelper.REQUIRED);

        // Visibility
		String szVisibility = getRequiredAndValidValue(VISIBILITY_CVS_NAME, visibilityValues);
		
        // Severity
		String szSeverity = getRequiredAndValidValue(SEVERITY_CVS_NAME, severityValues);
		
		// Classification
		String szClassification = getRequiredAndValidValue(CLASSIFICATION_CVS_NAME, classificationValues);
		
		// Expected closure date
		String szExpectedClosureDate = LoadServerHelper.getValue(
				EXPECTED_CLOSURE_DATE_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Usage restriction
		String szUsageRestriction = LoadServerHelper.getValue(
				USAGE_RESTRICTION_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Closure criterion
		String szClosureCriterion = LoadServerHelper.getValue(
				CLOSURE_CRITERION_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Safety impact
		String szSafetyImpactDerog = LoadServerHelper.getValue(
				SAFETY_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Performance impact
		String szPerformanceImpact = LoadServerHelper.getValue(
				PERFORMANCE_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Functionnal impact
		String szFunctionalImpact = LoadServerHelper.getValue(
				FUNCTIONAL_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Logistic impact
		String szLogisticImpact = LoadServerHelper.getValue(
				LOGISTIC_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Reliability impact
		String szReliabilityImpact = LoadServerHelper.getValue(
				RELIABILITY_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Visual impact
		String szVisualImpact = LoadServerHelper.getValue(
				VISUAL_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Size impact
		String szSizeImpact = LoadServerHelper.getValue(
				SIZE_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Weight impact
		String szWeightImpact = LoadServerHelper.getValue(
				WEIGHT_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

		// Other impact
		String szOtherImpact = LoadServerHelper.getValue(
				OTHER_IMPACT_CVS_NAME, attr_table, cmd_line, LoadServerHelper.NOT_REQUIRED);

    	AbstractValueView attrValueView = null;
		AttributeDefDefaultView attrDefinizerView = null;

		// --- Set IBA values on container
		
		// CTD
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.CTD_IBA_NAME);
		AbstractValueView attrValueViewCtd = new StringValueDefaultView((StringDefView) attrDefinizerView, szCtd);
		attrContainer.deleteAttributeValues(attrDefinizerView);
		attrContainer.addAttributeValue(attrValueViewCtd);

		// Visibility
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.VISIBILITY_IBA_NAME);
		attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szVisibility);
		attrContainer.addAttributeValue(attrValueView);

		// Severity
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.SEVERITY_IBA_NAME);
		attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szSeverity);
		attrContainer.addAttributeValue(attrValueView);
		
		// Classification
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.CLASSIFICATION_IBA_NAME);
		attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szClassification);
		attrContainer.addAttributeValue(attrValueView);

		// Expected closure date
		if (szExpectedClosureDate != null) {
			Timestamp createTimestamp = new Timestamp(sdf.parse(szExpectedClosureDate).getTime());
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.EXPECTED_CLOSURE_DATE_IBA_NAME);
			attrValueView = new TimestampValueDefaultView((TimestampDefView) attrDefinizerView, createTimestamp);
			attrContainer.addAttributeValue(attrValueView);
		}

		// Usage restriction
		if(szUsageRestriction != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.USAGE_RESTRICTION_IBA_NAME);
			attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szUsageRestriction);
			attrContainer.addAttributeValue(attrValueView);
		}

		// Lift criterion
		if(szClosureCriterion != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.CLOSURE_CRITERION_IBA_NAME);
			attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szClosureCriterion);
			attrContainer.addAttributeValue(attrValueView);
		}

		// Safety impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.SAFETY_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szSafetyImpactDerog));
		attrContainer.addAttributeValue(attrValueView);

		// Performance impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.PERFORMANCE_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szPerformanceImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Functionnal impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.FUNCTIONAL_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szFunctionalImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Logistic impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.LOGISTIC_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szLogisticImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Fiability impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.RELIABILITY_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szReliabilityImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Visual impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.VISUAL_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szVisualImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Size impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.SIZE_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szSizeImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Weight impact
		attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(InstanceBasedAttributeIF.WEIGHT_IMPACT_IBA_NAME);
		attrValueView = new BooleanValueDefaultView((BooleanDefView) attrDefinizerView, "1".equals(szWeightImpact));
		attrContainer.addAttributeValue(attrValueView);

		// Other impact
		if(szOtherImpact != null)
		{
			attrDefinizerView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath( InstanceBasedAttributeIF.OTHER_IMPACT_IBA_NAME);
			attrValueView = new StringValueDefaultView((StringDefView) attrDefinizerView, szOtherImpact);
			attrContainer.addAttributeValue(attrValueView);
		}
		
		// Save attribute container
		attrContainer = (DefaultAttributeContainer) 
			dbService.updateAttributeContainer(
					doc,
					attrContainer.getConstraintParameter(), 
					null, 
					null);
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
