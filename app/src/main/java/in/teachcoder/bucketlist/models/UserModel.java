package in.teachcoder.bucketlist.models;

import com.firebase.client.ServerValue;

import java.util.HashMap;

import in.teachcoder.bucketlist.Constants;

/**
 * Created by Arnav on 03-May-16.
 */
public class UserModel {
    String name, email;
    HashMap<String, Object> timestampJoined;

    public UserModel() {
    }

    public UserModel(String name, String email, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public void setTimestampJoined(HashMap<String, Object> timestampJoined) {
        this.timestampJoined = timestampJoined;
    }
}
