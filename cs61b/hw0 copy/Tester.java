import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author YOUR NAMES HERE
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, max(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(69, max(new int[] {1,4,6,8,69,40,13}));
        assertEquals(420, max(new int[] {23,24,8,69,420}));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(threeSum(new int[] {-6,2,5}));
        assertFalse(threeSum(new int[] {-12,5,3}));
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertFalse(threeSumDistinct(new int[] {-6, 2, 5}));
        assertTrue(threeSumDistinct(new int[] {8, 2, -1, -1, 15}));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
