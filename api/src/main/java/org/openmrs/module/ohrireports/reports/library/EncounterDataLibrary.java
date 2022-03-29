/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ohrireports.reports.library;

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.ObsForEncounterDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class EncounterDataLibrary extends BaseDefinitionLibrary<EncounterDataDefinition> {
	
	@Autowired
	ConceptService conceptService;
	
	@Override
	public Class<? super EncounterDataDefinition> getDefinitionType() {
		return EncounterDataDefinition.class;
	}
	
	@Override
	public String getKeyPrefix() {
		return "ohri.encounterData.hts.";
	}
	
	public ObsForEncounterDataDefinition getObsValue(String conceptUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		ofedd.setSingleObs(true);
		return ofedd;
	}
	
	public ObsForEncounterDataDefinition getObsValue(String conceptUuid, String answerUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		Concept answer = conceptService.getConceptByUuid(answerUuid);
		ofedd.setAnswers(Collections.singletonList(answer));
		ofedd.setSingleObs(true);
		return ofedd;
	}
}