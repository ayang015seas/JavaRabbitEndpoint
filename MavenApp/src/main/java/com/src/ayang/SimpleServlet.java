package com.src.ayang;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import org.json.JSONObject;
import org.json.HTTP;
import org.json.JSONException;

/**
 * Servlet implementation class SimpleServlet
 */
public class SimpleServlet extends HttpServlet {
	
    public static final Gauge metric_1 = Gauge.build().name("metric_1").help("metric_1").register();
    public static final Counter metric_2 = Counter.build().name("issues").help("issues").register();
    
	public static SimpleServlet _instance = null;

	public static SimpleServlet getInstance() {
		if (_instance == null) {
			_instance = new SimpleServlet();
		} 
		return _instance;
	}
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		metric_1.set(24);
		metric_2.inc();
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }

		  try {
		    JSONObject jsonObject =  HTTP.toJSONObject(jb.toString());
		    FileWriter fw = new FileWriter("App.log", true);
		    BufferedWriter buf = new BufferedWriter(fw);
		    buf.write(jsonObject.toString());
		    buf.close();
		    fw.close();
		    
		    File file = new File("App.log"); 
		    
		    BufferedReader br = new BufferedReader(new FileReader(file)); 
		    
		    String st; 
		    while ((st = br.readLine()) != null)  {
		      System.out.println(st); 
		    }
		    br.close();
		   // FileWriter fw = new FileWriter("App.log", true);
//	        BufferedWriter bw = new BufferedWriter(fw);
//	        bw.write("Spain");
//	        bw.newLine();
//	        bw.close();

		    System.out.println("PRINTING");
		    System.out.println(jsonObject.get("alertname"));
		    Iterator<String> keys = jsonObject.keys();
		    
//		    while(keys.hasNext()) {
//		        String key = keys.next();
//		        System.out.println(key);
//		        if (jsonObject.get(key) instanceof JSONObject) {
//		        	JSONObject obj = (JSONObject) jsonObject.get(key);
//		        	System.out.println(obj.toString());
//		              // do something with jsonObject here      
//		        }
//		    }
		    // JSONObject test1 = (JSONObject) jsonObject.get("labels");
			// System.out.println(test1.toString());
			} catch (JSONException e) {
		    // crash and burn
			System.out.println("Not Processed Correctly");
		    throw new IOException("Error parsing JSON request string");
		  }

		// doGet(request, response);
	}

}
