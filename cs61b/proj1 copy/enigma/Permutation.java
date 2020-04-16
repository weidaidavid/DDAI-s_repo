package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author David Dai
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cyc = cycles;
        for (int i = 0; i < cyc.length(); i++) {
            if (cyc.charAt(cyc.length() - 1) != ')') {
                throw error("now that's just bad form");
            }
            char checker = cyc.charAt(i);
            if (checker == '(') {
                int j = i + 1;
                while (cyc.charAt(j) !=  ')') {
                    char stripes = cyc.charAt(j);
                    if (stripes == '(') {
                        throw error("Yo bad form bro");
                    }
                    j++;
                }
                i = j;
            }

        }
        _cycles = new char[0][];
        for (int i = 0; i < cyc.length(); i++) {
            if (cyc.charAt(i) == '(') {
                int j = i + 1;
                while (cyc.charAt(j) != ')') {
                    j++;
                }
                String umm = cyc.substring(i + 1, j);
                addCycle(umm);
                i = j;

            }

        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char[][] nowwww = new char[_cycles.length + 1][];
        System.arraycopy(_cycles, 0, nowwww, 0, _cycles.length);
        nowwww[nowwww.length - 1] = cycle.toCharArray();
        _cycles = nowwww;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int what = wrap(p);
        char work = _alphabet.toChar(what);
        return _alphabet.toInt(permute(work));

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int what = wrap(c);
        char work = _alphabet.toChar(what);
        return _alphabet.toInt(invert(work));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length; j++) {
                if (_cycles[i].length == 1 && _cycles[i][j] == p) {
                    return p;
                } else if (p == _cycles[i][j] && j == _cycles[i].length - 1) {
                    return _cycles[i][0];
                } else {
                    if (_cycles[i][j] == p) {
                        return _cycles[i][j + 1];
                    }
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length; j++) {
                if (_cycles[i].length == 1 && _cycles[i][j] == c) {
                    return c;
                } else if (c == _cycles[i][j] && j == 0) {
                    return _cycles[i][_cycles[i].length - 1];
                } else {
                    if (_cycles[i][j] == c) {
                        return _cycles[i][j - 1];
                    }
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int sum = 0;
        for (int i = 0; i < _cycles.length; i++) {
            sum += _cycles[i].length;
            if (_cycles[i].length == 1) {
                return false;
            }
        }
        if (sum < _alphabet.size()) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** 2d array containing cycles. */
    private char[][] _cycles;
    /** string containing cycles before they are inputted into _cycles. */
    private String cyc;
}
