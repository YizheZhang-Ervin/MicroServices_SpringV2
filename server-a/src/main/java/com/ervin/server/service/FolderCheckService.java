package com.ervin.server.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FolderCheckService {
    // 生成目录MD5 or 比较目录MD5
    String type;
    // 编码格式
    String format;
    // 文件:MD5
    Map<String,String> codeMap = new HashMap<>();
    // 已遍历个数-记录是否缺文件
    long traverseCount = 0;
    // 结果集
    Map<String,String> resultMap = new HashMap<>();

    // 目录检核入口：
    public String checkFolder(String path1, String format,String type){
        this.type = type;
        this.format = format;
        // 遍历文件夹=>每个文件对比
        traverseFolder(path1);
        System.out.println("Now Files:"+traverseCount);
        System.out.println("Recorded Files:"+resultMap.size());
        if(traverseCount!=resultMap.size()){
            System.out.println("Has Loss File");
        }else{
            if(resultMap.containsValue("NONE")){
                System.out.println("Has Extra File");
            }else if(resultMap.containsValue("DIFF")){
                System.out.println("Has different File");
            }else{
                System.out.println("Files are the same");
            }
        }
        if("generate".equalsIgnoreCase(type)){
            return "Generate OK";
        }else{
            return JSONObject.toJSONString(resultMap);
        }
    }

    // 加密文件
    public String encodeFile(File f){
        try(InputStream fs = new FileInputStream(f)) {
            String fCode;
            if ("MD5".equalsIgnoreCase(format)) {
                fCode = DigestUtils.md5Hex(fs);
            } else {
                fCode = DigestUtils.sha1Hex(fs);
            }
            return fCode;
        }catch(Exception e){
            System.out.println("Read or Encode File Error: "+e.getMessage());
            return null;
        }
    }

    // 遍历文件夹
    public void traverseFolder(String path){
        // 递归遍历文件夹
        File file1 = new File(path);
        if(file1.exists()){
            File[] file1Lis = file1.listFiles();
            if(!ObjectUtils.isEmpty(file1Lis)){
                for(File f:file1Lis){
                    String pathNow = f.getAbsolutePath();
                    // 是目录
                    if(f.isDirectory()){
                        traverseFolder(pathNow);
                        // 是文件
                    }else{
                        // generate生成MD5/SHA1码
                        if("generate".equalsIgnoreCase(type)){
                            codeMap.put(pathNow,encodeFile(f));
                        // compare对比MD5orSHA1码
                        }else{
                            boolean flag = codeMap.containsKey(pathNow);
                            // 1 如果有过MD5orSHA1码
                            if(flag){
                                String codeFile = codeMap.get(pathNow);
                                // 1.1 如果MD5orSHA1码相同
                                System.out.println("Recorded Code: "+codeFile);
                                System.out.println("Now Code: "+encodeFile(f));
                                if(encodeFile(f).equalsIgnoreCase(codeFile)){
                                    resultMap.put(pathNow,"OK");
                                // 1.2 如果MD5orSHA1码不同
                                }else{
                                    resultMap.put(pathNow,"DIFF");
                                }
                            // 2 如果没有MD5orSHA1码
                            }else{
                                resultMap.put(pathNow,"NONE");
                            }
                            traverseCount+=1;
                        }
                    }
                }
            }else{
                System.out.println("目录为空");
            }
        }else{
            System.out.println("文件不存在");
        }
    }

    // 文件检核入口(逐字节比对)
    public boolean checkFileByBytes(String file1Path,String file2Path){
        // 检核标记
        boolean successFlag = true;

        try(BufferedInputStream file1Stream = new BufferedInputStream(new FileInputStream(file1Path));
            BufferedInputStream file2Stream = new BufferedInputStream(new FileInputStream(file2Path))) {
            // 导入文件
            File file1 = new File(file1Path);
            File file2 = new File(file2Path);

            // 比较文件长度
            if (file1Stream.available() == file2Stream.available()) {
                // 比较文件权限
                if (file1.canExecute() == file2.canExecute() && file1.canRead() == file2.canRead() && file1.canWrite() == file2.canWrite()) {
                    while (file1Stream.read() != -1 && file2Stream.read() != -1) {
                        if (file1Stream.read() != file2Stream.read()) {
                            successFlag = false;
                            break;
                        }
                    }
                } else {
                    successFlag = false;
                }
            } else {
                successFlag = false;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            successFlag = false;
        }

        return successFlag;
    }

    // 建文件入口
    public boolean createFile(String path,String power){
        File f = new File(path);
        boolean flag = true;
        if(!f.exists()){
            if("755".equalsIgnoreCase(power)) {
                flag = f.mkdir();
                f.setExecutable(true, false);
                f.setReadable(true, false);
                f.setWritable(false, false);
                f.setExecutable(true, true);
                f.setReadable(true, true);
                f.setWritable(true, true);
            }else if("775".equalsIgnoreCase(power)) {
                flag = f.mkdir();
                f.setExecutable(true);
                f.setReadable(true);
                f.setWritable(false);
                f.setExecutable(true, true);
                f.setReadable(true, true);
                f.setWritable(true, true);
            }else{
                // 根据系统umask决定
                flag = f.mkdir();
            }
        }
        return flag;
    }
}
