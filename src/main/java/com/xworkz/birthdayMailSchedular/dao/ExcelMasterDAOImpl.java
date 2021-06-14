package com.xworkz.birthdayMailSchedular.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xworkz.birthdayMailSchedular.dto.Subscriber;
import com.xworkz.birthdayMailSchedular.util.BdayExcelFileColumn;
import com.xworkz.birthdayMailSchedular.util.EncryptionHelper;

@Component
public class ExcelMasterDAOImpl implements ExcelMasterDAO{

	private Logger logger;
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	
	@Value("${excelFilelink}")
	private String excelFilelink;
	
	public ExcelMasterDAOImpl() {
		logger = LoggerFactory.getLogger(ExcelMasterDAOImpl.class);
	}

	@Override
	public List<Subscriber> getListOfSubscribersFromExcel() throws IOException {
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		Workbook workbook = null;
		ByteArrayInputStream inputStream = null;
		try {
			int i = 0;
			URI url = new URI(encryptionHelper.decrypt(excelFilelink));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
			if (Objects.nonNull(responseEntity)) {
				byte[] result = responseEntity.getBody();

				logger.info("Staring..........");
				inputStream = new ByteArrayInputStream(result);
				if (Objects.nonNull(inputStream)) {
					workbook = new XSSFWorkbook(inputStream);
					Sheet excelSheet = workbook.getSheetAt(0);
					logger.info("Last Row Number of Is Excel file {} ", excelSheet.getPhysicalNumberOfRows());
					logger.info("Excel file Is opened");

					if (Objects.nonNull(excelSheet)) {
						for (Row row : excelSheet) { // For each Row.
							if (row != null) {
								Cell nameCell = row.getCell(BdayExcelFileColumn.ExcelFile_NAME_CELL);
								nameCell.setCellType(CellType.STRING);
								nameCell = row.getCell(BdayExcelFileColumn.ExcelFile_NAME_CELL,
										MissingCellPolicy.RETURN_BLANK_AS_NULL);

								Cell emailCell = row.getCell(BdayExcelFileColumn.ExcelFile_EMAIL_CELL);
								emailCell.setCellType(CellType.STRING);
								emailCell = row.getCell(BdayExcelFileColumn.ExcelFile_EMAIL_CELL,
										MissingCellPolicy.RETURN_BLANK_AS_NULL);

								Cell dobCell = row.getCell(BdayExcelFileColumn.ExcelFile_DOB_CELL);
								dobCell.setCellType(CellType.NUMERIC);
								dobCell = row.getCell(BdayExcelFileColumn.ExcelFile_DOB_CELL,
										MissingCellPolicy.RETURN_BLANK_AS_NULL);
								if (nameCell != null || emailCell != null || dobCell != null) {
									subscribersList.add(new Subscriber(nameCell.getStringCellValue(),
											emailCell.getStringCellValue(), dobCell.getNumericCellValue()));
									// logger.info("No: {} Value: {} Data Is Read and Stored in List",
									// (++i),nameCell.getStringCellValue());
									++i;
								} else {
									logger.info("Cell Data is empty in getListOfSubscribersFromExcel in Row:{}", (++i));
								}

							} else {
								logger.info("Row: {} Data is empty in getListOfSubscribersFromExcel", (++i));
							}
						}
						logger.info("Total: {} Data Is Read and Stored in List", i);
						subscribersList.remove(0);
					}
				}
			} else {
				logger.info("responseEntity null in getListOfSubscribersFromExcel");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		} finally {
			inputStream.close();
			workbook.close();
		}
		return subscribersList;
	}
}
