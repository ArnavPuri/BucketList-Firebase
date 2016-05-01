package in.teachcoder.bucketlist.models;

import com.firebase.client.ServerValue;

import java.util.HashMap;

import in.teachcoder.bucketlist.Constants;

/**
 * Created by Arnav on 01-May-16.
 */
public class BucketCategory {
    String owner,title;
    HashMap<String, Object> createdAt, lastChangedAt;

    public BucketCategory() {

    }

    public BucketCategory(String owner, String title, HashMap<String, Object> createdAt) {
        this.owner = owner;
        this.title = title;
        this.createdAt = createdAt;
        HashMap<String, Object> timeStampNowObject = new HashMap<>();
        timeStampNowObject.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
        this.lastChangedAt = timeStampNowObject;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Object> getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(HashMap<String, Object> createdAt) {
        this.createdAt = createdAt;
    }
}
