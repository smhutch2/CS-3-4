
import java.lang.Math.*;
import java.lang.*;
import java.io.*;
import java.util.*;

class whatnum{
	
	static int count = 0;
	//the only numbers that need to be checked by the array
	static int[][] increments = {	{1,3,7,9},
									{2,4,6,8},
									{1,3,7,9},
									{2,4,6,8},
									{5},
									{2,4,6,8},
									{1,3,7,9},
									{2,4,6,8},
									{1,3,7,9},
									{0}	};
	
	public static void main(String args[]){ 
		System.out.println("The number: "+solve()+" in "+count+" checks");
	}
	
	//calculates the subnumber from the number array
	static long calcSub(int current[],  int base, int digit){
		long sum = 0;
		for(int i = 0; i<digit;i++){
			sum += current[i]*(Math.pow(base,digit-i-1));
		}
		return sum;
	}

	//checks if a digit has been used
	static boolean checkUsed(int current[], int number){
		for(int i = 0; i < current.length; i++){
			if(number == current[i]) return true;
		}
		return false;
	}
	
	//runs the iterations
	static boolean iterate(int Current[], int index, int last[]){
		
		//creates and fills new array
		int current[] = new int[10];
		for(int i = 0; i< 10;i++)	current[i]= Current[i];
		
		//sees if it is done
		if(index == 11){
			for(int i = 0; i< 10;i++)	last[i]= current[i];
			return true;
		} 
		
		//checks all the numbers that it needs to based on the index array at the beginning
		for(int i = 0; i < increments[index-1].length;i++){
			int val = increments[index-1][i];
			count++;
			if(!checkUsed(current,val)){
				current[index-1] = val;
				if(calcSub(current,10,index)%index == 0){
					System.out.println("here3 "+calcSub(current,10,index));
					if(iterate(current,index+1,last)){
						System.out.println("here4");
						return true;
					}
				}
			}
		}
		System.out.println("here1");
		return false;
	}
	
	//sets up and calls the recursion
	static long solve(){
		int current[] = new int[10];
		int last[] = new int[10];
  		for(int i = 0; i < current.length;i++){
			current[i] = -1;
		} 
		ArrayList<Integer> used = new ArrayList();
		iterate(current,1,last);
		return calcSub(last, 10, 10);
	}

}