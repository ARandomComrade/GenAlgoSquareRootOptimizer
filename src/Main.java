import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author armando
 * were going to make a converging expansion of the square root function using heron's method
 * up to the limits of the 32 bit float using genetic algorithms
 * herons method: xn+1=1/2(xn+s/xn) s being the input, and x being a guess between 0 and s
 * make an initial population of 1000 first guesses between 1 and s upper bound 8388607
 * use float chromosome using (1 bit sign)(8 bit signed exponent)(24 bit signed significand)
 * use 23 bit bit string for the chromosome
 * 
 * 
 *
 */
public class Main {
	private static int size = 100;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Please enter an integer to square between 0 and 2^23-1");
		Scanner scan = new Scanner(System.in);
		int s = scan.nextInt();
		System.out.println("population size is " + size);
		System.out.println("Ideal solution found is " + geneticAlgorithm(s));
		scan.close();
	}
	public static int geneticAlgorithm(int s) {
		int target = 0;
		int[] popul = generatePop(size, s);
		int[] select1 = selectBest(popul, s);
		int[] temp1 = new int[select1.length/2];
		int[] temp2 = new int[select1.length/2];
		for(int i = 0; i<select1.length/2;i++) {
			temp1[i]=select1[i];
			temp2[i]=select1[i+temp1.length];
		}
		for(int i=0; i<temp1.length; i++) {
			int[] tmp=cross(select1[i], select1[temp1.length+i]);
			temp1[i]=tmp[0];
			temp2[i]=tmp[1];
		}
		for(int i=0; i<temp1.length; i++) {
			select1[i]=temp1[i];
			select1[i+temp1.length]=temp2[i];
		}
		for(int i=0; i<select1.length; i++) {
			select1[i]=mutate(select1[i]);
		}
		int min=fitnessFunction(s, select1[0]);
		for(int i=1; i<select1.length; i++) {
			if(min<fitnessFunction(s, select1[i])){
				target=select1[i];
			}
		}
		return target;
	}
	public static int[] generatePop(int size, int s) {
		int[] result = new int[size];
		Random rand = new Random();
		
		for(int i=0; i<size; i++) {
			int tmp=rand.nextInt(s);
			if(tmp!=0) {
				result[i]=tmp;
			}
		}
		return result;
	}
	public static int mutate(int src) {
		Random rand = new Random();
		double pm = 0.005;
		int target = src;
		if(Math.random()<pm) {
			boolean[] array = toBinary(src, 23);
			int p1=rand.nextInt(array.length);
			int p2=rand.nextInt(array.length);
			boolean temp=array[p1];
			array[p1]=array[p2];
			array[p2]=temp;
			target=toInt(array);
		}
		return target;
	}
	public static int[] cross(int srcA, int srcB) {
		int[] out = new int[2];
    		int child1=srcA;
    		int child2=srcB;
    		double pc = 0.7;
    		Random rand = new Random();
    		boolean[] arrayA = toBinary(srcA, 23);
    		boolean[] arrayB = toBinary(srcB, 23);
    		int point = 0;
    		if(Math.random()>pc) {
    			point=rand.nextInt(arrayA.length);
    			boolean[] temp = new boolean[arrayA.length];
    			for(int i=point; i<arrayA.length; i++) {
    				temp[i]=arrayA[i];
    				arrayA[i]=arrayB[i];
    				arrayB[i]=temp[i];
    			}
    			child1=toInt(arrayA);
    			child2=toInt(arrayB);
    		}
    		out[0]=child1;
    		out[1]=child2;
    		return out;
	}
	//selects best 1 out of 2 in population
	public static int[] selectBest(int[] pop, int s) {
		int[] target = new int[pop.length/2]; 
		//int min=fitnessFunction(src, pop[0]);
		for(int j =1; j<pop.length; j++) {
			if(fitnessFunction(s, pop[j])<fitnessFunction(s, pop[pop.length-j])) {
				target[j]=pop[j];
			}
		}
		return target;
	}
	//if returns over 10, then fail
	public static int fitnessFunction(int src, float xn) {
		//float check = (float) Math.sqrt(src);
		int index = 0;
		float target=xn;
		while(index<10) {
			target=(target-(src/target))/2;
			index++;
		}
		return index;
		
	}
	public static boolean[] toBinary(int number, int length) {
	    boolean[] ret = new boolean[length];
	    for (int i = 0; i < length; i++) {
	        ret[length - 1 - i] = (1 << i & number) != 0;
	    }
	    return ret;
	}
	public static int toInt(boolean[] src) {
		int target = 0;
		for(int i = 0; i<src.length; i++) {
			if(src[i]==true) {
				target+=(i-src.length)^2;
			}
		}
		return target;
	}

}
