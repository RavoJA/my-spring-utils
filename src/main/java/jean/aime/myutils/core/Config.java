package jean.aime.myutils.core;

import jean.aime.myutils.annotation.FirstUpperCaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public FirstUpperCaseService upperCase() {
        return new FirstUpperCaseService();
    }
}
