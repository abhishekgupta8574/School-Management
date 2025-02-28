//package com.abhi;
//
//import java.util.Optional;
//
//import org.bouncycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi.sha256WithSM2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.abhi.entity.UserCredential;
//import com.abhi.repo.UserCredentialRepository;
//@Component
//public class runner implements CommandLineRunner {
//	
//	@Autowired
//	private UserCredentialRepository repo;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("this is runner class");
//		Optional<UserCredential> byName = repo.findByName("abhishek");
//		UserCredential userCredential = byName.get();
//		String email = userCredential.getName();
//		System.out.println(email);
//		
//	}
//
//}
