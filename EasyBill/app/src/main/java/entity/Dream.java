package entity;

public class Dream {
    public Dream(int _id, String mName, String mContent, double mNumber, double mHaveNum, String mBegin, String mEnd,
                 boolean mDone) {
        super();
        this._id = _id;
        this.mName = mName;
        this.mContent = mContent;
        this.mNumber = mNumber;
        this.mHaveNum = mHaveNum;
        this.mBegin = mBegin;
        this.mEnd = mEnd;
        this.mDone = mDone;
    }
    public Dream(){}
    int _id;
    String mName;
    String mContent;
    double mNumber;
    double mHaveNum;
    String mBegin;
    String mEnd;
    boolean mDone;
    public String getmName() {
        return mName;
    }
    public void setmName(String mName) {
        this.mName = mName;
    }
    public String getmContent() {
        return mContent;
    }
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
    public double getmNumber() {
        return mNumber;
    }
    public void setmNumber(double mNumber) {
        this.mNumber = mNumber;
    }
    public double getmHaveNum() {
        return mHaveNum;
    }
    public void setmHaveNum(double mHaveNum) {
        this.mHaveNum = mHaveNum;
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
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
}
