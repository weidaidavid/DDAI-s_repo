package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {
    /** FIXME
     */
    @Test

    public void naturalRuns_test(){
        int [][] bro = {{1,3,5,7},{5},{5,7,9}};
       IntListList broski = IntListList.list(bro);
       int [] bruv = {1,3,5,7,5,5,7,9};
       IntList bruh = IntList.list(bruv);

      assertEquals(broski, Lists.naturalRuns(bruh));
    }
    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
