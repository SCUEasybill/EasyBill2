package entity;

public class Project {
    public Project(int _id, String mName, double mNumber, double mLeaving, String mBegin, String mEnd, boolean mDone,
                   String mRemark) {
        super();
        this._id = _id;
        this.mName = mName;
        this.mNumber = mNumber;
        this.mLeaving = mLeaving;
        this.mBegin = mBegin;
        this.mEnd = mEnd;
        this.mDone = mDone;
        this.mRemark = mRemark;
    }
    public Project(){}
    int _id;
    String mName;
    double mNumber;
    double mLeaving;
    String mBegin;
    String mEnd;
    boolean mDone;
    String mRemark;
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getmName() {
        return mName;
    }
    public void setmName(String mName) {
        this.mName = mName;
    }
    public double getmNumber() {
        return mNumber;
    }
    public void setmNumber(double mNumber) {
        this.mNumber = mNumber;
    }
    public double getmLeaving() {
        return mLeaving;
    }
    public void setmLeaving(double mLeaving) {
        this.mLeaving = mLeaving;
    }
    public String getmBegin() {
        return mBegin;
    }
    public void setmBegin(String mBegin) {
        this.mBegin = mBegin;
    }
    public String getmEnd() {
        return mEnd;
    }
    public void setmEnd(String mEnd) {
        this.mEnd = mEnd;
    }
    public boolean ismDone() {
        return mDone;
    }
    public void setmDone(boolean mDone) {
        this.mDone = mDone;
    }
    public String getmRemark() {
        return mRemark;
    }
    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }
}
