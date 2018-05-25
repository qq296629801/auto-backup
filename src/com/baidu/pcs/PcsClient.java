package com.baidu.pcs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.baidu.pcs.exception.PcsException;
import com.baidu.pcs.exception.PcsHttpException;
import com.baidu.pcs.exception.PcsKnownException;
import com.baidu.pcs.http.HttpHelper;
import com.baidu.pcs.http.HttpHelper.BadRequestException;
import com.baidu.pcs.http.HttpHelper.RestHttpException;

public class PcsClient {

	public static final PcsClient dao = new PcsClient();
	private static final String TAG = "JavaPCS";
	final static Logger logger = Logger.getLogger(PcsClient.class);

	public static final String VERSION = "0.9";
	public static final long MAX_UPLOAD_SIZE = 1024 * 1024 * 1024; // 1G
	public static final String PCSHOST = "https://pcs.baidu.com/";

	public static final String ORDER_BY_TIME = "time";
	public static final String ORDER_BY_NAME = "name";
	public static final String ORDER_BY_SIZE = "size";

	public static final String ORDER_DESC = "desc";
	public static final String ORDER_SC = "sc";

	public static final String client_id = "ZQT9cHv758TzQqBSrTtMESRI";
	public static final String client_secret = "MVz18SI2seyCucSRBAweuvPz61eaUsFP";

	private String accessToken = "26.18c794e003651f01d7241c2720af7f05.2592000.1529419563.3442166296-1866274";
	// 一个月
	private String refreToken = "27.921e43d1f4d5efa978b281e47f18a334.315360000.1842187563.3442166296-1866274";
	// 十年
	private String appRoot = "";
	private HttpHelper httpHelper = new HttpHelper();

	public PcsClient() {
	}

	public static void main(String[] args) throws RestHttpException, HttpException, ParseException, PcsException {
		PcsClient pcs = new PcsClient();
		System.out.println(pcs.quota().toString());
		String remote = "/apps/tms/file/res";
		List<PcsFileEntry> list = pcs.list(remote);
		for (PcsFileEntry file : list) {
			if (file.isDir() == false) {
				System.out.println("C:\\Users\\Administrator\\Downloads\\" + file.getServerFilename());
				System.out.println(remote + file.getPath());
				pcs.downloadToFile(file.getPath(), "C:\\Users\\Administrator\\Downloads\\" + file.getServerFilename());
			}
		}
	}

	public void getToken() throws RestHttpException, HttpException, ParseException {
		// 设备获取方式 一个月token 十年refetoken
		String aaa = "";
		String url = "https://openapi.baidu.com/oauth/2.0/token?grant_type=device_token&code=7ebaa6ebae9dfa633cc21483f57e5d8e&client_id=ZQT9cHv758TzQqBSrTtMESRI&client_secret=MVz18SI2seyCucSRBAweuvPz61eaUsFP";
		// String aa =
		// "https://openapi.baidu.com/oauth/2.0/token?grant_type=refresh_token&refresh_token="
		// + refreToken
		// + "&client_id=" + client_id + "&client_secret=" + client_secret + "";
		String str = httpHelper.doGet(url);
		System.out.println(str);
		JSONObject obj = (JSONObject) new JSONParser().parse(str);
	}

	public PcsUploadResult uploadFile(String localPath, String remoteDir, String remoteName) throws PcsException {
		assertAuthenticated();
		logger.info("upload " + localPath + " to " + remoteDir + " / " + remoteName);
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "upload"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("dir", remoteDir));
		params.add(new BasicNameValuePair("filename", remoteName));

		try {
			url = HttpHelper.buildURL(url, params);
			String resp = httpHelper.doPostMultipart(url, localPath, null);
			return new PcsUploadResult(resp);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	public PcsUploadResult uploadFileBos(InputStream in, String fileName, String remoteDir, String remoteName)
			throws PcsException, IOException {
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/file";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "upload"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("dir", remoteDir));
		params.add(new BasicNameValuePair("filename", remoteName));
		try {
			url = HttpHelper.buildURL(url, params);
			String resp = httpHelper.doPostMultipartInpusteam(url, in, fileName, null);
			return new PcsUploadResult(resp);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	public String download(String remotePath, String locaPath)
			throws BadRequestException, RestHttpException, HttpException {
		String url = "https://d.pcs.baidu.com/rest/2.0/pcs/file";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "download"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("path", remotePath));
		url = HttpHelper.buildURL(url, params);
		httpHelper.doGetToFile(url, locaPath);
		return url;
	}

	public String videoSrc(String remotePath) throws PcsException, BadRequestException {
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/file";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "download"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("path", remotePath));
		return HttpHelper.buildURL(url, params);
	}

	public String videoSrc2(String remotePath) throws PcsException, BadRequestException {
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/stream";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "download"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("path", remotePath));
		return HttpHelper.buildURL(url, params);
	}

	public String play(String fileName, String type, String locaPath)
			throws BadRequestException, RestHttpException, HttpException, ParseException {
		String url = PCSHOST + "rest/2.0/pcs/file";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "streaming"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("path", ""));
		params.add(new BasicNameValuePair("type", type));
		url = HttpHelper.buildURL(url, params);
		httpHelper.doGetToFile(url, locaPath);
		return url;
	}

	public void downloadToFile(String remotePath, String locaPath) throws PcsException {
		assertAuthenticated();
		logger.info("download " + remotePath + "to" + locaPath);
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "download"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("path", remotePath));
		try {
			url = HttpHelper.buildURL(url, params);
			httpHelper.doGetToFile(url, locaPath);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	public PcsQuotaInfo quota() throws PcsException {
		assertAuthenticated();
		logger.info("quota ");
		String url = PCSHOST + "rest/2.0/pcs/quota";

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "info"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));

		String resp = httpGet(url, params);
		return new PcsQuotaInfo(resp);
	}

