package asia.izzi.member.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.util.*;

public class ExcelUtils {

    public static void exportExcel(InputStream is, Context context, OutputStream os) throws IOException {
        JxlsHelper.getInstance().processTemplate(is, os, context);
        is.close();
        os.close();
    }

    public static Map<Integer, String> getDataRowExcel(Row row) {
        Map<Integer, String> map = new HashMap<>();
        // For each row, iterate through each columns
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell != null) {
                CellType cellType = cell.getCellType();
                if (cellType == CellType.STRING) {
                    map.put(cell.getColumnIndex(), cell.getStringCellValue());
                } else if (cellType == CellType.NUMERIC) {
                    map.put(cell.getColumnIndex(), NumberToTextConverter.toText(cell.getNumericCellValue()));
                } else if (cellType == CellType.BOOLEAN) {
                    map.put(cell.getColumnIndex(), String.valueOf(cell.getBooleanCellValue()));
                } else if (cellType == CellType.BLANK) {
                    map.put(cell.getColumnIndex(), "");
                }
            }
        }
        return map;
    }


    public static List<Map<Integer, String>> getDataExcel(int startRow, XSSFWorkbook workbook) throws IOException {
        List<Map<Integer, String>> listImportResult = new ArrayList<>();
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        if (lastRow >= startRow) {
            for (int i = startRow; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                listImportResult.add(getDataRowExcel(row));
            }
        }
        workbook.close();
        return listImportResult;
    }
}
