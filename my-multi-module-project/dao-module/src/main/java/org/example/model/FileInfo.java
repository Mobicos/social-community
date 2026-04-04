package org.example.model;

import lombok.Data;

import java.util.Date;

@Data
public class FileInfo {
    private Long id;
    private String fileUrl;
    private String fileType;
    private String fileMd5;
    private Date createTime;
}
