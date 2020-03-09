package com.yao.demo.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author: bugProvider
 * @date: 2020/3/9 12:45
 * @description:
 */
@Controller
public class UploadController {
    // 上传文件到临时文件夹下  已天为文件夹分隔
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @PostMapping("/import")
    public String importData(MultipartFile file, HttpServletRequest req) throws IOException {
        String format = sdf.format(new Date());
        String realPath = req.getServletContext().getRealPath("/upload") + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file.transferTo(new File(folder, newName));
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
        System.out.println(url);
        return "ok";
    }

    @PostMapping("/2dfs")
    public String upload2Dfs(MultipartFile file, HttpServletRequest req) throws IOException, MyException {
        String format = sdf.format(new Date());

        String oldName = file.getOriginalFilename();
        String suffix = oldName.substring(oldName.lastIndexOf(".") + 1);

        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
        System.out.println(url);

        byte[] bytesData = file.getBytes();

        ClientGlobal.initByProperties("fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;

        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
        // 文件的附加元数据
        NameValuePair pairs[] = null;
        // 上传文件数据
        String filedId = client1.upload_appender_file1(bytesData, suffix, pairs);
        System.out.println(filedId);

        return "ok";
    }
}
