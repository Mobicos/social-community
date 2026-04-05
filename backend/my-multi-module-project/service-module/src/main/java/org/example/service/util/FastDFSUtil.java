package org.example.service.util;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.netty.util.internal.StringUtil;
import org.example.model.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class FastDFSUtil {

    @Autowired
    private AppendFileStorageClient appendFileStorageClient; // FastDFS 客户端，用于文件上传和修改

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    // 文件分片大小（3MB）
    private static final int FILE_SLICE_SIZE = 1024 * 1024 * 10;

    // Redis 中存储文件路径的键前缀
    private static final String FILE_PATH_KEY_PREFIX = "file-path-key:";

    // Redis 中存储已上传文件大小的键前缀
    private static final String UPLOADED_FILE_SIZE_KEY_PREFIX = "uploaded-file-size-key:";

    // Redis 中存储已上传分片数量的键前缀
    private static final String UPLOADED_SLICE_COUNT_KEY_PREFIX = "uploaded-slice-count-key:";

    // FastDFS 默认分组
    private static final String DEFAULT_FASTDFS_GROUP = "group1";

    @Value("${fdfs.http.storage-addr}")
    private String httpFdfsStorageAddr;

    private final RedisUtils redisUtils;

    @Autowired
    public FastDFSUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisUtils = new RedisUtils(redisTemplate);
    }

    /**
     * 将大文件分割为多个固定大小的分片文件
     * @param multipartFile 上传的文件
     * @throws Exception 文件操作异常
     */
    public void convertFileToSlices(MultipartFile multipartFile) throws Exception {
        String fileType = getFileType(multipartFile); // 获取文件类型
        File file = this.multipartFileToFile(multipartFile); // 将 MultipartFile 转换为临时文件
        long fileLength = file.length(); // 获取文件总大小
        int count = 1; // 分片编号
        for (int i = 0; i < fileLength; i += FILE_SLICE_SIZE) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"); // 随机访问文件
            randomAccessFile.seek(i); // 定位到当前分片的起始位置
            byte[] bytes = new byte[FILE_SLICE_SIZE]; // 创建缓冲区
            int len = randomAccessFile.read(bytes); // 读取分片内容
            String path = "G:\\code_read\\my-multi-module-project\\" + count + "." + fileType; // 分片文件路径
            File slice = new File(path); // 创建分片文件
            FileOutputStream fos = new FileOutputStream(slice); // 创建文件输出流
            fos.write(bytes, 0, len); // 写入分片内容
            fos.close(); // 关闭输出流
            randomAccessFile.close(); // 关闭随机访问文件
            count++; // 分片编号递增
        }
        file.delete(); // 删除临时文件
    }

    /**
     * 将 MultipartFile 转换为本地临时文件
     * @param multipartFile 上传的文件
     * @return 临时文件
     * @throws Exception 文件操作异常
     */
    public File multipartFileToFile(MultipartFile multipartFile) throws Exception {
        String originalFileName = multipartFile.getOriginalFilename(); // 获取原始文件名
        String[] fileName = originalFileName.split("\\."); // 分割文件名和扩展名
        File file = File.createTempFile(fileName[0], "." + fileName[1]); // 创建临时文件
        multipartFile.transferTo(file); // 将 MultipartFile 内容写入临时文件
        return file;
    }

    /**
     * 上传文件分片到 FastDFS，并支持断点续传
     * @param file 当前分片文件
     * @param fileMd5 文件的 MD5 值，用于标识文件
     * @param sliceNo 当前分片编号
     * @param totalSliceNo 总分片数量
     * @return 文件在 FastDFS 中的最终路径（所有分片上传完成后返回）
     * @throws Exception 文件上传异常
     */
    public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        if (file == null || sliceNo == null || totalSliceNo == null) {
            throw new ConditionException("参数异常！");
        }

        // 构造 Redis 中存储的键
        String filePathKey = FILE_PATH_KEY_PREFIX + fileMd5; // 文件路径键
        String uploadedFileSizeKey = UPLOADED_FILE_SIZE_KEY_PREFIX + fileMd5; // 已上传文件大小键
        String uploadedSliceCountKey = UPLOADED_SLICE_COUNT_KEY_PREFIX + fileMd5; // 已上传分片数量键

        String uploadedSliceCountStr = redisUtils.get(uploadedSliceCountKey);
        // 使用 Optional 包装字符串，并处理可能的 null 和格式错误
        Integer uploadedSliceCount = Optional.ofNullable(uploadedSliceCountStr)
                .map(Integer::valueOf) // 尝试将字符串转换为整数
                .orElse(0); // 如果字符串为 null 或转换失败，返回默认值 0
        if(uploadedSliceCount >= sliceNo){
            throw new ConditionException("分片 " + sliceNo + " 已经上传过了！");
        }

        // 获取已上传的文件大小
        String uploadedFileSizeStr = redisUtils.get(uploadedFileSizeKey);
        Long uploadedFileSize = 0L;
        if (!StringUtil.isNullOrEmpty(uploadedFileSizeStr)) {
            uploadedFileSize = Long.valueOf(uploadedFileSizeStr);
        }

        if (sliceNo == 1) { // 如果是第一个分片
            String path = this.uploadAppenderFile(file); // 上传第一个分片，创建可追加的文件
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败！");
            }
            redisUtils.set(filePathKey, path);// 存储文件路径
            redisUtils.set(uploadedSliceCountKey, "1");// 初始化已上传分片数量
        } else {
            String filePath = redisUtils.get(filePathKey);// 获取文件路径
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new ConditionException("上传失败！");
            }
            this.modifyAppenderFile(file, filePath, uploadedFileSize); // 追加当前分片内容
            redisUtils.increment(uploadedSliceCountKey);// 增加已上传分片数量
        }

        // 更新已上传的文件大小
        uploadedFileSize += file.getSize();
        redisUtils.set(uploadedFileSizeKey, String.valueOf(uploadedFileSize));

        // 如果所有分片都已上传完成
        uploadedSliceCountStr = redisUtils.get(uploadedSliceCountKey);
        uploadedSliceCount = Integer.valueOf(uploadedSliceCountStr);
        String resultPath = "";
        if (uploadedSliceCount.equals(totalSliceNo)) {
            resultPath = redisUtils.get(filePathKey);// 获取文件最终路径
            // 使用 ArrayList 来存储需要删除的 Redis 键
            List<String> keyList = new ArrayList<>();
            keyList.add(uploadedSliceCountKey);
            keyList.add(filePathKey);
            keyList.add(uploadedFileSizeKey);
            // 删除 Redis 中的相关记录
            redisUtils.delete(keyList);
        }
        return resultPath;
    }

    /**
     * 上传第一个分片，创建可追加的文件
     * @param file 第一个分片文件
     * @return 文件在 FastDFS 中的路径
     * @throws Exception 文件上传异常
     */
    public String uploadAppenderFile(MultipartFile file) throws Exception {
        String fileType = getFileType(file); // 获取文件类型
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_FASTDFS_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath(); // 返回文件路径
    }

    /**
     * 向已上传的文件中追加内容
     * @param file 当前分片文件
     * @param filePath 文件在 FastDFS 中的路径
     * @param offset 偏移量（已上传的文件大小）
     * @throws Exception 文件操作异常
     */
    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception {
        appendFileStorageClient.modifyFile(DEFAULT_FASTDFS_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    /**
     * 获取文件类型（扩展名）
     * @param file 文件
     * @return 文件类型
     */
    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename(); // 获取文件名
        int index = fileName.lastIndexOf("."); // 查找扩展名位置
        return fileName.substring(index + 1); // 返回扩展名
    }

    /**
     * 实现基于分片的在线视频播放
     * 通过 HTTP Range 请求实现视频的分段加载和播放
     *
     * @param request  HTTP 请求对象，用于获取客户端的请求信息
     * @param response HTTP 响应对象，用于向客户端返回响应数据
     * @param path     视频文件在 FastDFS 中的存储路径
     * @throws Exception 如果发生异常，如文件查询失败或网络问题等
     */
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String path) throws Exception {
        // 查询文件信息，获取文件的总大小
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_FASTDFS_GROUP, path);
        long totalFileSize = fileInfo.getFileSize();

        // 构造文件的完整访问 URL
        String url = httpFdfsStorageAddr + path;

        // 获取请求头中的所有字段名称，并存储到 Map 中，便于后续使用
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }

        // 获取请求头中的 Range 字段，该字段用于指定请求的字节范围
        String rangeStr = request.getHeader("Range");
        String[] range;

        // 如果 Range 字段为空，则默认请求整个文件的范围（从第 0 个字节到文件末尾）
        if (StringUtil.isNullOrEmpty(rangeStr)) {
            rangeStr = "bytes=0-" + (totalFileSize - 1);
        }

        // 将 Range 字段的值按 "bytes=" 和 "-" 分割，提取范围的起始和结束字节
        range = rangeStr.split("bytes=|-");

        // 解析范围的起始字节
        long begin = 0;
        if (range.length >= 2) {
            begin = Long.parseLong(range[1]);
        }

        // 解析范围的结束字节
        long end = totalFileSize - 1;
        if (range.length >= 3) {
            end = Long.parseLong(range[2]);
        }

        // 计算请求的字节长度
        long len = (end - begin) + 1;

        // 构造 Content-Range 响应头的值，格式为 "bytes <start>-<end>/<total>"
        String contentRange = "bytes " + begin + "-" + end + "/" + totalFileSize;

        // 设置响应头，告知客户端返回的字节范围
        response.setHeader("Content-Range", contentRange);

        // 设置响应头，表明服务器支持字节范围请求
        response.setHeader("Accept-Ranges", "bytes");

        // 设置响应头，指定返回的内容类型为视频文件
        response.setHeader("Content-Type", "video/mp4");

        // 设置响应的长度为请求的字节长度
        response.setContentLength((int) len);

        // 设置响应状态码为 206，表示返回的是部分内容
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        // 将请求转发到 FastDFS 的 HTTP 服务，由 FastDFS 返回指定范围的视频内容
        // 传递客户端请求的头信息和响应对象
        HttpUtil.get(url, headers, response);
    }
}