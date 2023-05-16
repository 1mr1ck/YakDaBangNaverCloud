package com.jxjtech.yakmanager.logging;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RequestServletWrapper extends HttpServletRequestWrapper {

    private String requestData = null;

    public RequestServletWrapper(HttpServletRequest request) {

        super(request);

        try (Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A")) {

            requestData = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // ByteArrayInputStream을 사용한 이유는 한글이 inputStream이 되지 않아서이다.
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestData.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            private final ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
