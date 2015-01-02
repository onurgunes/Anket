package com.onurgunes.anket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ImportExportExcel extends Activity implements OnClickListener{

	Button btnImportQuestions,
		   btnExportQuestions,
		   btnExportStatistics;
	Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_export_excel);
		
		db = new Database(ImportExportExcel.this);
		
		btnImportQuestions 	= (Button) findViewById(R.id.btnImportQuestions);
		btnExportQuestions 	= (Button) findViewById(R.id.btnExportQuestions);
		btnExportStatistics = (Button) findViewById(R.id.btnExportStatistics);
		
		btnImportQuestions.setOnClickListener(this);
		btnExportQuestions.setOnClickListener(this);
		btnExportStatistics.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.import_export_excel, menu);
		return true;
	}

	public static boolean isExternalStorageReadOnly() { 
        String extStorageState = Environment.getExternalStorageState(); 
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) { 
            return true; 
        } 
        return false; 
    } 
 
    public static boolean isExternalStorageAvailable() { 
        String extStorageState = Environment.getExternalStorageState(); 
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) { 
            return true; 
        } 
        return false; 
    }
    
    public void readExcelFile(Context context, String filename) { 
    	 
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) 
        { 
            Log.w("FileUtils", "Storage not available or read only"); 
            return; 
        } 
        try{
            // Creating Input Stream 
            File file = new File(context.getExternalFilesDir(null), filename); 
            FileInputStream myInput = new FileInputStream(file);
 
            // Create a POIFSFileSystem object 
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
 
            // Create a workbook using the File System 
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
 
            // Get the first sheet from workbook 
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            
            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();
 
            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.w("FileUtils", "Cell Value: " +  myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){e.printStackTrace(); }
 
        return;
    }
    
    public boolean saveStatisticsToExcelFile(Context context, String fileName) { 	 
        // check if available and not read only 
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) { 
            Log.w("FileUtils", "Storage not available or read only"); 
            return false; 
        } 
 
        boolean success = false; 

        //New Workbook
        Workbook wb = new HSSFWorkbook();
 
        Cell c = null;
 
        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.WHITE.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("anket");
        
        // Generate column headings
        Row row = sheet1.createRow(0);
        
        // Fill column headings to sheet
        String[] columnHeaders = {"Soru Metni",
        		"Seçenek",
        		"Tercih Edilme Sayýsý",
        		"Tercih Oraný"};
        for (int i = 0; i < columnHeaders.length; i++) {
        	c = row.createCell(i);
            c.setCellValue(columnHeaders[i]);
            c.setCellStyle(cs);
		}
        
        // Fill questions, options and statistics to sheet
        ArrayList<SurveyItem> itemList = db.getStatistics();
        int k = 1;
        for (int i = 0; i < itemList.size(); i++) {
			for (int j = 0; j < itemList.get(i).getOptions().size(); j++) {
				Row _row = sheet1.createRow(k);
				c = _row.createCell(0);
	            c.setCellValue(itemList.get(i).getQuestion().getQuestion());
	            c.setCellStyle(cs);
	            
	            c = _row.createCell(1);
	            c.setCellValue(itemList.get(i).getOptions().get(j).getOption());
	            c.setCellStyle(cs);
	            
	            c = _row.createCell(2);
	            c.setCellValue(itemList.get(i).getOptions().get(j).getSelectedCount());
	            c.setCellStyle(cs);
	            
	            c = _row.createCell(3);
	            c.setCellValue(itemList.get(i).getOptions().get(j).getSelectedCount());
	            c.setCellStyle(cs);
	            
	            k++;
			}
		}
        
        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 100));
        sheet1.setColumnWidth(3, (15 * 100));
 
        // Create a path where we will place our List of objects on external storage 
        File file = new File(context.getExternalFilesDir(null), fileName); 
        FileOutputStream os = null;
 
        try { 
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file); 
            success = true; 
        } catch (IOException e) { 
            Log.w("FileUtils", "Error writing " + file, e); 
        } catch (Exception e) { 
            Log.w("FileUtils", "Failed to save file", e); 
        } finally { 
            try { 
                if (null != os) 
                    os.close(); 
            } catch (Exception ex) { 
            } 
        } 
 
        return success; 
    }

    public boolean saveQuestionsToExcelFile(Context context, String fileName){
    	
    	return true;
    }
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnImportQuestions:
			this.readExcelFile(this, "import.xls");
			break;
		case R.id.btnExportQuestions:
			this.saveQuestionsToExcelFile(this, "myExcel.xls");
			break;
		case R.id.btnExportStatistics:
			this.saveStatisticsToExcelFile(this, "myExcel.xls");
			break;
		default:
			break;			
		}
	}
}
