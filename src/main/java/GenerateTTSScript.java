import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class GenerateTTSScript {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new RuntimeException("Argumentos Invalidos");
        }
        File xlsx = new File(args[0]);
        final String outputFilePath = args[1];
        final String COUNTRY = "{COUNTRY}";
        final String PRODUCT_TYPE = "{PRODUCT_TYPE}";
        final String PRODUCT_TEC = "{PRODUCT_TEC}";
        final String CHANNEL = "{CHANNEL}";
        final String ASR_NAME = "{ASR_NAME}";
        final String ENTITY_ID = "{ENTITY_ID}";
        final String CRM_NAME = "{CRM_NAME}";
        final String TTS = "{TTS}";
        final String SYNONYM_1 = "{SYNONYM_1}";
        final String SYNONYM_2 = "{SYNONYM_2}";
        final String SYNONYM_3 = "{SYNONYM_3}";
        final String SYNONYM_4 = "{SYNONYM_4}";
        final String SYNONYM_5 = "{SYNONYM_5}";


        String queryTemp = "insert into DTV_TTS_ITEMS\n" +
                "values(TTS_ITEM_SEQ.NEXTVAL,\n" +
                "(SELECT ID FROM DTV_TTS_COUNTRIES WHERE DESCRIPTION='{COUNTRY}'),\n" +
                "'{ASR_NAME}',\n" +
                "{ENTITY_ID},\n" +
                "(select id from DTV_TTS_PRODUCT_TYPE where description='{PRODUCT_TYPE}' and COUNTRY_ID=(SELECT ID FROM DTV_TTS_COUNTRIES WHERE DESCRIPTION='{COUNTRY}') ),\n" +
                "(select id from DTV_TTS_PRODUCT_TEC where description='{PRODUCT_TEC}' and COUNTRY_ID=(SELECT ID FROM DTV_TTS_COUNTRIES WHERE DESCRIPTION='{COUNTRY}')),\n" +
                "'',\n" +
                "'{CRM_NAME}',\n" +
                "'{TTS}',\n" +
                "'{SYNONYM_1}',\n" +
                "'{SYNONYM_2}',\n" +
                "'{SYNONYM_3}',\n" +
                "'{SYNONYM_4}',\n" +
                "'{SYNONYM_5}',\n" +
                "1,\n" +
                "'EXCEL');\n\n";
        System.out.println("Nombres de Columnas");

        try (Workbook wb = WorkbookFactory.create(xlsx)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Iterator<Cell> cellHeaderIterator = rowIterator.next().cellIterator();
            while (cellHeaderIterator.hasNext()) {
                String headerColumTitle = cellHeaderIterator.next().getStringCellValue();
                System.out.println(headerColumTitle);
            }

            Row rowData;
            String q = queryTemp;


            while (rowIterator.hasNext()) {
                rowData = rowIterator.next();
                System.out.println(rowData.getCell(9)!=null?String.valueOf(rowData.getCell(9).getNumericCellValue()):String.valueOf(-1));
                q = q.replace(COUNTRY, rowData.getCell(0)!=null?rowData.getCell(0).getStringCellValue():"")
                        .replace(ASR_NAME, rowData.getCell(1)!=null?rowData.getCell(1).toString().replace("'","''"):"")
                        .replace(CRM_NAME, rowData.getCell(2)!=null?rowData.getCell(2).toString().replace("'","''"):"")
                        .replace(TTS, rowData.getCell(3)!=null?rowData.getCell(3).toString().replace("'","''"):"")
                        .replace(SYNONYM_1, rowData.getCell(4)!=null?rowData.getCell(4).toString().replace("'","''"):"")
                        .replace(SYNONYM_2, rowData.getCell(5)!=null?rowData.getCell(5).toString().replace("'","''"):"")
                        .replace(SYNONYM_3, rowData.getCell(6)!=null?rowData.getCell(6).toString().replace("'","''"):"")
                        .replace(SYNONYM_4, rowData.getCell(7)!=null?rowData.getCell(7).toString().replace("'","''"):"")
                        .replace(SYNONYM_5, rowData.getCell(8)!=null?rowData.getCell(8).toString().replace("'","''"):"")
                        .replace(ENTITY_ID, rowData.getCell(9)!=null?String.valueOf(rowData.getCell(9).getNumericCellValue()):String.valueOf(-1))
                        .replace(PRODUCT_TEC, rowData.getCell(10)!=null?rowData.getCell(10).toString().replace("'","''"):"")
                        .replace(PRODUCT_TYPE, rowData.getCell(11)!=null?rowData.getCell(11).toString().replace("'","''"):"");


                appendStringToFile(q, outputFilePath);

                q = queryTemp;

            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void appendStringToFile(String data, String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.write(data);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
