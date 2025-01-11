package com.app;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointmentdata")
public class Appointment {
	@Id
	private String id;
	
	private String userName;
	private String email;
	private String phone;
	private String address;
	private String appointmentId;
	private String serviceType;
	private String appointmentDate;
	private String appointmentDuration;
	private String status;
}
