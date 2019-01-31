package pl.rucinski.antoni.wdprir;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IsingNet {
	private int [][] array;
	
	public IsingNet(int rowLen, int colLen){
		this.array = new int [rowLen][colLen];
		
	}
	
	
	
	
	/**
	 * wygenerowanie siatki uzupelnionej 1 i -1
	 * @param rowLen
	 * @param colLen
	 * @return
	 */
	
	public void setNet(){
		Random generator = new Random();
		
		for(int i = 0; i < array.length; i++)
		    for(int j = 0; j < array.length; j++)
		        array[i][j] =  2*(Math.abs(generator.nextInt())% 2)-1;
	}
	
	public double avarageSpin() {
		double avgSpin = 0;
		System.out.println("AL: "+ array.length);
		for(int i = 0; i < array.length; i++)
		    for(int j = 0; j < array.length; j++)
		        avgSpin += array[i][j];
		
		return avgSpin/(array.length*array.length);
	}
	
	
	
	public int randInt(int min, int max) {
		int randomInt = ThreadLocalRandom.current().nextInt(min, max);
		//System.out.print(randomInt + "\n");
		return randomInt;
	}
	
	public int getSpin(int i, int j) {
		return array[i][j];
	}
	
	/**
	 * zamiana spinu na przeciwny 
	 * @param i
	 * @param j
	 * @param lockers
	 */
	public void shiftSpin(int i, int j, double J, double h, double B, NetLockers lockers) {
		//System.out.println("h: " + h);
		double P;
		double H_old = calculateH(i, j, J, h);
		//System.out.println("H_o: " + H_old);
		if (lockers.setLock(i, j) == true) { // założenie zamka na dany spin + sasiadów 
			array[i][j] = array[i][j]*(-1); // jeżeli udało się założyć wszystkie zamki => zamiana spinu
		}
		else {
			//System.out.println("!!!");
			return;
		}
		
		double H_new = calculateH(i, j, J, h);
		//System.out.println("H_n: " + H_new);
		
		double delta_H = H_new - H_old;
		//System.out.println("delta_h: " + delta_H);
		
		if (delta_H > 0) { // jezeli H > 0 sprawdzamy czy zachowujemy dany stan
			
			P =  1 - Math.pow(Math.E, ((-1)*B*delta_H));
			
			//System.out.println("P; " + P);
			//System.out.println("(delta_H); " + (delta_H));
			//System.out.println("(B); " + B);
			//System.out.println("(-B*delta_H); " + ((-1)*B*delta_H));
			
			//System.out.println("(Math.pow(Math.E, (-B*delta_H)); " + Math.pow(Math.E, (-B*delta_H)));
			
			if (Math.random() < P ) {	// powracamy do poprzedniego stanu z P = 1 - exp^(-B*delta H)
				System.out.println("powrót do poprzedniego stanu");
				array[i][j] = array[i][j]*(-1);
			}
		}
		else {
			System.out.println("zostaje stan");
		}
		//System.out.print("lock w trakcie: " + lockers.getLock(i, j)+ "\n");
		lockers.removeLock(i, j);
		//System.out.print("lock po: " + lockers.getLock(i, j)+ "\n");
	}
	
	/**
	 * liczenie H
	 * @param i
	 * @param j
	 */
	public double calculateH(int i, int j, double J, double h) {
		double H = 0;
		//System.out.println("i: " + i);
		//System.out.println("i: " + j);
		
		int [] ii = {i, i-1, i+1, i, i}; // tablica wspolrzednych i-towych
		int [] jj = {j, j, j, j-1, j+1}; // tablica wspolrzednych j-towych
		
		/**
		for(int k = 0; k< 5 ; k++) {
			System.out.println(k + " "+ii[k] + " " + jj[k]);
			//System.out.println(array[ii[k]][jj[k]]);
		}
		**/
		
		/* 0: dany punkt
		 * 1: sasiad po lewej
		 * 2: sasiad po prawej
		 * 3: sasiad na gorze
		 * 4: sasiad na dole
		 */
		
		// periodyczne warunki brzegowe
		if(i == 0) {
			ii[1] = array.length-1; // sasiad po lewej
		}
		else if(i == array.length-1) {
			ii[2] = 0; // sasiad po prawej
		}
		
		if(j == 0) {
			jj[3] = array.length-1; // sasiad na gorze
		}
		else if(j == array.length-1) {
			jj[4] = 0;  // sasiad na dole
		}
		
		
		for(int k = 1; k< 5 ; k++) {
			//System.out.println(ii[k] + " " + jj[k]);
			//System.out.println(array[ii[k]][jj[k]]);
			H += (-1)*J * array[ii[0]][jj[0]] * array[ii[k]][jj[k]];
			//System.out.println("H0:" +H);
		}
		//System.out.println("H1:" +H);
		//for(int k = 0; k< 5 ; k++) {
		H += (-1) * h *  array[ii[0]][jj[0]];
		//System.out.println("H2:" +H);
		//}
		
		return H;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
