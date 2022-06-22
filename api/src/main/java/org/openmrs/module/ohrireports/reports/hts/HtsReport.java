/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ohrireports.reports.hts;

import org.openmrs.api.EncounterService;
import org.openmrs.module.ohrireports.reports.library.EncounterDataLibrary;
import org.openmrs.module.ohrireports.reports.library.PatientDataLibrary;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.ObsValueConverter;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.BasicEncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;

@Component
public class HtsReport implements ReportManager {
	
	@Autowired
	EncounterService encounterService;
	
	@Autowired
	PatientDataLibrary hpdl;
	
	@Autowired
	EncounterDataLibrary hedl;
	
	@Override
	public String getUuid() {
		return HTS_REPORT_UUID;
	}
	
	@Override
	public String getName() {
		return "HTS Report";
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public List<Parameter> getParameters() {
		return null;
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setUuid(getUuid());
		reportDefinition.setName(getName());
		reportDefinition.setDescription(getDescription());
		reportDefinition.setParameters(getParameters());
		
		EncounterCohortDefinition ecd = new EncounterCohortDefinition();
		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_RETROSPECTIVE_ENCOUNTER_TYPE));
		reportDefinition.setBaseCohortDefinition(Mapped.mapStraightThrough(ecd));
		
		EncounterDataSetDefinition edsd = new EncounterDataSetDefinition();
		edsd.addRowFilter(getEncounterQuery());
		addColumns(edsd);
		
		reportDefinition.addDataSetDefinition("HTS", edsd, Collections.EMPTY_MAP);
		return reportDefinition;
	}
	
	private void addColumns(EncounterDataSetDefinition edsd) {
		edsd.addColumn("Name", hpdl.getName(), "");
		edsd.addColumn("Age", hpdl.getAge(), "", new AgeConverter());
		edsd.addColumn("Sex", hpdl.getGender(), "");
		edsd.addColumn("Setting of HIV test", hedl.getObsValue(SETTING_OF_HIV_TEST), "", new ObsValueConverter());
		edsd.addColumn("Approach", hedl.getObsValue(APPROACH), "", new ObsValueConverter());
		edsd.addColumn("Population type", hedl.getObsValue(POPULATION_TYPE), "", new ObsValueConverter());
		edsd.addColumn("Initial HIV Test result", hedl.getObsValue(INITIAL_HIV_TEST_RESULT), "", new ObsValueConverter());
		edsd.addColumn("Confirmatory HIV test result", hedl.getObsValue(CONFIRMATORY_HIV_TEST_RESULT), "",
		    new ObsValueConverter());
		edsd.addColumn("Final HIV Result", hedl.getObsValue(FINAL_HIV_RESULT), "", new ObsValueConverter());
		edsd.addColumn("Date client received final result", hedl.getObsValue(DATE_CLIENT_RECEIVED_FINAL_RESULT), "",
		    new ObsValueConverter());
		edsd.addColumn("Linked to care and treatment in this facility",
		    hedl.getObsValue(LINKED_TO_CARE_AND_TREATMENT_IN_THIS_FACILITY), "", new ObsValueConverter());
	}
	
	private Mapped<? extends EncounterQuery> getEncounterQuery() {
		BasicEncounterQuery encounterQuery = new BasicEncounterQuery();
		encounterQuery.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
		encounterQuery.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_RETROSPECTIVE_ENCOUNTER_TYPE));
		
		return Mapped.mapStraightThrough(encounterQuery);
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createCsvReportDesign(HTS_REPORT_DESIGN_UUID, reportDefinition);
		
		return Arrays.asList(design);
	}
	
	@Override
	public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
		return null;
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
}
