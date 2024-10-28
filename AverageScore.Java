import java.util.Scanner;

public class AverageScore
{
   public static void main(String[] args)
   {
    Scanner scanner= new Scanner (System.in);  
    
    System.out.print("Enter score 1:");
    double score1= scanner.nextDouble();
    
    System.out.print("Enter score 2:");
    double score2= scanner.nextDouble();
    
    System.out.print("Enter score 3:");
    double score3= scanner.nextDouble();
    
    double average= (score1 + score2 + score3)/3;
    
    System.out.printf("The average score is %.2f%n",average);
    
    scanner.close();
    
    
   }
}
