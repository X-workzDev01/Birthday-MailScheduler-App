package com.xworkz.birthdayMailSchedular.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.xworkz.birthdayMailSchedular.dao.BirthdayMasterDAO;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;
import com.xworkz.birthdayMailSchedular.util.HelperUtil;

@Service
public class SpringMailServiceImpl implements SpringMailService {

	private Logger logger = LoggerFactory.getLogger(SpringMailServiceImpl.class);

	@Autowired
	private BirthdayMasterDAO birthdayMasterDAO;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${bdayMailSubject}")
	private String bdayMailSubject;
	@Value("${mailFrom}")
	private String mailFrom;
	@Value("${imagesJsonlink}")
	private String imagesJsonlink;
	@Value("${ccmailID}")
	private String[] ccmailID;
	@Value("${bdayMailreportSubject}")
	private String reportSubject;

	public SpringMailServiceImpl() {
		logger.info("created " + this.getClass().getSimpleName());
	}

	@Override
	public boolean validateAndSendMailByMailId(MimeMessagePreparator messagePreparator) {
		logger.info("invoked validateAndSendMailByMailId of SpringMailServiceImpl...");

		try {
			mailSender.send(messagePreparator);
			logger.info("Mail sent successfully");
			return true;
		} catch (MailException e) {
			logger.info("Mail sent Faild!");
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public List<GetSubscriberDTO> sendBirthdayMails() {
		try {
			int count = 0;
			String todayDate = HelperUtil.getTodaysDate();
			List<DetailsEntity> details = birthdayMasterDAO.getByDob(todayDate);
			List<GetSubscriberDTO> todaysBirthdayList = new ArrayList<GetSubscriberDTO>();
			if (Objects.nonNull(details)) {
				for (DetailsEntity detailsEntity : details) {
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
					ResponseEntity<String> responseEntity = restTemplate.exchange(imagesJsonlink, HttpMethod.GET,
							entity, String.class);
					if (Objects.nonNull(responseEntity)) {
						String data = responseEntity.getBody();
						String mailId;
						JSONObject jsonObject = new JSONObject(data);
						JSONArray arrayList = jsonObject.toJSONArray(jsonObject.names());
						Random random = new Random();
						Integer randomInt = random.nextInt(arrayList.length());
						Object imageLink = arrayList.get(randomInt);
						// logger.info("birthday image link= {}", imageLink);
						int age = HelperUtil
								.getTheCurrentAge(HelperUtil.convertStringIntoLocalDate(detailsEntity.getDob()));
						Context context1 = new Context();
						context1.setVariable("subcriberName", detailsEntity.getFullName());
						context1.setVariable("imageLink", imageLink);
						context1.setVariable("dob", detailsEntity.getDob());
						context1.setVariable("agePast", age - 1);
						context1.setVariable("agePresent", age);
						if (Objects.nonNull(context1)) {
							String content = templateEngine.process("birthdayMailTemplate", context1);
							mailId = detailsEntity.getEmailId();
							logger.info("subscriber mailID {} ", mailId);
							if (Objects.nonNull(mailId)) {
								if(!detailsEntity.isStatus()) {
									MimeMessagePreparator messagePreparator = mimeMessage -> {
										MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
										messageHelper.setFrom(mailFrom);
										messageHelper.setCc(ccmailID);
										messageHelper.setTo(mailId);
										messageHelper.setSubject(bdayMailSubject);
										messageHelper.setText(content, true);
									};
									boolean status = false;
									if (Objects.nonNull(messagePreparator))
										logger.info("sending birthday mail to "+ detailsEntity.getFullName() );
									status=validateAndSendMailByMailId(messagePreparator);
									if (status) {
										count++;
										birthdayMasterDAO.updateStatusByEmailId(detailsEntity.getEmailId());
										GetSubscriberDTO dto = new GetSubscriberDTO();
										dto.setFullName(detailsEntity.getFullName());
										dto.setEmail(detailsEntity.getEmailId());
										dto.setDob(detailsEntity.getDob());
										dto.setStatus(true);
										todaysBirthdayList.add(dto);
									}
								}else {
									GetSubscriberDTO dto = new GetSubscriberDTO();
									dto.setFullName(detailsEntity.getFullName());
									dto.setEmail(detailsEntity.getEmailId());
									dto.setDob(detailsEntity.getDob());
									dto.setStatus(detailsEntity.isStatus());
									todaysBirthdayList.add(dto);
									logger.info("mail already sent for this mail " + detailsEntity.getEmailId());
								}
								
							} else {
								logger.info(" mailId is empty {}", detailsEntity.getFullName());
							}
						}
					}
				}
					// String massage = "Total birthday mails sent";
					logger.info("Total birthday mails sent {}", count);
					// sendReportMail(massage, totalMailsent);
					return todaysBirthdayList;
			} else {
				String massage = "No birthday found for today's date";
				logger.info("No birthday found for today's date {}", todayDate);
				sendReportMail(massage, todayDate);
				logger.info("Report Mail sent");
				return todaysBirthdayList; 
			}

		} catch (Exception e) {
			logger.error("you have an exception in "+this.getClass().getSimpleName() );
			logger.error("exception Message is" +e.getMessage(),e);
		}
		return null;
	}
	
	private void sendReportMail(String message, Object object) {
		try {
			Context context = new Context();
			context.setVariable("reportMasseage", message);
			context.setVariable("reportTotal", object);

			if (Objects.nonNull(context)) {
				String content = templateEngine.process("dailyMailReport", context);
				MimeMessagePreparator messagePreparator = mimeMessage -> {
					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
					messageHelper.setFrom(mailFrom);
					messageHelper.setCc(ccmailID);
					messageHelper.setTo(mailFrom);
					messageHelper.setSubject(reportSubject);
					messageHelper.setText(content, true);
				};

				if (Objects.nonNull(messagePreparator))
					validateAndSendMailByMailId(messagePreparator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}

	}
}
