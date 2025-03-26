package com.example.SBEAM.exception;

/**
 * @Author: DingXiaoyu
 * @Date: 0:26 2023/11/26
 * 你可以在这里自定义Exception
*/
public class SBEAMException extends RuntimeException{

    public SBEAMException(String message){
        super(message);
    }
    public static SBEAMException phoneAlreadyExists(){
        return new SBEAMException("手机号已经存在!");
    }
    public static SBEAMException storeNameAlreadyExists(){
        return new SBEAMException("商店名称已经存在!");
    }
    public static SBEAMException productNameAlreadyExists(){
        return new SBEAMException("商品名称已经存在!");
    }
    public static SBEAMException storeNotExists(){
        return new SBEAMException("商店名称不存在!");
    }
    public static SBEAMException notLogin(){
        return new SBEAMException("未登录!");
    }
    public static SBEAMException phoneOrPasswordError(){
        return new SBEAMException("手机号或密码错误!");
    }
    public static SBEAMException fileUploadFail() {
        return new SBEAMException("文件上传失败!");
    }

}
