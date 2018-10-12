package com.cxyzj.cxyzjback;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Utils.ListToMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
public class CxyzjbackApplicationTests {
    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.context).build()初始化。

    @Autowired
    private WebApplicationContext context; // 注入WebApplicationContext
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ArticleJpaRepository articleJpaRepository;

    @Before // 在测试开始前初始化工作
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * @Author yaser
     * @desc 测试接口 获取用户列表
     */
    @Test
    public void testQ1() throws Exception {//接口测试
        String res = mockMvc.perform(MockMvcRequestBuilders.get("/v1/front/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        log.info(res);
    }

    @Test
    public void testQ2() {
        List<Article> articles = articleJpaRepository.findAll();
        ListToMap<Article> articleMap = new ListToMap<>();
        HashMap<String, Article> map = articleMap.getMap(articles, "articleId", Article.class);
        System.out.println(map);

    }

}
