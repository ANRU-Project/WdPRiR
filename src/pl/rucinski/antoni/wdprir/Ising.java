package pl.rucinski.antoni.wdprir;

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
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double J =  2.5;
		double h = 0.02;
		double B = 1;
		
		int rowLen = 1000;
		int colLen = 1000;		
		IsingNet matrix = new IsingNet(rowLen,colLen); // utworzenie obietu tablicy
		matrix.setNet(); // wywolanie funcji uzupelniającej obiekt tablicy -1 i 1
		
		NetLockers lockers = new NetLockers(rowLen,colLen); // utworzenie obietu lockersów
		lockers.resetLocker();
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

	}	
}


