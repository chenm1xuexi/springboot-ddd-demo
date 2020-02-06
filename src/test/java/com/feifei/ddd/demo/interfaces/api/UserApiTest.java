package com.feifei.ddd.demo.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feifei.ddd.demo.application.service.UserService;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.config.ServerConfiguration;
import com.feifei.ddd.demo.infrastructure.constant.ApiConstant;
import com.feifei.ddd.demo.infrastructure.tool.IdWorker;
import com.feifei.ddd.demo.infrastructure.tool.Pagination;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.vavr.API.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户接口层单元测试用例
 * <p>
 * 现在采用的TDD的开发模式，通过测试用例来驱动代码开发
 * <p>
 * 这里主要采用rest第3级的格式要求，第三级对应于前端人员是非常友好，
 * 因为它返回的并非冰冷的数据，而是返回数据的具体可进行的延展操作，想深入理解rest 3级（超媒体资源驱动）可自行学习
 * 但是实际开发中能作答rest第二级的格式要求就已经很不错了。对应于实际开发中
 * 的企业要求来选定具体的rest的格式级别
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserApi.class)
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
            fieldWithPath("[].msg").description("错误信息"));

    private static final PathParametersSnippet PATH_PARAMETERS = pathParameters(
            parameterWithName("id").description("资源主键"));

    private static final ResponseFieldsSnippet RESPONSE_FIELDS = responseFields(
            fieldWithPath("username").description("用户名"),
            fieldWithPath("create_at").description("创建时间 yyyy-MM-dd HH:mm:ss"),
            fieldWithPath("update_at").description("更新时间 yyyy-MM-dd HH:mm:ss"),
            subsectionWithPath("_links").description("自身资源链接地址"));

    private static final RequestFieldsSnippet REQUEST_FIELDS = requestFields(
            fieldWithPath("username").description("用户名"),
            fieldWithPath("password").description("密码"));

    private static final ResponseHeadersSnippet RESPONSE_HEADERS = responseHeaders(
            headerWithName(HttpHeaders.LOCATION).description("资源唯一URI"));

    /**
     * 自定义格式化文档打印
     *
     * @author shixiongfei
     * @date 2020-02-06
     * @updateDate 2020-02-06
     * @updatedBy shixiongfei
     * @param
     * @return
     */
    private ResultHandler customDocument(String name, Snippet... snippets) {
        return document(name, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), snippets);
    }

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
                .andDo(customDocument("user-create",
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
                .andDo(customDocument("user-create-logic-400", ERRORS));

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
                .andDo(customDocument("user-create-business-400", ERRORS));

    }

    /**
     * 通过主键id来查询用户详情信息
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void getUserInfoShouldReturn200() throws Exception {
        // 因为领域模型实体是充血模型，所以尽量不要暴露给外界，所以需要在应用层做一个转换
        // 假设返回的是一个DTO，命名为UserInfoDTO
        val now = LocalDateTime.now();
        val dto = new UserInfoDTO(IdWorker.getId(), "Gibson", now, now);
        // 因为是测试驱动开发，所以我们查询的接口所调用的应用服务层我们需要mock 一下
        Mockito.when(service.getInfo(Mockito.anyString())).thenReturn(Option(dto));
        mvc.perform(RestDocumentationRequestBuilders.get(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId()))
                // 期望返回200
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(customDocument("user-info-200", PATH_PARAMETERS, RESPONSE_FIELDS,
                        links(
                                linkWithRel("self").description("资源自身链接"),
                                linkWithRel(ApiConstant.EDIT_REL).description("资源修改链接"),
                                linkWithRel(ApiConstant.DELETE_REL).description("资源删除链接")
                        )));

        // 原先返回的数据格式为: {"username":"bibi", ...}
        // 3级rest返回的数据格式为: {"username":"bibi", "_links":{"self":"www.xiaofeifei.com"}}
    }

    /**
     * 查询不到资源，则返回404
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void getUserInfoShouldReturn404() throws Exception {

        // 模拟返回数据为null,这里采用Option来避免空指针异常
        Mockito.when(service.getInfo(anyString())).thenReturn(None());
        mvc.perform(RestDocumentationRequestBuilders
                .get(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId()))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(customDocument("user-info-404"));
    }

    /**
     * 编辑用户信息，成功返回200
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void editUserShouldReturn200() throws Exception {

        val editDTO = new UserEditDTO("Gibson", "654321");

        val now = LocalDateTime.now();
        UserInfoDTO infoDTO = new UserInfoDTO(IdWorker.getId(), "Gibson", now, now);

        // 模拟一个编辑操作，返回修改后的用户详情信息
        when(service.edit(anyString(), any(UserEditDTO.class))).thenReturn(Option(Right(infoDTO)));

        // 因为用到了地址栏中的参数，所以需要采用RestDocumentationRequestBuilders来完成put请求的构造
        mvc.perform(RestDocumentationRequestBuilders.put(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(editDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(customDocument("user-edit", PATH_PARAMETERS, REQUEST_FIELDS, RESPONSE_FIELDS,
                        links(
                                linkWithRel(Link.REL_SELF).description("资源自身链接"),
                                linkWithRel(ApiConstant.REL_INFO).description("资源详情链接"),
                                linkWithRel(ApiConstant.DELETE_REL).description("资源删除链接")
                        )));
    }

    /**
     * 编辑逻辑校验错误返回400
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void editUserShouldReturnLogic400() throws Exception {
        val editDTO = new UserEditDTO(null, null);
        mvc.perform(RestDocumentationRequestBuilders.put(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(editDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(customDocument("user-edit-logic-400", ERRORS));
    }

    /**
     * 编辑用户业务校验错误返回400
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void editUserShouldReturnBusiness400() throws Exception {
        val editDTO = new UserEditDTO("Gibson", "54321");

        when(service.edit(anyString(), any(UserEditDTO.class))).thenReturn(Option(Left(Seq(ApiError.create(1, "密码不足6位")))));

        mvc.perform(RestDocumentationRequestBuilders.put(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(editDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(customDocument("user-edit-business-400", ERRORS));
    }

    /**
     * 编辑不存在的用户时返回错误信息404
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     */
    @Test
    public void editUserShouldReturn404() throws Exception {
        val editDTO = new UserEditDTO("Gibson", "54321");
        when(service.edit(anyString(), any(UserEditDTO.class))).thenReturn(None());
        mvc.perform(RestDocumentationRequestBuilders.put(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(editDTO)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(customDocument("user-edit-404"));
    }

    /**
     * 删除用户信息成功返回204
     *
     * @param
     * @return
     * @author xiaofeifei
     * @date 2020-02-04
     * @updateDate 2020-02-04
     * @updatedBy xiaofeifei
     */
    @Test
    public void deleteUserShouldReturn204() throws Exception {
        mvc.perform(RestDocumentationRequestBuilders
                .delete(ApiConstant.USER_ENDPOINT + "/{id}", IdWorker.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(customDocument("user-delete", PATH_PARAMETERS));

        Mockito.verify(service, times(1)).delete(anyString());
    }

    /**
     * 获取列表信息成功则返回列表
     * 返回的为rest第三级的分页
     *
     * @param
     * @return
     * @author shixiongfei
     * @date 2020-02-05
     * @updateDate 2020-02-05
     * @updatedBy shixiongfei
     */
    @Test
    public void getListShouldReturnList() throws Exception {

        val now = LocalDateTime.now();
        val infoDTO = new UserInfoDTO(com.baomidou.mybatisplus.core.toolkit.IdWorker.getIdStr(), "xiaofeifei", now, now);
        val infoDTO1 = new UserInfoDTO(com.baomidou.mybatisplus.core.toolkit.IdWorker.getIdStr(), "feifei", now.plusDays(2), now.plusDays(3));

        val page = new PageImpl<>(Arrays.asList(infoDTO, infoDTO1), PageRequest.of(1, 2), 2);
        when(service.list(any(Pagination.class))).thenReturn(page);
        mvc.perform(get(ApiConstant.USER_ENDPOINT)
                .param("page", page.getNumber() + "")
                .param("size", page.getSize() + ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(customDocument("user-list",
                        relaxedRequestParameters(
                                parameterWithName("page").description("页码"),
                                parameterWithName("size").description("每页条数")
                        ),
                        links(
                                linkWithRel(Link.REL_SELF).description("集合资源自身链接"),
                                linkWithRel(Link.REL_FIRST).description("第一页资源自身链接"),
                                linkWithRel(Link.REL_PREVIOUS).description("上一页资源自身链接").optional(),
                                linkWithRel(Link.REL_NEXT).description("下一页资源自身链接").optional(),
                                linkWithRel(Link.REL_LAST).description("最后一页资源自身链接")
                        ),
                        responseFields(
                                fieldWithPath("page.size").description("每页条数"),
                                fieldWithPath("page.total_elements").description("总条数"),
                                fieldWithPath("page.total_pages").description("总页数"),
                                fieldWithPath("page.number").description("每页条数"),
                                subsectionWithPath("_links").description("资源地址"),
                                fieldWithPath("_embedded.data[].username").description("用户名"),
                                fieldWithPath("_embedded.data[].create_at").description("创建时间 yyyy-MM-dd HH:mm:ss"),
                                fieldWithPath("_embedded.data[].update_at").description("用户名 yyyy-MM-dd HH:mm:sss"),
                                subsectionWithPath("_embedded.data[]._links").description("资源地址")
                        )
                ));
    }
}