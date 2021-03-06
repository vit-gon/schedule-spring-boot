package com.vitgon.schedule.service.database.translation;

import com.vitgon.schedule.model.database.translation.SchoolTranslation;
import com.vitgon.schedule.model.database.translation.pk.SchoolTranslationId;
import com.vitgon.schedule.service.database.base.Service;

import java.util.Optional;

public interface SchoolTranslationService extends Service<SchoolTranslation, SchoolTranslationId> {
	Optional<SchoolTranslation> findByLocaleIdAndSchoolId(Integer localeId, Integer schoolId);
	Integer save(Integer schoolId, Integer localeId, String translation);
	void deleteBySchoolIdAndLocaleId(Integer schoolId, Integer localeId);
}
