package com.iflytek.gulimall.product.web;

import com.iflytek.gulimall.common.feign.SeckillServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SecSessionSkuVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.product.entity.CategoryEntity;
import com.iflytek.gulimall.product.service.CategoryService;
import com.iflytek.gulimall.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    SeckillServiceAPI seckillServiceAPI;


    @GetMapping({"/", "index.html"})
    public String indexPage(Model model, HttpSession session) {
        //查询一级分类
        List<CategoryEntity> categories = categoryService.findFirstCategory();
        model.addAttribute("categories", categories);
        //查询当前时间的秒杀商品,如果秒杀商品挂了,会影响首页展示,应从前端发送ajax请求
        //ResultBody<List<SecSessionSkuVO>> resultBody = seckillServiceAPI.getCurrentSecSessionSkuVO();
        //List<SecSessionSkuVO> secSessionSkuVOS = resultBody.getData();
        //model.addAttribute("secSessionSkuVOS", secSessionSkuVOS);
        session.getAttribute("loginUser");
        System.out.println(session.getAttribute("loginUser"));
        return "index";
    }

    /**
     * 查询分类
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/json/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalog() {
        long start = System.currentTimeMillis();
        Map<String, List<Catalog2Vo>> catalogs = categoryService.getCatalog();
        System.out.println("执行时间:" + (System.currentTimeMillis() - start) + "毫秒");
        return catalogs;
    }


}
