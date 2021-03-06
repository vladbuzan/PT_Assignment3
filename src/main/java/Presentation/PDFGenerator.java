package Presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

/**
 * Class used to handle the generation of
 * PDF reports. Uses only static methods, so
 * no instance is required.
 */
public class PDFGenerator {
    /**
     * Method used to generate a PDF containing
     * a title and a table. The title is the name
     * of the Class whose instances are contained in
     * the list given as argument, whilst the table
     * contents are as follows: the headers are the
     * fields of the class while the values are taken
     * from instances inside the list.
     * @param list List containing objects the user
     *             wants to populate the table with
     */
    public static void generatePDF(List list) {
        Document document = new Document();
        String name = list.get(0).getClass().getSimpleName();
        String time = getTimeString();
        try {
            System.out.println(name + "_" + time + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(name + "_" + time + ".pdf"));
            document.open();
            Chunk chunk = new Chunk(name);
            document.add(chunk);
            writeTable(document, list);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to generate a PDF containing a title
     * and a table. The contents of the table are represented
     * by the two lists given as arguments.
     * @param list1 List whose objects will populate the table
     * @param list2 List whose objects will populate the table
     * @param name Title of the PDF report
     */
    public static void generatePDF(List list1, List list2, String name) {
        Document document = new Document();
        String time = getTimeString();
        try {
            System.out.println(name + "_" + time + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(name + "_" + time + ".pdf"));
            document.open();
            document.add(new Chunk(name));
            writeTable(document, list1, list2);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to generate a String containing
     * some time information, namely
     * DAYOFMONTH_HOUROFDAY_MINUTE_MILISECOND.
     * This string is used in naming the PDF
     * reports, so that the generated reports
     * won't have the same name.
     * @return String containing some time information
     */
    private static String getTimeString() {
        Calendar currentTime = Calendar.getInstance();
        String timeString = currentTime.get(Calendar.DAY_OF_MONTH) + "_" +
                currentTime.get(Calendar.HOUR_OF_DAY) +
                "_" + currentTime.get(Calendar.MINUTE) + "_" +
                currentTime.get(Calendar.MILLISECOND);
        return timeString;
    }

    /**
     * Method used to write a table inside
     * a PDF document.
     * @param document Document in which the user wishes to write
     * @param list List of objects the user wants to populate the table with
     */
    private static void writeTable(Document document, List list) {
        Class cls = list.get(0).getClass();
        PdfPTable table = new PdfPTable(cls.getDeclaredFields().length);
        addTableHeader(table, cls);
        addRows(table, list);
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overloaded writeDocument(Document,List) method
     * in such way that it populates the table using
     * two lists given as arguments.
     * @param document Document in which the user wishes to write
     * @param list1 List of objects the user wants to populate the table with
     * @param list2 List of objects the user wants to populate the table with
     */
    private static void writeTable(Document document, List list1, List list2) {
        Class cls1 = list1.get(0).getClass();
        Class cls2 = list2.get(0).getClass();
        PdfPTable table = new PdfPTable(cls1.getDeclaredFields().length + cls2.getDeclaredFields().length);
        addTableHeader(table, cls1);
        addTableHeader(table, cls2);
        addRows(table, list1, list2);
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates a PdfPTable's headers with the
     * fields of a Class object passed as an argument.
     * @param table PdfPTable to whom headers will be added
     * @param cls Class object to get the fields from
     */
    private static void addTableHeader(PdfPTable table, Class cls) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(3);
            header.setPhrase(new Phrase(field.getName()));
            table.addCell(header);
        }
    }

    /**
     * Populates a PdfPTable passed as argument with
     * the values of the fields contained by the objects
     * in the list passed as arguments.
     * @param table PdfPTable to be populated
     * @param list List of objects used to populate the table
     */
    private static void addRows(PdfPTable table, List list) {
        Class cls = list.get(0).getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) field.setAccessible(true);
        for (Object listObject : list) {
            for (Field field : fields) {
                try {
                    String cellString = field.get(listObject).toString();
                    table.addCell(cellString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Overloaded addRows(PdfPTable, List) so that it
     * works with two lists of objects.
     * @param table PdfPTable to be populated
     * @param list1 List of objects used to populate the table
     * @param list2 List of objects used to populate the table
     */
    private static void addRows(PdfPTable table, List list1, List list2) {
        Class cls1 = list1.get(0).getClass();
        Class cls2 = list2.get(0).getClass();
        Field[] fields1 = cls1.getDeclaredFields();
        Field[] fields2 = cls2.getDeclaredFields();
        for (Field field : fields1) field.setAccessible(true);
        for (Field field : fields2) field.setAccessible(true);
        for(int i = 0; i < list1.size(); i++) {
            for (Field field : fields1) {
                try {
                    String cellString = field.get(list1.get(i)).toString();
                    table.addCell(cellString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Field field : fields2) {
                try {
                    String cellString = field.get(list2.get(i)).toString();
                    table.addCell(cellString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


