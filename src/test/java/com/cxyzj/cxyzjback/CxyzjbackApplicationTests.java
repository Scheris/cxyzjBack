package com.cxyzj.cxyzjback;

import com.cxyzj.cxyzjback.Bean.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class CxyzjbackApplicationTests {
    @Test
    public void contextLoads() {
        User user = new User();
        log.info(user.getClass().getSimpleName().toLowerCase());
    }
}
