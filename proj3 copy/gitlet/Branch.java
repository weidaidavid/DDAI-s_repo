package gitlet;

import java.io.Serializable;

/** the branch class you wanted.
 * @author David Dai
 */
public class Branch implements Serializable {
    /** commitID.*/
    private String commitID;
    /** name.*/
    private String _name;
    /** current commit.*/
    private Commit current;
    /** what are you looking for.*/
    /** this contructs a branch object.
     * @param name what it sounds like
     * @param commit the commit
     * @param commitID1 the
     */
    @SuppressWarnings("unchecked")
    public Branch(String name, Commit commit, String commitID1) {
        _name = name;
        this.current = commit;
        this.commitID = commitID1;
    }
    /** gets the commit.
     * @return something */
    public Commit getCommit() {
        return current;
    }

    /** Sets the head commit and the corresponding commitID.
     * @param commit head commit.
     * @param commitID1 corresponding ID.
     */
    public void setHeadCommit(Commit commit, String commitID1) {
        current = commit;
        commitID = commitID1;
    }

    /** Returns the name of the current branch.
     * @return name of branch.
     */
    public String getBranchName() {
        return _name;
    }

    /** gets the head commit.
     *
     * @return the head commit
     */
    public Commit getHead() {
        return current;
    }



}
