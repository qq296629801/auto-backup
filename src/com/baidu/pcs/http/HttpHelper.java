package com.baidu.pcs.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.baidu.pcs.PcsUploadResult;
import com.baidubce.services.bos.model.SetBucketAclRequest;

public class HttpHelper {
	private static final String TAG = "JavaPCS";
	final static Logger logger = Logger.getLogger(HttpHelper.class);

	private static final int CONNECT_TIMEOUT = 30 * 1000; // ms
	private static final int T_TIMEOUT = 60 * 60 * 1000; // ms

	private static final List<NameValuePair> EMPTY_PARAMS = new ArrayList<NameValuePair>();

	public HttpHelper() {

	}

	/**
	 * @param baseURL
	 *            基本url 如http://xxx.com/path
	 * @param params
	 *            [{'a':'b'}, {'c':'d'}]
	 * @return: 用于传入doGet, doPost 的url, 如 http://xxx.com/path?a=b&c=d
	 * @throws BadRequestException
	 * 
	 */
	public static String buildURL(String baseURL, List<NameValuePair> params) throws BadRequestException {
		if (params == null)
			params = EMPTY_PARAMS;

		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "utf8");
			String url = baseURL + '?' + EntityUtils.toString(entity);
			return url;
		} catch (UnsupportedEncodingException e1) {
			throw new BadRequestException("error on buildURL" + baseURL + params);
		} catch (IOException e) {
			throw new BadRequestException("error on buildURL" + baseURL + params);
		}
	}

	public String doGet(String url) throws RestHttpException, HttpException {
		logger.debug("doget " + url);
		final HttpGet request = new HttpGet(url);
		return execute(request);
	}

	public void doGetToFile(String url, String localFilePath) throws RestHttpException, HttpException {
	logger.debug("doget " + url);
		final HttpGet request = new HttpGet(url);
		final HttpResponse resp;
		try {
			resp = newHttpClient().execute(request);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			FileOutputStream out = new FileOutputStream(localFilePath);
			resp.getEntity().writeTo(out);
			out.close();
				
				return ;
		} else {
				logger.error("http errorcode " + resp.getStatusLine().getStatusCode());
				final String response = EntityUtils.toString(resp.getEntity());
				throw new RestHttpException(resp.getStatusLine().getStatusCode(), response);
		}
		} catch (final IOException e) {
			logger.error("Exception in http request ");
			e.printStackTrace();
			throw new HttpException("IOException " + e.toString());
		}
	}
	
	/**
	 * post application/x-www-form-urlencoded
	 * @throws HttpException 
	 */
	public String doPost(String url, List<NameValuePair> params) throws RestHttpException, HttpException {
		logger.debug("doPost " + url);
		if (params == null)
			params = EMPTY_PARAMS;

		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "utf8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		final HttpPost request = new HttpPost(url);
		request.addHeader(entity.getContentType());

		request.setEntity(entity);
		return execute(request);
	}

	public String doPostMultipart(String url, String filePath, List<NameValuePair> params) throws RestHttpException, HttpException {
		logger.debug("doPostMultipart" + url);
		if (params == null)
			params = EMPTY_PARAMS;

		HttpPost request = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity();
		if (filePath != null && !filePath.isEmpty()) {
			FileBody bin = new FileBody(new File(filePath));
			reqEntity.addPart("uploadedfile", bin);
		}

		for (NameValuePair kv : params) {
			multipartAddKV(reqEntity, kv.getName(), kv.getValue());
		}
		request.setEntity(reqEntity);

		return execute(request);
	}
	public String doPostMultipartInpusteam(String url, InputStream in,String fileName, List<NameValuePair> params) throws RestHttpException, HttpException, IOException {
		logger.debug("doPostMultipart" + url);
		if (params == null)
			params = EMPTY_PARAMS;
		HttpPost request = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity();
		
		ContentBody is =  new InputStreamBody(in,fileName);
		reqEntity.addPart("uploadedfile",is);
		for (NameValuePair kv : params) {
			multipartAddKV(reqEntity, kv.getName(), kv.getValue());
		}
		request.setEntity(reqEntity);
		return execute(request);
	}

	private String execute(HttpRequestBase request) throws RestHttpException, HttpException {
		request.addHeader("User-Agent", TAG);
		final HttpResponse resp;
		try {
			resp = newHttpClient().execute(request);
			final String response = EntityUtils.toString(resp.getEntity());

			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				logger.debug("http response: " + response);
				return response;
			} else {
				logger.error("http errorcode " + resp.getStatusLine().getStatusCode());
				throw new RestHttpException(resp.getStatusLine().getStatusCode(), response);
			}
		} catch (final IOException e) {
			logger.error("Exception in response");
			e.printStackTrace();
			throw new HttpException("IOException " + e.toString());
		}
	}

	private HttpClient newHttpClient() {
		HttpClient httpClient;
		httpClient = TrustAllSSLSocketFactory.getNewHttpClient();

		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// HttpConnectionParams.setSoTimeout(params, -1);
		ConnManagerParams.setTimeout(params, T_TIMEOUT);
		return httpClient;
	}

	private static void multipartAddKV(MultipartEntity reqEntity, String key, String value) {
		StringBody body = null;
		try {
			body = new StringBody(value);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		reqEntity.addPart(key, body);
	}

	public static class BadRequestException extends Exception {
		public BadRequestException(String detailMessage) {
			super(detailMessage);
		}
	}

	public static class RestHttpException extends Exception {
		public int statusCode;
		public String responseBody;

		public RestHttpException(int statusCode, String responseBody) {
			super("" + statusCode + ":" + responseBody);
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}
	}
}
