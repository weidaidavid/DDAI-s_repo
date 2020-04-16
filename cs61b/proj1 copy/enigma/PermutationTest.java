package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author David Dai
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
    @Test
    public void checkPermute() {
        Permutation permy =
                new Permutation("(AELTPHQXRU) (BKNW)"
                        + " (CMOY) (DFG) (IV) (JZ) (S)",
                UPPER);
        char e = 'E';
        char b = 'B';
        char w = 'W';
        char s = 'S';
        assertEquals('E', permy.permute('A'));
        char a = permy.permute('W');
        assertEquals('B', a);
        assertEquals('S', permy.permute('S'));

    }
    @Test
    public void checkPermInt() {
        Permutation percy =
                new Permutation("(AELTPHQXRU) (BKNW)"
                        + " (CMOY) (DFG) (IV) (JZ) (S)",
                UPPER);
        assertEquals(4, percy.permute(0));

    }

    @Test
    public void testderangement() {
        Permutation percy =
                new Permutation("(AELTPHQXRU) (BKNW)"
                        + " (CMOY) (DFG) (IV) (JZ) (S)",
                UPPER);
        Permutation percival =
                new Permutation("(AELTPHQXRU) (BKNW)"
                        + " (CMOY) (DFG) (IV) (JZS)",
                UPPER);
        Permutation perseus =
                new Permutation("(AELTPHQXRU) (BKNW)"
                        + " (CMOY) (DFG) (IV) (JZ)",
                UPPER);
        assertEquals(true, percival.derangement());
        assertEquals(false, percy.derangement());
        assertEquals(false, perseus.derangement());
    }
    @Test
    public void testInvert() {
        Permutation permy =
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY)"
                        + " (DFG) (IV) (JZ) (S)",
                UPPER);
        assertEquals('A', permy.invert('E'));

        assertEquals('W', permy.invert('B'));
        assertEquals('S', permy.invert('S'));
    }
}
