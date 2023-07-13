package com.ncs.iconnect.sample.lab.generated.web.rest.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.StringUtilities;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class ValidationUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationUtils.class);
	
	private static final char[] CHAR_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8'};
	private static final char[] CHAR_LETTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
	
	@Value("${iconnect.fileupload.doctype}")
	private String uploadDocType;
	
	@Value("${iconnect.fileupload.uploadfilesize}")
	private String uploadDocSize;

	
	private static ValidationUtils instance = null;
	
	public static synchronized ValidationUtils getInstance(){
		if(instance==null) {
			instance = new ValidationUtils();
		}
		return instance;
	}
	
	public  String getValidInput(String input){
		try {
			if(StringUtils.isNotEmpty(input)) {
				return ESAPI.validator().getValidInput("Validate intput value"
						, input, "SafeString", 200, true);
			}
			return input;
		} catch (IntrusionException | ValidationException e) {
			log.error("Intrusion attack detected when validate input value");
			return null;
		}
	}
	
	public  String encoding(String input){
    	return ESAPI.encoder().encodeForHTML(input);
    }
	
	public  String getValidInputWithEncode(String input){
		return encoding(getValidInput(input));
	}
	
	public  String getRandomString(int length){
		char[] charAlpha = StringUtilities.union(CHAR_LETTERS,CHAR_DIGITS);
		return ESAPI.randomizer().getRandomString(length,charAlpha);
	}
	
	public  String getRandomDigital(int length){
		return ESAPI.randomizer().getRandomString(length,CHAR_DIGITS);
	}
	
	public  String getRandomLetter(int length){
		return ESAPI.randomizer().getRandomString(length,CHAR_DIGITS);
	}
	
	public  String getRandomGUID(){
		try {
			return ESAPI.randomizer().getRandomGUID();
		} catch (EncryptionException e) {
			log.error("Encryption Exception Occur when generate ID with ESAPI");
		}
		return null;
	}
	
	public  String getRandomFilename(String fileExtension){
		return ESAPI.randomizer().getRandomFilename(fileExtension);
	}
	
	public  String getRandomLong(){
		long randomLong = ESAPI.randomizer().getRandomLong();
		return Long.toString(randomLong);
	}
	
	public  String getValidDirectoryPath(String input,File parent){
		try {
			return ESAPI.validator().getValidDirectoryPath("Valiate File Path", input, parent, true);
		} catch (IntrusionException | ValidationException e) {
			String msg = e.getMessage();
            log.error(msg, e);
		}
		return null;
	}
	
	public  String getValidPath(String input)throws ValidationException {
		if(input==null){
			return null;
		}
		ESAPI.validator().getValidInput("validate path", input, "DirectoryName", 255, false);
		return input;
		
	}
	
	public  boolean assertValidFileUpload(String filename,byte[] content, int maxBytes, List<java.lang.String> allowedExtensions) 
			throws IntrusionException, ValidationException
	{
		ESAPI.validator().getValidFileName("Validate Upload File Name", filename,allowedExtensions, true);
		ESAPI.validator().getValidFileContent("Validate Upload File Content",content,maxBytes,true);
		return true;
	}
	
	public List<String> uploadFileType(){
		List<String> uploadFileTypeList = new ArrayList<>();
		if(StringUtils.isNotEmpty(uploadDocType)){
			if(uploadDocType.contains(",")){
				String[] upType = uploadDocType.split(",");
				uploadFileTypeList = new ArrayList<>(Arrays.asList(upType));
			}else{
				uploadFileTypeList.add(uploadDocType);
			}
		}
		return uploadFileTypeList;
	}
	
	public  boolean checkMultipartFiles(MultipartFile[] files) throws ValidationException{
		if(files==null || files.length==0){
			return false;
		}
		
		for(MultipartFile file: files) {
			List<String> uploadFileTypeList = uploadFileType();
			if(uploadFileTypeList!=null && !uploadFileTypeList.isEmpty()) {
				try {
					if(file.getBytes()!=null && file.getBytes().length>Integer.valueOf(uploadDocSize)){
						throw new ValidationException("Uploading file exceed maxlength","exceed maxlength");
					}
					assertValidFileUpload(file.getOriginalFilename(), file.getBytes(), Integer.valueOf(uploadDocSize), uploadFileTypeList);
				} catch (IntrusionException | ValidationException | IOException e) {
					throw new ValidationException("Uploading doc type is not allow","");
				}
			}
		}
		return true;
	}
	
	public boolean checkMultipartFile(MultipartFile file) throws ValidationException{
		if(file==null){
			return false;
		}
		
		List<String> uploadFileTypeList = uploadFileType();
		if(uploadFileTypeList!=null && !uploadFileTypeList.isEmpty()) {
			try {
				if(file.getBytes()!=null && file.getBytes().length>Integer.valueOf(uploadDocSize)){
					throw new ValidationException("Uploading file exceed maxlength","exceed maxLength");
				}
				assertValidFileUpload(file.getOriginalFilename(), file.getBytes(), Integer.valueOf(uploadDocSize), uploadFileTypeList);
			} catch (IntrusionException | ValidationException | IOException e) {
				throw new ValidationException("Uploading doc type is not allow","docType Disallowed");
			}
		}
		return true;
	}
}
