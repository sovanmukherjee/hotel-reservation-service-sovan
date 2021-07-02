package com.hotel.jwt.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Value("${gateway.restricted}")
	private boolean gatewayRestricted;

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	private static final List<String> allowedHost = Arrays.asList("localhost:8098");

	@Autowired
	private TokenProvider jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String proxyForwardedHostHeader = req.getHeader("X-Forwarded-Host");
		log.info("proxyForwardedHostHeader:{}", proxyForwardedHostHeader);
		log.info("gatewayRestricted?:{}", gatewayRestricted);
		if (gatewayRestricted && !allowedHost.contains(proxyForwardedHostHeader)) {
			(res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access.");
		} else {
			String header = req.getHeader(HEADER_STRING);
			if (header != null && header.startsWith(TOKEN_PREFIX)) {
				String authToken = header.replace(TOKEN_PREFIX, "");
				var claims = jwtTokenUtil.getAllClaimsFromToken(authToken);
				String username = jwtTokenUtil.getUsernameFromToken(authToken);
				List<String> rolesMap = claims.get("role", List.class);
				List<GrantedAuthority> authorities = new ArrayList<>();
				for (String rolemap : rolesMap) {
					authorities.add(new SimpleGrantedAuthority(rolemap));
				}
				var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			chain.doFilter(req, res);
		}
	}
}