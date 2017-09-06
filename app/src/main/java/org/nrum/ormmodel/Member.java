package org.nrum.ormmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by rajdhami on 4/16/2017.
 */
@Table(name = "Member")
public class Member extends Model {

    // Declare table name as public
    public static final String TABLE_NAME = "Member";

    // Declare all column names private
    private static final String COLUMN_MEMBER_ID = "member_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESIGNATION = "designation";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";

    @Column(name = COLUMN_MEMBER_ID,
            unique = true,
            onUniqueConflict = Column.ConflictAction.REPLACE,
            index = true)
    public int member_id;

    @Column(name = COLUMN_NAME)
    public String name;

    @Column(name = COLUMN_DESIGNATION)
    public String designation;

    @Column(name = COLUMN_PROFILE_IMAGE)
    public String profile_image;

    @Column(name = COLUMN_DISPLAY_ORDER)
    public int display_order;

    @Column(name = COLUMN_EMAIL)
    public String email;

    @Column(name = COLUMN_PHONE)
    public String phone;

    @Column(name = COLUMN_ADDRESS)
    public String address;

    //Mandatory no arg constructor
    public Member(){
        super();
    }

    //retrieve all items
    public static List<Member> getAllMember() {
        List<Member> result = new Select().from(News.class).orderBy("display_order DESC").execute();
        return result;
    }

    //retrieve limited Member
    public static List<Member> getMember(int limit) {
        List<Member> result = new Select().from(Member.class).orderBy("display_order DESC").limit(limit).execute();
        return result;
    }

    //retrieve single item using id
    public static Member getItem(int id) {
        Member result = new Select().from(Member.class).where("member_id = ?", id).executeSingle();
        return result;
    }
 }