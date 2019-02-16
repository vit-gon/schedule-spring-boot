package com.vitgon.schedule.model.database.translation.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vitgon.schedule.model.database.Locale;
import com.vitgon.schedule.model.database.Subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubjectTranslationId implements Serializable {
	
	private static final long serialVersionUID = -7802327655330293030L;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;
	
	@ManyToOne
	@JoinColumn(name = "locale_id")
	private Locale locale;
}