package com.src.ayang;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.prometheus.client.Gauge;
import io.prometheus.client.hotspot.DefaultExports;
import java.io.File;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Startup implements ServletContextListener {
    private final static String QUEUE_NAME = "hello";
    public static final Gauge java_endpoint = Gauge.build().name("java_endpoint").help("java_endpoint").register();

	public static Startup _instance = null;

	public static Startup getInstance() {
		if (_instance == null) {
			_instance = new Startup();
		} 
		return _instance;
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DefaultExports.initialize();
		java_endpoint.set(15);
		try {
			File logFile = new File("App.log");
			if (logFile.createNewFile()) {
				System.out.println("File Created");
			} else {
				System.out.println("File Exists");
			}
			
			System.out.println(System.getProperty("user.dir"));
			
		} catch (IOException e) {
			System.out.println("ERROR");
		}
		
		try {
		    ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
	
		    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		    
	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	    		java_endpoint.set(14);
	            String message = new String(delivery.getBody(), "UTF-8");
	            System.out.println(" [x] Received '" + message + "'");
	        };
	        
	        
	        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		    
		    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		} catch (Exception e) {
			System.out.println("Fail");
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
