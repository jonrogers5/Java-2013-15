import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class HappyNumber {
	public static boolean isHappyPrime(int input) {
		if (input < 7 || input % 2 == 0)
			return false;
		
		for (int z = 3;z*z<=input;z+=2)
			if (input % z == 0)
				return false;
		
		List<Integer> set = new ArrayList<Integer>();
		
		while (!set.contains(input) && input > 1){
			int result = 0;
			set.add(input);
			char numStr[] = Integer.toString(input).toCharArray();
			
			for (char character : numStr){
				int digit = Character.digit(character, 10);
				result += (digit * digit);
			}
			input=result;
		}
		return (input == 1);
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter a number: ");
		int origVal;
		int value = origVal = scan.nextInt(); 
		System.out.println();
		
		System.out.println(isHappyPrime(value));
		System.exit(0);
	}

}
