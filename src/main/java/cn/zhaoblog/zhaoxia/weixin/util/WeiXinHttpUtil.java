package cn.zhaoblog.zhaoxia.weixin.util;

import cn.zhaoblog.util.FileUtil;
import com.joysuch.core.util.JsonUtil;
import com.joysuch.core.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Set;

/**
 * 微信HTTP工具类
 */
public class WeiXinHttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(WeiXinHttpUtil.class);
	// 连接超时时间，默认15秒
	private static int socketTimeout = 15000;
	// 传输超时时间，默认30秒
	private static int connectTimeout = 30000;

	public static String postPay(String inputUrl, String content, String certLocalPath, String certPassword) {
		if(StringUtil.isEmpty(inputUrl) || StringUtil.isEmpty(inputUrl)) {
            return null;
        }
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			logger.info("****** Get KeyStore Instance Success ******");
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
		if (keyStore != null) {
			try (FileInputStream instream = new FileInputStream(new File(certLocalPath));){
				// 加载本地的证书进行https加密传输
				keyStore.load(instream, certPassword.toCharArray());// 设置证书密码
				logger.info("****** KeyStore Load Success ******");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = null;
			try {
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
				// Allow TLSv1 protocol only
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
				// 根据默认超时限制初始化requestConfig
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
				HttpPost httpPost = new HttpPost(inputUrl);
				logger.info("****** Post Url[" + inputUrl + "] ******");
				logger.info("****** Post Parameter[" + content + "] ******");
				StringEntity postEntity = new StringEntity(content, "UTF-8");
		        httpPost.addHeader("Content-Type", "text/xml");
		        httpPost.setEntity(postEntity);
		        //设置请求器的配置
		        httpPost.setConfig(requestConfig);
		        try {
		            HttpResponse response = httpClient.execute(httpPost);
		            HttpEntity entity = response.getEntity();
		            return EntityUtils.toString(entity, "UTF-8");
		        } catch (ConnectionPoolTimeoutException e) {
		        	e.printStackTrace();
		        } catch (ConnectTimeoutException e) {
		        	e.printStackTrace();
		        } catch (SocketTimeoutException e) {
		        	e.printStackTrace();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        } finally {
		            httpPost.abort();
		        }
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String uploadFile(String inputurl, Map<String, Object> params, File file) {
		if (StringUtil.notEmpty(inputurl) && file != null) {
			logger.info("****** uploadFile Url[" + inputurl + "] ******");
			HttpURLConnection httpUrlConn = null;
			PrintWriter pw = null;
			BufferedReader bufferedReader = null;
			try {
				String end = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";
				URL url = new URL(inputurl);
				httpUrlConn = (HttpURLConnection) url.openConnection();
				httpUrlConn.setDoOutput(true);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);
				httpUrlConn.setRequestMethod("POST");
				/* setRequestProperty */
				httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
				httpUrlConn.setRequestProperty("Charset", "UTF-8");
				httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

				DataOutputStream ds = new DataOutputStream(httpUrlConn.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\"media\";filename=\"" + file.getName() + "\"" + end);
				ds.writeBytes(end);
				FileInputStream fStream = new FileInputStream(file);
				int bufferSize = 1024;
				byte[] bytebuffer = new byte[bufferSize];
				int length = -1;
				while ((length = fStream.read(bytebuffer)) != -1) {
					ds.write(bytebuffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				fStream.close();
				ds.flush();
				bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8"));
				String line = null;
				StringBuffer buffer = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					buffer.append(line);
				}
				return buffer.toString();
			} catch (Exception e) {
				logger.error("****** uploadFile URL[" + inputurl + "] error ******", e);
				e.printStackTrace();
			} finally {
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
				if (pw != null) {
					pw.close();
				}
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	public static String doPost(String inputurl, String content) {
		if (StringUtil.notEmpty(inputurl)) {
			logger.info("****** Post Url[" + inputurl + "] ******");
			HttpURLConnection httpUrlConn = null;
			PrintWriter pw = null;
			BufferedReader bufferedReader = null;
			try {
				URL url = new URL(inputurl);
				httpUrlConn = (HttpURLConnection) url.openConnection();
				httpUrlConn.setDoOutput(true);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);
				httpUrlConn.setRequestMethod("POST");
				logger.info("****** Post ParamString[" + content + "] ******");
				if (StringUtil.notEmpty(content)) {
					pw = new PrintWriter(new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8"));
					pw.print(content);
					pw.flush();
				}
				bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8"));
				String line = null;
				StringBuffer buffer = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					buffer.append(line);
				}
				return buffer.toString();
			} catch (Exception e) {
				logger.error("****** Post[" + inputurl + "] error ******", e);
				e.printStackTrace();
			} finally {
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
				if (pw != null) {
					pw.close();
				}
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	public static String doPost(String inputurl, Map<String, Object> params) {
		if (StringUtil.notEmpty(inputurl)) {
			String paramsString = null;
			if (params != null && params.size() > 0) {
				paramsString = JsonUtil.toJson(params);
				logger.info("****** Post ParamString[" + paramsString + "] ******");
			}
			return doPost(inputurl, paramsString);
		}
		return null;
	}

	public static String doGet(String inputurl, Map<String, Object> params) {
		if (StringUtil.notEmpty(inputurl)) {
			String paramsString = contactParams(inputurl, params);
			if (StringUtil.notEmpty(paramsString)) {
				inputurl += paramsString;
			}
			logger.info("****** Get Url[" + inputurl + "] ******");
			HttpURLConnection httpUrlConn = null;
			BufferedReader bufferedReader = null;
			try {
				URL url = new URL(inputurl);
				httpUrlConn = (HttpURLConnection) url.openConnection();

				httpUrlConn.setDoOutput(false);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);

				httpUrlConn.setRequestMethod("GET");
				httpUrlConn.connect();

				bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "utf-8"));

				String str = null;
				StringBuffer buffer = new StringBuffer();
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				return buffer.toString();
			} catch (Exception e) {
				logger.error("****** Get[" + inputurl + "] error ******", e);
				e.printStackTrace();
			} finally {
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}
	
	public static String contactParams(String inputurl, Map<String, Object> params) {
		StringBuffer buffer = new StringBuffer();
		if (params != null && params.size() > 0) {
			if(StringUtil.isEmpty(inputurl) || inputurl.indexOf("?") == -1){
				buffer.append("?");
			}else{
				buffer.append("&");
			}
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				buffer.append(key + "=" + params.get(key) + "&");
			}
			if (buffer.length() > 0) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			return buffer.toString();
		}
		return null;
	}

    public static String contactParams(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("?");
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                buffer.append(key + "=" + params.get(key) + "&");
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            return buffer.toString();
        }
        return null;
    }
	
	/**
	 * 
	 * @param inputurl
	 * @param content
	 * @param destFolder
	 * @param descFileName 不包含后缀
	 * @param defaultExt
	 * @return
	 */
	public static String downloadFile(String inputurl, String content, String destFolder, String descFileName, String defaultExt){
		if (StringUtil.notEmpty(inputurl)) {
			logger.info("****** DownloadFile Url[" + inputurl + "] ******");
			HttpURLConnection httpUrlConn = null;
			PrintWriter pw = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			try {
				URL url = new URL(inputurl);
				httpUrlConn = (HttpURLConnection) url.openConnection();
				httpUrlConn.setDoOutput(true);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);
				httpUrlConn.setRequestMethod("POST");
				logger.info("****** DownloadFile Post ParamString[" + content + "] ******");
				if (StringUtil.notEmpty(content)) {
					pw = new PrintWriter(new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8"));
					pw.print(content);
					pw.flush();
				}
				String contentDisposition = httpUrlConn.getHeaderField("Content-Disposition");
				String contentType = httpUrlConn.getHeaderField("Content-Type");
				logger.info("****** Content-Disposition : " + contentDisposition);
				logger.info("****** Content-Type : " + contentType);
				String fileExt = FileUtil.getExt(contentDisposition);
				if(StringUtil.isEmpty(fileExt)){
					fileExt = FileUtil.getExt(contentType);
					if(StringUtil.isEmpty(fileExt)){
						fileExt = defaultExt;
						logger.info("****** set default ext :" + fileExt + " ******");
					}
				}
				logger.info("****** get file ext :" + fileExt + " ******");
				File folder = new File(destFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				String fullPath = destFolder + File.separator + descFileName + fileExt;
				bis = new BufferedInputStream(httpUrlConn.getInputStream());
				fos = new FileOutputStream(fullPath);
				byte[] buf = new byte[8096];
				int size = 0;
				while((size = bis.read(buf)) != -1){
					fos.write(buf, 0, size);
				}
				return descFileName + fileExt;
			} catch (Exception e) {
				logger.error("****** DownloadFile Post[" + inputurl + "] error ******", e);
				e.printStackTrace();
			} finally {
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
				if (pw != null) {
					pw.close();
				}
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	/**
	 *wxcardSignatureParams
	 *2016年6月23日 上午10:05:29
	 *@param params
	 *@return
	 *TODO：获取卡券签名的参数
	 */
	public static String wxcardSignatureParams(Map<String, Object> params) {
		StringBuffer buffer = new StringBuffer();
		if(params != null && params.size() > 0){
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				buffer.append(key);
			}
		}
		return buffer.toString();
	}
}
