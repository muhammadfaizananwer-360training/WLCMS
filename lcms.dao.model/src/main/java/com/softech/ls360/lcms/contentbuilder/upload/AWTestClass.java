package com.softech.ls360.lcms.contentbuilder.upload;




public class AWTestClass {
	
	public static class A {
		public static void a() {
			
		}
	}

	public static class B extends A {
		public static void a() {
			
		}
	}
	public static void main(String[] args) {
		A a = new B();
		a.a();
		String str1 = "hello";
		String str2 = new String("hello");
		String str3 = "hello".intern();
		if ( str1 == str2 ){
		System.out.println("str1 and str2 are same"); 
		}
		if ( str1 == str3 ){
		System.out.println("str1 and str3 are same" ); 
		}
		

	}
	


}
