package in.teachcoder.bucketlist.models;

import com.firebase.client.ServerValue;

import java.util.HashMap;

import in.teachcoder.bucketlist.Constants;

/**
 * Created by Arnav on 01-May-16.
 */
public class BucketCategoryItem {
    String owner,title;
    HashMap<String, Object> createdAt, lastChangedAt;

    public BucketCategoryItem(String owner, String title, HashMap<String, Object> createdAt) {
        this.owner = owner;
        this.title = title;
        this.createdAt = createdAt;
        HashMap<String, Object> timeStampNowObject = new HashMap<>();
        timeStampNowObject.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
        this.lastChangedAt = timeStampNowObject;
    }
}
