package in.teachcoder.bucketlist;

/**
 * Created by Arnav on 30-Apr-16.
 */
public class Constants {
    public static final String FIREBASE_LOCATION_BUCKETLIST_CATEGORY = "bucketListCategory";
    public static final String FIREBASE_LOCATION_BUCKETLIST_ITEMS = "bucketListItems";
    public static final String FIREBASE_LOCATION_BUCKETLIST_USERS = "users";


    public static final String FIREBASE_BASE_URL = "https://bucketlist0607.firebaseio.com/";


    public static final String FIREBASE_CATEGORIES_URL = FIREBASE_BASE_URL + FIREBASE_LOCATION_BUCKETLIST_CATEGORY;
    public static final String FIREBASE_ITEMS_URL = FIREBASE_BASE_URL + FIREBASE_LOCATION_BUCKETLIST_ITEMS;
    public static final String FIREBASE_USER_URL = FIREBASE_BASE_URL + FIREBASE_LOCATION_BUCKETLIST_USERS;


    public static final String FIREBASE_TIMESTAMP_PROPERTY = "timestamp";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";

    public static final String KEY_ID = "key";
    public static final String GOOGLE_ID = "GOOGLE_EMAIL";
    public static final String KEY_PROVIDER = "PROVIDER";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";

    public static final String PASSWORD_PROVIDER = "password";
    public static final String GOOGLE_PROVIDER = "google";

    public static String updateEmail(String defaultEmail){
        return defaultEmail.replace('.', ',');
    }
}
