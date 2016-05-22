package com.bigcom.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigcom.test.conf.Env;

public class Utils {
	public static Logger log = (Logger) LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	public ObjectMapper objMap = new ObjectMapper();
	public ObjectWriter pretty = objMap.writerWithDefaultPrettyPrinter();
	public static Env env = Env.instance();
	public static final String F = "METHOD FAIL";
	public static final String E = "METHOD END";
	public static final String S = "METHOD START";
	public static final String SK = "METHOD SKIP";
	public final String jsonContent = "application/json"; 


	public HttpClient createClient() throws Exception {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(env.user, env.userkey));
		CloseableHttpClient httpClient = 
				HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		return httpClient;
	}

	public HttpResponse execGet(String url) throws Exception {
		HttpResponse resp = null;
		for (int i = 0; i < 5; i++) {
			resp = execGetSingleTime(url);
			Thread.sleep(2000);
			if (isStatusOK(resp)) {
				break;
			}
		}
		return resp;
	}

	public HttpResponse execPost(String url, String body) throws Exception {
		long before = System.currentTimeMillis();
		HttpResponse resp = execPostAttach(url, body, jsonContent);
		long after = System.currentTimeMillis();
		long delta = after - before;
		log.info(url + " time= " + delta + " ms");
		return resp;
	}

	public HttpResponse execPut(String url, String body) throws Exception {
		log.info(url);
		long before = System.currentTimeMillis();
		HttpResponse resp = execPutAttach(url, body, jsonContent);
		long after = System.currentTimeMillis();
		long delta = after - before;
		log.info(url + " time= " + delta + " ms");
		return resp;
	}

	public HttpResponse execDelete(String url) throws Exception {
		HttpClient client = createClient();
		HttpDelete delete = new HttpDelete(url);
		HttpResponse resp = client.execute(delete);
		return resp;
	}

	public HttpResponse execGetSingleTime(String url) throws Exception, IOException, Exception {
		HttpClient client = createClient();
		HttpGet get = new HttpGet(url);
		long before = System.currentTimeMillis();
		HttpResponse resp = client.execute(get);
		long after = System.currentTimeMillis();
		long delta = after - before;
		log.info(url + " time " + delta + " ms");
		return resp;
	}

	public boolean isStatusOK(HttpResponse resp) {
		int code = resp.getStatusLine().getStatusCode();
		String stat = resp.getStatusLine().getReasonPhrase();
		log.info("code" + code + " : " + "stat " + stat);
		if (code == 200 && stat.equalsIgnoreCase("ok")) {
			return true;
		} else {
			return false;
		}
	}

	public HttpResponse execPostAttach(String url, String body, String content) throws Exception {
		HttpClient client = createClient();
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", content);
		post.setEntity(new StringEntity(body, "UTF-8"));
		HttpResponse resp = client.execute(post);
		return resp;
	}

	public HttpResponse execPutAttach(String url, String body, String content) throws Exception {
		HttpClient client = createClient();
		HttpPut put = new HttpPut(url);
		put.addHeader("Content-Type", content);
		put.setEntity(new StringEntity(body, "UTF-8"));
		HttpResponse resp = client.execute(put);
		return resp;
	}

	public JSONObject getResponse(HttpResponse response) throws UnsupportedOperationException, IOException, org.xml.sax.SAXException {
		HttpEntity entity = response.getEntity();	        
		InputStream content = entity.getContent();
		InputStreamReader isr = new InputStreamReader(content);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}      
		JSONObject jsonObject = XML.toJSONObject(sb.toString());
		log.info("Response: "+jsonObject.toString());
		return jsonObject;
	}
}
