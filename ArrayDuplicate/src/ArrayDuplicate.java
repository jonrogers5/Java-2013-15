import java.util.HashSet;
import java.util.Set;
public class ArrayDuplicate {

	public static void main(String[] args) {
		int i[] = {2,5,2,67};
		System.out.println(duplicate(i));

	}
	public static boolean duplicate(int input[]){
		Set<Integer> dupSet = new HashSet<Integer>(input.length);

	    for (int a : input) {
	        if (!dupSet.add(a))
	            return true;
	    }

	    return false;
	}

}
