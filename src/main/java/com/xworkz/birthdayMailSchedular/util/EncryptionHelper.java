package com.xworkz.birthdayMailSchedular.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EncryptionHelper {
	static Cipher cipher;
	final static String secretKeyString = "aeroplaneaeropla";
	static Logger logger = LoggerFactory.getLogger(EncryptionHelper.class);

	public EncryptionHelper() {
		logger.info("{} is Created...........");
	}

	public String encrypt(String plainText) {
		SecretKey secretKey;
		try {
			secretKey = new SecretKeySpec(secretKeyString.getBytes("UTF-8"), "AES");
			cipher = Cipher.getInstance("AES");
			byte[] plainTextByte = plainText.getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedByte = cipher.doFinal(plainTextByte);
			Base64.Encoder encoder = Base64.getEncoder();
			String encryptedText = encoder.encodeToString(encryptedByte);
			return encryptedText;
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding is unsupported exception is {} and message is {}", e, e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Algorithm is not found exception is {} and message is {}", e, e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error("padding is not found exception is {} and message is {}", e, e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error("Key is invalid exception is {} and message is {}", e, e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error("exception is {} and message is {}", e, e.getMessage());
		} catch (BadPaddingException e) {
			logger.error("exception is {} and message is {}", e, e.getMessage());
		}
		return null;
	}

	public String decrypt(String encryptedText) {
		SecretKey secretKey;
		try {
			secretKey = new SecretKeySpec(secretKeyString.getBytes("UTF-8"), "AES");
			cipher = Cipher.getInstance("AES");
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] encryptedTextByte = decoder.decode(encryptedText);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
			String decryptedText = new String(decryptedByte);
			return decryptedText;
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding is unsupported exception is {} and message is {}", e, e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Algorithm is not found exception is {} and message is {}", e, e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error("padding is not found exception is {} and message is {}", e, e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error("Key is invalid exception is {} and message is {}", e, e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error("exception is {} and message is {}", e, e.getMessage());
		} catch (BadPaddingException e) {
			logger.error("exception is {} and message is {}", e, e.getMessage());
		}
		return null;

	}

	public String encryptEmailId(String emailId) {
		logger.info("invoking {}");
		logger.info("encrypting emailId");
		if (emailId.contains(MailSchedularConstants.Gmail_value_Replacer)) {
			emailId = emailId.replace(MailSchedularConstants.Gmail_value_Replacer,
					MailSchedularConstants.Gmail_value_placer);
			logger.info("emailId encrypted");
		} else if (emailId.contains(MailSchedularConstants.Outlook_value_Replacer)) {
			emailId = emailId.replace(MailSchedularConstants.Outlook_value_Replacer,
					MailSchedularConstants.Outlook_value_placer);
			logger.info("emailId encrypted");
		} else if (emailId.contains(MailSchedularConstants.Yahoo_value_Replacer)) {
			emailId = emailId.replace(MailSchedularConstants.Yahoo_value_Replacer,
					MailSchedularConstants.Yahoo_value_placer);
			logger.info("emailId encrypted");
		}
		return emailId;
	}

	public String decryptEmailId(String emailId) {
		logger.info("invoking {}");
		logger.info("decrypting emailId");
		if (emailId.contains(MailSchedularConstants.Gmail_value_placer)) {
			emailId = emailId.replace(MailSchedularConstants.Gmail_value_placer,
					MailSchedularConstants.Gmail_value_Replacer);
			logger.info("emailId decrypted");
		} else if (emailId.contains(MailSchedularConstants.Outlook_value_placer)) {
			emailId = emailId.replace(MailSchedularConstants.Outlook_value_placer,
					MailSchedularConstants.Outlook_value_Replacer);
			logger.info("emailId decrypted");
		} else if (emailId.contains(MailSchedularConstants.Yahoo_value_placer)) {
			emailId = emailId.replace(MailSchedularConstants.Yahoo_value_placer,
					MailSchedularConstants.Yahoo_value_Replacer);
			logger.info("emailId decrypted");
		}
		return emailId;
	}

	public Integer encryptDateOfBirth(Date date) {
		logger.info("formating date");
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		
		String formatedDate = format.format(date);
		logger.info("date formated "+formatedDate+" and converting into Integer");
		Integer dob = Integer.parseInt(formatedDate);
		logger.info("date converted into Integer "+ dob);
		logger.info("encrypting Date of Birth");
		if (dob != null && dob.toString().length()>=7) {
			dob+=MailSchedularConstants.ExcelCell_Dycription_value;
			logger.info("encrypted Date of Birth"+dob);
		}
		return dob;
	}

	public Integer decryptDateOfBirth(Integer dob) {
		logger.info("invoking {}");
		logger.info("decrypting Date of Birth");
		if (dob != null && dob.toString().length()>=7) {
			dob-=MailSchedularConstants.ExcelCell_Dycription_value;
			logger.info("decrypted Date of Birth");
		}
		return dob;
	}
}
