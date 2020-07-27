package gitlet;

import java.io.File;
import java.io.Serializable;


import static gitlet.Main.GITLET;
/** heard you wanted a java doc.
 * @author David Dai*/

@SuppressWarnings("unchecked")
/** blob class stores the data.*/
public class Blob implements Serializable {
    /** Stores a hashmap from blob reference ID to the contents of the files.*/
    static final File BLOBHASH = Utils.join(GITLET, "blobHash");
    /** the sha1 id.*/
    private String _sha;
    /** the data in byte array form.*/
    private byte[] data;

    /** constructs blob object.
     * @param data1 stores the data.
     */
    public Blob(byte[] data1) {
        data = data1;
        _sha = Utils.sha1(Utils.serialize(this));


    }

    /** gets you the byte array.
     *
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }
    /** gets you the sha1..
     *
     * @return the sha1
     */
    public String getSHA1() {
        return _sha;
    }
}
