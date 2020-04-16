package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author David Dai
 */
class Alphabet {

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        _chars = chars;
        _charArray = new char[chars.length()];
        _size = 0;
        for (int k = 0; k < _chars.length(); k++) {
            _charArray[k] = _chars.charAt(k);
            _size += 1;
        }
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return _size;
    }

    /**
     * Returns true if CH is in this alphabet.
     */
    boolean contains(char ch) {
        for (int i = 0; i < _size; i++) {
            if (_charArray[i] == ch) {
                return true;
            }
        }
        return false;
    }

    /**private String getChars() {
        return _chars;
    }
    */
    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        return _charArray[index];
    }

    /**
     * Returns the index of character CH which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        int yep = 0;
        for (int i = 0; i < _size; i++) {
            if (ch == _charArray[i]) {
                yep = i;
            }
        }
        return yep;
    }
    /** characters you wanna use. */
    private String _chars;
    /** all the characters put into an array. */
    private char[] _charArray;
    /** the size of the alphabet. */
    private int _size;
}

