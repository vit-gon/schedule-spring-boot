package com.vitgon.schedule.service.database;

import com.vitgon.schedule.model.database.Subject;
import com.vitgon.schedule.projection.SubjectProjection;
import com.vitgon.schedule.service.database.base.Service;

import java.util.List;

public interface SubjectService extends Service<Subject, Integer> {
	Subject findByName(String name);
	List<SubjectProjection> findAllByBrowserDefaultLocale();
	List<SubjectProjection> findAllByLocaleId(Integer localeId);
}
