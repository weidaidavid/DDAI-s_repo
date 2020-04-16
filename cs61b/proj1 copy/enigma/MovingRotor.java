package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author David Dai
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches.toCharArray();
        _perm = perm;

    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(_perm.wrap(setting() + 1));
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length; i++) {
            if (_perm.alphabet().toChar(setting()) == _notches[i]) {
                return true;
            }
        }
        return false;
    }
    /** permutation used for moving motors. */
    private Permutation _perm;
    /** the notches of the rotor. */
    private char[] _notches;

}
