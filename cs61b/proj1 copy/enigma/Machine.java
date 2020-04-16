package enigma;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author David Dai
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numPawls = pawls;
        _numRotors = numRotors;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotorSlots = new Rotor[rotors.length];

        for (int i = 0; i < rotors.length; i++) {
            boolean A = false;
            for (Rotor r : _allRotors) {
                if (rotors[i].equals(r.name())) {
                    A = true;
                    _rotorSlots[i] = r;
                    _rotorSlots[i].set(0);
                }
            }
            if (!A) {
                throw error(" bad bad bad");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotorSlots[i + 1].set(setting.charAt(i));
        }
    }
    /** Set my rotors according to RSETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor Rsetting (not counting the reflector).  */
    void setRotorsRingstellung(String rsetting) {
        for (int i = 0; i < rsetting.length(); i++) {
            _rotorSlots[i + 1].setRingstalun(rsetting.charAt(i));
        }
    }


    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int input = c;
        if (_plugboard != null) {
            input = _plugboard.permute(c);
        }

        for (int i = _numRotors - _numPawls; i < _rotorSlots.length - 2; i++) {
            if (_rotorSlots[i + 1].atNotch()) {
                _rotorSlots[i].advance();
                _rotorSlots[i + 1].advance();
                i++;
            } else {
                continue;
            }
        }
        if (_rotorSlots[_rotorSlots.length - 1].atNotch()) {
            _rotorSlots[_rotorSlots.length - 2].advance();
        }
        _rotorSlots[_rotorSlots.length - 1].advance();


        int whatYouWant = input;
        for (int i = _rotorSlots.length - 1; i >= 0; i -= 1) {
            int bluh = _rotorSlots[i].convertForward(whatYouWant);
            whatYouWant = bluh;
        }
        for (int i = 1; i < _rotorSlots.length; i++) {
            int bluh = _rotorSlots[i].convertBackward(whatYouWant);
            whatYouWant = bluh;
        }
        if (_plugboard != null) {
            whatYouWant = _plugboard.invert(whatYouWant);
        }
        return whatYouWant;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String mSg = msg.replaceAll(" ", "");
        String msgs = "";
        for (int i = 0; i < mSg.length(); i++) {
            msgs += _alphabet.toChar(convert(_alphabet.toInt(mSg.charAt(i))));
        }
        return msgs;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** number of rotors. */
    private int _numRotors;
    /** number of pawls/moving rotors. */
    private int _numPawls;
    /** a collection of rotors/ later in the form of an array list. */
    private Collection<Rotor> _allRotors;
    /** array of rotors for the machine. */
    private Rotor[] _rotorSlots;
    /** the plug board permutation to permute
     *  any inputs matching what's in plugboard. */
    private Permutation _plugboard;

}
