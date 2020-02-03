package com.feifei.ddd.demo.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feifei.ddd.demo.application.service.UserService;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.config.ServerConfiguration;
import com.feifei.ddd.demo.infrastructure.constant.ApiConstant;
import com.feifei.ddd.demo.infrastructure.tool.IdWorker;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.vavr.API.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户接口层单元测试用例
 * <p>
 * 现在采用的TDD的开发模式，通过测试用例来驱动代码开发
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserApi.class)
// 生成rest文档目录输出地址
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@FieldDefaults(level = AccessLevel.PRIVATE)
// 导入对象映射的策略类
@ImportAutoConfiguration(ServerConfiguration.class)
public class UserApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService service;

    /**
     * 设置文档的响应参数
     */
    private static final ResponseFieldsSnippet ERRORS = responseFields(
            // 这个是代表返回单个错误信息
            //fieldWithPath("code")
            // 这个是代表多个错误信息一起返回
            fieldWithPath("[].code").description("错误码"),
            fieldWithPath("[].msg").description("错误信息")
    );


    @Test
    public void userCreateShouldReturn201() throws Exception {
        val request = new UserCreate("Gibson", "123456");
        // 调用service来完成用户创建的处理，所以此时需要mock一个service来完成应用服务层的方法调用
        when(service.create(any(UserCreate.class))).thenReturn(Right(IdWorker.getId()));
        // 开启模拟发起rest 创建用户的请求
        mvc.perform(post(ApiConstant.USER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                // 传输请求的内容
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated())
                // 打印处理流程
                .andDo(print())
                // 生成开发文档
                .andDo(document("user-create",
                        // 设置文档的请求参数
                        requestFields(
                                fieldWithPath("username").description("用户名"),
                                fieldWithPath("password").description("密码")
                        ),
                        // 设置文档的响应头
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("资源唯一URI")
                        )
                ));
    }

    /**
     * 编写400错误的测试用例（这里是逻辑校验）
     * 在ddd中数据校验分为逻辑校验和业务校验
     * 逻辑校验意味着与业务无关的校验，所以逻辑校验一般用于接口层进行校验，
     * 校验失败则直接返回，不会下沉到领域服务中
     * 业务校验就是在特定的领域服务中进行校验，应用服务不做任何处理，只是对请求进行编排和转发。
     * <p>
     * 测试结果出错，则说明接口层没有对参数进行逻辑校验
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     */
    @Test
    public void userCreateShouldReturnLogic400() throws Exception {
        val request = new UserCreate(null, null);
        // 这里移除了对应用服务的mock,是因为逻辑校验在接口层就进行了，不会下沉到应用服务层
        // 直接发起rest请求
        mvc.perform(post(ApiConstant.USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        // 传输请求的内容
                        .content(objectMapper.writeValueAsString(request))
                // 期望返回400
        ).andExpect(status().isBadRequest())
                // 打印处理流程
                .andDo(print())
                // 生成开发文档
                .andDo(document("user-create-logic-400", ERRORS));

        // 返回400的响应体为：
        // 400 bad request
        // [{"code":1, "msg":"用户名为空"},{"code":2, "msg":"密码为空"}]
    }

    /**
     * 业务校验测试用例
     * <p>
     * 业务校验在领域层中进行的校验
     * 为什么不在应用服务层中做校验，是为了明确职责的分离
     * 应用服务层需要确保它的轻量级，以及确保领域模型中的对象和聚合根保持充血状态，防止出现贫血
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     */
    @Test
    public void userCreateShouldReturnBusiness400() throws Exception {
        val request = new UserCreate("Gibson", "12345");

        // mock应用服务来完成业务校验
        when(service.create(any(UserCreate.class))).thenReturn(Left(Seq(ApiError.create(1, "密码不足6位"))));

        // 直接发起rest请求
        mvc.perform(post(ApiConstant.USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        // 传输请求的内容
                        .content(objectMapper.writeValueAsString(request))
                // 期望返回400
        ).andExpect(status().isBadRequest())
                // 打印处理流程
                .andDo(print())
                // 生成开发文档
                .andDo(document("user-create-business-400", ERRORS));

    }
}