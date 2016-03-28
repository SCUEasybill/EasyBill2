package scu.function;

import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExportExcel{
    public void ExportDate(ArrayList<String []> array,String str[],String name){
        //array 输入的数据，以一个String[]为一行
        //str[] 标题行，每一列数据的含义为一个String
        //name  新建Sheet的名字，即数据库内表的名字的含义
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(name);//新建一个Sheet
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell;
        HSSFFont font=workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
        style.setWrapText(true);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft((short) 1);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight((short) 1);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
        style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
        style.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．

        //第一行，标题
        for (int i=0;i<str.length;i++){
            cell=row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(str[i]);
        }
        //各种数据
        for (int i=0;i<array.size();i++) {
            row=sheet.createRow(i+1);
            for (int j=0;j<str.length;j++){
                cell=row.createCell(j);
                cell.setCellStyle(style);
                cell.setCellValue(array.get(i)[j]);
            }
        }
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "EasyBill数据.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
