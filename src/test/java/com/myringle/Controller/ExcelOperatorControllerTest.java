package com.myringle.Controller;


import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelOperatorControllerTest {


    @Test
    public  void  test1(){
        //1.创建Excel对象
        XSSFWorkbook wb = new XSSFWorkbook();
        //2.创建Sheet对象
        Sheet sheet = wb.createSheet();
        //3.创建行对象(索引从0开始)
        Row nRow = sheet.createRow(0);
        //4.设置行高和列宽

        nRow.setHeightInPoints(26.25f);
        sheet.setColumnWidth(1,26*256); //(列的索引,列宽*256(理解为固定写法))
        //5.创建单元格对象(索引从0开始)
        Cell nCell = nRow.createCell(0);
        //6.设置单元格内容
        nCell.setCellValue("dinTalk");
        //==============================
        //7.创建单元格样式对象
        CellStyle style = wb.createCellStyle();
        //8.创建字体对象
        Font font = wb.createFont();
        //9.设置字体和其大小及效果
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        font.setBold(true); //加粗
        //10.设置样式
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);        //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//纵向居中
        style.setBorderTop(BorderStyle.THIN);                //上细线
        style.setBorderBottom(BorderStyle.THIN);            //下细线
        style.setBorderLeft(BorderStyle.THIN);                //左细线
        style.setBorderRight(BorderStyle.THIN);                //右细线
        //11.为单元格应用样式
        nCell.setCellStyle(style);
    }

}
