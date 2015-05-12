package Cargo;

import java.util.List; 
import java.util.Iterator; 
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


//This is a simple program to demonstrate inserting data into a MySQL database
public class ManageCargo {
	 private static SessionFactory factory;
	 private static StandardServiceRegistryBuilder builder;
	 private static Configuration configuration;
	   public static void main(String[] args) {
	      try{
	    	  configuration = new Configuration().configure();
	    	  builder = new StandardServiceRegistryBuilder().
	    	  applySettings(configuration.getProperties());
	    	  factory = configuration.buildSessionFactory(builder.build());
	      }catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
	      ManageCargo MC = new ManageCargo();
	      
	      //add cargo
	      MC.addInventory(3, "Furniture", "Chair");
	      
	      //Display results
	      MC.listInventory();
	      System.exit(0);
	   }
	    
	  public void addInventory(int id, String category, String item){
		  Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try{
	          tx = session.beginTransaction();
	          
	          //Populate table
	          Inventory inventory = new Inventory(id, category, item);
	          session.save(inventory);
	          tx.commit();
	       }catch (HibernateException e) {
	          if (tx!=null) tx.rollback();
	          e.printStackTrace(); 
	       }finally {
	          session.close(); 
	       }
	  }
	  
	  public void listInventory( ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         List cargo = session.createQuery("FROM Inventory").list(); 
	         for (Iterator iterator = 
	                           cargo.iterator(); iterator.hasNext();){
	            Inventory inventory = (Inventory) iterator.next(); 
	            System.out.print("ID: " + inventory.getid()); 
	            System.out.print("Category: " + inventory.getCategory()); 
	            System.out.println("Item: " + inventory.getItem()); 
	         }
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	      
}
