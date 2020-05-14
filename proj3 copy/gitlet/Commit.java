package gitlet;
import java.io.File;
import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import static gitlet.Main.GITLET;

/** the commit class you deserve. */
/** the commit class you deserve.
 * @author David Dai*/
public class Commit implements Serializable {
    /** Stores a hashmap from commit reference ID to the commit object.*/
    static final File COMMITHASH = Utils.join(GITLET, "commitHash");
    /** commit message. */
    private String _message;
    /** the sha1 id.*/
    private String _sha1;
    /**the blobs.*/
    private HashMap<String, String> _blobs;
    /** the sha1 of the parent. */
    private String _parent;
    /** the merge parent.*/
    private String _mergeparent;
    /** time stamp. */
    private String _time;
/** makes a new commit.
 * @param message message for commit
 * @param parent sha1 of parent commit
 * @param mergeparent sha1 of merge-parent
 * @param blobs hash of files in commit name to blob sha1*/
    @SuppressWarnings("unchecked")
    public Commit(String message, String parent,
                  String mergeparent, HashMap<String, String> blobs) {
        this._message = message;
        this._blobs = blobs;
        this._parent = parent;
        _mergeparent = mergeparent;
        this._sha1 = Utils.sha1(Utils.serialize(this));
        Date dateObj = new Date();
        SimpleDateFormat form = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
        this._time = form.format(dateObj) + " -0600";

    }
    /** inital commit.*/
    public Commit() {
        this._blobs = new HashMap<>();
        this._time = "Wed Dec 31 18:00:00 1969 -0600";
        this._parent = null;
        _message = "initial commit";
        _sha1 = Utils.sha1(Utils.serialize(this));
        _mergeparent = "no";


    }
    /** gets the sha1.
     * @return the sha1 id */
    public String getID() {
        return _sha1;
    }
    /** gets the blobs.
     * @return  the blobs*/
    public HashMap<String, String> getBlobs() {
        return _blobs;
    }
    /** gets the parent sha1.
     * @return _parent */
    public String getParent() {
        return _parent;
    }
    /** gets the commit message.
     * @return the message*/
    public String getMessage() {
        return _message;
    }
    /** gets the timestamp.
     * @return the timestamp*/
    public String getTime() {
        return _time;
    }
    /** gets the merge parent.
     * @return the merge parent*/
    public String getMergeparent() {
        return _mergeparent;
    }
}


