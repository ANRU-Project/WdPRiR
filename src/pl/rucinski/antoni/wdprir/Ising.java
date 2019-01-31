package pl.rucinski.antoni.wdprir;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * model isinga 
 * H = - sum J * s_i s_j - h sum s_i // systarczy ze bedziemy sumowac tylko po najblizszych sasiadach: gora dol lewo prawo
 * zamieniamy losowo wybrany spin sprawdzamy czy zmieniaja stan
 * jezeli delta H < 0 => akceptujemy zamiane stanu  H liczymy tylko dla sąsasiadów
 * delta H > 0 => akceptujemy zamiane stanu z P = e^-beta * delta H
 * robimy rownolegle tak aby zamienicac kilka spinów na raz => tylko nie mozemy rownolegle sasiadów  
 * 
 * 			mam taki pomysl aby losowac z tych ktorzy nie naleza do sasiadów obecnie wykonywanych (getneighbours())
 * 
 * na koniec wykres magnetyzacji (sredni spin) od beta, 
 * parametry J, h, Beta, N
 * periodyczne warunki brzegowe
 * J = 1, -1, h != 0, B - zmienna
 */



public class Ising {
	
	
	
public static void CreateThreads(IsingNet matrix, NetLockers lockers, ExecutorService executor, 
		int changes,
		double J, double h, double B, 
		int rowLen, int colLen) throws InterruptedException {
		
		int numberOfProcessors = Runtime.getRuntime().availableProcessors(); // liczba procesorów
		Runnable[] threads = new Runnable[numberOfProcessors]; // utworzenie tablicy watków o rozmiarze = liczbie procesorów
		CountDownLatch latch = new CountDownLatch(numberOfProcessors);  // tworzymy countdownlatcha dla watków równych liczbie procesorów
					
		
		
		for (int w = 0; w<threads.length; w++) {
			System.out.println(w);
			threads[w] = new Thread() {
				public void run() {
					

					for (int r = 0; r < changes; r ++) {
						//int i =  matrix.randInt(0,rowLen);
						//int j =  matrix.randInt(0,colLen);
						matrix.shiftSpin(matrix.randInt(0,rowLen), matrix.randInt(0,rowLen), J, h, B,lockers);	
					}
					
					System.out.println("wątek zakończony");
					latch.countDown();  // odpala countdwonlatcha w momencie gdy zakonczymy wykonywać wątek
					
				}
			};
		
			
		}
		
		for (int i=0; i<numberOfProcessors; i++) {
			executor.execute(threads[i]); // startujemy watek		
		}
	
		latch.await(); // czekamy az wszystkie watki się wykonają
	}
	

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		WriteToFile file = new WriteToFile("lab2_results.tsv");
		
		double J =  0.1;
		double h = 5;
		//double B = 0;
		
		int changes = 10000;
		
		int rowLen = 100;
		int colLen = 100;		
		IsingNet matrix = new IsingNet(rowLen,colLen); // utworzenie obietu tablicy
		//matrix.setNet(); // wywolanie funcji uzupelniającej obiekt tablicy -1 i 1
		
		NetLockers lockers = new NetLockers(rowLen,colLen); // utworzenie obietu lockersów
		//lockers.resetLocker();
		
		
		int numberOfProcessors = Runtime.getRuntime().availableProcessors(); 
		
		ExecutorService executor = Executors.newFixedThreadPool(numberOfProcessors);
		
		double B = 0.2;
		
		while (B<1000) {
			matrix.setNet(); // wywolanie funcji uzupelniającej obiekt tablicy -1 i 1
			lockers.resetLocker();
			System.out.println("avg sping" + matrix.avarageSpin());
			
			CreateThreads(matrix, lockers, executor, 
					changes,
					J, h, B, 
					rowLen, colLen);  
			
			file.writeToFile(B, matrix.avarageSpin());
			B+=10;
		}
		executor.shutdown();

		//System.out.println("avg sping" + matrix.avarageSpin());
		
		/**
		int i;
		int j;
		
		for (int z = 0; z< 10000; z++) {
			
			//System.out.print("spin: "+ matrix.getSpin(1, 1)+ "\n");
			//System.out.print("lock przed: " + lockers.getLock(1, 1)+ "\n");
			
			i = matrix.randInt(0,rowLen);
			j = matrix.randInt(0,rowLen);
			
			matrix.shiftSpin(i, j, J, h, B,lockers);	// zamiana na przeciwny znak konkretnego spinu
			
			//System.out.print("spin: "+ matrix.getSpin(1, 1)+ "\n");
			//System.out.print("lock po: " + lockers.getLock(1, 1)+ "\n");
			//System.out.print(matrix.getSpin(1, 1)+ "\n");
			
		//wypisanie lockera dla 1,1
		//System.out.print(lockers.getLock(1, 1)+ "\n");

		//System.out.println(matrix.calculateH(1, 1, J, h));
		//System.out.println(matrix.randInt(0,rowLen)); // losowanie liczby
		System.out.println("avg Spin: " +matrix.avarageSpin());	
		}
		**/

	}	
}


