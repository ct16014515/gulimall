package com.iflytek.gulimall.search.controller;


import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.search.service.MallSearchService;
import com.iflytek.gulimall.search.vo.SearchParam;

import com.iflytek.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * catalog3Id=167&keyword=手机&sort=saleCount_desc&brandId=1 可以直接用对象接收
     *
     * @param paramEo
     * @return
     */
    @GetMapping({"/", "list.html"})
    public String list(SearchParam paramEo, Model model) throws IOException {
        System.out.println(paramEo);
        SearchResult searchResult = mallSearchService.search(paramEo);
        ResultBody<SearchResult> resultBody;
        if (searchResult!=null){
            resultBody=new ResultBody<>(searchResult);
        }else {
            resultBody=new ResultBody<>(GulimallExceptinCodeEnum.PRODUCT_SEARCH_ERROR,null);
        }
        model.addAttribute("resultBody",resultBody);
        return "list";
    }


}
