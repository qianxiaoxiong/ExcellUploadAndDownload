package com.myringle.Controller;


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
import java.io.*;
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
}
