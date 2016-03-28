package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by anquan on 2016/3/12.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBhelper";

    private Context mContext;

    public DBOpenHelper(Context context, String databaseName,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        mContext = context;
    }

    /**
     * 数据库第一次创建时调用
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        boolean done = executeAssetsSQL(db, "easybill_mobile2.sql");
        if (done) {
            Log.e(TAG, "应用初始化创建数据库schema成功！");
            try {
                DataCollection sqlCollection = new DataCollection();
                insertOrUpdateBataBatch(db, sqlCollection.getUserDates());
                insertOrUpdateBataBatch(db, sqlCollection.getAccountDates());
                insertOrUpdateBataBatch(db, sqlCollection.getLabelDatas());
                insertOrUpdateBataBatch(db, sqlCollection.getBuggetDatas());
                insertOrUpdateBataBatch(db, sqlCollection.getDebtDatas());
                insertOrUpdateBataBatch(db, sqlCollection.getDreamDatas());
                insertOrUpdateBataBatch(db, sqlCollection.getProjectDatas());
                Log.e(TAG, "应用数据库初始化数据成功！");
            } catch (Exception e) {
                Log.e(TAG, "应用数据库初始化数据失败！！！");
            }
        }
    }

    /**
     * 批量初始化数据
     */
    private void insertOrUpdateBataBatch(SQLiteDatabase db, List<String> sqls) throws Exception {
        db.beginTransaction();
        try {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            throw new Exception("失败！");
        } finally {
            db.endTransaction();
        }
    }


    /**
     * 数据库升级时调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库不升级
        if (newVersion <= oldVersion) {
            return;
        }
        Configuration.oldVersion = oldVersion;

        int changeCnt = newVersion - oldVersion;
        for (int i = 0; i < changeCnt; i++) {
            // 依次执行updatei_i+1文件      由1更新到2 [1-2]，2更新到3 [2-3]
            String schemaName = "update" + (oldVersion + i) + "_"
                    + (oldVersion + i + 1) + ".sql";
            executeAssetsSQL(db, schemaName);
        }
    }

    /**
     * 读取数据库文件（.sql），并执行sql语句
     */
    private boolean executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets()
                    .open(schemaName)));

            String line;//每一行，一行可能并不是完整的一句sql
            String buffer = "";//每句sql的缓存，一行可能并不是完整的一句sql
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        return true;
    }
}
