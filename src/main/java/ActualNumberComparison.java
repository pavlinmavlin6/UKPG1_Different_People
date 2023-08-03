import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
public class ActualNumberComparison {
    public static void main(String[] args) throws IOException {
        Workbook everyday = getSheets(new File(args[0]));
        Sheet datatypeSheet = everyday.getSheetAt(1);
        Iterator<Row> excelRowsFromEveryDaySheet = datatypeSheet.iterator();
        Workbook fact = getSheets(new File(args[1]));
        Sheet datatypeSheet1 = fact.getSheetAt(0);
        Iterator<Row> excelRowsFromFactSheet = datatypeSheet1.iterator();
        int rowsNumberEveryDay = getCount(excelRowsFromEveryDaySheet);
        int rowsNumberFact = getCount(excelRowsFromFactSheet);
        if (rowsNumberFact<rowsNumberEveryDay){
            getDifference(everyday,fact);
        }
        else {
            getDifference(fact,everyday);
        }
    }
    public static Workbook getSheets(File path) throws IOException {
       // File file = new File("/Users/aleksejpikulik/OK-UKPG1/"+name);
        FileInputStream excelFile = new FileInputStream(path);
        return new XSSFWorkbook(excelFile);
    }
    public static int getCount(Iterator<Row> iteratorEveryDay){
        int count = 0;
        for (Iterator<Row> it = iteratorEveryDay; it.hasNext(); ) {
            Object i = it.next();
            count++;
        }
        return count;
    }
    public static void getDifference(Workbook sheetPeoopleEveryDay, Workbook sheetPeopleFact){
        Sheet datatypeSheet = sheetPeoopleEveryDay.getSheetAt(1);
        Iterator<Row> iteratorEveryDay = datatypeSheet.iterator();
        ArrayList<Worker> initialsEveryDay = new ArrayList<>();
        while (iteratorEveryDay.hasNext()) {
            try {
                Row next = iteratorEveryDay.next();
                if(next.getCell(4).getStringCellValue().equalsIgnoreCase("Ковыктинское ГКМ Этап 5 УКПГ-2")){
                    Worker worker = new Worker();
                    worker.setName(next.getCell(2).getStringCellValue());
                    worker.setTitle(next.getCell(3).getStringCellValue());
                    worker.setStructure(next.getCell(1).getStringCellValue());
                    initialsEveryDay.add(worker);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Sheet datatypeSheet1 = sheetPeopleFact.getSheetAt(0);
        Iterator<Row> iteratorFact = datatypeSheet1.iterator();
        ArrayList<Worker> initialsFact = new ArrayList<>();
        while (iteratorFact.hasNext()){
            try {
                Row next = iteratorFact.next();
                Worker worker = new Worker();
                worker.setName(next.getCell(2).getStringCellValue());
                worker.setTitle(next.getCell(4).getStringCellValue());
                initialsFact.add(worker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Worker> filterSheetPeopleEveryDay = new ArrayList<>(initialsEveryDay);
        List<Worker> filterSheetPeopleFact = new ArrayList<>(initialsFact);
        filterSheetPeopleEveryDay.removeAll(initialsFact);
        filterSheetPeopleFact.removeAll(initialsEveryDay);
        try {
            createTemplate(filterSheetPeopleEveryDay,filterSheetPeopleFact);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void createTemplate(List<Worker> filterSheetPeopleEveryDay, List<Worker>filterPeopleFact) throws IOException {
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet sheet = hwb.createSheet("не соответсвие");
        HSSFRow rowHead = sheet.createRow((short) 0);
        rowHead.createCell(0).setCellValue("ФЧ отсутсвуют");
        rowHead.createCell(1).setCellValue("ЯЧ отсутсвуют");
        int i = 1;
        for (Worker l1 : filterSheetPeopleEveryDay) {
            HSSFRow row = sheet.createRow((short) i);
            row.createCell(0).setCellValue(l1.getStructure());
            row.createCell(1).setCellValue(l1.getName());
            row.createCell(2).setCellValue(l1.getTitle());
            if(i<filterPeopleFact.size()) {
                row.createCell(3).setCellValue(filterPeopleFact.get(i).getName());
                row.createCell(4).setCellValue(filterPeopleFact.get(i).getTitle());
            }
            i++;
        }
        for (int j = 0; j < 5; j++) {
            sheet.autoSizeColumn(j, true);
        }
        FileOutputStream fileOut = new FileOutputStream("personmistake.xls");
        hwb.write(fileOut);
        fileOut.close();
    }
}
class Worker{
    private String structure;
    private String name;
    private String title;
    public Worker() {
    }
    public String getStructure() {
        return structure;
    }
    public void setStructure(String structure) {
        this.structure = structure;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(name, worker.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}