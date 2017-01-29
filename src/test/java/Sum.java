package test.java;

import org.testng.annotations.Test;


public class Sum {
	@Test
	public void sum(){
	int s=10;
	Multiply m1=new Multiply();
	int c=m1.multiply(10,20);
	
	System.out.println(c+"   "+"hello");
	}
	
}
