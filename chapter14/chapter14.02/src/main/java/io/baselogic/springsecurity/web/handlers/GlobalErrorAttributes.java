package io.baselogic.springsecurity.web.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component

// Lombok Annotations:
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes{

    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String message = "Global Error Attributes";

    @Override
    public Map<String, Object> getErrorAttributes(final ServerRequest request,
                                                  final ErrorAttributeOptions options) {
        log.error("*** ERROR GlobalErrorAttributes ***");
        log.error("* Request: [{}]", request);
        log.error("* includes: [{}]", options.getIncludes());

        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("status", getStatus().value());
        map.put("error", getMessage());

        map.forEach(log::error);

        log.error("* status: [{}], message: [{}]", getStatus().toString(), getMessage());

        return map;
    }

} // The End...
