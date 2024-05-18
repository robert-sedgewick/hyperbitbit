import java.util.HashSet;
import randomhash.RandomHashFamily;
public class HyperBitBit64
{
   public static int estimateHBB64(String[] stream, int N)
   {
      RandomHashFamily rhf = new RandomHashFamily(2);

      int M = 64;
      long sketch1 = 0;
      long sketch2 = 0;
      int count1 = 0;
      int count2 = 0;
      int U = 1; // Power-of-two estimate of substream cardinality
      for (int i = 0; i < N; i++)
      {  
         long[] x = rhf.hashes(stream[i]);
         int k = (int) ((x[0] >> 20) & 0xFFFL) % M;  
//       int k = (int) (x[0] & 0xFFL) % M;  Doesn't work. WHY???
//                  Are low-order bits of x[0] and x[1] correlated??
         if ((x[1] & (U-1)) == (U-1))
         {
              if ((sketch1 & (1L << k)) == 0) count1++;
              sketch1 = sketch1 | (1L << k);
         }

         if ((x[1] & (4*U-1)) == (4*U-1))
         {
              if ((sketch2 & (1L << k)) == 0) count2++;
              sketch2 = sketch2 | (1L << k);
         }

         if (count1 >= 62) 
         {  
            U = 4*U; 
            sketch1 = sketch2; count1 = count2;
            sketch2 = 0; count2 = 0;
         }
      }   
      double beta = 1.0 - 1.0*count1/M;
      double bias = 1.1*Math.log(1.0/beta);
      return (int) (U*M*bias);
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
      StdOut.println("Estimate " + estimateHBB64(a, N));
      StdOut.println("Actual   " + hset.size());
   }
}
