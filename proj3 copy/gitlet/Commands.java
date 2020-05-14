package gitlet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.Main.*;
import static gitlet.Utils.*;
import static gitlet.Blob.BLOBHASH;
import static gitlet.Commit.COMMITHASH;


/** the commands class we deserve.
 * @author david
 *
 */
public class Commands {

    /**
     * stagingArea directory file path.
     */
    static final File STAGINGAREA = Utils.join(GITLET, "stagingArea");
    /** current branch we're on. */
    static final File CURRBRANCH = Utils.join(GITLET, "currBranch");
    /**
     * Staging area --> forAddition directory file path.
     */
    static final File ADDITION = Utils.join(STAGINGAREA, "forAddition");
    /**
     * Staging area --> forRemoval directory file path.
     */
    static final File REMOVAL = Utils.join(STAGINGAREA, "forRemoval");
    /**
     * Stores a hashmap from branch name to branch pointer (commit).
     */
    static final File BRANCHES = Utils.join(GITLET, "branchMap");
    /** intializes the directories and necessary documents.*/
    @SuppressWarnings("unchecked")
    public static void init() {
        if (GITLET.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory. ");
            System.exit(0);
        }
        GITLET.mkdir();
        STAGINGAREA.mkdir();
        HashMap<String, byte[]> blobs = new HashMap<String, byte[]>();
        Utils.writeObject(BLOBHASH, blobs);
        HashMap<String, Commit> allcommits = new HashMap<String, Commit>();
        Utils.writeObject(COMMITHASH, allcommits);
        HashMap<String, String> stage = new HashMap<String, String>();
        HashMap<String, String> stage1 = new HashMap<String, String>();
        Utils.writeObject(ADDITION, stage);
        Utils.writeObject(REMOVAL, stage1);
        Commit initial = new Commit();
        String initID = initial.getID();
        HashMap<String, Commit> githash =
                Utils.readObject(COMMITHASH, HashMap.class);
        githash.put(initID, initial);
        Utils.writeObject(COMMITHASH, githash);
        HashMap<String, Branch> branches = new HashMap<String, Branch>();
        Branch master = new Branch("master", initial, initID);
        branches.put("master", master);
        Utils.writeObject(BRANCHES, branches);
        Utils.writeObject(CURRBRANCH, master);



    }

    /** creates a commit object and links it to the necessary
     * files pertinent to the data structure.
     *
     * @param message the commit message
     */
    @SuppressWarnings("unchecked")
    public static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit latest = Utils.readObject(CURRBRANCH, Branch.class).getCommit();
        HashMap<String, String> parentblobs = latest.getBlobs();
        HashMap<String, String> thestage =
                Utils.readObject(ADDITION, HashMap.class);
        HashMap<String, String> gtfo = Utils.readObject(REMOVAL, HashMap.class);
        if (thestage.isEmpty() && gtfo.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        for (String key : parentblobs.keySet()) {
            if (!thestage.containsKey(key)) {
                thestage.put(key, parentblobs.get(key));
            }
        }
        for (String key1 : gtfo.keySet()) {
            if (thestage.containsKey(key1)) {
                thestage.remove(key1);
            }
        }
        Branch thabrnach = Utils.readObject(CURRBRANCH, Branch.class);

        Commit thelatest =
                new Commit(message, latest.getID(), "no", thestage);
        Utils.writeObject(HEADPOINTER, thelatest);
        HashMap<String, Commit> githash =
                Utils.readObject(COMMITHASH, HashMap.class);
        githash.put(thelatest.getID(), thelatest);
        Utils.writeObject(COMMITHASH, githash);
        thabrnach.setHeadCommit(thelatest, thelatest.getID());
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        branches.put(thabrnach.getBranchName(), thabrnach);
        writeObject(BRANCHES, branches);
        writeObject(ADDITION, new HashMap<String, String>());
        writeObject(REMOVAL, new HashMap<String, String>());
        writeObject(CURRBRANCH, thabrnach);

    }

