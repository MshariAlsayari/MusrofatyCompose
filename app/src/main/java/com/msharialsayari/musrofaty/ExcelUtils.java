package com.msharialsayari.musrofaty;



import android.content.Context;
import android.os.Environment;
import android.util.Log;


import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel;
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel;
import com.msharialsayari.musrofaty.utils.DateUtils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ExcelUtils {
    public final String TAG = "ExcelUtil";
    private Cell cell;
    private Sheet sheet;
    private Workbook workbook;
    private CellStyle headerCellStyle;
    private CellStyle dividerCellStyle;
    private ExcelModel importedExcelData;

    private Context context;
    private String fileName;

    /**
     * Import data from Excel Workbook
     *
     * @param context  - Application Context
     * @param fileName - Name of the excel file
     * @return importedExcelData
     */

    public ExcelUtils(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;

    }

    public ExcelModel readFromExcelWorkbook(Context context, String fileName) {
        return retrieveExcelFromStorage(context, fileName);
    }

    /**
     * Export Data into Excel Workbook
     *
     * @param excelModel - Contains the actual data to be displayed in excel
     */
    public boolean exportDataIntoWorkbook(ExcelModel excelModel) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();
        Map<String, List<SmsModel>> smsMap = Utils.getMappedSmsByMonth(excelModel.getSmsList(), DateUtils.DEFAULT_MONTH_YEAR_PATTERN);



        //create All Sms Sheet
        initSheet(context.getString(R.string.common_all));
        fillDataIntoExcel(excelModel.getSmsList(), context);


        //create sheet based on months
        for (Map.Entry<String, List<SmsModel>> entry : smsMap.entrySet()) {
            initSheet(entry.getKey());
            fillDataIntoExcel(entry.getValue(), context);
            // fillDivider();

        }


        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return isWorkbookWrittenIntoStorage;
    }

    private void initSheet(String sheetName){
        setHeaderCellStyle();
        setDividerCellStyle();
        sheet = workbook.createSheet(sheetName);
        int COLUMN_WIDTH = 15 * 400;
        for (ExcelColumns value : ExcelColumns.values()) {
            sheet.setColumnWidth(value.getIndex(), COLUMN_WIDTH);
        }
        setHeaderRow(context);

    }


    /**
     * Checks if Storage is READ-ONLY
     *
     * @return boolean
     */
    private boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**
     * Checks if Storage is Available
     *
     * @return boolean
     */
    private boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**
     * Setup header cell style
     */
    private void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    private void setDividerCellStyle() {
        dividerCellStyle = workbook.createCellStyle();
        dividerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dividerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        dividerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }

    /**
     * Setup Header Row
     */
    private void setHeaderRow(Context context) {

        Row headerRow = sheet.createRow(0);

        for (ExcelColumns value : ExcelColumns.values()) {
            cell = headerRow.createCell(value.getIndex());
            cell.setCellValue(context.getString(value.getTitle()));
            cell.setCellStyle(headerCellStyle);
        }

    }

    private void setDividerRow(int row) {

        Row headerRow = sheet.createRow(row);

        for (ExcelColumns value : ExcelColumns.values()) {
            cell = headerRow.createCell(value.getIndex());
            cell.setCellValue("");
            cell.setCellStyle(dividerCellStyle);
        }

    }

    /**
     * Fills Data into Excel Sheet
     * <p>
     * NOTE: Set row index as i+1 since 0th index belongs to header row
     *
     * @param dataList - List containing data to be filled into excel
     */
    private void fillDataIntoExcel(List<SmsModel> dataList, Context context) {


        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i+1);
            SmsModel smsModel = dataList.get(i);

            // Create Cells for each row
            //BankName Cell
            cell = rowData.createCell(ExcelColumns.SENDER_NAME_COLUMN.getIndex());
            cell.setCellValue(smsModel.getSenderName());

            //Sms Cell
            cell = rowData.createCell(ExcelColumns.SMS_BODY.getIndex());
            cell.setCellValue(smsModel.getBody());

            //Store Name  Cell
            cell = rowData.createCell(ExcelColumns.STORE_NAME.getIndex());
            cell.setCellValue(smsModel.getStoreName());

            //Store Category  Cell
            cell = rowData.createCell(ExcelColumns.STORE_CATEGORY.getIndex());
            cell.setCellValue(CategoryModel.Companion.getDisplayName(context, Objects.requireNonNull(smsModel.getStoreAndCategoryModel()).getCategory()));

            double incomeAmount = 0.0;
            double expensesAmount = 0.0;
            switch (Objects.requireNonNull(smsModel.getSmsType())) {
                case INCOME:
                    incomeAmount = smsModel.getAmount();
                    break;
                case EXPENSES:
                    expensesAmount = smsModel.getAmount();
                    break;
            }


            //Expenses Amount  Cell
            cell = rowData.createCell(ExcelColumns.EXPENSES_AMOUNT_COLUMN.getIndex());
            cell.setCellValue(String.valueOf(expensesAmount));

            //Income Amount  Cell
            cell = rowData.createCell(ExcelColumns.INCOME_AMOUNT_COLUMN.getIndex());
            cell.setCellValue(String.valueOf(incomeAmount));

            //Amount Currency  Cell
            cell = rowData.createCell(ExcelColumns.AMOUNT_CURRENCY_COLUMN.getIndex());
            cell.setCellValue(smsModel.getCurrency());

            //Sms type  Cell
            cell = rowData.createCell(ExcelColumns.SMS_TYPE_COLUMN.getIndex());
            cell.setCellValue(context.getString(smsModel.getSmsType().getValueString()));


            //Sms date Cell
            cell = rowData.createCell(ExcelColumns.SMS_DATE.getIndex());
            String date = DateUtils.getDateByTimestamp(smsModel.getTimestamp(), DateUtils.DEFAULT_DATE_TIME_PATTERN);
            cell.setCellValue(date);
        }
    }

    /**
     * Store Excel Workbook in external storage
     *
     * @param context  - application context
     * @param fileName - name of workbook which will be stored in device
     * @return boolean - returns state whether workbook is written into storage or not
     */
    private boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Log.e(TAG, "Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save file due to Exception: ", e);
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return isSuccess;
    }

    /**
     * Retrieve excel from External Storage
     *
     * @param context  - application context
     * @param fileName - name of workbook to be read
     * @return importedExcelData
     */
    private ExcelModel retrieveExcelFromStorage(Context context, String fileName) {
        importedExcelData = new ExcelModel();

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            Log.e(TAG, "Reading from Excel" + file);

            // Create instance having reference to .xls file
            workbook = new HSSFWorkbook(fileInputStream);

            // Fetch sheet at position 'i' from the workbook
            sheet = workbook.getSheetAt(0);

            // Iterate through each row
            for (Row row : sheet) {
                int index = 0;
                List<String> rowDataList = new ArrayList<>();
                List<SmsModel> smsEntities = new ArrayList<>();

                if (row.getRowNum() > 0) {
                    // Iterate through all the columns in a row (Excluding header row)
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Cell cell1 = cellIterator.next();
                    Cell cell2 = cellIterator.next();
                    rowDataList.add(cell1.getStringCellValue());
                    rowDataList.add(cell2.getStringCellValue());
//                    smsEntities.add(new SmsModel("", rowDataList.get(0), "", rowDataList.get(1)));


                    /**
                     * Index 0 of rowDataList will Always have name.
                     * So, passing it as 'name' in ExcelModel
                     *
                     * Index 1 onwards of rowDataList will have phone numbers (if >1 numbers)
                     * So, adding them to phoneNumberList
                     *
                     * Thus, importedExcelData list has appropriately mapped data
                     */

                    //   importedExcelData = new ExcelModel(smsEntities, rowDataList);
                }

            }

        } catch (IOException e) {
            Log.e(TAG, "Error Reading Exception: ", e);

        } catch (Exception e) {
            Log.e(TAG, "Failed to read file due to Exception: ", e);

        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return importedExcelData;
    }

}

