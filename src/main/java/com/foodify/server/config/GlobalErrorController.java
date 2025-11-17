package com.foodify.server.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalErrorController implements ErrorController {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorController.class);

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        Integer statusCode = status != null ? Integer.valueOf(status.toString()) : 500;
        HttpStatus httpStatus = HttpStatus.resolve(statusCode);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Log the error with stack trace
        if (exception instanceof Throwable) {
            log.error("Error occurred at {}: {}", requestUri, message, (Throwable) exception);
        } else {
            log.error("Error occurred at {}: {} (status: {})", requestUri, message, statusCode);
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", httpStatus.getReasonPhrase());
        errorResponse.put("message", message != null ? message.toString() : "An error occurred");
        errorResponse.put("status", statusCode);
        errorResponse.put("path", requestUri);

        // Include stack trace in response for 500 errors
        if (statusCode >= 500 && exception instanceof Throwable) {
            Throwable throwable = (Throwable) exception;
            errorResponse.put("exception", throwable.getClass().getName());
            
            // Include stack trace elements
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StringBuilder stackTraceStr = new StringBuilder();
                stackTraceStr.append(throwable.toString()).append("\n");
                for (int i = 0; i < Math.min(stackTrace.length, 10); i++) {
                    stackTraceStr.append("\tat ").append(stackTrace[i].toString()).append("\n");
                }
                if (stackTrace.length > 10) {
                    stackTraceStr.append("\t... ").append(stackTrace.length - 10).append(" more\n");
                }
                errorResponse.put("trace", stackTraceStr.toString());
            }
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
