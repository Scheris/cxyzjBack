package com.cxyzj.cxyzjback.Controller.Article;


import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * @Description 写文章
     */
    @PostMapping(value = "/write")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')  and principal.username.equals(#user_id)")
    public String writeArticle(@RequestParam String title, @RequestParam String text, @RequestParam String type_id,
                               @RequestParam String article_sum, @RequestParam String thumbnail,
                               @RequestParam int status_id, @RequestParam String user_id){
        return articleService.writeArticle(title, text, type_id, article_sum, thumbnail, status_id, user_id);
    }

    /**
     * @Description 获取文章类别
     */
    @GetMapping(value = "/types")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getTypes(){
        return articleService.getTypes();
    }

    /**
     * @Description 获取文章详情
     */
    @GetMapping(value = "/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String articleDetails(@PathVariable(name = "article_id") String article_id){
        return articleService.articleDetails(article_id);
    }

    /**
     * @Description 发表评论
     */
    @PutMapping(value = "/collect/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String collect(@PathVariable(name = "article_id") String article_id){
        return articleService.collect(article_id);
    }

    /**
     * @Description 收藏文章
     */
    @DeleteMapping(value = "/collect/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String collectDel(@PathVariable(name = "article_id") String article_id){
        return articleService.collectDel(article_id);
    }

    /**
     * @Description 删除文章
     */
    @DeleteMapping(value = "/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS') and principal.username.equals(#user_id)")
    public String articleDel(@PathVariable(name = "article_id") String article_id,  @RequestParam String user_id){
        return articleService.articleDel(article_id, user_id);
    }

    /**
     * @Description 更新文章
     */
    @PostMapping(value = "/update/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS') and principal.username.equals(#user_id)")
    public String articleUpdate(@PathVariable(value = "article_id") String article_id,
                                @RequestParam String thumbnail,@RequestParam String user_id, @RequestParam String title,
                                @RequestParam String text, @RequestParam String article_sum, @RequestParam String label_id,
                                @RequestParam int status_id){
        return articleService.articleUpdate(article_id, title, text, article_sum, label_id, thumbnail, status_id);
    }

    /**
     * @Description
     */
    @GetMapping(value = "/draft_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String draftList(@PathVariable(name = "{page_num}") int pageNum){
        return articleService.draftList(pageNum);
    }


    /**
     * @Description 访问文章
     */
    @PutMapping(value = "/visit/{article_id}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String visitArticle(@PathVariable(name = "article_id") String article_id){
        return articleService.visitArticle(article_id);
    }


}
