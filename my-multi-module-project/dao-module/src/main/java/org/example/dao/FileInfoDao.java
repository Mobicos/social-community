package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.FileInfo;

import java.util.List;

@Mapper
public interface FileInfoDao {
    List<FileInfo> getAllFiles();
    FileInfo getFileById(Long id);
    int insertFile(FileInfo file);
    int updateFile(FileInfo file);
    int deleteFile(Long id);
    FileInfo getFileByMD5(String md5);
}
