package com.vitgon.schedule.controller.rest.control;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.vitgon.schedule.dto.MajorDtoControl;
import com.vitgon.schedule.service.MajorConverterService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/control/majors")
public class MajorsRestControllerControl {
	
	private MajorConverterService majorConverterService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<MajorDtoControl> getMajors(@RequestParam("localeId") int localeId) {
		if (localeId < 0) {
			throw new IllegalArgumentException("Locale id must be equal or greater than 0!");
		}
		List<MajorDtoControl> majorDtoControlList = null;
		
		if (localeId == 0) {
			majorDtoControlList = majorConverterService.convertToMajorDtoControlList();
		} else {
			majorDtoControlList = majorConverterService.convertToMajorDtoControlList(localeId);
		}
		
		return majorDtoControlList;
	}
}