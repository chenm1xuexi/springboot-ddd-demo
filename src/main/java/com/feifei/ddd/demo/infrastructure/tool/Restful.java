package com.feifei.ddd.demo.infrastructure.tool;

import com.feifei.ddd.demo.infrastructure.ApiError;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

/**
 * Rest接口返回
 */
public interface Restful
{
  /**
   * 无校验Created
   */
  static ResponseEntity created(String id)
  {
    val location = ServletUriComponentsBuilder.fromCurrentRequest()
                     .path("/{id}").buildAndExpand(id).toUri();

    return ResponseEntity.created(location).build();
  }

  /**
   * 包含校验Created
   */
  static ResponseEntity created(Either<Seq<ApiError>, String> either)
  {
    return Match(either).of(
      Case($Left($()), Restful::badRequest),
      Case($Right($()), Restful::created)
    );
  }

  static ResponseEntity badRequest(ApiError errors)
  {
    return badRequest(API.Seq(errors));
  }

  static ResponseEntity badRequest(Seq<ApiError> errors)
  {
    return ResponseEntity.badRequest().body(errors);
  }

  static <T> ResponseEntity ok(Option<T> data)
  {
    return data.map(Restful::ok)
             .getOrElse(Restful::notFound);
  }

  static <T> ResponseEntity ok(T data)
  {
    return ResponseEntity.ok(data);
  }

  static ResponseEntity notFound()
  {
    return ResponseEntity.notFound().build();
  }

  static ResponseEntity noContent()
  {
    return ResponseEntity.noContent().build();
  }

  static ResponseEntity error(ApiError errors) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
  }
}
