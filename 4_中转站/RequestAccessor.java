package com.ifast.ipaymy.apigateway.util.wrapper;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.AbstractServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.netty.http.server.HttpServerRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

public final class RequestAccessor {

    private final ServerWebExchange exchange;
    private final ServerHttpRequest request;

    private RequestAccessor(ServerWebExchange exchange) {
        this.exchange = Objects.requireNonNull(exchange);
        this.request = Objects.requireNonNull(exchange.getRequest());
    }

    public static @NonNull RequestAccessor of(@NonNull ServerWebExchange exchange) {
        return new RequestAccessor(Objects.requireNonNull(exchange));
    }

    public @Nullable String authorizationHeader() {
        return getHeader(HttpHeaders.AUTHORIZATION);
    }

    public @Nullable String acceptHeader() {
        return getHeader(HttpHeaders.ACCEPT);
    }

    public @Nullable String dateHeader() {
        return getHeader(HttpHeaders.DATE);
    }

    public @Nullable String hostHeader() {
        return getHeader(HttpHeaders.HOST);
    }

    public @Nullable String contentLengthHeader() {
        return getHeader(HttpHeaders.CONTENT_LENGTH);
    }

    public @Nullable String contentTypeHeader() {
        return getHeader(HttpHeaders.CONTENT_TYPE);
    }

    public @Nullable String mediaTypeHeader() {
        return getHeader(HttpHeaders.CONTENT_TYPE);
    }

    public @Nullable String userAgentHeader() {
        return getHeader(HttpHeaders.USER_AGENT);
    }

    public @NonNull String getRequestTarget() {
        return getRawPath();
    }

    public @NonNull String getRequestLine() {
        return String.format("%s %s %s", getHttpMethodName(), getRawPath(), getHttpVersion());
    }

    public @NonNull String getHttpVersion() {
        if (request instanceof AbstractServerHttpRequest abstractRequest) {
            Object nativeReq = abstractRequest.getNativeRequest();
            if (nativeReq instanceof HttpServerRequest nettyRequest) {
                return nettyRequest.version().text();
            }
        }
        return "UNKNOWN";
    }

    public @Nullable String getBearerToken() {
        String authorizationHeader = authorizationHeader();
        return Optional.ofNullable(authorizationHeader)
            .map(authHeader -> authHeader
                .replace("Bearer ", "")
            )
            .orElse(null);
    }

    public @Nullable Route getRoute() {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }

    public @Nullable String getRouteId() {
        Route route = getRoute();
        return Optional.ofNullable(route).map(Route::getId).orElse(null);
    }

    public @NonNull URI getURI() {
        return request.getURI();
    }

    public @NonNull Set<URI> getOriginalURIs() {
        return exchange.getAttributeOrDefault(
            ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR,
            Set.of()
        );
    }

    public @Nullable URI getOriginalURI() {
        Set<URI> uris = getOriginalURIs();
        Iterator<URI> iterator = uris.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public @Nullable String getOriginalURIAsString() {
        URI uri = getOriginalURI();
        return Optional.ofNullable(uri).map(URI::toString).orElse(null);
    }

    public @NonNull String getURIAsString() {
        return getURI().toString();
    }

    public @NonNull String getPath() {
        return getURI().getPath();
    }

    public @NonNull String getRawPath() {
        return getURI().getRawPath();
    }

    public @Nullable String getClientIp() {
        return Optional.ofNullable(request.getRemoteAddress())
            .map(InetSocketAddress::getAddress)
            .map(InetAddress::getHostName)
            .orElse(null);
    }

    public @Nullable URI getRouteURI() {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
    }

    public @Nullable String getRouteURIAsString() {
        URI routeUri = getRouteURI();
        return Optional.ofNullable(routeUri).map(URI::toString).orElse(null);
    }

    public @Nullable MediaType getMediaType() {
        return request.getHeaders().getContentType();
    }

    public @Nullable MediaType getContentType() {
        return request.getHeaders().getContentType();
    }

    public @NonNull List<MediaType> getAccept() {
        return request.getHeaders().getAccept();
    }

    public long getDate() {
        return request.getHeaders().getDate();
    }

    public Long getContentLength() {
        return request.getHeaders().getContentLength();
    }

    public @NonNull List<Charset> getAcceptCharset() {
        return request.getHeaders().getAcceptCharset();
    }

    public @NonNull ServerHttpRequest getRequest() {
        return request;
    }

    public @NonNull HttpMethod getHttpMethod() {
        return request.getMethod();
    }

    public @NonNull String getHttpMethodName() {
        return request.getMethod().name();
    }

    public boolean isMethod(HttpMethod httpMethod) {
        return httpMethod == this.request.getMethod();
    }

    public boolean isPostMethod() {
        return request.getMethod() == HttpMethod.POST;
    }

    public boolean isGetMethod() {
        return request.getMethod() == HttpMethod.GET;
    }

    public boolean isPutMethod() {
        return request.getMethod() == HttpMethod.PUT;
    }

    public boolean isDeleteMethod() {
        return request.getMethod() == HttpMethod.DELETE;
    }

    public boolean isPatchMethod() {
        return request.getMethod() == HttpMethod.PATCH;
    }

    public boolean isOptionsMethod() {
        return request.getMethod() == HttpMethod.OPTIONS;
    }

    public boolean isHeadMethod() {
        return request.getMethod() == HttpMethod.HEAD;
    }

    public boolean isTraceMethod() {
        return request.getMethod() == HttpMethod.TRACE;
    }

    public @NonNull HttpHeaders getHeaders() {
        return request.getHeaders();
    }

    public @NonNull Map<String, String> getHeadersAsMap() {
        return request.getHeaders().asSingleValueMap();
    }

    public @NonNull List<String> getHeaders(@NonNull String headerName) {
        return request.getHeaders().getOrEmpty(headerName);
    }

    public @Nullable String getHeader(@NonNull String headerName) {
        return request.getHeaders().getFirst(headerName);
    }

    public @NonNull MultiValueMap<String, HttpCookie> getCookies() {
        return request.getCookies();
    }

    public @NonNull Map<String, HttpCookie> getCookiesAsMap() {
        return request.getCookies().asSingleValueMap();
    }

    public @NonNull List<HttpCookie> getCookies(@NonNull String cookieName) {
        return request.getCookies().get(cookieName);
    }

    public @Nullable HttpCookie getCookie(@NonNull String cookieName) {
        return request.getCookies().getFirst(cookieName);
    }

    public @NonNull MultiValueMap<String, String> getQueryParams() {
        return request.getQueryParams();
    }

    public @NonNull Map<String, String> getQueryParamsAsMap() {
        return request.getQueryParams().asSingleValueMap();
    }

    public @NonNull List<String> getQueryParams(String queryParam) {
        return request.getQueryParams().get(queryParam);
    }

    public @Nullable String getQueryParam(String queryParam) {
        return request.getQueryParams().getFirst(queryParam);
    }


}
