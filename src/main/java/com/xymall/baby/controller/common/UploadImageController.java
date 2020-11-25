package com.xymall.baby.controller.common;


import com.xymall.baby.Utils.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("utilController")
@Controller
public class UploadImageController {
    /**
     * 上传图片
     *
     * @param request
     * @return Map<String, Object> location(图片要上传的位置)
     * @throws
     * @Title: uploadImage
     * @Description: 上传图片
     * //     * @param
     */
    @RequestMapping("/uploadImage")
    public @ResponseBody
    Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file,
                                    HttpServletRequest request) throws Exception {
        Map<String, Object> ret = new HashMap<>();

        String realPath = request.getSession().getServletContext().getRealPath("/"); //获得真实路径
        String stuId = request.getParameter("stuId");//获得学号
        Integer maxLogId = Integer.parseInt(request.getParameter("maxLogId")); // 获得最大的日志编号
        Integer logId = maxLogId + 1;//当前日志编号
        String location = ImageUtil.uploadImage(file, realPath, stuId, logId);

        ret.put("location", location);

        return ret;
    }
}
