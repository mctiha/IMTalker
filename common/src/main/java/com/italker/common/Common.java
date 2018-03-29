package com.italker.common;

/**
 * Created by mth on 2018/3/17.
 */

public class Common {
    public interface Constance{
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";
        String API_URL = "http://192.168.31.232:8080/api/";
        // 最大的上传图片大小860kb
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;

    }
}
