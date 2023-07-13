package com.ncs.iconnect.sample.lab.generated.web.rest.util;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "iconnectSampleAppLabApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-iconnectSampleAppLabApp-alert", message);
        headers.add("X-iconnectSampleAppLabApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-iconnectSampleAppLabApp-error", "error." + errorKey);
        headers.add("X-iconnectSampleAppLabApp-params", entityName);
        return headers;
    }

    public static HttpHeaders createAlertNoAppNamme(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlertNoAppName(String entityName, String param) {
        return createAlertNoAppNamme(entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlertNoAppName(String entityName, String param) {
        return createAlertNoAppNamme(entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlertNoAppName(String entityName, String param) {
        return createAlertNoAppNamme(entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlertNoAppName(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + "error", "error." + errorKey);
        headers.add("X-" + "params", entityName);
        return headers;
    }
}
