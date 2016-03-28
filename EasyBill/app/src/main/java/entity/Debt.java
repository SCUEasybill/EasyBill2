package entity;

public class Debt {
    public Debt(int _id, int mIo, double mNum, String mWho, String mBegin, String mEnd, String mAtualEnd, int over,
                String mRemak) {
        super();
        this._id = _id;
        this.mIo = mIo;
        this.mNum = mNum;
        this.mWho = mWho;
        this.mBegin = mBegin;
        this.mEnd = mEnd;
        this.mAtualEnd = mAtualEnd;
        this.mOver = over;
        this.mRemak = mRemak;
    }
    public Debt(){}
    int _id;
    int mIo;
    double mNum;
    String mWho;
    String mBegin;
    String mEnd;
    String mAtualEnd;
    int mOver;
    String mRemak;
    public int getmIo() {
        return mIo;
    }
    public void setmIo(int mIo) {
        this.mIo = mIo;
    }
    public double getmNum() {
        return mNum;
    }
    public void setmNum(double mNum) {
        this.mNum = mNum;
    }
    public String getmWho() {
        return mWho;
    }
    public void setmWho(String mWho) {
        this.mWho = mWho;
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
    public String getmAtualEnd() {
        return mAtualEnd;
    }
    public void setmAtualEnd(String mAtualEnd) {
        this.mAtualEnd = mAtualEnd;
    }
    public int getmOver() {
        return mOver;
    }
    public void setmOver(int over) {
        this.mOver = over;
    }
    public String getmRemak() {
        return mRemak;
    }
    public void setmRemak(String mRemak) {
        this.mRemak = mRemak;
    }
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

}
