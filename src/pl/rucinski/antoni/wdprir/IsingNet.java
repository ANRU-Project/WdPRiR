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
		    for(int j = 0; j < array[i].length; j++)
		        array[i][j] =  2*(Math.abs(generator.nextInt())% 2)-1;
	}
	
	public double avarageSpin() {
		double avgSpin = 0;
		for(int i = 0; i < array.length; i++)
		    for(int j = 0; j < array[i].length; j++)
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
	public void shiftSpin(int i, int j, double J, double h, double B,NetLockers lockers) {
		if (lockers.setLock(i, j) == true) { // założenie zamka na dany spin + sasiadów 
			array[i][j] = array[i][j]*(-1); // jeżeli udało się założyć wszystkie zamki => zamiana spinu
		}
		
		double H = calculateH(i, j, J, h);
		if (H > 0) { // jezeli H > 0 sprawdzamy czy zachowujemy dany stan
			if (Math.random() < (1 - Math.pow(Math.E, (-B*H))) ) {	// powracamy do poprzedniego stanu z P = 1 - exp^(-B*H)
				System.out.println("powrót do poprzedniego stanu");
				array[i][j] = array[i][j]*(-1);
			}
		}
		System.out.print("lock w trakcie: " + lockers.getLock(1, 1)+ "\n");
		lockers.removeLock(i, j);
		
	}
	
	/**
	 * liczenie H
	 * @param i
	 * @param j
	 */
	public double calculateH(int i, int j, double J, double h) {
		double H = 0;
		
		int [] ii = {i, i-1, i+1, i, i}; // tablica wspolrzednych i-towych
		int [] jj = {j, j, j, j-1, j+1}; // tablica wspolrzednych j-towych
		
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
			H += -J * array[0][0] * array[ii[k]][jj[k]];
		}
		
		for(int k = 0; k< 5 ; k++) {
			H += -h *  array[ii[k]][jj[k]];
		}
		
		return H;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
