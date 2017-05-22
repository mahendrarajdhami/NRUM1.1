package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 4/16/2017.
 */
@Table(name = "News")
public class News extends Model {

    // Declare table name as public
    public static final String TABLE_NAME = "News";

    // Declare all column names private
    private static final String COLUMN_NEWS_ID = "news_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DETAIL = "details";
    private static final String COLUMN_FEATURE_IMAGE = "feature_image";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    @Column(name = COLUMN_NEWS_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int news_id;

    @Column(name = COLUMN_TITLE)
    public String title;

    @Column(name = COLUMN_DETAIL)
    public String details;

    @Column(name = COLUMN_FEATURE_IMAGE)
    public String feature_image;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public String display_order;

    //Mandatory no arg constructor
    public News(){
        super();
    }

    //retrieve all items
    public static List<News> getAllNews() {
        List<News> result = new Select().from(News.class).execute();
        return result;
    }

    //retrieve single item using id
    public static News getItem(int id) {
        News result = new Select().from(News.class).where("news_id = ?", id).executeSingle();
        return result;
    }
 }