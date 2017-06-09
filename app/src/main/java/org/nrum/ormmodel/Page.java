package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by rajdhami on 5/30/2017.
 */
@Table(name = "Page")
public class Page extends Model {
    // Declare table name as public
    public static final String TABLE_NAME = "Page";

    // Declare all column names private
    private static final String COLUMN_PAGE_ID = "page_id";
    private static final String COLUMN_PAGE_TITLE = "page_title";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_BANNER_IMAGE = "banner_image";
    private static final String COLUMN_PUBLISH_DATE= "publish_date";
    private static final String COLUMN_DISPLAY_ORDER= "display_order";

    @Column(name = COLUMN_PAGE_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int page_id;

    @Column(name = COLUMN_PAGE_TITLE)
    public String page_title;

    @Column(name = COLUMN_DETAILS)
    public String details;

    @Column(name = COLUMN_BANNER_IMAGE)
    public String banner_image;

    @Column(name = COLUMN_PUBLISH_DATE)
    public String publish_date;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public int display_order;

    //Mandatory no arg constructor
    public Page(){
        super();
    }

    //retrieve single item using id
    public static Page getItem(int id) {
        Page result = new Select().from(Page.class).where("page_id = ?", id).executeSingle();
        return result;
    }
}
