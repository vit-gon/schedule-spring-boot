package com.vitgon.schedule.model.translation.pk;

import java.io.Serializable;

import com.vitgon.schedule.model.Group;
import com.vitgon.schedule.model.Locale;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"group", "locale"})
public class GroupTranslationId implements Serializable {
	
	private static final long serialVersionUID = 6221021972068258200L;
	
	private Group group;
	private Locale locale;
}
