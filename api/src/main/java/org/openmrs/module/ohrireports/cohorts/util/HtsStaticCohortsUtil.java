/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ohrireports.cohorts.util;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.CohortTypeService;

import static org.openmrs.module.ohrireports.cohorts.util.CohortConstants.*;

public class HtsStaticCohortsUtil {
	
	public static void setupCohortType() {
		CohortType hts = new CohortType();
		hts.setUuid(HTS_COHORT_TYPE_UUID);
		hts.setName(HTS_COHORT_TYPE_NAME);
		hts.setDescription(HTS_COHORT_TYPE_DESCRIPTION);
		CohortTypeService cohortTypeService = Context.getService(CohortTypeService.class);
		
		CohortType existing = cohortTypeService.getByUuid(HTS_COHORT_TYPE_UUID);
		if (existing != null) {
			hts.setId(existing.getId());
			try {
				Context.evictFromSession(existing);
			}
			catch (IllegalArgumentException e) {
				
			}
		}
		cohortTypeService.saveCohortType(hts);
	}
	
	public static void setupHtsCohorts() {
		setupCohortType();
		setupHtsCohort(HTS_PRE_TEST_COHORT_UUID, HTS_PRE_TEST_COHORT_NAME);
		setupHtsCohort(HTS_WAITING_FOR_TEST_COHORT_UUID, HTS_WAITING_FOR_TEST_COHORT_NAME);
		setupHtsCohort(HTS_POST_TEST_COHORT_UUID, HTS_POST_TEST_COHORT_NAME);
	}
	
	public static void setupHtsCohort(String uuid, String name) {
		CohortM cm = new CohortM();
		cm.setUuid(uuid);
		cm.setName(name);
		CohortService cohortService = Context.getService(CohortService.class);
		CohortTypeService cohortTypeService = Context.getService(CohortTypeService.class);
		cm.setCohortType(cohortTypeService.getByUuid(HTS_COHORT_TYPE_UUID));
		
		CohortM existing = cohortService.getCohortByUuid(uuid);
		if (existing != null) {
			cm.setId(existing.getId());
			try {
				Context.evictFromSession(existing);
			}
			catch (IllegalArgumentException e) {
				
			}
		}
		cohortService.saveCohort(cm);
	}
}
