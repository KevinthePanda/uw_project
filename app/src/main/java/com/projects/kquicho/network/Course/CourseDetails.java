package com.projects.kquicho.network.Course;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kevin Quicho on 3/14/2016.
 */
public class CourseDetails extends Course implements Parcelable {
    private String mInstructions = null;
    private String mPrerequisites = null;
    private String mAntirequisites = null;
    private String mCorequisites = null;
    private String mCrossListings = null;
    private String mTermsOffered = null;
    private String mNotes  =null;
    private Boolean mNeedsDepartmentConsent = false;
    private Boolean mNeedsInstructorConsent = false;
    private String mExtra = null;
    private String mCalendarYear = null;
    private String mUrl = null;

    private Boolean mOnline = false;
    private Boolean mOnlineOnly = false;
    private Boolean mStJeromes = false;
    private Boolean mStJeromesOnly = false;
    private Boolean mRenison = false;
    private Boolean mRenisonOnly = false;
    private Boolean mConradGrebel = false;
    private Boolean mConradGrebelOnly = false;

    public String getInstructions() {

        return mInstructions;
    }

    public void setInstructions(String instructions) {
        instructions = instructions.replace("[", "");
        instructions = instructions.replace("]", "");
        if(instructions.equals("")){
            mTermsOffered = "";
            return;
        }
        instructions = instructions.replace("\"", "");
        instructions = instructions.replace(",", ", ");

        mInstructions = instructions;
    }

    public String getPrerequisites() {
        return mPrerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        mPrerequisites = prerequisites;
    }

    public String getAntirequisites() {
        return mAntirequisites;
    }

    public void setAntirequisites(String antirequisites) {
        mAntirequisites = antirequisites;
    }

    public String getCorequisites() {
        return mCorequisites;
    }

    public void setCorequisites(String corequisites) {
        mCorequisites = corequisites;
    }

    public Boolean getNeedsInstructorConsent() {
        return mNeedsInstructorConsent;
    }

    public String getCrossListings() {
        return mCrossListings;
    }

    public void setCrossListings(String crossListings) {
        mCrossListings = crossListings;
    }

    public String getTermsOffered() {
        return mTermsOffered;
    }

    public void setTermsOffered(String termsOffered) {
        termsOffered = termsOffered.replace("[", "");
        termsOffered = termsOffered.replace("]", "");
        if(termsOffered.equals("")){
            mTermsOffered = termsOffered;
            return;
        }
        termsOffered = termsOffered.replace("\"", "");
        termsOffered = termsOffered.replace(",", ", ");
        mTermsOffered = termsOffered;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public Boolean isNeedsDepartmentConsent() {
        return mNeedsDepartmentConsent;
    }

    public void setNeedsDepartmentConsent(Boolean needsDepartmentConsent) {
        mNeedsDepartmentConsent = needsDepartmentConsent;
    }

    public Boolean isNeedsInstructorConsent() {
        return mNeedsInstructorConsent;
    }

    public void setNeedsInstructorConsent(Boolean needsInstructorConsent) {
        mNeedsInstructorConsent = needsInstructorConsent;
    }

    public String getExtra() {
        return mExtra;
    }

    public void setExtra(String extra) {
        mExtra = extra;
    }

    public String getCalendarYear() {
        return mCalendarYear;
    }

    public void setCalendarYear(String calendarYear) {
        mCalendarYear = calendarYear;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Boolean getNeedsDepartmentConsent() {
        return mNeedsDepartmentConsent;
    }

    public Boolean getOnline() {
        return mOnline;
    }

    public void setOnline(Boolean online) {
        mOnline = online;
    }

    public Boolean getOnlineOnly() {
        return mOnlineOnly;
    }

    public void setOnlineOnly(Boolean onlineOnly) {
        mOnlineOnly = onlineOnly;
    }

    public Boolean getStJeromes() {
        return mStJeromes;
    }

    public void setStJeromes(Boolean stJeromes) {
        mStJeromes = stJeromes;
    }

    public Boolean getStJeromesOnly() {
        return mStJeromesOnly;
    }

    public void setStJeromesOnly(Boolean stJeromesOnly) {
        mStJeromesOnly = stJeromesOnly;
    }

    public Boolean getRenison() {
        return mRenison;
    }

    public void setRenison(Boolean renison) {
        mRenison = renison;
    }

    public Boolean getRenisonOnly() {
        return mRenisonOnly;
    }

    public void setRenisonOnly(Boolean renisonOnly) {
        mRenisonOnly = renisonOnly;
    }

    public Boolean getConradGrebel() {
        return mConradGrebel;
    }

    public void setConradGrebel(Boolean conradGrebel) {
        mConradGrebel = conradGrebel;
    }

    public Boolean getConradGrebelOnly() {
        return mConradGrebelOnly;
    }

    public void setConradGrebelOnly(Boolean conradGrebelOnly) {
        mConradGrebelOnly = conradGrebelOnly;
    }

    public CourseDetails(){}

    public CourseDetails (Parcel in){
        mInstructions = in.readString();
        mPrerequisites = in.readString();
        mAntirequisites = in.readString();
        mCorequisites = in.readString();
        mCrossListings = in.readString();
        mTermsOffered = in.readString();
        mNotes = in.readString();
        mNeedsDepartmentConsent = in.readByte() != 0;
        mNeedsInstructorConsent = in.readByte() != 0;
        mExtra = in.readString();
        mCalendarYear = in.readString();
        mUrl = in.readString();
        mOnline = in.readByte() != 0;
        mOnlineOnly = in.readByte() != 0;
        mStJeromes = in.readByte() != 0;
        mStJeromesOnly = in.readByte() != 0;
        mRenison = in.readByte() != 0;
        mRenisonOnly = in.readByte() != 0;
        mConradGrebel = in.readByte() != 0;
        mConradGrebelOnly = in.readByte() != 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CourseDetails createFromParcel(Parcel in) {
            return new CourseDetails(in);
        }

        public CourseDetails[] newArray(int size) {
            return new CourseDetails[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mInstructions);
        dest.writeString(mPrerequisites);
        dest.writeString(mAntirequisites);
        dest.writeString(mCorequisites);
        dest.writeString(mCrossListings);
        dest.writeString(mTermsOffered);
        dest.writeString(mNotes);
        dest.writeByte((byte) (mNeedsDepartmentConsent ? 1 : 0));
        dest.writeByte((byte) (mNeedsInstructorConsent ? 1 : 0));
        dest.writeString(mExtra);
        dest.writeString(mCalendarYear);
        dest.writeString(mUrl);
        dest.writeByte((byte) (mOnline ? 1 : 0));
        dest.writeByte((byte) (mOnlineOnly ? 1 : 0));
        dest.writeByte((byte) (mStJeromes ? 1 : 0));
        dest.writeByte((byte) (mStJeromesOnly ? 1 : 0));
        dest.writeByte((byte) (mRenison ? 1 : 0));
        dest.writeByte((byte) (mRenisonOnly ? 1 : 0));
        dest.writeByte((byte) (mConradGrebel ? 1 : 0));
        dest.writeByte((byte) (mConradGrebelOnly ? 1 : 0));

    }




}
