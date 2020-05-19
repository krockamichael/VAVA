package com.example.Autoservis;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Stream;

public class PDFSampleMain {

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                args = new String[13];
                args[0] = "temp1";
                args[1] = "temp2";
                args[2] = "temp3";
                args[3] = "temp4";
                args[4] = "temp4";
                args[5] = "0";
                args[6] = "temp4";
                args[7] = "temp4";
                args[8] = "ola is me";
                args[9] = "ola is me";
                args[10] = "ola is me";
                args[11] = "ola is me";
                args[12] = "ola is me";
            }
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("repair.pdf"));
            document.open();

            // start adding content into document
            addTitle(document);
            addCarDetails(document, args);
            addTable(document, args);
            addRepairDescription(document, args);
            addTotalCost(document, args);
            addWhiteSpace(document);
            addDateAndPlace(document);

            document.close();
            System.out.println("Generated PDF.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTitle(Document layoutDocument) throws Exception {
        Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);
        Paragraph title = new Paragraph("Motor Vehicle Repair Bill\n\n", titleFont);  // static title text
        title.setAlignment(Element.ALIGN_CENTER);
        layoutDocument.add(title);
    }

    private static void addCarDetails(Document doc, String[] args) throws Exception {
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph car_brand_company_name = new Paragraph("               Car brand:\t " + args[0]); // car brand
        car_brand_company_name.add(new Chunk(glue));
        car_brand_company_name.add("Malik Industries Autoservis s.r.o               ");
        doc.add(car_brand_company_name);

        Paragraph car_model_street_strnum = new Paragraph("               Car model:\t " + args[1]); // car model
        car_model_street_strnum.add(new Chunk(glue));
        car_model_street_strnum.add("Nizkozemska 98, Bratislava               ");
        doc.add(car_model_street_strnum);

        Paragraph vin_num_country = new Paragraph("               VIN number:\t " + args[2]); // VIN number
        vin_num_country.add(new Chunk(glue));
        vin_num_country.add("Slovak Republic                ");
        doc.add(vin_num_country);

        doc.add(new Paragraph("               Fuel type:\t " + args[12])); // Fuel type
        doc.add(new Paragraph("               Customer name:\t " + args[9]));
        doc.add(new Paragraph("               Customer e-mail:\t " + args[10]));
        doc.add(new Paragraph("               Customer phone number:\t " + args[11] + "\n\n"));
    }

    private static void addTable(Document doc, String[] args) throws Exception {
        PdfPTable table = new PdfPTable(5);
        Stream.of("Item no.", "Mechanic", "Start day", "End day", "Days total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("1"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);
        table.addCell(args[7]);  // mechanic name + surname
        table.addCell(args[3]);  // start day
        table.addCell(args[4]);  // end day

        int days_total = Integer.parseInt(args[5]) + 1;  // number of total days, but + 1
        PdfPCell total_days = new PdfPCell(new Phrase(String.valueOf(days_total)));
        total_days.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(total_days);

        doc.add(table);
        doc.add(new Paragraph("\n"));
    }

    private static void addRepairDescription(Document doc, String[] args) throws Exception {
        PdfPTable table = new PdfPTable(1);
        Stream.of("Repair description")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
        PdfPCell repair_desc = new PdfPCell(new Phrase(args[8])); // repair description from DB
        repair_desc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(repair_desc);
        doc.add(table);
    }

    private static void addTotalCost(Document doc, String[] args) throws Exception {
        Font font = new Font(Font.FontFamily.UNDEFINED, 14, Font.BOLD);
        Paragraph total_cost = new Paragraph("\nTotal cost of repair: " + args[6] + " €", font);
        total_cost.setAlignment(Element.ALIGN_CENTER);
        doc.add(total_cost);
    }

    private static void addWhiteSpace(Document doc) throws Exception {
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
    }

    private static void addDateAndPlace(Document doc) throws Exception {
        DateFormat df = new SimpleDateFormat("dd.MM.");
        String dateAsString = df.format(Calendar.getInstance().getTime()); // get current date and format it
        Paragraph datePar = new Paragraph("Bratislava, " + dateAsString);
        datePar.setAlignment(Element.ALIGN_RIGHT);
        doc.add(datePar);
    }
}
