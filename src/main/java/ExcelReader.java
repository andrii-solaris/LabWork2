import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {

    public Map<String, List<Double>> readExcel(int sheetNumber) {
        var map = new HashMap<String, List<Double>>();

        try {
            var file = new FileInputStream("src/main/java/testdata/TestData.xlsx");
            var workbook = new XSSFWorkbook(file);
            var sheet = workbook.getSheetAt(sheetNumber);

            for (Row row : sheet) {
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                var rowHeaderCell = true;
                var rowHeader = "";
                var rowValues = new ArrayList<Double>();

                for (Cell cell : row) {
                    if (rowHeaderCell) {
                        rowHeader = cell.getStringCellValue();
                        rowHeaderCell = false;
                    } else {
                        if (cell.getNumericCellValue() != 0.00) {
                            rowValues.add(cell.getNumericCellValue());
                        }
                    }
                }

                if (rowHeader.isEmpty() || rowHeader.contains("number")) {
                    continue;
                }

                map.put(rowHeader, rowValues);
            }
            file.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}
