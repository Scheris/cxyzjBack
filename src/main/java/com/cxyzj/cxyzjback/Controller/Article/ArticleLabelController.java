package com.cxyzj.cxyzjback.Controller.Article;

import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:16
 * @Description:
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/article")
public class ArticleLabelController {

    private final ArticleLabelService articleLabelService;

    @Autowired
    public ArticleLabelController(ArticleLabelService articleLabelService) {
        this.articleLabelService = articleLabelService;
    }

    @GetMapping(value = "/labels/details")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String labelDetails() {
        return articleLabelService.labelDetails();
    }

}
