package com.softech.ls360.lcms.contentbuilder.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;


public class RestClient implements RestOperations{

	private static final Logger log = Logger.getLogger(RestClient.class);
	
	/*
	 * Make the HTTP POST request, marshaling the request(java-object) to JSON,
	 * and the response to a String
	 */
	public String postForObject(final Object obj, final String servicePath,final int connTimeOut) throws IOException {
		final String POST_JSON = JSONObject.fromObject(obj).toString();
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpPost httpPost = new HttpPost(servicePath);
		StringEntity entity = new StringEntity(POST_JSON, "UTF-8");
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		// httpPost.getParams().setBooleanParameter("http.protocol.expect-continue",false);
		entity.setContentType(basicHeader);
		httpPost.setEntity(entity);
		HttpResponse predictResponse = httpClient.execute(httpPost);
		InputStream inStream = predictResponse.getEntity().getContent();
		return read(inStream);

	}

	public String postForObject(Object obj, String servicePath)
			throws IOException {
		return postForObject(obj, servicePath, 10000);
	}

	/*
	 * Make the HTTP GET request, marshaling the the response to a String
	 */
	public String get(final String servicePath,final int connTimeOut) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpGet httpGet = new HttpGet(servicePath);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		httpGet.setHeader(basicHeader );
		HttpResponse predictResponse = httpClient.execute(httpGet);
		InputStream inStream = predictResponse.getEntity().getContent();
		return read(inStream);
	}

	/*
	 * Make the HTTP GET request, making the response to JSONObject
	 */
	public JSONObject getForObject(final String servicePath,final int connTimeOut) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpGet httpGet = new HttpGet(servicePath);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		httpGet.setHeader(basicHeader );
		HttpResponse predictResponse = httpClient.execute(httpGet);
        JSONObject jsonObj = JSONObject.fromObject(read(predictResponse.getEntity().getContent()));
		int status = predictResponse.getStatusLine().getStatusCode();
		if(status == 200){
			// OK
			return jsonObj;
		}else{
			// Error occur
			return jsonObj;
		}
	}

	public JSONObject postFile(String servicePath, File file) throws IOException {
		return postFile(servicePath, file, (5*60*1000));
	}
	
	public JSONObject postFile(String servicePath, File file, int timeout) throws IOException {
		log.debug("posting file=" + file.getName() + " on " + servicePath);
		HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout);
		HttpPost httpPost = new HttpPost(servicePath);
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		multipartEntity.addPart(file.getName(), new FileBody(file));
		httpPost.setEntity(multipartEntity);
		HttpResponse response = httpClient.execute(httpPost);
		log.debug(response.getStatusLine());
		log.debug(response.getEntity().getContentType());
		String responseString = read(response.getEntity().getContent());
		log.debug(responseString);
		JSONObject jsonObj = JSONObject.fromObject(responseString);
		return jsonObj;
	}
	
	private String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(in),
				1024);
		for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	public <T> T getForObject(String url, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getForObject(String url, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getForObject(URI url, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> getForEntity(String url,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> getForEntity(String url,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpHeaders headForHeaders(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpHeaders headForHeaders(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public URI postForLocation(String url, Object request,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public URI postForLocation(String url, Object request,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public URI postForLocation(URI url, Object request)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T postForObject(String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T postForObject(String url, Object request,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T postForObject(URI url, Object request, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> postForEntity(URI url, Object request,
			Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(String url, Object request, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public void put(String url, Object request, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public void put(URI url, Object request) throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public void delete(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public void delete(String url, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public void delete(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		
	}

	public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<HttpMethod> optionsForAllow(String url,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method,
			HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T execute(String url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T execute(String url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T execute(URI url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> re, Class<T> type) throws RestClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> re, ParameterizedTypeReference<T> ptr) throws RestClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

