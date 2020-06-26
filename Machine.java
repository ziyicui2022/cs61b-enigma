package enigma;


import java.util.Collection;


import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ziyi
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray();
        _rotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.length; j++) {
                if (rotors[i].equals(((Rotor) _allRotors[j]).name())) {
                    _rotors[i] = (Rotor) _allRotors[j];
                }
            }
        }
        if (_rotors.length != rotors.length) {
            throw new EnigmaException("Misnamed rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _rotors.length - 1) {
            throw new EnigmaException("Initial positions string wrong length");
        }
        for (int i = 1; i < _rotors.length; i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException("Initial string not in alphabet");
            }
            _rotors[i].set(setting.charAt(i - 1)); }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** rotor_advance. */
    private void rotorsAdvance() {
        boolean isFastMoved = false;
        Rotor alwaysMove = _rotors[_rotors.length - 1];
        for (int i = _rotors.length - 1; i >= 1; i--) {
            if (_rotors[i].atNotch() && _rotors[i - 1].rotates()) {
                if (_rotors[i] == alwaysMove) {
                    isFastMoved = true;
                }
                _rotors[i].advance();
                _rotors[i - 1].advance();
            }
        }
        if (!isFastMoved) {
            alwaysMove.advance();
        }
    }
    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        rotorsAdvance();
        c = _plugboard.permute(c);
        for (int i = _rotors.length - 1; i >= 0; i--) {
            c = _rotors[i].convertForward(c);
        }
        for (int i = 1; i < _rotors.length; i++) {
            c = _rotors[i].convertBackward(c);
        }
        c = _plugboard.permute(c);
        return c;
    }
    /** Return access my rotors.
     * @param x */
    Rotor getRotor(int x) {
        return _rotors[x];
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.toUpperCase().replaceAll(" ", "");
        for (int i = 0; i < msg.length(); i++) {
            if (_alphabet.contains(msg.charAt(i))) {
                int num = _alphabet.toInt(msg.charAt(i));
                char converted = _alphabet.toChar(convert(num));
                result += converted;
            } else {
                result = result + msg.charAt(i);
            }

        }
        return result;

    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** number if rotors. */
    private int _numRotors;
    /** number of pawls. */
    private int _pawls;
    /** collection of all rotors. */
    private Object[]  _allRotors;
    /** plugboard. */
    private Permutation _plugboard;
    /** rotor list. */
    private Rotor[] _rotors;

}
