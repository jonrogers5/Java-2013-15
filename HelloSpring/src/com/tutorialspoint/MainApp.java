package com.tutorialspoint;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class MainApp {
	public static void main(String[] args) {
	      ApplicationContext context = 
	             new ClassPathXmlApplicationContext("Beans.xml");

	      HelloWorld obj = (HelloWorld) context.getBean("helloWorld");
	      int i[] = new int[10];
	      obj.setMessage("value = "+i.length);

	      
	      obj.getMessage();
	      obj.setMessage("value = "+(i.length+1));

	      obj.getMessage();
	      
	      HelloWorld obj2 = (HelloWorld) context.getBean("helloWorld");
	      
	      obj2.setMessage("value = "+i.length);

	      
	      obj2.getMessage();
	      obj2.setMessage("value = "+(i.length+10));

	      obj2.getMessage();

	}
}
