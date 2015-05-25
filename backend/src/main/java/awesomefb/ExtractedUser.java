package awesomefb;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.social.facebook.api.Reference;

/**
 * Created by earl on 5/25/2015.
 */
public class ExtractedUser {
    private String mUserId;
    private String mFacebookId;
    private String mName;

    /**
     * Saves user data into users collection,
     * returns DBObject with newly inserted id and some basic info (FB ID, name)
     * @param reference
     */
    public ExtractedUser(Reference reference) {
        mFacebookId = reference.getId();
        mName = reference.getName();
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        BasicDBObject userObject = new BasicDBObject("fb_id", mFacebookId).append("name", mName);
        mUserId = new ObjectId().toString();
        userObject.put("_id", mUserId);

        System.out.println("[awesomeFb] Inserting basic user data " + mFacebookId);
        databaseManager.insertUser(this);
    }

    public BasicDBObject toDBObject() {
        return new BasicDBObject("user_id", mUserId)
                .append("fb_id", mFacebookId).append("name", mName);
    }
}
