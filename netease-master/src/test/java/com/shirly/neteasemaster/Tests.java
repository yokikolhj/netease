package com.shirly.neteasemaster;

import com.shirly.neteasemaster.function.aop.service.SPAPrincess;
import com.shirly.neteasemaster.function.aop.service.SPAService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author shirly
 * @Date 2020/1/21 14:30
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {

    @Test
    public void test() {
        SPAService service = new SPAPrincess();
        service.aromaOilMassage("shirly");
        service.aromaOilMassage("mike");
    }
}
