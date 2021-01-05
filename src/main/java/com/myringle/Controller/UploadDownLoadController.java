package com.myringle.Controller;


import com.myringle.model.User;
import com.myringle.utils.DownloadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
public class UploadDownLoadController {

    @GetMapping("/upload")
    public  void  upload(@RequestParam("file")MultipartFile file) throws IOException {

        File file1 = new File("D:/");
        if(!file1.exists()){
            file1.mkdir();   //如果该目录不存在，就创建此抽象路径名指定的目录。
        }
        String prefix = UUID.randomUUID().toString();
        prefix = prefix.replace("-","");
        String fileName = prefix+"_"+file.getOriginalFilename();//使用UUID加前缀命名文件，防止名字重复被覆盖

        InputStream in= file.getInputStream();;//声明输入输出流

        OutputStream out=new FileOutputStream(new File("d:\\"+"\\"+file.getName()));//指定输出流的位置;

        byte []buffer =new byte[1024];
        int len=0;
        while((len=in.read(buffer))!=-1){
            out.write(buffer, 0, len);
            out.flush();                //类似于文件复制，将文件存储到输入流，再通过输出流写入到上传位置
        }                               //这段代码也可以用IOUtils.copy(in, out)工具类的copy方法完成

        out.close();
        in.close();

    }


    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws Exception {

        ServletContext servletContext = request.getServletContext();
        String fileName="aa.png";
//        String realPath = servletContext.getRealPath("D:\\shengran\\uploadLoadDemo\\src\\main\\resources\\assets\\"+fileName);//得到文件所在位置

        InputStream in=new FileInputStream(new File("D:\\shengran\\uploadLoadDemo\\src\\main\\resources\\assets\\aa.jpg"));//将该文件加入到输入流之中
        byte[] body=null;
        body=new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
        in.read(body);//读入到输入流里面

        fileName=new String(fileName.getBytes("gbk"),"iso8859-1");//防止中文乱码
        HttpHeaders headers=new HttpHeaders();//设置响应头
        headers.add("Content-Disposition", "attachment;filename="+fileName);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }


    /**
     * 下载用户新增表
     * @param inputDate 格式为:2019-01
     */
    @RequestMapping(value = "/printExcel",name = "下载用户新增表")
    public void printExcel(String inputDate, HttpServletRequest req, HttpServletResponse response, HttpSession session) throws Exception {
        //1.用servletContext对象获取excel模板的真实路径
//        String templatePath = session.getServletContext().getRealPath("/dintalk/xlsprint/dtUSER.xlsx");
        String templatePath = "D://test.xlsx";
        //2.读取excel模板,创建excel对象
        XSSFWorkbook wb = new XSSFWorkbook(templatePath);
        //3.读取sheet对象
        Sheet sheet = wb.getSheetAt(0);
        //4.定义一些可复用的对象
        int rowIndex = 0; //行的索引
        int cellIndex = 1; //单元格的索引
        Row nRow = null;
        Cell nCell = null;
        //5.读取大标题行
        nRow = sheet.getRow(rowIndex++); // 使用后 +1
        //6.读取大标题的单元格
        nCell = nRow.getCell(cellIndex);
        //7.设置大标题的内容
        String bigTitle = inputDate.replace("-0","-").replace("-","年")+"月份新增用户表";
//        String bigTitle = "202010"+"月份新增用户表.xlsx";
        nCell.setCellValue(bigTitle);
        //8.跳过第二行(模板的小标题,我们要用)
        rowIndex++;
        //9.读取第三行,获取它的样式
        nRow = sheet.getRow(rowIndex);
        //读取行高
        float lineHeight = nRow.getHeightInPoints();
        //10.获取第三行的5个单元格中的样式
        CellStyle cs1 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs2 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs3 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs4 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs5 = nRow.getCell(cellIndex++).getCellStyle();
        //11.通过月份查询新增用户列表
        List<User> newUserList = new ArrayList<>();
        for (int i = 0; i < 22; i++) {

            newUserList.add(User.builder().id(BigDecimal.valueOf(i)).age(18).sex("男").UserName("钱立博").Email("1139452801@qq.com").Phone("17695512329")
                    .build());

        }

        //12.遍历数据
        for(User user : newUserList){
            //13.创建数据行
            nRow = sheet.createRow(rowIndex++);
            //16.设置数据行高
            nRow.setHeightInPoints(lineHeight);
            //17.重置cellIndex,从第一列开始写数据
            cellIndex = 1;
            //18.创建数据单元格，设置单元格内容和样式
            //用户名
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs1);
            nCell.setCellValue(user.getUserName());
            //性别
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs2);
            nCell.setCellValue(user.getSex());
            //年龄
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs3);
            nCell.setCellValue(user.getAge());
            //手机号
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs4);
            nCell.setCellValue(user.getPhone());
            //邮箱
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs5);
            nCell.setCellValue(user.getEmail());
        }

        //最后，下载新增用户表，字节数组的输出流，它可存可取，带缓冲区
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos); //将工作簿写到输出流中
        DownloadUtil.download(bos,response,bigTitle);
        bos.close();
        wb.close();
    }


    /**
     * 批量导入用户
     * @param companyId
     * @param file
     * @return
     */
    @RequestMapping("/import")
    public String ImportExcel(String companyId, MultipartFile file) throws IOException {
        //定义一个list 集合保存从excel解析的用户
        List<User> list = new ArrayList<>();
        //1.读取上传的文件
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        //2.获取工作表对象
        XSSFSheet sheet = wb.getSheetAt(0);
        //3.得到行的迭代器
        Iterator<Row> iterator = sheet.iterator();
        int rowNum = 0;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            //跳过标题行
            if (rowNum == 0) {
                rowNum++;
                continue;
            }
            //4.遍历,把每一行数据存到Object数组中
            Object[] obj = new Object[6];
            for (int i = 1; i < 6; i++) {
                obj[i] = getValue(row.getCell(i));//获取到单元格内的数据，方法见下
            }
            //5.创建用户对象(用户实体类的有参构造方法)
            User user = User.builder().UserName((String) obj[0]).sex((String) obj[1]).age((Integer) obj[2]).Phone((String) obj[3]).Email((String) obj[4]).build();
            //6.将用户对象保存到集合中
            list.add(user);
        }
        //7.读取完数据后,调用service层方法进行批量保存
//        UserService.saveAll(list);
        //8.重定向到企业列表
        return list.toString();
//        return "redirect:/dintalk/company/list.do";
    }
    /**
     * 获取单元格内的数据,并进行格式转换
     * @param cell
     * @return
     */
    private Object getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:// 数值和日期均是此类型,需进一步判断
                if (DateUtil.isCellDateFormatted(cell)) {
                    //是日期类型
                    return cell.getDateCellValue();
                } else {
                    //是数值类型
                    return cell.getNumericCellValue();
                }
            default:
                return null;
        }
    }
}
