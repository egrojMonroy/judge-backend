import java.util.Scanner;
 //11727 - Cost Cutting - Java
 public class Main {  
      static int testCases;  
      static int max;  
      static int min;  
      static int a;  
      static int b;  
      static int c;  
      public static void main(String[] args) {  
           Scanner scanner = new Scanner(System.in);  
           testCases = scanner.nextInt();  
           for(int i = 1; i < testCases+1; i++) {  
                a = scanner.nextInt();  
                b = scanner.nextInt();  
                c = scanner.nextInt();  
                System.out.print("Case " + i + ": ");  
                if (a > b) {  
                     if (b > c) {  
                          System.out.println(b); 
				System.out.println('5');	 
                          continue;  
                     }  
                     if (a > c) {  
                          System.out.println(c);  
                          continue;  
                     }  
                     System.out.println(a);  
                     continue;  
                }  
                if (a > c) {  
                     System.out.println(a);  
                     continue;  
                }  
                if (b > c) {  
                     System.out.println(c);  
                     continue;  
                }  
                System.out.println(b);  
                continue;  
           }  
      }  
 }  