enum ExcelColumns{
    SENDER_NAME_COLUMN(0, R.string.excel_sender_name_header),
    SMS_BODY(1, R.string.excel_sms_body_header),
    STORE_NAME(2, R.string.excel_store_name_header),
    STORE_CATEGORY(3, R.string.excel_store_category_header),
    EXPENSES_AMOUNT_COLUMN(4, R.string.excel_expenses_amount_header),
    INCOME_AMOUNT_COLUMN(5, R.string.excel_income_amount_header),
    AMOUNT_CURRENCY_COLUMN(6, R.string.excel_amount_currency_header),
    SMS_TYPE_COLUMN(7, R.string.excel_sms_type_header),
    SMS_DATE(8, R.string.excel_sms_date_header);

    private int index;
    private int title;
    ExcelColumns(int index, int title){
        this.index = index;
        this.title  = title;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}


// cell = headerRow.createCell(BANK_NAME_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_sender_name_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(SMS_BODY_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_sms_body_header));
//         cell.setCellStyle(headerCellStyle);
//
//
//         cell = headerRow.createCell(STORE_NAME);
//         cell.setCellValue(context.getString(R.string.excel_expenses_amount_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(STORE_CATEGORY);
//         cell.setCellValue(context.getString(R.string.excel_income_amount_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(EXPENSES_AMOUNT_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_expenses_amount_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(INCOME_AMOUNT_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_income_amount_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(AMOUNT_CURRENCY_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_amount_currency_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(SMS_TYPE_COLUMN);
//         cell.setCellValue(context.getString(R.string.excel_sms_type_header));
//         cell.setCellStyle(headerCellStyle);
//
//         cell = headerRow.createCell(SMS_DATE);
//         cell.setCellValue(context.getString(R.string.excel_sms_date_header));
//         cell.setCellStyle(headerCellStyle);

