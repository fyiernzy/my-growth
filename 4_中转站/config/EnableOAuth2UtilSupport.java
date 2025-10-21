package com.ifast.ipaymy.oauth2.config;

import com.ifast.ipaymy.backend.util.apilog.EnableApiLogAop;
import com.ifast.ipaymy.backend.util.audittrail.EnableAuditTrailAop;
import com.ifast.ipaymy.backend.util.cache.EnableCache;
import com.ifast.ipaymy.backend.util.enums.EnableEnumSupport;
import com.ifast.ipaymy.backend.util.excel.EnableExcelFileGenerator;
import com.ifast.ipaymy.backend.util.exception.EnableGlobalExceptionHandling;
import com.ifast.ipaymy.backend.util.idempotent.EnableIdempotentAop;
import com.ifast.ipaymy.backend.util.observability.EnableObservabilitySupport;
import com.ifast.ipaymy.backend.util.pagination.EnablePaginationSupport;
import com.ifast.ipaymy.backend.util.querydsl.EnableQueryDsl;
import com.ifast.ipaymy.backend.util.serialnumber.EnableSerialNumberSupport;
import com.ifast.ipaymy.backend.util.ssl.EnableSSL;
import com.ifast.ipaymy.backend.util.threadpool.EnableThreadPoolSupport;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
@Target(value = {TYPE})
@Documented
@EnableAuditTrailAop
@EnableCache
@EnableExcelFileGenerator
@EnableEnumSupport
@EnableGlobalExceptionHandling
@EnableIdempotentAop
@EnablePaginationSupport
@EnableQueryDsl
@EnableSSL
@EnableSerialNumberSupport
@EnableApiLogAop
@EnableThreadPoolSupport
@EnableObservabilitySupport
public @interface EnableOAuth2UtilSupport {

}
