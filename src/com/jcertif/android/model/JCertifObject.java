
package com.jcertif.android.model;

/**
 *
 * @author
 *  
 */
public class JCertifObject {
   
    int version;
    boolean deleted;

    public JCertifObject() {
    }

    public JCertifObject(int version, boolean deleted) {
        this.version = version;
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
