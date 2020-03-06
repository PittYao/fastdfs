package com.yao.demo.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@SpringBootTest
class FastDfsApplicationTests {

    @Test
    void contextLoads() {
    }

   @Test
   public void test(){
       System.out.println(1);
   }

    // 上传文件
    @Test
    public void testUpload2dfs() throws IOException, MyException {
        ClientGlobal.initByProperties("fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;

        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
        // 文件的附加元数据
        NameValuePair pairs[] = null;
        // 上传文件数据
        String filedId = client1.upload_appender_file1("C:\\Users\\fanya\\Pictures\\instgarm\\666.jpg", "jpg", pairs);
        System.out.println(filedId);
        // 文件id group1/M00/00/00/i4FmFl5ht5-EIcyhAAAAAAAnkuA594.jpg
    }


    // 下载文件
    @Test
    public void testDownLoad4dfs() throws IOException, MyException {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            // 上传时返回的文件id
            byte[] bytes = client.download_file1("group1/M00/00/00/i4FmFl5ht5-EIcyhAAAAAAAnkuA594.jpg");
            FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\fanya\\Pictures\\instgarm\\777.jpg"));

            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成token,用于在访问文件时使用token，无token不能访问
    // eg: http://139.129.102.22/group1/M00/00/00/i4FmFl5ht5-EIcyhAAAAAAAnkuA594.jpg?token=a251aaa62a336a7e03c5d6cdcc552435&ts=1583475385
    @Test
    public void testGetToken() throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
        int ts = (int) Instant.now().getEpochSecond();
        // 文件id,时间戳,fastDfs的密钥
        String token = ProtoCommon.getToken("M00/00/00/i4FmFl5ht5-EIcyhAAAAAAAnkuA594.jpg", ts, "FastDFS1234567890");
        StringBuilder sb = new StringBuilder();
        sb.append("?token=").append(token);
        sb.append("&ts=").append(ts);
        System.out.println(sb.toString());
        // ?token=a251aaa62a336a7e03c5d6cdcc552435&ts=1583475385
    }

}