    /** my neat trick to deal with special merge commits.
     *
     * @param message commit message
     * @param mergeparent sha1 of merge parent
     */
    @SuppressWarnings("unchecked")
    public static void commit2(String message, String mergeparent) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit latest = Utils.readObject(CURRBRANCH, Branch.class).getCommit();
        HashMap<String, String> parentblobs = latest.getBlobs();
        HashMap<String, String> thestage =
                Utils.readObject(ADDITION, HashMap.class);
        HashMap<String, String> gtfo = Utils.readObject(REMOVAL, HashMap.class);
        if (thestage.isEmpty() && gtfo.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        for (String key : parentblobs.keySet()) {
            if (!thestage.containsKey(key)) {
                thestage.put(key, parentblobs.get(key));
            }
        }
        for (String key1 : gtfo.keySet()) {
            if (thestage.containsKey(key1)) {
                thestage.remove(key1);
            }
        }
        Branch thabrnach = Utils.readObject(CURRBRANCH, Branch.class);

        Commit thelatest =
                new Commit(message, latest.getID(), mergeparent, thestage);
        Utils.writeObject(HEADPOINTER, thelatest);
        HashMap<String, Commit> githash =
                Utils.readObject(COMMITHASH, HashMap.class);
        githash.put(thelatest.getID(), thelatest);
        Utils.writeObject(COMMITHASH, githash);
        thabrnach.setHeadCommit(thelatest, thelatest.getID());
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        branches.put(thabrnach.getBranchName(), thabrnach);
        writeObject(BRANCHES, branches);
        writeObject(ADDITION, new HashMap<String, String>());
        writeObject(REMOVAL, new HashMap<String, String>());
        writeObject(CURRBRANCH, thabrnach);
    }

    /** adds file to staging area and gets rid of it from the removal area.
     *
     * @param file file name
     */
    @SuppressWarnings("unchecked")
    public static void add(String file) {
        File workingFile = Utils.join(CWD, file);
        HashMap<String, byte[]> thaBlob =
                Utils.readObject(BLOBHASH, HashMap.class);
        HashMap<String, String> stage =
                Utils.readObject(ADDITION, HashMap.class);
        HashMap<String, String> stageRem =
                Utils.readObject(REMOVAL, HashMap.class);
        Commit current = Utils.readObject(CURRBRANCH, Branch.class).getCommit();

        if (!workingFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            HashMap<String, String> blobs = current.getBlobs();
            Blob theses = new Blob(Utils.readContents(workingFile));
            String thesesSha1 = theses.getSHA1();
            if (!thaBlob.containsKey(thesesSha1)) {
                thaBlob.put(thesesSha1, theses.getData());
                Utils.writeObject(BLOBHASH, thaBlob);
            }
            if (blobs.containsKey(file)) {
                if (blobs.get(file).equals(thesesSha1)) {
                    stageRem.remove(file);
                    Utils.writeObject(REMOVAL, stageRem);
                } else {
                    stage.put(file, thesesSha1);
                    Utils.writeObject(ADDITION, stage);
                }
            } else {
                stage.put(file, thesesSha1);
                writeObject(ADDITION, stage);
            }


        }

    }
    /** prints log of past commits in special format. */
    @SuppressWarnings("unchecked")
    public static void log() {
        Commit current = Utils.readObject(CURRBRANCH, Branch.class).getCommit();
        HashMap<String, Commit> commithas =
                readObject(COMMITHASH, HashMap.class);
        String sha1 = current.getID();
        while (sha1 != null) {
            System.out.println("===");
            System.out.println("commit " + current.getID());
            if (!current.getMergeparent().equals("no")) {
                System.out.println("Merge: "
                        + current.getParent().substring(0, 7)
                        + " " + current.getMergeparent().substring(0, 7));
            }
            System.out.println("Date: " + current.getTime());
            System.out.println(current.getMessage());
            System.out.println();
            current = commithas.get(current.getParent());
            if (current != null) {
                sha1 = current.getID();
            } else {
                break;
            }


        }


    }
    /** like log but for all commits as opposed to
     * just the ones on the current branch. */
    @SuppressWarnings("unchecked")
    public static void globallog() {
        HashMap<String, Commit> commithas =
                Utils.readObject(COMMITHASH, HashMap.class);
        for (String key : commithas.keySet()) {
            Commit current = commithas.get(key);
            System.out.println("===");
            System.out.println("commit " + current.getID());
            if (!current.getMergeparent().equals("no")) {
                System.out.println("Merge: "
                        + current.getParent().substring(0, 7)
                        + " " + current.getMergeparent().substring(0, 7));
            }
            System.out.println("Date: " + current.getTime());
            System.out.println(current.getMessage());
            System.out.println();
        }
    }

