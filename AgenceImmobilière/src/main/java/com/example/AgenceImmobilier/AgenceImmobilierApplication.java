package com.example.AgenceImmobilier;

import com.example.AgenceImmobilier.models.user.AuthProvider;
import com.example.AgenceImmobilier.models.user.ERole;
import com.example.AgenceImmobilier.models.user.Role;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.repositories.userR.RoleRepository;
import com.example.AgenceImmobilier.repositories.userR.UserRepository;
import com.example.AgenceImmobilier.security.AppProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AgenceImmobilierApplication {


	@Autowired
	PasswordEncoder encoder;
	public static void main(String[] args) {
		SpringApplication.run(AgenceImmobilierApplication.class, args);


	}
	@Bean
	CommandLineRunner run(RoleRepository roleRpository ,  UserRepository userRepository){
		return args -> {

			if (roleRpository.count()<1) {

				roleRpository.save(new Role( null,ERole.ROLE_ADMIN));
				roleRpository.save(new Role(null,ERole.ROLE_USER));
				roleRpository.save(new Role(null,ERole.ROLE_HOST));
			}
			if(!userRepository.existsByEmail("Admin")){
				UserModel user = new UserModel(
						"Admin",

						encoder.encode("password")
				);
				user.setProvider(AuthProvider.local);
				Role userRole = roleRpository.findRoleByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));

				user.getRoles().add(userRole);
				userRepository.save(user);
			}
		};
	}
	@Configuration
	public class WebConfiguration implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedMethods("*");
		}
	}
	@PostConstruct
	void started() {
		// set JVM timezone as UTC
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Tunis"));
	}
}
