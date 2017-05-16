package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 5/16/2017.
 */
@Table(name = "Banner")
public class Banner extends Model {

    // Declare table name as public
    public static final String TABLE_NAME = "Banner";

    // Declare all column names private
    private static final String COLUMN_BANNER_ID = "banner_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PC_IMAGE = "pc_image";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    @Column(name = COLUMN_BANNER_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int banner_id;

    @Column(name = COLUMN_TITLE)
    public String title;

    @Column(name = COLUMN_DESCRIPTION)
    public String description;

    @Column(name = COLUMN_PC_IMAGE)
    public String pc_image;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public String display_order;

    //Mandatory no arg constructor
    public Banner(){
        super();
    }

    //retrieve all items
    public static List<Banner> getAllNews() {
        List<Banner> result = new Select().from(Banner.class).execute();
        return result;
    }

}
