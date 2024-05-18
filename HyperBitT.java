import java.util.HashSet;
import randomhash.RandomHashFamily;
public class HyperBitT
{
   public static int estimateHB(String[] stream, 
                              int N, int M, int T)
   {
      // Assumes M = 64, 128, 256, 512, 1024, 2048, or 4096
      RandomHashFamily rhf = new RandomHashFamily(2);

      int twoT = (int) Math.pow(2, T);
      long[] sketch = new long [M/64];
      for (int j = 0; j < M/64; j++)
         sketch[j] = 0;
      int count = 0;
      for (int i = 0; i < N; i++)
      {  
         long[] x = rhf.hashes(stream[i]);
         int k = (int) ((x[0] >> 20) & 0xFFFL) % M;  

         // Probability of == is the same as > (!)
         if ((x[1] & (twoT-1)) == (twoT-1))
         {
            if ((sketch[k/64] & (1L << (k % 64))) == 0) count++;
            sketch[k/64] = sketch[k/64] | (1L << (k % 64));
         }
      }
      double beta = 1.0 - 1.0*count/M;
      double bias = Math.log(1.0/beta);
      return (int) (twoT*M*bias);
   }

   public static void main(String[] args)
   {
      int S = Integer.parseInt(args[0]);
      int M = Integer.parseInt(args[1]);
      int T = Integer.parseInt(args[2]);
      String[] a = new String[S];

      HashSet<String> hset = new HashSet<String>();

      for (int i = 0; i < S; i++)
      {
	  a[i] = StdIn.readLine();
	  hset.add(a[i]);
      }
      int Nhat = estimateHB(a, S, M, T);
      StdOut.println("Estimate " + Nhat);
      int N = hset.size();
      StdOut.println("Actual   " + N);

      double beta = Math.exp(-1.0*Nhat/M/Math.pow(2, T));
//      StdOut.println("beta " +  beta);

      double f1 = Math.sqrt(1.0/beta - 1);
      double f2 = Math.sqrt(M);
      double f3 = Math.log(1.0/beta);
//      StdOut.println(f1 + " " + f2 + " " + f3);

      double acchat = f1/f2/f3;
//      StdOut.println("Estimated error " + acchat);

      double acc = Math.abs(1.0*(N - Nhat))/N;
//      StdOut.println("Actual error   " + acc ); 

      StdOut.printf("%d %d %6.3f %6.3f\n", (int) (M*beta), Nhat, acchat, acc);

   }
}
