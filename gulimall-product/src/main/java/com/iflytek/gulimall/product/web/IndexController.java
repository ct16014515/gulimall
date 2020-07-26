package com.iflytek.gulimall.product.web;

import com.iflytek.gulimall.product.entity.CategoryEntity;
import com.iflytek.gulimall.product.service.CategoryService;
import com.iflytek.gulimall.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 跟页面相关的放入web文件夹
 */
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;


    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {
        //查询一级分类
        List<CategoryEntity> categories = categoryService.findFirstCategory();
        model.addAttribute("categories", categories);
        //thymeleaf默认前缀是classpath:/templates/ 默认后缀 .html 视图解析器会生成此路径
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
