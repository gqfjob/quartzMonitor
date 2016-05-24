package com.data.collect.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.data.collect.model.Team;


public class collectService implements doCollect {

	public void collect() {
		/*String pathUrl = "http://china.nba.com/static/data/season/schedule_2016_05.json";   
		
		try {
			// 建立连接   
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// //设置连接属性   
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出   
			httpConn.setDoInput(true);// 使用 URL 连接进行输入   
			httpConn.setUseCaches(false);// 忽略缓存   
			//httpConn.setRequestMethod("POST");// 设置URL请求方法   
			String requestString = "客服端要以以流方式发送到服务端的数据...";   
			  
			   
			// 设置请求属性   
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致   ENCODING_UTF_8
			byte[] requestStringBytes = requestString.getBytes("UTF-8");   
			//httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);   
			httpConn.setRequestProperty("Content-Type", "application/json");   
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接   
			httpConn.setRequestProperty("Charset", "UTF-8");
			
			// 建立输出流，并写入数据   
			OutputStream outputStream = httpConn.getOutputStream();   
			outputStream.write(requestStringBytes);   
			outputStream.close(); 
			httpConn.getInputStream();
			// 获得响应状态   
			int responseCode = httpConn.getResponseCode();   
			
			   
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功   
			// 当正确响应时处理数据   
			StringBuffer sb = new StringBuffer();   
			String readLine;   
			BufferedReader responseReader;   
			// 处理响应流，必须与服务器响应流输出的编码一致   
			 responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));   
			while ((readLine = responseReader.readLine()) != null) {  
				System.out.println(readLine.toString());
			sb.append(readLine).append("\n");   
			}   
			responseReader.close();   
			//tv.setText(sb.toString());   
			}else{
				System.out.println("状态码："+responseCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}*/
//		HttpURLConnection urlCon=new 

	}
	
	/**
     * Do GET request
     * @param url
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String doGet(String url) {
    	//String url = "http://www.google.com/search?q=httpClient";
    	try {
    		HttpClient client = HttpClientBuilder.create().build();
        	HttpGet request = new HttpGet(url);

        	// add request header
        	request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
        	HttpResponse response = client.execute(request);

        	System.out.println("Response Code : " 
                        + response.getStatusLine().getStatusCode());

        	BufferedReader rd = new BufferedReader(
        		new InputStreamReader(response.getEntity().getContent()));

        	StringBuffer result = new StringBuffer();
        	String line = "";
        	while ((line = rd.readLine()) != null) {
        		result.append(line);
        	}
        	return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }
    
	public static void main(String[] args) {
		String pathUrl = "http://china.nba.com/static/data/season/schedule_2016_05.json";
		collectService service=new collectService();
		//long time=1462131000000L;
		
		
		
		try {
			String data=new String(service.doGet(pathUrl));
			System.out.println(data);
			JSONObject jsonObj=JSONObject.fromObject(data);
			System.out.println(jsonObj.getString("timestamp").toString());
			//long ot=Long.valueOf(jsonObj.getString("timestamp").toString());
			//String utc=DateFormatUtils.formatUTC(ot, DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
			JSONArray games=jsonObj.getJSONObject("payload").getJSONArray("dates");
			System.out.println("时间					对阵(客@主)			比分			地点			转播			链接");
			for (int i = 0; i < games.size(); i++) {
				JSONObject gameObjs=games.getJSONObject(i);
				JSONArray gameUTC=gameObjs.getJSONArray("games");
				for (int j = 0; j < gameUTC.size(); j++) {
					JSONObject gameObj=gameUTC.getJSONObject(j);
					JSONObject profile=gameObj.getJSONObject("profile");
					long ot=Long.valueOf(profile.getString("utcMillis").toString());
					String utc=DateFormatUtils.formatUTC(ot, DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
					System.out.print(utc+"			");
					JSONObject homeTeam=gameObj.getJSONObject("homeTeam").getJSONObject("profile");
					JSONObject awayTeam=gameObj.getJSONObject("awayTeam").getJSONObject("profile");
					StringBuffer column2=new StringBuffer();
					if(awayTeam!=null){
						Team awayTeamBean=(Team)JSONObject.toBean(awayTeam, Team.class);
						column2.append(awayTeamBean.getName());
						column2.append("@");
					}
					if(homeTeam!=null){
						Team homeTeamBean=(Team)JSONObject.toBean(homeTeam, Team.class);
						column2.append(homeTeamBean.getName());
						column2.append("			");
					}
					if(!column2.toString().isEmpty()){
						System.out.print(column2.toString());
					}
					//获取比分start
					JSONObject boxScore=gameObj.getJSONObject("boxscore");
					if(boxScore!=null){
						System.out.print(boxScore.getString("awayScore")+"-"+boxScore.getString("homeScore")+"			");
					}
					
					//获取地点start
					System.out.println(profile.getString("arenaName")+"			");
				}
				
				
				
				
			}
			//System.out.println("数据更新时间："+utc);
			//System.out.println("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//service.collect();
	}

}
