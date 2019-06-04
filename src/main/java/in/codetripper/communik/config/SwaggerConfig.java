/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package in.codetripper.communik.config;

import in.codetripper.communik.email.EmailController;
import in.codetripper.communik.sms.SmsController;
import in.codetripper.communik.template.NotificationTemplateController;
import java.util.Collections;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
@ComponentScan(basePackageClasses = {EmailController.class, SmsController.class,
        NotificationTemplateController.class})
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).paths(paths()).build().apiInfo(metaInfo());
    }

    private Predicate<String> paths() {
        return Predicate.not(PathSelectors.regex("/actuator.*"));
    }

    private ApiInfo metaInfo() {
        return new ApiInfo("Communik API", "Communik API - centralizes all your notifications",
                "API TOS", "Terms of service",
                new Contact("Code Tripper", "https://github.com/CodeTripper/",
                        "admin@condetripper.in"),
                "MIT", "API license URL", Collections.emptyList());
    }
}
