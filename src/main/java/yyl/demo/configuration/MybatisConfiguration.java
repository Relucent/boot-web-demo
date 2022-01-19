package yyl.demo.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;

/**
 * _Mybatis 配置
 */
@Configuration
@AutoConfigureBefore(PageHelperAutoConfiguration.class)
public class MybatisConfiguration {

    @Value("${pagehelper.banner:false}")
    private boolean pagehelperBanner;

    @PostConstruct
    public void initialize() {
        // 禁用掉 _pagehelper 插件的 banner
        System.setProperty("pagehelper.banner", Boolean.toString(pagehelperBanner));
    }
}
