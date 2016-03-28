package entity;

/**
 * Created by anquan on 2016/3/14.
 */
public class Account {

    private int aIcon;
    private String aName;

    public Account() {
    }

    public Account(int aIcon, String aName) {
        this.aIcon = aIcon;
        this.aName = aName;
    }

    public int getaIcon() {
        return aIcon;
    }

    public String getaName() {
        return aName;
    }

    public void setaIcon(int aIcon) {
        this.aIcon = aIcon;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }
}