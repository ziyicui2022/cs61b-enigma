package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;


/**
 *  Enigma simulator.
 *
 * @author Ziyi Cui
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * return.
     * @param args the args.
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * return.
     * @param name the name.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * return.
     * @param name the name.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * process.
     */
    private void process() {
        Machine M = readConfig();
        boolean b1 = false;
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            boolean b2 = next.startsWith("*");
            if (b2) {
                b1 = setUp(M, next);
            } else if (b1) {
                printMessageLine(M.convert(next));
            }
        }
    }

    /**
     * return.
     */
    private Machine readConfig() {
        try {
            String alpha = _config.nextLine().trim().toUpperCase();
            _alphabet = new Alphabet(alpha);
            int numrotor = _config.nextInt();
            int nummoving = _config.nextInt();
            ArrayList<Rotor> rotorarr = new ArrayList<>();
            while (_config.hasNext()) {
                Rotor r1 = readRotor();
                rotorarr.add(r1);
            }
            Machine m1 = new Machine(_alphabet, numrotor, nummoving,
                    rotorarr);
            return m1;
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * return.
     */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String notch = _config.next();
            String cycle1 = "";
            while (_config.hasNext("\\s*[(].*[)]")) {
                cycle1 = cycle1.concat(_config.next() + " ");
            }
            Permutation perm = new Permutation(cycle1, _alphabet);
            if (notch.startsWith("M")) {
                return new MovingRotor(name, perm, notch.substring(1));
            } else if (notch.startsWith("N")) {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * return.
     * @param M the machine.
     * @param settings the settings.
     */
    private boolean setUp(Machine M, String settings) {
        boolean b1 = false;
        String[] settinglist = settings.trim().split(" ");
        String[] rotors = new String[M.numRotors()];
        String steckered = "";
        for (int i = 1; i < settinglist.length; i = i + 1) {
            if (i <= M.numRotors()) {
                rotors[i - 1] = settinglist[i];
                if (b1 || i % M.numRotors() == 0) {
                    b1 = true;
                } else {
                    b1 = false;
                }
            } else if (i > M.numRotors() + 1) {
                steckered = steckered.concat(settinglist[i] + " ");
                b1 = b1 | M.numRotors() < settinglist.length;
            }
            if (b1 || i > M.numRotors()) {
                b1 = true;
            } else {
                b1 = false;
            }
        }
        for (int i = 0; i < rotors.length - 1; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("Repeated Rotor");
                }
            }
        }
        M.insertRotors(rotors);
        if (!M.getRotor(0).reflecting()) {
            throw new EnigmaException("First Rotor should be a reflector");
        }
        M.setRotors(settinglist[M.numRotors() + 1]);
        M.setPlugboard(new Permutation(steckered, _alphabet));
        return b1;
    }

    /**
     * @param msg the msg.
     */
    private void printMessageLine(String msg) {
        char[] chararr = msg.toCharArray();
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < chararr.length; i++) {
            if ((i - 5) >= 0) {
                if ((i - 5) % 5 == 0) {
                    s1.append(" ");
                }
            }
            s1.append(chararr[i]).charAt(0);
        }
        _output.print(s1 + "\n" + "");
    }

    /**
     * alaphabet.
     */
    private Alphabet _alphabet;

    /**
     * input.
     */
    private Scanner _input;

    /**
     * config.
     */
    private Scanner _config;

    /**
     * output.
     */
    private PrintStream _output;
}
