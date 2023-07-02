package com.audition.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ResponseHeaderInjector extends OncePerRequestFilter {
    private static final String TRACE_ID = "X-openTelemetryTraceId";
    private static final String SPAN_ID = "X-openTelemetrySpanId";

    // TODO Inject openTelemetry trace and span Ids in the response headers.

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                 final FilterChain chain) throws IOException, ServletException {
        response.setHeader(TRACE_ID, "-");
        response.setHeader(SPAN_ID, "-");
        chain.doFilter(request, response);
    }
}
