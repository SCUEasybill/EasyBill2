package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by anquan on 2016/3/12.
 */
public class DBManager {
    private DBOpenHelper helper = null;
    public SQLiteDatabase db = null;
    private Context mContext;

    public DBManager(Context context) {
        helper = new DBOpenHelper(context, Configuration.DB_NAME, null, 1);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
        this.mContext = context;
    }

    public boolean close(){
        db.close();
        return true;
    }
}
