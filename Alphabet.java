package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ziyi
 */
class Alphabet {
    /** chars. */
    private String _chars;
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        this._chars = chars;
        if (!checkalphaduplicate()) {
            throw EnigmaException.error("alphabet duplicate error");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Return alphabet duplication check. */
    boolean checkalphaduplicate() {
        for (int i = 0; i < _chars.length(); i++) {
            for (int j = i + 1; j < _chars.length(); j++) {
                if (_chars.charAt(i) == _chars.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }
    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {

        for (int i = 0; i < size(); i++) {
            if (_chars.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {

        return _chars.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _chars.indexOf(String.valueOf(ch));
    }

}
