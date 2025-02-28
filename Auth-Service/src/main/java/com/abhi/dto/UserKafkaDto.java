package com.abhi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserKafkaDto {

	private Integer id;
	private String name;
	private String email;
	private String techStack;
	private LocalDateTime date;
}
