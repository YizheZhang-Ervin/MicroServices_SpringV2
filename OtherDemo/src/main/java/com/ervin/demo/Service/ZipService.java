package com.ervin.demo.Service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {

    public void doZip(HttpServletResponse response){
        try {
            //设置响应头
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("demo.zip".getBytes("GB2312"), StandardCharsets.ISO_8859_1));  // 需要编码否则中文乱码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/zip;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        List<String> fileContent = new ArrayList<>();
        fileContent.add("abc123\n996");
        fileContent.add("def123\\n996");
        fileContent.add("ghi123\n\t996");
        try {
            //创建压缩文件 将文件直接以流的方式相应给前端
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            fileContent.forEach(item -> {
                //解码
                try {
                    //对base64字符串解码成二级制数组
                    ZipEntry zipEntry = new ZipEntry(item.substring(0,3) + ".txt");
                    //将每个文件添加到压缩文件
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(item.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            zipOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
