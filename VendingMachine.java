import java.util.Scanner;

public class VendingMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gumSold = 0;
        int chocolateSold = 0;
        int popcornSold = 0;
        int juiceSold = 0;

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("[1] Get gum");
            System.out.println("[2] Get chocolate");
            System.out.println("[3] Get popcorn");
            System.out.println("[4] Get juice");
            System.out.println("[5] Display total sold so far");
            System.out.println("[6] Quit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    gumSold++;
                    System.out.println("Here is your gum");
                    break;
                case 2:
                    chocolateSold++;
                    System.out.println("Here is your chocolate");
                    break;
                case 3:
                    popcornSold++;
                    System.out.println("Here is your popcorn");
                    break;
                case 4:
                    juiceSold++;
                    System.out.println("Here is your juice");
                    break;
                case 5:
                    System.out.println(gumSold + " items of gum sold");
                    System.out.println(chocolateSold + " items of chocolate sold");
                    System.out.println(popcornSold + " items of popcorn sold");
                    System.out.println(juiceSold + " items of juice sold");
                    break;
                case 6:
                    System.out.println("Thank you for using the vending machine. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Error, options 1-6 only!");
                    break;
            }
        }
    }
}
