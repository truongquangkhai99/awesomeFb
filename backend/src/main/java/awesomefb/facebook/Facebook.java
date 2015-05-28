package awesomefb.facebook;

import awesomefb.utils.JsonReader;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by earl on 5/25/2015.
 */
public class Facebook {
    private static Facebook instance = null;
    private final String API_ENDPOINT = "https://graph.facebook.com/v2.3/";
    private final String
            ACCESS_TOKEN = "CAAJK2NXosZAABAPjvopDAeoFgmBFZB98ixFdop1sFGiBKsZB2ZAZCrPgVein9u6lxxG28fDHELyvE1Unyvmi2XSkaG5wuIflAYTc2vp7DT4xkD6j9lFN1l60la0sVsLbPRysxbOV6kmbAKpLPXbvza4Rb1bW5k9gu0ZBZARbI4ZBcUYwlb5Wat2OhZC4zaqEoXZCDSLAy6CTZBSvXtNujkGRipF";

    public static Facebook getInstance() {
        if (instance == null) {
            instance = new Facebook();
        }
        return instance;
    }

    protected Facebook() {}

    public JSONObject request(String node, Object params) {
        String url = API_ENDPOINT + node + "?";
        if (params != null) {
            url += params + "&";
        }
        url += "access_token=" + ACCESS_TOKEN;

        try {
            JSONObject obj = JsonReader.readJsonFromUrl(url);
            return obj;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}