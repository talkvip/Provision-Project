package com.cpn.vsp.provision.notify;

import static com.cpn.xml.XMLUtil.toXML;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.cpn.xml.XMLUtil;

public abstract class AbstractNotificationQuery<T> implements UnmarshallerHelper<T> {

	private int timeFailed = 0;

	@SuppressWarnings("unchecked")
	public static <T> T unmarshall(final Node aNode, final Class<T> anUnmarshaller) {
		try {
			return (T) anUnmarshaller.getDeclaredMethod("unmarshall", Node.class).invoke(null, aNode);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	public static <T> T unmarshall(final Document aDocument, final UnmarshallerHelper<T> aHelper) {
		try {
			if ((aHelper != null) && (aHelper.getUnmarshallingClass() != null) && (aHelper.getUnmarshallingXPath() != null)) {
				// System.out.println(XMLUtil.prettyPrint(aDocument));
				return unmarshall(XMLUtil.xPathNode(aDocument, aHelper.getUnmarshallingXPath()), aHelper.getUnmarshallingClass());
			} else {
				LoggerFactory.getLogger(AbstractNotificationQuery.class).warn("I don't have a way to unmarshall the following XML: " + XMLUtil.prettyPrint(aDocument));
				return null;
			}
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private String url;

	public AbstractNotificationQuery(String aUrl) {
		url = aUrl;
	}

	public T execute() throws IOException {

		final HttpClient client = new DefaultHttpClient();

		OAuthConsumer consumer = new DefaultOAuthConsumer("secure-private-network-214", "celkQtinO6PikZt9");
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		String signedUrl;
		try {
			signedUrl = consumer.sign(url);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e1) {
			throw new RuntimeException(e1.getMessage(), e1);
		}

		HttpRequestBase request;
		request = new HttpGet(signedUrl);
		final AbstractNotificationQuery<T> ref = this;
		try {
			String result = client.execute(request, new ResponseHandler<String>() {
				/**
				 * Returns the response body as a String if the response was successful
				 * (a 2xx status code). If no response body exists, this returns null.
				 * If the response was unsuccessful (>= 300 status code), throws an
				 * {@link HttpResponseException}.
				 */
				@Override
				public String handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
					final StatusLine statusLine = response.getStatusLine();
					final HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						if (timeFailed++ < 3) {
							return "RETRY";
						}
						if (entity != null) {
							final String body = EntityUtils.toString(entity);
							System.out.println(body);
							throw new NotificationQueryError(statusLine.getStatusCode(), body, ref);
						} else {
							throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
						}
					}

					return entity == null ? null : EntityUtils.toString(entity);
				}
			});
			if ("RETRY".equals(result)) {
				Thread.sleep(250);
				return execute();
			}
			return unmarshall(toXML(result), this);
		} catch (final ClientProtocolException | InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	public UnmarshallerHelper<T> getUnmarshallerHelper() {
		return this;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("url", url);
		return builder.toString();
	}

}
