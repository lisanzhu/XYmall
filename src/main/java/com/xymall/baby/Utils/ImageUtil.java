package com.xymall.baby.Utils;
import java.io.File;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

    public static String uploadImage(MultipartFile image, String basePath, String stuId, Integer logId) throws Exception {
        String ret = "";

        //生成uuid作为文件名称
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //获得文件类型，如果不是图片，禁止上传
        String contentType = image.getContentType();
        //获得文件的后缀名
        String suffixName = contentType.substring(contentType.indexOf("/") + 1);
        //得到文件名
        String imageName = uuid + "." + suffixName;
        //获取文件夹路径
        String direPath = basePath + "uploadImage\\log\\" + stuId + "\\" + logId.toString();
        File direFile = new File(direPath);
        //文件夹如果不存在，新建文件夹
        if (direFile.exists() == false || direFile.isDirectory() == false) {
            direFile.mkdir();
        }
        //得到文件路径
        String path = direPath + "\\" + imageName;

        image.transferTo(new File(path));

        ret = "/Test/uploadImage/log/" + stuId + "/" + logId.toString() + "/" + imageName;

        return ret;

    }
}
