package io.sim;

public class Rec {

	public static void main(String[] args) {


		double[] y = new double[] { 0,	22.45,	15.08,	3.32,	1.64,	6.98,	7.01,	5.81,	2.95,	1.98,	65};

		double[] v = new double[] { 0,	5.159090909,	8.094545455,	8.300606061,	2.091313131,	0.686464646,
				0.818080808,	0.882727273,	4.633838384,	0.504646465,	0};

		double[][] A = new double[][] {{ 1, 1, 1, 1, 1, 1, 1 ,1 ,1 ,1 ,-1}};

		Reconciliation rec = new Reconciliation(y, v, A);
		System.out.println("Y_hat:");
		rec.printMatrix(rec.getReconciledFlow());
	}


}
