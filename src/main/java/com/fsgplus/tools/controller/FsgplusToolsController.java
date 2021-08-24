package com.fsgplus.tools.controller;

import com.fsgplus.tools.utils.AESUtil;
import com.fsgplus.tools.utils.AESUtils;
import com.google.gson.JsonObject;
import com.isuper.eden.adam.encry.AESType;
import com.isuper.eden.eve.boot.common.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FsgplusToolsController @Description @Author admin @Date 8/24/2021 3:03 PM @Version 1.0
 */
@Api(value = "大客户用户单点登录接口", tags = "大客户用户单点登录接口")
@RestController
@RequestMapping("fsgplus/tools")
@Slf4j
public class FsgplusToolsController {

  @PostMapping("aesEncryptByUserCenter")
  @ApiOperation(value = "AES加密后进行Hex", notes = "本方法可以用作调用用户中心登录时候的union的加密", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "unicode",
        value = "需要加密的文件",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "secret",
        value = "加密的密钥",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class)
  })
  public RestResponse<String> aesEncrypt(
      @RequestParam(value = "unicode", required = true) String unicode,
      @RequestParam(value = "secret", required = true) String secret)
      throws Exception {
    return new RestResponse<String>("00000000", "success", AESUtils.encrypt(unicode, secret));
  }

  @PostMapping("corpLoginDataDecrypt")
  @ApiOperation(value = "解密传参数据", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "data",
        value = "加密后数据",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "aeskey",
        value = "aeskey",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "aestype",
        value = "aestype",
        dataType = "String",
        defaultValue = "AES_128",
        required = true,
        paramType = "query",
        dataTypeClass = AESType.class)
  })
  public RestResponse<String> corpLoginDataDecrypt(
      @RequestParam(value = "data", required = true) String data,
      @RequestParam(value = "aeskey", required = true) String aeskey,
      @RequestParam(value = "aestype", required = true) AESType aestype) {
    return new RestResponse<String>("00000000", "success", AESUtil.decrypt(data, aeskey, aestype));
  }

  @PostMapping("corpLoginDataEncrypt")
  @ApiOperation(value = "按照参数生成加密数据", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "unicode",
        value = "unicode",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "oid",
        value = "oid",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "registType",
        value = "registType",
        dataType = "String",
        required = false,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "timestamp",
        value = "链接时间戳",
        dataType = "String",
        required = false,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "orgCode",
        value = "orgCode",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "next",
        value = "next",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "aeskey",
        value = "aeskey",
        dataType = "String",
        required = true,
        paramType = "query",
        dataTypeClass = String.class),
    @ApiImplicitParam(
        name = "aestype",
        value = "aestype",
        dataType = "String",
        defaultValue = "AES_128",
        required = true,
        paramType = "query",
        dataTypeClass = AESType.class)
  })
  public RestResponse<String> corpLoginDataEncrypt(
      @RequestParam(value = "unicode", required = true) String unicode,
      @RequestParam(value = "oid", required = true) String oid,
      @RequestParam(value = "registType", required = false) String registType,
      @RequestParam(value = "timestamp", required = false) String timestamp,
      @RequestParam(value = "orgCode", required = true) String orgCode,
      @RequestParam(value = "next", required = true) String next,
      @RequestParam(value = "aeskey", required = true) String aeskey,
      @RequestParam(value = "aestype", required = true) AESType aestype) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("unicode", unicode);
    jsonObject.addProperty("oid", oid);
    if (StringUtils.isNotBlank(registType)) {
      jsonObject.addProperty("registType", registType);
    }
    jsonObject.addProperty(
        "timestamp",
        StringUtils.isBlank(timestamp)
            ? String.valueOf(System.currentTimeMillis() / 1000)
            : timestamp);
    jsonObject.addProperty("orgCode", orgCode);
    jsonObject.addProperty("next", next);

    log.info(jsonObject.toString());
    return new RestResponse<String>(
        "00000000", "success", AESUtil.encrypt(jsonObject.toString(), aeskey, aestype));
  }
}
