package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 5/24/2017.
 */
public class Faq extends Model{
    // Declare table name as public
    public static final String TABLE_NAME = "Faq";

    // Declare all column names private
    private static final String COLUMN_FAQ_ID = "faq_id";
    private static final String COLUMN_FAQ_QUESTION = "question";
    private static final String COLUMN_DETAIL = "answer";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";
    private static final String COLUMN_CATEGORY_NAME = "category_name";

    @Column(name = COLUMN_FAQ_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int faq_id;

    @Column(name = COLUMN_FAQ_QUESTION)
    public String question;

    @Column(name = COLUMN_DETAIL)
    public String answer;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public int display_order;

    @Column(name = COLUMN_CATEGORY_NAME)
    public String category_name;

    //Mandatory no arg constructor
    public Faq(){
        super();
    }

    //retrieve all items
    public static List<Faq> getAllFaq() {
        List<Faq> result = new Select().from(Faq.class).execute();
        return result;
    }

    public static Faq getLatestFaq() {
        Faq result = new Select().from(Faq.class).orderBy("display_order DESC").executeSingle();
        return result;
    }
}
