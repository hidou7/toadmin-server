package com.hidou7.toadmin.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.hidou7.toadmin.common.enums.ErrorCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class Knife4jConfig {

    @Bean
    public Docket userApi() {
        // token请求头
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("token").defaultValue("")
                .modelRef(new ModelRef("String")).parameterType("header").required(true).build();
        pars.add(tokenPar.build());
        // 添加全局响应状态码
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(ErrorCode.values()).forEach(error -> 
            responseMessageList.add(new ResponseMessageBuilder().code(error.code).message(error.message)
                    .responseModel(new ModelRef("String")).build()));
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .apiInfo(new ApiInfoBuilder().title("ToAdmin接口文档").build())
                .pathMapping("/")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }
}
