package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 6/1/2017.
 */
@Table(name = "Post")
public class Post extends Model {
    // Declare table name as public
    public static final String TABLE_NAME = "Post";

    // Declare all column names private
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_POST_TITLE = "post_title";
    private static final String COLUMN_DETAIL = "details";
    private static final String COLUMN_BANNER_IMAGE = "banner_image";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";
    private static final String COLUMN_PUBLISH_DATE_FROM = "publish_date_from";
    private static final String COLUMN_CATEGORY_NAME = "category_name";

    @Column(name = COLUMN_POST_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int post_id;

    @Column(name = COLUMN_POST_TITLE)
    public String post_title;

    @Column(name = COLUMN_DETAIL)
    public String details;

    @Column(name = COLUMN_BANNER_IMAGE)
    public String banner_image;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public int display_order;

    @Column(name = COLUMN_PUBLISH_DATE_FROM)
    public String publish_date_from;

    @Column(name = COLUMN_CATEGORY_NAME)
    public String category_name;

    //Mandatory no arg constructor
    public Post(){
        super();
    }

    //retrieve all items
    public static List<Post> getAllPosts() {
        List<Post> result = new Select().from(Post.class).orderBy("publish_date_from DESC").execute();
        return result;
    }

    //retrieve limited post
    public static List<Post> getPosts(int limit) {
        List<Post> result = new Select().from(Post.class).orderBy("publish_date_from DESC").limit(limit).execute();
        return result;
    }

    //retrieve single item using id
    public static Post getItem(int id) {
        Post result = new Select().from(Post.class).where("post_id = ?", id).executeSingle();
        return result;
    }
}