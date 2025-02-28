package com.abhi.service;

import java.util.Optional;

import org.apache.kafka.common.errors.UnsupportedByAuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.abhi.dto.TeacherDto;
import com.abhi.entity.UserCredential;
import com.abhi.repo.UserCredentialRepository;

@Service
public class TeacherService {
	@Autowired
	private UserCredentialRepository repo;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private static String TOPIC = "teacher-service";

	public TeacherDto getData(Integer id) {

		TeacherDto teacherDto = new TeacherDto();
		UserCredential userCredential = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if (!userCredential.getRole().equalsIgnoreCase("teacher")) {
			throw new UnsupportedByAuthenticationException("You are not able to acces Student data");
		}
		BeanUtils.copyProperties(userCredential, teacherDto);
		return teacherDto;
	}

	public String updateData(Integer id, UserCredential user) {
		if (id != user.getId()) {
			throw new RuntimeException("User id and Queryparam must be same");
		}
		UserCredential orElseThrow = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if (!orElseThrow.getRole().equalsIgnoreCase("teacher")) {
			throw new UnsupportedByAuthenticationException("You are not able to modify Student data");
		}
		user.setPassword(orElseThrow.getPassword());
		repo.save(user);
		// kafkaTemplate.send(TOPIC,"Teacher Data Up-to-date");
		return "Teacher data sucessfully updated";
	}

	public String updatePartially(Integer id, TeacherDto teacherDto) {
		Optional<UserCredential> existingTeacher = repo.findById(id);

		UserCredential teacher;
		if (existingTeacher.isPresent()) {
			teacher = existingTeacher.get();
			
			if(!teacher.getRole().equalsIgnoreCase("teacher")) {
           	 throw new UnsupportedByAuthenticationException("You are not able to modify Student data");
            }

			if (teacherDto.getName() != null) {
				teacher.setName(teacherDto.getName());
			}
			if (teacherDto.getEmail() != null) {
				teacher.setEmail(teacherDto.getEmail());
			}
			if (teacherDto.getRole() != null) {
				teacher.setRole(teacherDto.getRole());
			}
			if (teacherDto.getTechStack() != null) {
				teacher.setTechStack(teacherDto.getTechStack());
			}
		} else {
			throw new RuntimeException("Teacher not found by this id:" + id);
		}

		repo.save(teacher);

		return "Data up-to-date ";

	}

	public boolean deleteData(Integer id) {
		// kafkaTemplate.send(TOPIC,"Teacher trying to delete the data");
		UserCredential orElseThrow = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if(!orElseThrow.getRole().equalsIgnoreCase("teacher")) {
			throw new UnsupportedByAuthenticationException("you are not able to delete Student data");
		}
		repo.deleteById(id);
		// kafkaTemplate.send(TOPIC,"Teacher Data sucessfully Deleted");
		return true;
	}
}
