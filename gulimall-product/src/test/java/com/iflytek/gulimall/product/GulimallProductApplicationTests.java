package com.iflytek.gulimall.product;

import com.aliyun.oss.OSS;
import com.iflytek.gulimall.product.core.OrderDao;
import com.iflytek.gulimall.product.entity.BrandEntity;
import com.iflytek.gulimall.product.service.BrandService;
import com.iflytek.gulimall.product.service.impl.SkuInfoServiceImpl;
import com.iflytek.gulimall.product.vo.SkuItemVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Autowired
    private OSS ossClient;

    @Autowired
    private SkuInfoServiceImpl  skuInfoService;

    @Autowired
    OrderDao orderDao;

    @Test
    public void testUpload() throws FileNotFoundException {
        ossClient.putObject("rclin", "aldi.jpg", new FileInputStream("C:\\Users\\rclin\\Desktop\\e22e59c17757255682dc1699d2bbbb0e_259_194.jpg"));
        System.out.println("上传成功");
    }

    @Test
   public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
    }
    @Test
    public void testFuture() throws ExecutionException, InterruptedException {
       // SkuItemVo skuItem1 = skuInfoService.getSkuItem1(17L);
        SkuItemVo skuItem1 = skuInfoService.getSkuItem(17L);
    }





    @Test
   public void orderTest(){
        orderDao.update();
    }


}
