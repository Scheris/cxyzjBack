package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelDetail;
import com.cxyzj.cxyzjback.Repository.Article.ArticleLabelJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleLabelService;
import com.cxyzj.cxyzjback.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 15:14
 * @Description: 文章标签系统的API
 * @checked false
 */

@Service
public class ArticleLabelServiceImpl implements ArticleLabelService {

    private final ArticleLabelJpaRepository articleLabelJpaRepository;

    private Response response;

    @Autowired
    public ArticleLabelServiceImpl(ArticleLabelJpaRepository articleLabelJpaRepository) {
        this.articleLabelJpaRepository = articleLabelJpaRepository;
    }

    @Override
    public String labelDetails() {
        response = new Response();
        ArrayList<ArticleLabelDetail> articleLabelDetails = new ArrayList<>();
        List<ArticleLabel> articleLabels = articleLabelJpaRepository.findAll();

        for (ArticleLabel articleLabel : articleLabels) {
            ArticleLabelDetail articleLabelDetail = new ArticleLabelDetail(articleLabel);
            articleLabelDetails.add(articleLabelDetail);
        }

        response.insert("label", articleLabelDetails);
        return response.sendSuccess();
    }
}
