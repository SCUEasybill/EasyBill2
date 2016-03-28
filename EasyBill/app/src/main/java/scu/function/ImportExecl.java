package scu.function;

/**
 * Created by 流波 on 2016/3/7.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import xyz.anmai.easybill.R;

public class ImportExecl extends Activity {
    TextView txt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_data);
        txt = (TextView)findViewById(R.id.txt_show);
        txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        readExcel();
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SuppressLint("NewApi")
    public void readExcel() {
        try {
            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "EasyBill数据.xls");
            InputStream is = new FileInputStream(file);
            //Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
            Workbook book = Workbook.getWorkbook(is);

            int num = book.getNumberOfSheets();
            txt.setText("the num of sheets is " + num+ "\n");
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            txt.append("the name of sheet is " + sheet.getName() + "\n");
            txt.append("total rows is " + Rows + "\n");
            txt.append("total cols is " + Cols + "\n");
            for (int i = 0; i < Cols; ++i) {
                for (int j = 0; j < Rows; ++j) {
                    // getCell(Col,Row)获得单元格的值
                    txt.append("contents:" + sheet.getCell(i,j).getContents() + "\n");
                }
            }
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
