   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.servlet.config.annotation.CorsRegistry;
   import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
   import org.springframework.lang.NonNull;


   @Configuration
   public class WebConfig implements WebMvcConfigurer {

       @Override
       public void addCorsMappings(@NonNull CorsRegistry registry) {
           registry.addMapping("/**")
                   .allowedOrigins("http://localhost:3000") // Replace with your React app's URL
                   .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific methods
                   .allowedHeaders("*") // Allow all headers
                   .allowCredentials(true); // Allow cookies and authentication
       }
   }