	public List<PcsFileEntry> list(String remoteDir) throws PcsException {
		return list(remoteDir, ORDER_BY_TIME, ORDER_DESC, 0, 100);
	}

	public ArrayList<PcsFileEntry> list(String remoteDir, String orderBy, String sortOrder, int limit_start,
			int limit_end) throws PcsException {
		assertAuthenticated();
		logger.info("list " + remoteDir);
		String url = PCSHOST + "rest/2.0/pcs/file";
		String limit = limit_start + "-" + limit_end;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "list"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("dir", remoteDir));
		params.add(new BasicNameValuePair("by", orderBy));
		params.add(new BasicNameValuePair("order", sortOrder));

		params.add(new BasicNameValuePair("limit", limit));
		String responseBody = httpGet(url, params);
		return PcsFileEntry.parseArrayFromJsonString(responseBody);
	}

	public ArrayList<PcsFileEntry> search(String remoteDir, String keyword, boolean recursive) throws PcsException {
		assertAuthenticated();
		logger.info("search " + remoteDir + " for " + keyword);
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "search"));
		params.add(new BasicNameValuePair("access_token", this.accessToken));
		params.add(new BasicNameValuePair("dir", remoteDir));
		params.add(new BasicNameValuePair("wd", keyword));
		params.add(new BasicNameValuePair("re", new Boolean(recursive).toString()));

		String responseBody = httpGet(url, params);
		return PcsFileEntry.parseArrayFromJsonString(responseBody);
	}

	public void delete(String remotePath) throws PcsException {
		logger.info("delete " + remotePath);
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> qs_params = new ArrayList<NameValuePair>();
		qs_params.add(new BasicNameValuePair("method", "delete"));
		qs_params.add(new BasicNameValuePair("access_token", this.accessToken));
		qs_params.add(new BasicNameValuePair("path", remotePath));
		httpPost(url, qs_params);
	}

	public void mkdir(String remotePath) throws PcsException {
		logger.info("mkdir " + remotePath);
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> qs_params = new ArrayList<NameValuePair>();
		qs_params.add(new BasicNameValuePair("method", "mkdir"));
		qs_params.add(new BasicNameValuePair("access_token", this.accessToken));
		qs_params.add(new BasicNameValuePair("path", remotePath));

		httpPost(url, qs_params);
	}

	public void move(String remoteFromPath, String remoteToPath) throws PcsException {
		moveOrCopy(remoteFromPath, remoteToPath, "move");
	}

	public void copy(String remoteFromPath, String remoteToPath) throws PcsException {
		moveOrCopy(remoteFromPath, remoteToPath, "copy");
	}

	private void moveOrCopy(String from, String to, String method) throws PcsException {
		assertAuthenticated();
		String url = PCSHOST + "rest/2.0/pcs/file";

		ArrayList<NameValuePair> qs_params = new ArrayList<NameValuePair>();
		qs_params.add(new BasicNameValuePair("method", method));
		qs_params.add(new BasicNameValuePair("access_token", this.accessToken));
		qs_params.add(new BasicNameValuePair("from", from));
		qs_params.add(new BasicNameValuePair("to", to));

		httpPost(url, qs_params);
	}

	private String httpPostParam(String url, ArrayList<NameValuePair> qs_params, JSONObject jsonObject)
			throws PcsException {
		try {
			url = HttpHelper.buildURL(url, qs_params);
			String jsonString = jsonObject.toJSONString();
			logger.debug("post json: " + jsonString);

			ArrayList<NameValuePair> post_params = new ArrayList<NameValuePair>();
			post_params.add(new BasicNameValuePair("param", jsonString));

			return httpHelper.doPostMultipart(url, null/* file */, post_params);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	private String httpGet(String url, ArrayList<NameValuePair> params) throws PcsException {
		try {
			url = HttpHelper.buildURL(url, params);
			return httpHelper.doGet(url);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	private void httpPost(String url, ArrayList<NameValuePair> qs_params) throws PcsException, PcsKnownException {
		try {
			url = HttpHelper.buildURL(url, qs_params);
			httpHelper.doPostMultipart(url, null/* file */, null/* params */);
		} catch (HttpException e) {
			e.printStackTrace();
			throw new PcsHttpException(e.toString());
		} catch (BadRequestException e) {
			e.printStackTrace();
			throw new PcsException("bad request");
		} catch (RestHttpException e) {
			e.printStackTrace();
			throw new PcsKnownException(e.responseBody);
		}
	}

	private void assertAuthenticated() throws PcsException {
		if (this.accessToken == null)
			throw new PcsException("no access_token set");
	}

}
