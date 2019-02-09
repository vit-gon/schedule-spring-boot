package com.vitgon.schedule.dto;

import java.io.Serializable;

import com.vitgon.schedule.model.Schedule;
import com.vitgon.schedule.model.Subject;
import com.vitgon.schedule.model.auth.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditScheduleDTO implements Serializable {
	private static final long serialVersionUID = 4460476751959094969L;

	private int scheduleId;
	
	private Subject subject;
	private String lessonType;
	private User user;
	private String classroom;
}
