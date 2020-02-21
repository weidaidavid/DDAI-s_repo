package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author David Dai FIXME
*/

public class ArraysTest {
    /**
     Test for C1
     */
    @Test
    public void CatenateTest() {
        int [] some_nat_nums = {2,3,4,5,6};
        int [] some_prime_nums = {2,3,5,7};
        int [] even_nums = {2,4,6,8,10};
        int [] nat_n_primes = {2,3,4,5,6,2,3,5,7};
        int [] even_n_primes = {2,4,6,8,10,2,3,5,7};
        assertArrayEquals(nat_n_primes, Arrays.catenate(some_nat_nums, some_prime_nums));
        assertArrayEquals(even_n_primes, Arrays.catenate(even_nums,some_prime_nums));
        assertArrayEquals(even_nums, Arrays.catenate(even_nums,null));
        assertArrayEquals(even_nums, Arrays.catenate(null, even_nums));
        assertArrayEquals(null, Arrays.catenate(null,null));
    }

    @Test
    public void RemoveTest() {
        int[] a = {1,2,3,4,5,6,7,8};
        int [] b = {1,2,6,7,8};
        int [] c = {1,3,2,4,8};
        int[] d  = {1,2,4,8};
        assertArrayEquals(b,  Arrays.remove(a,3,3));
        assertArrayEquals(d, Arrays.remove(c,2,1));
    }
    @Test
    public void Natural_Runs_test() {
        int[] TI = {1,2,3,4,5,1,2,3,4,4,3,4,5,};
        int[][] CUN = {{1,2,3,4,5},{1,2,3,4},{4},{3,4,5}};
        assertArrayEquals(CUN, Arrays.naturalRuns(TI));

    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
