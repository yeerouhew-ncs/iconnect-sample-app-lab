package com.ncs.iconnect.sample.lab.generated.web.rest.util;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import org.junit.jupiter.api.Test;
import org.owasp.esapi.errors.ValidationException;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ValidationUtilsIT {

    @Test
    public void getValidInput() {
        ValidationUtils.getInstance().getValidInput(" ");
    }

    @Test
    public void encoding() {
        ValidationUtils.getInstance().encoding("iConnect");
    }

    @Test
    public void getValidInputWithEncode() {
        ValidationUtils.getInstance().getValidInputWithEncode("java");
    }

    @Test
    public void getRandomString() {
        ValidationUtils.getInstance().getRandomString(6);
    }

    @Test
    public void getRandomDigital() {
        ValidationUtils.getInstance().getRandomDigital(8);
    }

    @Test
    public void getRandomLetter() {
        ValidationUtils.getInstance().getRandomLetter(10);
    }

    @Test
    public void getRandomGUID() {
        ValidationUtils.getInstance().getRandomGUID();
    }

    @Test
    public void getRandomFilename() {
        ValidationUtils.getInstance().getRandomFilename("txt");
    }

    @Test
    public void getRandomLong() {
        ValidationUtils.getInstance().getRandomLong();
    }

    @Test
    public void getValidDirectoryPath() {
        ValidationUtils.getInstance().getValidDirectoryPath("report.pdf", new File("d:"));
    }

    @Test
    public void getValidPath() throws IOException, ValidationException {
        ValidationUtils.getInstance().getValidPath(null);
        ValidationUtils.getInstance().getValidPath("D:\\Iconnect\\");
    }

    @Test
    public void uploadFileType() {
        ValidationUtils.getInstance().uploadFileType();
    }

    @Test
    public void checkMultipartFiles() throws ValidationException {
        ValidationUtils.getInstance().checkMultipartFiles(null);
        ValidationUtils.getInstance().checkMultipartFile(null);
    }
}
