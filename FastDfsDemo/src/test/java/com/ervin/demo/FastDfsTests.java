package com.ervin.demo;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


@SpringBootTest
public class FastDfsTests {

    public StorageClient1 getClient() throws IOException, MyException {
        //加载 fastDFS的配置文件 因为这里使用的是Properties 格式的文件 所以使用initByProperties
        ClientGlobal.initByProperties("fdfs/fastdfs-client.properties");
        System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        System.out.println("charset=" + ClientGlobal.g_charset);
        //创建TrackerClient
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        //创建StorageServer
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
        return client;
    }

    @Test
    public void testUpload() {
        try {
            StorageClient1 client = getClient();
            //文件上传的元信息
            NameValuePair[] metaList = new NameValuePair[1];
            //这里第一个参数随意写，第二个参数是上传文件是命名的文件名
            metaList[0] = new NameValuePair("fileName", "1.png");
            //第一个参数是本地文件的地址，第一个是文件的后缀名，第三个是元信息
            String fileId = client.upload_file1("F:\\code\\1.png", null, metaList);
            System.out.println("upload success. file id is: " + fileId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testQuery() {
        // http访问：http://192.168.137.10:8080/group1/M00/00/00/xxx.jpg
        try {
            StorageClient1 client = getClient();
            //执行查询
            //两行代码是一样的
            //FileInfo fileInfo = client.query_file_info("group1", "M00/00/00/xxx.png");
            FileInfo fileInfo = client.query_file_info1("group1/M00/00/00/xxx.png");
            //查询元信息
            NameValuePair[] metadata1 = client.get_metadata1("group1/M00/00/00/xxx.png");
            //NameValuePair[] metadata2 = client.get_metadata("group1","M00/00/00/xxx.png");
            System.out.println(fileInfo);
            System.out.println("元信息-----");
            System.out.println(Arrays.toString(metadata1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void  testDownload(){
        try {
            StorageClient1 client = getClient();
            //文件下载
            byte[] bytes = client.download_file1("group1/M00/00/00/xxx.png");
            //将文件写出
            File file = new File("f:/abd.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void  testDelete(){
        try {
            StorageClient1 client = getClient();
            //文件删除
            int i = client.delete_file1("group1/M00/00/00/xxx.png");
            if(i>=0){
                System.out.println("删除成功!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
