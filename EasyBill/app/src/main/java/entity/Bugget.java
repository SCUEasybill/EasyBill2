package entity;

public class Bugget {
    public Bugget(int _id, String mBegin, String mEnd, double mNum, double mUsed, boolean mOver) {
        super();
        this._id = _id;
        this.mBegin = mBegin;
        this.mEnd = mEnd;
        this.mNum = mNum;
        this.mUsed = mUsed;
        this.mOver = mOver;
    }
    public Bugget(){}
    int _id;
    String mBegin = null;
    String mEnd = null;
    double mNum = 0;
    double mUsed = 0;
    boolean mOver = false;
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
    public double getmNum() {
        return mNum;
    }
    public void setmNum(double mNum) {
        this.mNum = mNum;
    }
    public double getmUsed() {
        return mUsed;
    }
    public void setmUsed(double mUsed) {
        this.mUsed = mUsed;
    }
    public boolean ismOver() {
        return mOver;
    }
    public void setmOver(boolean mOver) {
        this.mOver = mOver;
    }
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

}
