package org.authorizationserver.configs.model;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpServletRspWrapper extends HttpServletResponseWrapper {
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintWriter writer = new PrintWriter(outputStream);

	public HttpServletRspWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		writer.flush();
		super.flushBuffer();
	}

	public String getCapturedBody() {
		writer.flush();
		return outputStream.toString();
	}
}