    /** checks out the file.
     *
     * @param file file name
     */
    @SuppressWarnings("unchecked")
    public static void checkout1(String file) {
        HashMap<String, String> bloby =
                readObject(CURRBRANCH, Branch.class).getCommit().getBlobs();
        HashMap<String, String> blobbert =
                Utils.readObject(BLOBHASH, HashMap.class);
        if (!bloby.containsKey(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {

            File deinterest = Utils.join(CWD, file);
            Utils.writeContents(deinterest, blobbert.get(bloby.get(file)));
        }

    }

    /** checks out the commit.
     *
     * @param cmmitid id of commit
     * @param name name of commit
     */
    @SuppressWarnings("unchecked")
    public static void checkout2(String cmmitid, String name) {
        HashMap<String, String> bobby =
                Utils.readObject(BLOBHASH, HashMap.class);
        HashMap<String, Commit> commithash =
                readObject(COMMITHASH, HashMap.class);
        String cmmitid1 = cmmitid;
        for (String key : commithash.keySet()) {
            if (key.contains(cmmitid)) {
                cmmitid1 = key;

            }
        }
        if (!commithash.containsKey(cmmitid1)) {
            System.out.println("No commit with that ID exists");
            System.exit(0);
        } else {
            Commit blobsy = commithash.get(cmmitid1);
            HashMap<String, String> blobs = blobsy.getBlobs();
            if (!blobs.containsKey(name)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
            File deinterest = Utils.join(CWD, name);
            Utils.writeContents(deinterest, bobby.get(blobs.get(name)));

        }

    }

    /** checks out a branch.
     *
     * @param name name of branch
     */
    @SuppressWarnings("unchecked")
    public static void checkout3(String name) {
        HashMap<String, byte[]> theblobs = readObject(BLOBHASH, HashMap.class);
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        Branch current = readObject(CURRBRANCH, Branch.class);
        if (!branches.containsKey(name)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        Branch checkout = branches.get(name);
        if (checkout.getBranchName().equals(current.getBranchName())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit currcommit = current.getCommit();
        HashMap<String, String> currblob = currcommit.getBlobs();
        Commit checkoutcommit = checkout.getCommit();
        HashMap<String, String> checkoutblobs = checkoutcommit.getBlobs();
        List<String> allfiles = plainFilenamesIn(CWD);
        for (String file : allfiles) {
            if (!currblob.containsKey(file)
                    && checkoutblobs.containsKey(file)) {
                System.out.println("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        for (String filename : checkoutblobs.keySet()) {
            File fille = join(CWD, filename);
            writeContents(fille, theblobs.get(checkoutblobs.get(filename)));
        }
        writeObject(CURRBRANCH, checkout);
        for (String key : currblob.keySet()) {
            if (!checkoutblobs.containsKey(key)) {
                join(CWD, key).delete();
            }
        }
        writeObject(ADDITION, new HashMap<String, String>());

    }
    /** gets you what you want.
     * @param message message*/
    @SuppressWarnings("unchecked")
    public static void find(String message) {
        HashMap<String, Commit> commithash =
                readObject(COMMITHASH, HashMap.class);
        boolean whatever = false;
        for (String key : commithash.keySet()) {
            Commit check = commithash.get(key);
            String msg = check.getMessage();
            if (msg.equals(message)) {
                whatever = true;
                String id = check.getID();
                System.out.println(id);
            }
        }
        if (!whatever) {
            System.out.println(
                    "Found no commit with that message.");
            System.exit(0);
        }
    }
    /** gets you the status of the diff folders. */
    @SuppressWarnings("unchecked")
    public static void status() {
        if (GITLET.exists()) {
            System.out.println("=== Branches ===");
            HashMap<String, Branch> brnaches =
                    Utils.readObject(BRANCHES, HashMap.class);
            ArrayList<String> branch = new ArrayList<String>();
            for (String key : brnaches.keySet()) {
                String aga =
                        readObject(CURRBRANCH, Branch.class).getBranchName();
                if (key.equals(aga)) {
                    String output = "*" + key;
                    branch.add(output);
                } else {
                    branch.add(key);
                }
            }
            Collections.sort(branch);
            for (String brancho : branch) {
                System.out.println(brancho);
            }
            System.out.println();
            System.out.println("=== Staged Files ===");
            HashMap<String, String> staged =
                    Utils.readObject(ADDITION, HashMap.class);
            ArrayList<String> added = new ArrayList<String>(staged.keySet());
            Collections.sort(added);
            for (String add : added) {
                System.out.println(add);
            }
            System.out.println();
            System.out.println("=== Removed Files ===");
            HashMap<String, String> removed =
                    Utils.readObject(REMOVAL, HashMap.class);
            ArrayList<String> removal =
                    new ArrayList<String>(removed.keySet());
            Collections.sort(removal);
            for (String remov : removal) {
                System.out.println(remov);
            }
            System.out.println();
            System.out.println("=== Modifications Not Staged For Commit ===");
            System.out.println();
            System.out.println("=== Untracked Files ===");
            System.out.println();

        } else {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /** creates a new branch.
     *
     * @param name name of the branch you want to create
     */
    @SuppressWarnings("unchecked")
    public static void branch(String name) {
        Commit curr = Utils.readObject(CURRBRANCH, Branch.class).getCommit();
        Branch newone = new Branch(name, curr, curr.getID());
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        if (branches.containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branches.put(newone.getBranchName(), newone);
        writeObject(BRANCHES, branches);

    }

    /** puts desired file on removal area and gets it out of the CWD.
     *
     * @param name name of file you don't fuck with anymore
     */
    @SuppressWarnings("unchecked")
    public static void rm(String name) {
        boolean a = true;
        boolean b = true;
        HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
        HashMap<String, String> remove = readObject(REMOVAL, HashMap.class);
        Commit current = Utils.readObject(CURRBRANCH, Branch.class).getCommit();
        HashMap<String, String> blobs = current.getBlobs();
        if (stage.containsKey(name)) {
            a = false;
            stage.remove(name);
            writeObject(ADDITION, stage);
        }
        if (blobs.containsKey(name)) {
            b = false;
            remove.put(name, blobs.get(name));
            writeObject(REMOVAL, remove);
            if (Utils.join(CWD, name).exists()) {
                Utils.join(CWD, name).delete();
            }
        }
        if (a && b) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }


    }

    /** gets rid of the branch.
     *
     * @param name name of branch you won't be fucking with no more
     */
    @SuppressWarnings("unchecked")
    public static void removebranch(String name) {
        Branch current = Utils.readObject(CURRBRANCH, Branch.class);
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        if (current.getBranchName().equals(name)) {
            System.out.println(" Cannot remove the current branch.");
            System.exit(0);
        }
        if (!branches.containsKey(name)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        branches.remove(name);
        writeObject(BRANCHES, branches);

    }

    /** reset to a specific commit.
     *
     * @param cmmtid commit id of commit you want to reset back to
     */
    @SuppressWarnings("unchecked")
    public static void reset(String cmmtid) {
        HashMap<String, Commit> thecommits =
                readObject(COMMITHASH, HashMap.class);
        if (!thecommits.containsKey(cmmtid)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit dacommit = thecommits.get(cmmtid);
        HashMap<String, String> blobs = dacommit.getBlobs();
        Commit curr = readObject(CURRBRANCH, Branch.class).getCommit();
        HashMap<String, String> curblobs = curr.getBlobs();
        List<String> allfiles = Utils.plainFilenamesIn(CWD);
        for (String file : allfiles) {
            if ((!curblobs.containsKey(file)) && blobs.containsKey(file)) {
                System.out.println("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        for (String key : blobs.keySet()) {
            checkout2(cmmtid, key);
        }

        for (String key : curblobs.keySet()) {
            if (!blobs.containsKey(key)) {
                Utils.join(CWD, key).delete();
            }
        }
        Branch branchnow = readObject(CURRBRANCH, Branch.class);
        branchnow.setHeadCommit(dacommit, dacommit.getID());
        writeObject(CURRBRANCH, branchnow);
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        branches.put(branchnow.getBranchName(), branchnow);
        writeObject(BRANCHES, branches);
        HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
        stage.clear();
        writeObject(ADDITION, stage);


    }

    /** finds the latest common ancestor of 2 commits.
     *
     * @param rancho the first commit
     * @param mancho the second commit
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String sppt(Commit rancho, Commit mancho) {
        ArrayList<String> candidate = new ArrayList<String>();
        HashMap<String, Commit> commits = readObject(COMMITHASH, HashMap.class);
        Commit temp = rancho;
        Commit cataclysm;
        while (temp != null) {
            candidate.add(temp.getID());
            temp = commits.get(temp.getParent());
        }
        Commit temp1 = mancho;
        while (temp1 != null) {
            if (candidate.contains(temp1.getMergeparent())) {
                return temp1.getMergeparent();
            } else if (candidate.contains(temp1.getID())) {
                return temp1.getID();
            }
            temp1 = commits.get(temp1.getParent());
        }
        return temp1.getID();

    }

    /** a function i probably wont finish in time.
     *
     * @param branchname what it sounds like
     */
    @SuppressWarnings("unchecked")
    public static void merge(String branchname) {
        boolean mc = false; mergeerrors(branchname);
        Branch given = getBr(branchname); Branch current = getCbr();
        HashMap<String, Commit> commits = readObject(COMMITHASH, HashMap.class);
        Commit sp = commits.get(sppt(current.getCommit(),
                        given.getCommit()));
        Commit currentcommit = current.getCommit();
        Commit givencommit = given.getCommit();
        HashMap<String, String> bc = currentcommit.getBlobs();
        HashMap<String, String> bg = givencommit.getBlobs();
        HashMap<String, String> bs = sp.getBlobs();

        for (String key : bc.keySet()) {
            String c = bs.get(key); String d = bc.get(key);
            Boolean a = bs.containsKey(key);
            String z = bg.get(key);
            if (!bg.containsKey(key) && d.equals(c)) {
                rm(key);
            }
            if (a && !d.equals(c) && !bg.containsKey(key)) {
                mc = true; cr(bc, bg, key, false, true);
            }
            if (bg.containsKey(key)) {
                mc = true;
                if (a && !d.equals(c) && !z.equals(c) && !z.equals(d)) {
                    cr(bc, bg, key, false, false);
                } else if (!a && !z.equals(d)) {
                    cr(bc, bg, key, false, false);
                }
            }
        }
        for (String key : bg.keySet()) {
            String a = bs.get(key); String b = bg.get(key);
            Boolean c = bg.containsKey(key); String d = bs.get(key);
            Boolean e = bs.containsKey(key);
            if (bc.containsKey(key)) {
                if (!b.equals(a) && bc.get(key).equals(a)) {
                    checkout2(givencommit.getID(), key);
                    HashMap<String, String> stage =
                            readObject(ADDITION, HashMap.class);
                    stage.put(key, b); writeObject(ADDITION, stage);
                }
            } else if (c && !e && !bc.containsKey(key)) {
                checkout2(givencommit.getID(), key);
                HashMap<String, String> stage =
                        readObject(ADDITION, HashMap.class);
                stage.put(key, b); writeObject(ADDITION, stage);
            } else if (c && !d.equals(b) && e && !bc.containsKey(key)) {
                mc = true; cr(bc, bg, key, true, false);
            }
        }
        if (mc) {
            System.out.println("Encountered a merge conflict.");
        }
        commit2("Merged " + given.getBranchName() + " into "
                + current.getBranchName() + ".", givencommit.getID());


    }


    /** takes in branch name and check for errors.
     * @param branchname branch name*/
    @SuppressWarnings("unchecked")
    public static void mergeerrors(String branchname) {
        HashMap<String, Branch> branches = readObject(BRANCHES, HashMap.class);
        Branch given = branches.get(branchname);
        Branch current = readObject(CURRBRANCH, Branch.class);
        HashMap<String, Commit> commits = readObject(COMMITHASH, HashMap.class);
        Commit splitpoint =
                commits.get(sppt(current.getCommit(),
                        given.getCommit()));
        if (splitpoint.getID().equals(given.getCommit().getID())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        }
        if (splitpoint.getID().equals(current.getCommit().getID())) {
            checkout3(given.getBranchName());
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        if (!branches.containsKey(branchname)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        Commit currentcommit = current.getCommit();
        Commit givencommit = given.getCommit();
        List<String> allfiles = plainFilenamesIn(CWD);
        for (String file : allfiles) {
            if (!currentcommit.getBlobs().containsKey(file)
                    && givencommit.getBlobs().containsKey(file)) {
                System.out.println("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        if (current.getBranchName().equals(given.getBranchName())) {
            System.out.println(" Cannot merge a branch with itself.");
            System.exit(0);
        }
        HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
        if (!stage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }

    /** resolves merge conflicts.
     *
     * @param currblob current blobs
     * @param givenblob given blobs
     * @param key file we're on
     * @param thisone if special conflict
     * @param thatone if special conflict
     */
    @SuppressWarnings("unchecked")
    public static void cr(
            HashMap<String, String> currblob,
            HashMap<String, String> givenblob,
            String key, Boolean thisone,
            Boolean thatone) {
        HashMap<String, byte[]> allblobs = readObject(BLOBHASH, HashMap.class);
        if (thisone) {
            String currstring = "";
            String givenstring = new String(allblobs.get(givenblob.get(key)),
                    StandardCharsets.UTF_8);
            String contents = "<<<<<<< HEAD\n"
                    + currstring + "=======\n" + givenstring + ">>>>>>>\n";
            byte[] forblob = contents.
                    getBytes(StandardCharsets.UTF_8);
            Blob newcontent = new Blob(forblob);
            allblobs.put(newcontent.getSHA1(), newcontent.getData());
            writeObject(BLOBHASH, allblobs);
            File newfile = Utils.join(CWD, key);
            writeContents(newfile, contents);
            HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
            stage.put(key, newcontent.getSHA1());
            writeObject(ADDITION, stage);
        }
        if (thatone) {
            String currstring =
                    new String(allblobs.get(currblob.get(key)),
                            StandardCharsets.UTF_8);
            String givenstring = "";
            String contents = "<<<<<<< HEAD\n"
                    + currstring + "=======\n" + givenstring + ">>>>>>>\n";
            byte[] forblob = contents.
                    getBytes(StandardCharsets.UTF_8);
            Blob newcontent = new Blob(forblob);
            allblobs.put(newcontent.getSHA1(), newcontent.getData());
            writeObject(BLOBHASH, allblobs);
            HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
            File newfile = Utils.join(CWD, key);
            writeContents(newfile, contents);
            stage.put(key, newcontent.getSHA1());
            writeObject(ADDITION, stage);
        } else {
            String currstring = new String(allblobs.
                    get(currblob.get(key)), StandardCharsets.UTF_8);
            String givenstring = new String(allblobs.
                    get(givenblob.get(key)), StandardCharsets.UTF_8);
            String contents = "<<<<<<< HEAD\n"
                    + currstring + "=======\n" + givenstring + ">>>>>>>\n";
            byte[] forblob = contents.getBytes(StandardCharsets.UTF_8);
            Blob newcontent = new Blob(forblob);
            allblobs.put(newcontent.getSHA1(), newcontent.getData());
            writeObject(BLOBHASH, allblobs);
            HashMap<String, String> stage = readObject(ADDITION, HashMap.class);
            File newfile = Utils.join(CWD, key);
            writeContents(newfile, contents);
            stage.put(key, newcontent.getSHA1());
            writeObject(ADDITION, stage);
        }



    }

    /** gets you a branch.
     *
     * @param name name
     * @return a branch
     */
    @SuppressWarnings("unchecked")
    public static Branch getBr(String name) {
        HashMap<String, Branch> branches =
                readObject(BRANCHES, HashMap.class);
        return branches.get(name);
    }

    /** gets you the current branch.
     *
     * @return the current branch
     */
    @SuppressWarnings("unchecked")
    public static Branch getCbr() {
        return readObject(CURRBRANCH, Branch.class);
    }

}


