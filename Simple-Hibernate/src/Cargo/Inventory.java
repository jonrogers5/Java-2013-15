package Cargo;


//Hibernate POJO class
public class Inventory {
	int id;
	String item;
	String category;
	public Inventory() {}
	
	public Inventory(int id, String category, String item){
		this.item = item;
		this.category = category;
		this.id = id;
		
	}
	public int getid(){
		return id;
	}
	public void setid(int id){
		this.id = id;
	}
	
	public String getItem(){
		return item;
	}
	public void setItem(String item){
		this.item = item;
	}
	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return category;
	}
	
}
