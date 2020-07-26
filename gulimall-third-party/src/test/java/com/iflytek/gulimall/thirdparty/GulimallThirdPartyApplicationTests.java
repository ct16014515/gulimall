package com.iflytek.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private OSS ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {
        ossClient.putObject("rclin", "aldi.jpg", new FileInputStream("C:\\Users\\rclin\\Desktop\\e22e59c17757255682dc1699d2bbbb0e_259_194.jpg"));
        System.out.println("上传成功");
    }
}
