package pl.rucinski.antoni.wdprir;

import java.util.concurrent.atomic.AtomicInteger;


public class NetLockers {

	private AtomicInteger [][] array;
	
	public NetLockers(int rowLen, int colLen){
		this.array = new AtomicInteger [rowLen][colLen];
		
	}
	
	
	
	
	/**
	 * wygenerowanie siatki uzupelnionej 1 i -1
	 * @param rowLen
	 * @param colLen
	 * @return
	 */
	
	public void resetLocker(){
		for(int i = 0; i < array.length; i++)
		    for(int j = 0; j < array.length; j++)
		        array[i][j] = new AtomicInteger(0);
	}
	
	
	
	public int getLock(int i, int j) {
		// sprwadza ij element - 0 mozna zmienic 1 nie mozna zmienic; range -- liczba sasiadów
		return array[i][j].get();
	}
	
	public Boolean setLock(int i, int j) {
		
		Boolean flag = true;
		int [] ii = {i, i-1, i+1, i, i}; // tablica wspolrzednych i-towych
		int [] jj = {j, j, j, j-1, j+1}; // tablica wspolrzednych j-towych
		
		/* 0: dany punkt
		 * 1: sasiad po lewej
		 * 2: sasiad po prawej
		 * 3: sasiad na gorze
		 * 4: sasiad na dole
		 */
		
		//System.out.println(j);
		//System.out.println(array.length);
		
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
		
		// ustawienie lockerów na ij i na sasiadach
		for (int k= 0; k<5; k++) {
			if (array[ii[k]][jj[k]].compareAndSet(0, 1)) {} // sprawdz czy jest 0, jezeli jest to zmien na 1
			else { // jezeli nie jest zero
				flag = false; // niepowodzenie blokowania => ustawienie flagi na false
				
				for(int l = 0; l < k; l++) {
					array[ii[l]][jj[l]].compareAndSet(1,0); // wycofaj wszystkie zmiany, które zostaly wprowadzone 
				}
				break; // przerwanie lockowania dalszych sasiadów
			}
		}
		return flag;
	}
				
		/**
		if  (array[i][j].compareAndSet(0,1)) { 
			if (array[i-1][j].compareAndSet(0,1)) {
				if (array[i+1][j].compareAndSet(0,1)) {
					if (array[i][j+1].compareAndSet(0,1)) {
						if (array[i][j-1].compareAndSet(0,1)) {}
						else {
							array[i][j].compareAndSet(1,0);
							array[i-1][j].compareAndSet(1,0);
							array[i+1][j].compareAndSet(1,0);
							array[i][j+1].compareAndSet(1,0);
						}
					}
					else {
						array[i][j].compareAndSet(1,0);
						array[i-1][j].compareAndSet(1,0);
						array[i+1][j].compareAndSet(1,0);
					}
					
				}
				else {
					array[i][j].compareAndSet(1,0);
					array[i-1][j].compareAndSet(1,0);
				}	
			}
			else {
				array[i][j].compareAndSet(1,0); // odblokowanie zamku ktory zablokowalismy
			}	
		}	
		**/	
	
	/**
	 * usunięcie zamków na ij pozycji + najblizszych sasiadach ij
	 * @param i
	 * @param j
	 */
	public void removeLock(int i, int j) {
		
		
		
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
		
		// ustawienie lockerów na ij i na sasiadach
		for (int k= 0; k<5; k++) {
			array[ii[k]][jj[k]].compareAndSet(1,0); 
		}
		
		
		//array[i][j].compareAndSet(1,0); // sprawdzenie czy jest 1 i zdjęcie lockera na i,j na 0
		//array[i-1][j].compareAndSet(1,0); // zdjęcie lockera na i-1, j
		//array[i+1][j].compareAndSet(1,0);
		//array[i][j+1].compareAndSet(1,0);
		//array[i][j-1].compareAndSet(1,0);	
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
