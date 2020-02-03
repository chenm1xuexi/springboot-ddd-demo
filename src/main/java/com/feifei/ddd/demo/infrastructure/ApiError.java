package com.feifei.ddd.demo.infrastructure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ApiError
{
  int    code;
  String msg;

  public static ApiError create(int code, String msg)
  {
    return new ApiError(code, msg);
  }
}
