package awesomefb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookFocusedCrawler {
    private DatabaseManager mDatabaseManager;

    public FacebookFocusedCrawler() {
        mDatabaseManager = DatabaseManager.getInstance();
    }

    public void run() {
        String rootPage = "yannews";

        // List of pages to crawl
        Queue<String> queue = new LinkedList<String>();
        queue.add(rootPage);

        // List of pages crawled
        List<String> crawledPages = new ArrayList<String>();

        while (true) {
            // Get page id from queue
            String pageId = queue.remove();
            // Call request and get basic page info (id, name)
            Page page = new Page(pageId);
            pageId = page.getName();

            // If that page exists and is not processed
            if (page.getId() != null && !crawledPages.contains(pageId)) {
                System.out.println("[awesomeFb] Processing page " + pageId);
                List<String> pageLikes = page.getLikes();
                queue.addAll(pageLikes.stream().filter(like -> !crawledPages.contains(like)).collect(Collectors.toList()));
                // Get feed as JSON array
                JSONArray feed = page.getPosts(pageId);

                if (feed != null) {
                    int count = 0;
                    for (int i = 0; i < feed.length(); i++) {
                        // Extract each post's data from feed
                        JSONObject pageObject = feed.getJSONObject(i);
                        // Skip if post does not contain message
                        if (!pageObject.has("message")) continue;
                        Post post = new Post(pageObject);

                        // Get list of comments as JSON array
                        List<Comment> comments = post.getComments();
                        if (comments != null) {
                            for (Comment comment : comments) {
                                User commentCreator = comment.getCreator();
                                // If comment creator is page, add it to queue
                                if (commentCreator.isPage()) {
                                    queue.add(commentCreator.getFacebookId());
                                }
                            }
                        }

                        // Save post data to database
                        mDatabaseManager.insertPost(post);
                        count++;
                    }
                    System.out.println("[awesomeFb] " + count + " posts processed.");
                }

                // Mark page id as processed
                crawledPages.add(pageId);
            }

            if (queue.isEmpty()) {
                break;
            }
        }
    }
}
