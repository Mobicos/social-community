package org.example.service;

import io.netty.util.internal.StringUtil;
import org.example.dao.FileInfoDao;
import org.example.model.FileInfo;
import org.example.service.util.FastDFSUtil;
import org.example.service.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class FileService {

    @Autowired
    private FileInfoDao fileInfoDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    public String uploadFileBySlices(MultipartFile slice,
                                     String fileMD5,
                                     Integer sliceNo,
                                     Integer totalSliceNo) throws Exception {
        FileInfo dbFileMD5 = fileInfoDao.getFileByMD5(fileMD5);
        if(dbFileMD5 != null){
            return dbFileMD5.getFileUrl();
        }
        String url = fastDFSUtil.uploadFileBySlices(slice, fileMD5, sliceNo, totalSliceNo);
        if(!StringUtil.isNullOrEmpty(url)){
            dbFileMD5 = new FileInfo();
            dbFileMD5.setCreateTime(new Date());
            dbFileMD5.setFileMd5(fileMD5);
            dbFileMD5.setFileUrl(url);
            dbFileMD5.setFileType(fastDFSUtil.getFileType(slice));
            fileInfoDao.insertFile(dbFileMD5);
        }
        return url;
    }

    public String getFileMD5(MultipartFile file) throws Exception {
        return MD5Util.getFileMD5(file);
    }

}
