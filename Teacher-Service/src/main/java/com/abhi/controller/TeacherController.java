package com.abhi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abhi.dto.TeacherDto;
import com.abhi.entity.UserCredential;
import com.abhi.service.TeacherService;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
	@Autowired
	private TeacherService service;

	@GetMapping("/find/{id}")
	public ResponseEntity<TeacherDto> getDataById(@PathVariable Integer id) {
		TeacherDto data = service.getData(id);
		return new ResponseEntity<TeacherDto>(data, HttpStatus.OK);
	}

	@PutMapping("/putUpdate")
	public ResponseEntity<?> updateUsingPut(@RequestParam("id") Integer id, @RequestBody UserCredential user) {
		String msg = service.updateData(id, user);
		return new ResponseEntity<String>(msg, HttpStatus.OK);

	}

	@PatchMapping("patchUpdate/{id}")
	public ResponseEntity<String> updateUsingPatch(@PathVariable Integer id, @RequestBody TeacherDto teacherDto) {
			String updatePartially = service.updatePartially(id, teacherDto);
			return new ResponseEntity<String>(updatePartially, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteDataById(@PathVariable Integer id) {
		boolean deleteData = service.deleteData(id);
		if (deleteData) {
			return new ResponseEntity<String>("Data deleted", HttpStatus.OK);
		} else

			return new ResponseEntity<String>("some technical try after some time", HttpStatus.BAD_GATEWAY);
	}

}
