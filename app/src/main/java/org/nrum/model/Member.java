package org.nrum.model;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class Member {
    private String name, profileImage, designation, address,email,phone;
    private int member_id;
    public Member () {

    }
    public Member(int member_id, String name, String profileImage, String designation, String address, String email, String phone) {
        this.member_id  = member_id;
        this.name       = name;
        this.profileImage   = profileImage;
        this.designation    = designation;
        this.address        = address;
        this.email          = email;
    }

    public int getMemberID() {
        return member_id;
    }
    public void setMemberID(int memberID) {
        this.member_id = memberID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}