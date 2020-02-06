package com.feifei.ddd.demo.infrastructure.tool;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

/**
 * 自定义分页类，只是用来接收返回数据的对象，
 * 具体如何实现数据检索，看具体的持久化框架如何实现，
 * 该类只用于接收持久化框架返回的数据内容，不做任何其他处理
 * 也可直接采用spring自带的Pageable类来替代分页
 * 如果有其他更好的方案请自行实现
 *
 * @author xiaofeifei
 * @date 2020-02-06
 * @since
 */
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pagination<T> {

    /**
     * 查询数据列表
     */
    List<T> data = Collections.emptyList();

    /**
     * 当前页
     */
    long current = 1;

    /**
     * 每页显示条数，默认 10
     */
    long size = 10;

    /**
     * 总页数
     */
    long pages = 0;

    /**
     * 总数
     */
    long total = 0;

    public Pagination(long current, long size) {
        this.current = current;
        this.size = size;
    }

    public Pagination(List<T> data, long current, long size) {
        this.data = data;
        this.current = current;
        this.size = size;
    }
}