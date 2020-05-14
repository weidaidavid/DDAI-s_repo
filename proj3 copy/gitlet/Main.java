package gitlet;

import java.io.File;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author David Dai
 *  collaborated with Rishabh Chhhabria  and
 *  Sameer Hassen Prabhu in coming up
 *  with data structure to be used as interlinked files.
 */
public class Main {


    /** Current Working Directory file path. */
    static final File CWD = new File(".");
    /** Gitlet directory file path.*/
    static final File GITLET = Utils.join(CWD, ".gitlet");
    /** Reference ID of headpointer which points to the latest commit. */
    static final File HEADPOINTER = Utils.join(GITLET, "headPointer");
    /** Stores a hash that records if files are being tracked. */



    /** length of sha1 ref id. */
    static final int REFIDLEN = 40;
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        } else if (args[0].equals("add")) {
            if (args.length == 2) {
                Commands.add(args[1]);
            } else if (args.length > 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("init")) {
            Commands.init();
        } else if (args[0].equals("commit")) {
            if (args.length == 2) {
                Commands.commit(args[1]);
            } else if (args.length > 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);

            }

        } else if (args[0].equals("log")) {
            if (args.length == 1) {
                Commands.log();
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("global-log")) {
            if (args.length == 1) {
                Commands.globallog();
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("checkout")) {
            if (args.length == 3) {
                if (args[1].equals("--")) {
                    Commands.checkout1(args[2]);
                }
            } else if (args.length == 4) {
                if (args[2].equals("--")) {
                    Commands.checkout2(args[1], args[3]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
            } else {
                if (args.length == 2) {
                    Commands.checkout3(args[1]);
                }

            }
        } else {
            main2(args);
        }
    }
    /** part 2 of main.
     *@param args same args as before*/
    public static void main2(String... args) {
        if (args[0].equals("status")) {
            if (args.length == 1) {
                Commands.status();
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }

        } else if (args[0].equals("find")) {
            if (args.length == 2) {
                Commands.find(args[1]);
            }
        } else if (args[0].equals("rm")) {
            if (args.length == 2) {
                Commands.rm(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }

        } else if (args[0].equals("rm-branch")) {
            if (args.length == 2) {
                Commands.removebranch(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("branch")) {
            if (args.length == 2) {
                Commands.branch(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("global-log")) {
            if (args.length == 1) {
                Commands.globallog();
            }

        } else if (args[0].equals("reset")) {
            if (args.length == 2) {
                Commands.reset(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("merge")) {
            if (args.length == 2) {
                Commands.merge(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);

            }
        } else {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

}
