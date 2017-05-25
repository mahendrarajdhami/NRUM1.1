package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 5/24/2017.
 */
public class Notice extends Model{
    // Declare table name as public
    public static final String TABLE_NAME = "Notice";

    // Declare all column names private
    private static final String COLUMN_NOTICE_ID = "banner_id";
    private static final String COLUMN_NOTICE_TITLE = "notice_title";
    private static final String COLUMN_DETAIL = "detail";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    @Column(name = COLUMN_NOTICE_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int notice_id;

    @Column(name = COLUMN_NOTICE_TITLE)
    public String notice_title;

    @Column(name = COLUMN_DETAIL)
    public String detail;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public int display_order;

    //Mandatory no arg constructor
    public Notice(){
        super();
    }

    //retrieve all items
    public static List<Notice> getAllNotices() {
        List<Notice> result = new Select().from(Notice.class).execute();
        return result;
    }

    public static Notice getLatestNotice() {
        Notice result = new Select().from(Notice.class).orderBy("display_order DESC").executeSingle();
        return result;
    }
}
