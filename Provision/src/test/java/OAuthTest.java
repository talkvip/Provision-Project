import java.net.URL;
import java.net.URLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.junit.Test;

public class OAuthTest {

	@Test
	public void testOAuth() throws Exception {
		OAuthConsumer consumer = new DefaultOAuthConsumer("Dummy", "secret");
		URL url = new URL("https://www.appdirect.com/AppDirect/rest/api/events/dummyChange");
		URLConnection request = url.openConnection();
		consumer.sign(request);
		request.connect();
	}
	
	@Test
	public void testSig() throws Exception {
		OAuthConsumer consumer = new DefaultOAuthConsumer("Dummy", "secret");
		consumer.setSigningStrategy( new QueryStringSigningStrategy());
		String url = "https://www.appdirect.com/AppDirect/finishorder?success=true&accountIdentifer=Alice";
		String signedUrl = consumer.sign(url);
		System.out.println(signedUrl);
	}
}
