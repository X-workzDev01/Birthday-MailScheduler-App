package com.xworkz.birthdayMailSchedular.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.xworkz.birthdayMailSchedular.util.EncryptionHelper;

@Configuration
public class AppConfig {
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	@Value("${JavaMailSenderImpl.username}")
	private String username;
	@Value("${JavaMailSenderImpl.password}")
	private String password;
	
	@Bean
	public JavaMailSender getMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername(encryptionHelper.decrypt(username));
		mailSender.setPassword(encryptionHelper.decrypt(password));
		
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");
		
		
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	 @Bean
	    public LocalSessionFactoryBean sessionFactory() {
	        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource());
	        sessionFactory.setPackagesToScan("com.xworkz.birthdayMailSchedular.entity");
	        return sessionFactory;
	    }

	    @Bean
	    public DataSource dataSource() {
	    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	        dataSource.setUrl("jdbc:mysql://localhost:3306/xworkz_master");
	        dataSource.setUsername("root");
	        dataSource.setPassword("root");

	        return dataSource;
	    }
}