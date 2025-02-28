package com.abhi.service;

import java.util.Optional;

import org.apache.kafka.common.errors.UnsupportedByAuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import com.abhi.dto.StudentDto;
import com.abhi.entity.UserCredential;
import com.abhi.repo.UserCredentialRepository;

@Service
public class StudentService {
	@Autowired
	private UserCredentialRepository repo;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private static String TOPIC = "student-service";

	public StudentDto getData(Integer id) {
		
		StudentDto studentDto = new StudentDto();
		UserCredential userCredential = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if(!userCredential.getRole().equalsIgnoreCase("student")) {
			throw new UnsupportedByAuthenticationException("You are not able to acces teacher data");
		}
		BeanUtils.copyProperties(userCredential, studentDto);
		return studentDto;
	}

	public String updateData(Integer id, UserCredential user) {
		if(id!=user.getId()) {
			throw new RuntimeException("User id and Queryparam must be same");
		}
		
		UserCredential orElseThrow = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if(!orElseThrow.getRole().equalsIgnoreCase("student")) {
			throw new UnsupportedByAuthenticationException("You are not able to modify Teacher data");
		}
		user.setPassword(orElseThrow.getPassword());
		repo.save(user);
		//kafkaTemplate.send(TOPIC,"Student Data Up-to-date");
		return "Student data sucessfully updated";
	}
	
	public String updatePartially(Integer id, StudentDto studentDto)
	{
	 Optional<UserCredential> existingStudent = repo.findById(id);
	 
	 UserCredential student;
        if (existingStudent.isPresent()) {
             student = existingStudent.get();
             
             if(!student.getRole().equalsIgnoreCase("student")) {
            	 throw new UnsupportedByAuthenticationException("You are not able to modify Teacher data");
             }
        
            // Update only non-null fields
            if (studentDto.getName() != null) {
                student.setName(studentDto.getName());
            }
            if (studentDto.getEmail() != null) {
                student.setEmail(studentDto.getEmail());
            }
            if (studentDto.getRole() != null) {
                student.setRole(studentDto.getRole());
            }
            if (studentDto.getTechStack() != null) {
                student.setTechStack(studentDto.getTechStack());
            }
        }else {
        	throw new RuntimeException("Student not found by this id:"+ id);
        }
        
        repo.save(student);
        
            return "Data up-to-date ";
		
	}	

	public boolean deleteData(Integer id) {
		//kafkaTemplate.send(TOPIC,"Student trying to delete the data");
		UserCredential orElseThrow = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("No records found by this id: " + id));
		if(!orElseThrow.getRole().equalsIgnoreCase("student")) {
			throw new UnsupportedByAuthenticationException("you are not able to delete Teacher data");
		}
		repo.deleteById(id);
		//kafkaTemplate.send(TOPIC,"Student Data sucessfully Deleted");
		return true;
	}
}
