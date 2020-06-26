package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Ziyi
 */
public class MachineTest {
    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;
    private Machine _M;
    /* ***** TESTS ***** */
    @Test
    public void convertTest() {
        Alphabet alpha1 = new Alphabet();
        String s1 = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p1 = new Permutation(s1, alpha1);

        String s2 = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
        Permutation p2 = new Permutation(s2, alpha1);

        String s3 = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";
        Permutation p3 =  new Permutation(s3, alpha1);

        String s4 = "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)";
        Permutation p4 =  new Permutation(s4, alpha1);

        String s5 = "(AVOLDRWFIUQ) (BZKSMNHYC) (EGTJPX)";
        Permutation p5 = new Permutation(s5, alpha1);

        String s6 = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
        Permutation p6 = new Permutation(s6, alpha1);

        String s7 = "(AFNIRLBSQWVXGUZDKMTPCOYJHE)";
        Permutation p7 = new Permutation(s7, alpha1);
        String s81 = "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) ";
        String s82 = "(IJ) (LO) (MP) (RX) (SZ) (TV)";
        String s8 = s81 + s82;
        String s91 = "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) ";
        String s92 = "(IV) (LM) (PW) (QZ) (SX) (UY)";
        String s9 = s91 + s92;
        MovingRotor rotor1 = new MovingRotor("I", p1, "Q");
        MovingRotor rotor2 = new MovingRotor("II", p2, "E");
        MovingRotor rotor3 = new MovingRotor("III", p3, "V");
        MovingRotor rotor4 = new MovingRotor("IV", p4, "J");
        MovingRotor rotor5 = new MovingRotor("V", p5, "Z");
        FixedRotor rotorbeta = new FixedRotor("Beta", p6);
        FixedRotor rotorgamma = new FixedRotor("Gamma", p7);
        Reflector rotorB = new Reflector("B", new Permutation(s8, alpha1));
        Reflector rotorC = new Reflector("C", new Permutation(s9, alpha1));
        Rotor[] allRotors = new Rotor[9];
        allRotors[0] = rotor1;
        allRotors[1] = rotor2;
        allRotors[2] = rotor3;
        allRotors[3] = rotor4;
        allRotors[4] = rotor5;
        allRotors[5] = rotorbeta;
        allRotors[6] = rotorgamma;
        allRotors[7] = rotorB;
        allRotors[8] = rotorC;
        List<Rotor> rotorlist = Arrays.asList(allRotors);
        _M = new Machine(alpha1, 5, 3, rotorlist);
        _M.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        _M.setRotors("AXLE");
        _M.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", alpha1));
        assertEquals(5, _M.numRotors());
        assertEquals(3, _M.numPawls());
        assertEquals(16, _M.convert(5));
        assertEquals(21, _M.convert(17));
    }
}
