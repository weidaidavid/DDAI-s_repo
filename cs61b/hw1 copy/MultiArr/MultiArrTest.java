import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] Broseph = {{1,2,3,4},{6,7,8,9}};
        assertEquals(9,MultiArr.maxValue(Broseph));
    }

    @Test
    public void testAllRowSums() {
        int[] test_sums = {10,30};
        int[][] Broseph = {{1,2,3,4},{6,7,8,9}};
        assertEquals(new int[]{10, 30}[0], MultiArr.allRowSums(Broseph)[0]);
        assertEquals(new int[]{10, 30}[1], MultiArr.allRowSums(Broseph)[1]);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
