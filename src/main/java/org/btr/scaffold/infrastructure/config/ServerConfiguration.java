package org.btr.scaffold.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 服务器配置
 */
@Configuration
public class ServerConfiguration
{
  @Bean
  ObjectMapper objectMapper()
  {
    val objectMapper = new ObjectMapper();
    // vavr模块注册
    objectMapper.registerModule(new VavrModule());
    // JSON蛇形命名
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    // 非null非""字段才参与序列化
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    // 反序列化忽略实体没有的字段
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    // Java8TimeApi处理
    val timeModule = new JavaTimeModule();
    timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    objectMapper.registerModule(timeModule);

    return objectMapper;
  }

  @Bean
  HttpMessageConverters jacksonMessageConverter(ObjectMapper objectMapper)
  {
    return new HttpMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper));
  }
}
