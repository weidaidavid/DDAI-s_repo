package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author David Dai
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
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

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _M = readConfig();
        if (!_input.hasNext("\\*")) {
            throw error("ha you thought");
        } else {
            while (_input.hasNextLine()) {
                String yup = _input.nextLine();
                if (yup.isBlank()) {
                    _output.println();
                } else {
                    if (yup.trim().charAt(0) == '*') {
                        setUp(_M, yup.trim());
                    } else {
                        String result = _M.convert(yup);
                        printMessageLine(result);
                    }
                }
            }

        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {

            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            _allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }


            return new Machine(_alphabet, numRotors, pawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String typeNnotches = _config.next();
            String type = typeNnotches.substring(0, 1);

            String notches = typeNnotches.substring(1);
            String cycles = _config.nextLine().trim();
            while (_config.hasNext("\\(.+\\)")) {
                cycles += _config.next();
            }
            if (type.equals("M")) {
                return new MovingRotor(name,
                        new Permutation(cycles, _alphabet), notches);
            } else if (type.equals("N")) {
                return new Rotor(name, new Permutation(cycles, _alphabet));

            } else if (type.equals("R")) {
                return new Reflector(name, new Permutation(cycles, _alphabet));

            } else {
                throw error("this ain't the rotor you be looking for");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int count = M.numRotors();
        String[] rotors = new String[count];
        Scanner theStuff = new Scanner(settings);
        if (!theStuff.hasNext("\\*")) {
            throw error("enigma error");
        } else {
            theStuff.next();
            for (int i = 0; i < count; i++) {
                rotors[i] = theStuff.next();
            }
            M.insertRotors(rotors);
            String setting = theStuff.next();
            M.setRotors(setting);
            if ((!(theStuff.hasNext("\\(.+\\)"))) && theStuff.hasNext()) {
                M.setRotorsRingstellung(theStuff.next());
            }
            String plugboard = "";
            while (theStuff.hasNext()) {
                plugboard += theStuff.next();
            }
            if (!plugboard.equals("")) {
                M.setPlugboard(new Permutation(plugboard, _alphabet));
            }

        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        String temp = msg;
        while (!temp.equals("")) {
            if (temp.length() >= 5) {
                result += temp.substring(0, 5) + " ";
                temp = temp.substring(5);
            } else {
                result += temp;
                temp = "";
            }
        }
        result = result.trim();
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** Machine generated. */
    private Machine _M;
    /** array of all rotors from config. */
    private ArrayList<Rotor> _allRotors;
}
