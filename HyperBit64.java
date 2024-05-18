import java.util.HashSet;
import randomhash.RandomHashFamily;
public class HyperBit64
{
   public static int estimateHB(String[] stream, double alpha, int N)
   {
      RandomHashFamily rhf = new RandomHashFamily(2);

      int M = 64;
      long sketch = 0;
      int count = 0;
      int U = 1; // Power-of-two estimate of substream cardinality
      for (int i = 0; i < N; i++)
      {  
         long[] x = rhf.hashes(stream[i]);
         int k = (int) ((x[0] >> 20) & 0xFFFL) % M;  
//       int k = (int) (x[0] & 0xFFL) % M;  Doesn't work. WHY???
//                  Are low-order bits of x[0] and x[1] correlated??
         if ((x[1] & (U-1)) == (U-1))
         {
              if ((sketch & (1L << k)) == 0) count++;
              sketch = sketch | (1L << k);
         }

         if (count >= alpha*M) 
         {  
            U += U; 
            sketch = 0;
            count = 0;
         }
      }   
      double beta = 1.0 - 1.0*count/M;
      double bias = Math.log(1.0/beta/alpha);
      // No analytic justification for the 0.71 factor
      return (int) (U*M*bias*0.71);
   }

   public static void main(String[] args)
   {
      int N = Integer.parseInt(args[0]);
      int M = Integer.parseInt(args[1]);
      String[] a = new String[N];

      HashSet<String> hset = new HashSet<String>();

      for (int i = 0; i < N; i++)
      {
	  a[i] = StdIn.readLine();
	  hset.add(a[i]);
      }
      StdOut.println("Estimate " + estimateHB(a, 0.5, N));
      StdOut.println("Actual   " + hset.size());
   }
}
