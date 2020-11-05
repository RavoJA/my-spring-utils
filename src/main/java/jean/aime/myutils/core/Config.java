package jean.aime.myutils.core;


import com.mdg.it.annotation.AnnotationServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public AnnotationServiceImpl myAnnotation() {
        return new AnnotationServiceImpl();
    }
}
