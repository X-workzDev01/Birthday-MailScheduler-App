package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.xworkz.birthdayMailSchedular.dao.ExcelMasterDAOImpl;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.Subscriber;
import com.xworkz.birthdayMailSchedular.util.MailSchedularConstants;

@Service("emailService")
public class MailSchedularServiceImpl implements MailSchedularService {

	private Logger logger = LoggerFactory.getLogger(MailSchedularServiceImpl.class);

	@Autowired
	private SpringMailService emailService;
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ExcelMasterDAOImpl excelMasterDAOImpl;
	

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
	private String mailId;


	public String birthdayMailSender() throws URISyntaxException, IOException {
		logger.info("Invoked birthadyMailSender in service");

		try {
			int totalMailsent = 0;
			boolean flag = false;
			Date today = new Date();
			List<Subscriber> subcriberList = excelMasterDAOImpl.getListOfSubscribersFromExcel();

			if (Objects.nonNull(subcriberList)) {
				for (Subscriber subscriber : subcriberList) {
					Integer originaldate = (int) ((subscriber.getDob())
							- MailSchedularConstants.ExcelCell_Dycription_value);
					String originalStringDate = originaldate.toString();
					// logger.info("originaldate {}", originalStringDate);
					originalStringDate = validateDOBLength(originaldate, originalStringDate);

					SimpleDateFormat formatter1 = new SimpleDateFormat(
							MailSchedularConstants.SimpleDateReadFormat_value);
					Date date = formatter1.parse(originalStringDate);
					SimpleDateFormat formatter2 = new SimpleDateFormat(
							MailSchedularConstants.SimpleDateWriteFormat_value);
					String formatedTodayDate = formatter2.format(today);
					// logger.info("local date {}", formatedTodayDate);

					String formatedDob = formatter2.format(date);

					LocalDate dob = new java.sql.Date(date.getTime()).toLocalDate();
					// logger.info("custom formated originaldate date {}", formatedDob);

					if (Objects.nonNull(formatedDob) && Objects.nonNull(formatedTodayDate)) {
						if ((formatedTodayDate).equals(formatedDob)) {
							flag = true;
							totalMailsent = extractedAndEmailSending(subscriber, formatedTodayDate, formatedDob, dob);
						}
					}
				}
				if (flag == false) {
					String massage = "No birthday found for today's date";
					logger.info("No birthday found for today's date {}", today);
					sendReportMail(massage, today);
					logger.info("Report Mail sent");
					return "No birthday found for today's date,Report Mail sent";
				} else {
					// String massage = "Total birthday mails sent";
					logger.info("Total birthday mails sent {}", totalMailsent);
					// sendReportMail(massage, totalMailsent);
					return "Total birthday mails sent " + totalMailsent;
				}
			} else {
				logger.info("subcriberList from getListOfSubscribersFromExcel Is null");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}
		return "Something went wrong try again!";
	}

	@Override
	public List<GetSubscriberDTO> getTodaysBirthdayList() throws IOException {
		List<GetSubscriberDTO> todayBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto= new GetSubscriberDTO();
		try {
			Date today = new Date();
			List<Subscriber> subcriberList = excelMasterDAOImpl.getListOfSubscribersFromExcel();
			SimpleDateFormat formatter1 = new SimpleDateFormat(MailSchedularConstants.SimpleDateReadFormat_value);
			SimpleDateFormat formatter2 = new SimpleDateFormat(MailSchedularConstants.SimpleDateWriteFormat_value);
			String formatedTodayDate = formatter2.format(today);
			if (Objects.nonNull(subcriberList)) {
				for (Subscriber subscriber : subcriberList) {
					Integer originalDate = (int) (subscriber.getDob()
							- MailSchedularConstants.ExcelCell_Dycription_value);
					logger.debug("original date of birth ", originalDate);
					String originalStringDate = originalDate.toString();
					// logger.info("originaldate {}", originalStringDate);

					originalStringDate = validateDOBLength(originalDate, originalStringDate);
					Date date = formatter1.parse(originalStringDate);
					// logger.info("local date {}", formatedTodayDate);

					String formatedDob = formatter2.format(date);
					if (Objects.nonNull(formatedDob) && Objects.nonNull(formatedTodayDate)) {
						if ((formatedTodayDate).equals(formatedDob)) {
							dto.setFullName(subscriber.getFullName());
							dto.setDob(formatedDob);
							todayBirthdayList.add(dto);
						}
					}
				}
			} else {
				logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
			}
		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return todayBirthdayList;
	}
	
	@Override
	public List<GetSubscriberDTO> getCurrentWeekBirthdayList() {
		List<GetSubscriberDTO> currentWeekBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto ;
		try {
			List<Subscriber> subcriberList = excelMasterDAOImpl.getListOfSubscribersFromExcel();
			SimpleDateFormat formatter1 = new SimpleDateFormat(MailSchedularConstants.SimpleDateReadFormat_value);
			SimpleDateFormat formatter2 = new SimpleDateFormat(MailSchedularConstants.SimpleDateWriteFormat_value);
			String[] arr= getCurrentWeekDate();
			if (Objects.nonNull(subcriberList)) {
				for (Subscriber subscriber : subcriberList) {
					Integer originalDate = (int) (subscriber.getDob()
							- MailSchedularConstants.ExcelCell_Dycription_value);
					logger.debug("original date of birth ", originalDate);
					String originalStringDate = originalDate.toString();
					// logger.info("originaldate {}", originalStringDate);

					originalStringDate = validateDOBLength(originalDate, originalStringDate);
					Date date = formatter1.parse(originalStringDate);
					// logger.info("local date {}", formatedTodayDate);

					String formatedDob = formatter2.format(date);
					if (Objects.nonNull(formatedDob) && Objects.nonNull(arr)) {
						boolean flag = false;
						for (int i = 0; i < arr.length; i++) {
							
							if (formatedDob.equals(arr[i])) {
								flag = true;
							}
						}
						if(flag) {
							 dto = new GetSubscriberDTO();
							 dto.setFullName(subscriber.getFullName());
							 dto.setDob(formatedDob);
							currentWeekBirthdayList.add(dto);
						}
					}
				}
			} else {
				logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
			}
		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return currentWeekBirthdayList;
	}

	@Override
	public List<GetSubscriberDTO> getCurrentMonthBirthdayList() {
		List<GetSubscriberDTO> currentMonthBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto ;
		try {
			Date today = new Date();
			List<Subscriber> subcriberList = excelMasterDAOImpl.	getListOfSubscribersFromExcel();
			SimpleDateFormat formatter1 = new SimpleDateFormat(MailSchedularConstants.SimpleDateReadFormat_value);
			SimpleDateFormat formatter2 = new SimpleDateFormat(MailSchedularConstants.SimpleMonthWriteFormat_value);
			String formatedTodayDate = formatter2.format(today);
			SimpleDateFormat formatter3 = new SimpleDateFormat(MailSchedularConstants.SimpleDateWriteFormat_value);
			if (Objects.nonNull(subcriberList)) {
				for (Subscriber subscriber : subcriberList) {
					Integer originalDate = (int) (subscriber.getDob()
							- MailSchedularConstants.ExcelCell_Dycription_value);
					logger.debug("original date of birth ", originalDate);
					String originalStringDate = originalDate.toString();
					// logger.info("originaldate {}", originalStringDate);

					originalStringDate = validateDOBLength(originalDate, originalStringDate);
					Date date = formatter1.parse(originalStringDate);
					// logger.info("local date {}", formatedTodayDate);

					String formatedMonth = formatter2.format(date);
					String formatedDob = formatter3.format(date);
					if (Objects.nonNull(formatedMonth) && Objects.nonNull(formatedTodayDate)) {
						if ((formatedTodayDate).equals(formatedMonth)) {
							dto = new GetSubscriberDTO();
							 dto.setFullName(subscriber.getFullName());
							 dto.setDob(formatedDob);
							currentMonthBirthdayList.add(dto);
						}
					}
				}
			} else {
				logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
			}
		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return currentMonthBirthdayList;
		
	}

	
	private String validateDOBLength(Integer originaldate, String originalStringDate) {
		Integer length = originalStringDate.length();
		if (Objects.nonNull(length) && Objects.nonNull(originaldate)) {
			if (length.equals(7)) {
				DecimalFormat decimalFormat = new DecimalFormat(MailSchedularConstants.DecimalFormat_value);
				originalStringDate = decimalFormat.format(originaldate);
				// logger.info("originaldate value={}", originaldate);
				// logger.info("updated value= {}", originalStringDate);
			}
		} else {
			logger.info("originalStringDate length Is null");
		}
		return originalStringDate;
	}

	public int j = 0;

	private int extractedAndEmailSending(Subscriber subscriber, String formatedTodayDate, String formatedDob,
			LocalDate date) {
		if (Objects.nonNull(subscriber) && Objects.nonNull(formatedTodayDate) && Objects.nonNull(formatedDob)) {
			logger.info("no= {} subscriber = {} Macthed DOB = {}", (++j), subscriber.getFullName(),
					(formatedTodayDate).equals(formatedDob));
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(imagesJsonlink, HttpMethod.GET, entity,
					String.class);
			if (Objects.nonNull(responseEntity)) {
				String data = responseEntity.getBody();
				JSONObject jsonObject = new JSONObject(data);
				JSONArray arrayList = jsonObject.toJSONArray(jsonObject.names());
				Random random = new Random();
				Integer randomInt = random.nextInt(arrayList.length());
				Object imageLink = arrayList.get(randomInt);
				// logger.info("birthday image link= {}", imageLink);
				int age = getTheCurrentAge(date);
				Context context1 = new Context();
				context1.setVariable("subcriberName", subscriber.getFullName());
				context1.setVariable("imageLink", imageLink);
				context1.setVariable("dob", date);
				context1.setVariable("agePast", age - 1);
				context1.setVariable("agePresent", age);
				if (Objects.nonNull(context1)) {
					String content = templateEngine.process("birthdayMailTemplate", context1);
					mailId = subscriber.getEmailId();
					logger.info("subscriber mailID {} ", mailId);
					if (Objects.nonNull(mailId)) {
						if (mailId.contains(MailSchedularConstants.Gmail_value_placer)) {
							mailId = mailId.replace(MailSchedularConstants.Gmail_value_placer,
									MailSchedularConstants.Gmail_value_Replacer);
							if (mailId.contains(MailSchedularConstants.Outlook_value_placer)) {
								mailId = mailId.replace(MailSchedularConstants.Outlook_value_placer,
										MailSchedularConstants.Outlook_value_Replacer);
								if (mailId.contains(MailSchedularConstants.Yahoo_value_placer)) {
									mailId = mailId.replace(MailSchedularConstants.Yahoo_value_placer,
											MailSchedularConstants.Yahoo_value_Replacer);
								}
							}
						}
						MimeMessagePreparator messagePreparator = mimeMessage -> {
							MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
							messageHelper.setFrom(mailFrom);
							messageHelper.setCc(ccmailID);
							messageHelper.setTo(mailId);
							messageHelper.setSubject(bdayMailSubject);
							messageHelper.setText(content, true);
						};
						if (Objects.nonNull(messagePreparator))
							emailService.validateAndSendMailByMailId(messagePreparator);
					} else {
						logger.info("subscriber mail is empty {}", subscriber.getFullName());
					}
				}
			}
		} else {
			logger.info("got null object in extractedAndEmailSending");
		}
		return j;
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
					emailService.validateAndSendMailByMailId(messagePreparator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}

	}

	private int getTheCurrentAge(LocalDate givenDate) {
		Period period = Period.between(givenDate, LocalDate.now());
		int age = period.getYears();
		return age;
	}
	
	
	public static String[] getCurrentWeekDate() {
		String date[] = new String[7];
		LocalDate now = LocalDate.now();
		DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
		LocalDate startOfCurrentWeek = now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
		LocalDate printDate = startOfCurrentWeek;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MailSchedularConstants.SimpleDateWriteFormat_value);
		for (int i = 0; i < 7; i++) {
			date[i] = printDate.format(formatter); 
			printDate = printDate.plusDays(1);
		}	
		return date;
	}
}