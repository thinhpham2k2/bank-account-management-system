package com.system.transaction_service.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        if (requestWrapper.getRequestURI().toLowerCase().contains("/api/v1")) {

            long time = System.currentTimeMillis();
            try {

                filterChain.doFilter(requestWrapper, responseWrapper);
            } finally {

                time = System.currentTimeMillis() - time;
                String remoteIpAddress = requestWrapper.getHeader("X-FORWARDED-FOR");
                if (remoteIpAddress == null || remoteIpAddress.isEmpty()) {

                    remoteIpAddress = requestWrapper.getRemoteAddr();
                }

                String requestBody = new String(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
                String responseBody = requestWrapper.getMethod().equalsIgnoreCase(HttpMethod.GET.toString()) ? "" :
                        new String(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

                try {

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Object json = gson.fromJson(responseBody, Object.class);
                    responseBody = gson.toJson(json);
                } catch (Exception ignored) {

                }

                log.info(""" 
                                
                                Client IP: {}\s
                                Method: {}\s
                                Path: {}\s
                                Parameters: {}\s
                                Content type: {}\s
                                Status code: {}\s
                                Time: {}ms\s
                                Request body: {}\s
                                Response body: {}\s""",
                        remoteIpAddress, requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                        getParameter(requestWrapper.getParameterMap()), responseWrapper.getContentType(),
                        responseWrapper.getStatus(), time, requestBody, responseBody);
                requestWrapper.getInputStream();
                responseWrapper.copyBodyToResponse();
            }
        } else {

            filterChain.doFilter(requestWrapper, responseWrapper);
            requestWrapper.getInputStream();
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getParameter(Map<String, String[]> map) {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {

            String key = entry.getKey();
            String[] values = entry.getValue();
            for (String value : values) {

                sb.append(key).append("=").append(value).append(", ");
            }
        }

        return sb.toString();
    }
}
