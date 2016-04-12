package com.redmonkeysoftware.sitescraper.web.controllers;

import com.redmonkeysoftware.sitescraper.logic.Link;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

public class ScrapeReportView extends AbstractXlsxStreamingView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Scrape scrape = (Scrape) model.get("scrape");
        Sheet successfulSheet = workbook.createSheet("Successful");
        successfulSheet.setDefaultColumnWidth(30);
        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        // create header row
        Row successfulHeader = successfulSheet.createRow(0);
        successfulHeader.createCell(0).setCellValue("Link");
        successfulHeader.getCell(0).setCellStyle(style);
        successfulHeader.createCell(1).setCellValue("Email");
        successfulHeader.getCell(1).setCellStyle(style);

        int successfulRowNumber = 1;
        for (Link link : scrape.getSuccessfulLinks()) {
            int innerRowNumber = 0;
            for (String email : link.getEmails()) {
                Row row = successfulSheet.createRow(successfulRowNumber++);
                row.createCell(0);
                row.getCell(0).setCellValue(innerRowNumber > 0 ? "" : link.getUrl());
                row.createCell(1);
                row.getCell(1).setCellValue(email);
                innerRowNumber++;
            }
        }

        Sheet failedSheet = workbook.createSheet("Failed");
        failedSheet.setDefaultColumnWidth(30);
        Row failedHeader = failedSheet.createRow(0);
        failedHeader.createCell(0).setCellValue("Link");
        failedHeader.getCell(0).setCellStyle(style);
        int failedRowNumber = 1;
        for (Link link : scrape.getFailedLinks()) {
            Row row = failedSheet.createRow(failedRowNumber++);
            row.createCell(0);
            row.getCell(0).setCellValue(link.getUrl());
        }
    }

}
