package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ziyi
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles.trim();
        _cyclelist = cycles.split(" ");


    }
    /** Return false if parenthethis get wrong in cycles. */
    boolean checkparen() {
        if (!_cycles.equals("")) {
            char char2 = _cycles.charAt(_cycles.length() - 1);
            if (_cycles.charAt(0) != '(' || (char2 != ')')) {
                return false;
            }
            int count1 = 0;
            int count2 = 0;
            for (String str : _cyclelist) {
                for (int i = 0; i < str.length(); i++) {
                    char char1 = str.charAt(i);
                    boolean boo1 = _alphabet.contains(char1);
                    if (char1 == '(') {
                        count1++;
                    }
                    if (char1 == ')') {
                        count2++;
                    }
                    if (i != 0 && i != str.length() - 1 && !boo1) {
                        return false;
                    }
                }
            }
            if (count1 != count2) {
                return false;
            }
        }
        return true;
    }
    /** Return false if duplicate is wrong in cycles. */
    boolean checkduplicate() {
        for (String str : _cyclelist) {
            for (int i = 0; i < str.length(); i++) {
                for (int j = i + 1; j < str.length(); j++) {
                    if (str.charAt(i) == str.charAt(j)) {
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < _cycles.length(); i++) {
            for (int j = i + 1; j < _cycles.length(); j++) {
                if (_cycles.charAt(i) == _cycles.charAt(j)) {
                    if (_cycles.charAt(i) != '(' && _cycles.charAt(i) !=  ')') {
                        if (_cycles.charAt(i) != ' ') {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            for (int j = i + 1; j < cycle.length(); j++) {
                if (cycle.charAt(i) == cycle.charAt(j)) {
                    throw EnigmaException.error("addcycle error");
                }
            }
        }
        for (int i = 0; i < cycle.length(); i++) {
            if (_cycles.indexOf(cycle.charAt(i)) >= 0) {
                throw EnigmaException.error("addcycle error");
            }
        }
        char char2 = cycle.charAt(cycle.length() - 1);
        if ((cycle.charAt(0) != '(') || (char2 != ')')) {
            throw EnigmaException.error("addcycle error");
        }
        _cycles = _cycles + cycle;

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
        int index = wrap(p);
        char newchar = permute(_alphabet.toChar(index));
        return _alphabet.toInt(newchar);

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int index = wrap(c);
        char newchar = invert(_alphabet.toChar(index));
        return _alphabet.toInt(newchar);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        String curstr = String.valueOf(p);
        int curindex = _cycles.indexOf(curstr);
        int index = 0;
        char result = p;
        int n1 = curindex + 1;
        if (_cycles.indexOf(curstr) > 0 && _cycles.charAt(n1) == ')') {
            for (int i = 0; i <= curindex + 1; i++) {
                if (_cycles.charAt(i) == '(') {
                    index = i;
                }
            }
            result = _cycles.charAt(index + 1);
        } else if (_cycles.indexOf(curstr) > 0) {
            if (_cycles.indexOf(_cycles.charAt(curindex + 1)) > 0) {
                result = _cycles.charAt(curindex + 1);
            }
        }
        return result;

    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        String curstr = String.valueOf(c);
        int curind = _cycles.indexOf(curstr);
        int index = 0;
        char result = c;

        for (int i = curind; i < _cycles.length(); i++) {
            if (i > 0 && _cycles.charAt(i) == ')') {
                index = i;
                break;
            }
        }
        if (_cycles.indexOf(curstr) > 0 && _cycles.charAt(curind - 1) == '(') {
            result = _cycles.charAt(index - 1);
        } else if (_cycles.indexOf(curstr) > 0) {
            if (_cycles.indexOf(_cycles.charAt(curind - 1)) > 0) {
                result = _cycles.charAt(curind - 1);
            }
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _cycles.length(); i++) {
            char char1 = _cycles.charAt(i);
            char char2 = _cycles.charAt(i - 1);
            char char3 = _cycles.charAt(i + 1);
            if (_alphabet.contains(char1) && (char2 == '(') && (char3 == ')')) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** cycles. */
    private String _cycles;
    /** list of cycles. */
    private String[] _cyclelist;

}
