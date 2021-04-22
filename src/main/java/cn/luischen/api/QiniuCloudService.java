package cn.luischen.api;

import cn.luischen.utils.TaleUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Donghua.Chen on 2018/5/1.
 */
@Component
public class QiniuCloudService {

////    @Value("${qiniu.accesskey}")
//    public String ACCESS_KEY = "VNZ1IJmB2caj7Hb3A_LrW7dZ2dYFERagvYRuN-Zd";
//    @Value("${qiniu.serectkey}")
//    public String SECRET_KEY = "LUBZtjJL8k3jTalM6r4uw4SAoJWbs84O41rDl3ZJ";
//    /**
//     * 仓库
//     */
////    @Value("${qiniu.bucket}")
//    public String BUCKET = "yjlblogtest";
//    /**
//     * 七牛云外网访问地址
//     */
////    @Value("${qiniu.sdn.url}")
    public String QINIU_UPLOAD_SITE="http://qrw7k2eht.hn-bkt.clouddn.com";

    public String upload(MultipartFile file, String fileName) {

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String ACCESS_KEY = "VNZ1IJmB2caj7Hb3A_LrW7dZ2dYFERagvYRuN-Zd";
        String SECRET_KEY = "LUBZtjJL8k3jTalM6r4uw4SAoJWbs84O41rDl3ZJ";
        String BUCKET = "yjlblogtest";
//        String QINIU_UPLOAD_SITE="http://qrw7k2eht.hn-bkt.clouddn.com";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try{
                Response response = uploadManager.put(file.getInputStream(), fileName, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return putRet.key;
            }catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

}
