package com.cpn.vsp.provision.notify;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.client.HttpResponseException;

@SuppressWarnings("serial")
@Immutable
public class NotificationQueryError extends HttpResponseException {

	private final AbstractNotificationQuery<?> command;

	private final String rawBody;
	private final int statusCode;

	public NotificationQueryError(final int statusCode, final String rawBody, final AbstractNotificationQuery<?> command) {
		super(statusCode, rawBody);

		this.statusCode = statusCode;
		this.rawBody = rawBody;
		this.command = command;
	}

	public AbstractNotificationQuery<?> getCommand() {
		return command;
	}

	public String getRawBody() {
		return rawBody;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("statusCode", statusCode).append("command", command);
		return builder.toString();
	}
}
