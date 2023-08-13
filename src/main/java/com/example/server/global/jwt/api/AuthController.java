package com.example.server.global.jwt.api;

import javax.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.server.global.dto.ApiResponse;
import com.example.server.global.jwt.api.dto.AccessTokenRequest;
import com.example.server.global.jwt.api.dto.AccessTokenResponse;
import com.example.server.global.jwt.application.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtService jwtService;

	@GetMapping("/redirect-to-kakao")
	public RedirectView redirectToKakao() {
		return new RedirectView("/oauth2/authorization/kakao");
	}

	@GetMapping("/redirect-to-naver")
	public RedirectView redirectToNaver() {
		return new RedirectView("/oauth2/authorization/naver");
	}

	@GetMapping("/success-login")
	public String successLogin(@PathParam("token") String token) {
		return token;
	}

	// refresh token 을 활용해 access token 재발급
	@PostMapping("/re-issue")
	public ResponseEntity<AccessTokenResponse> reissueAccessToken(@RequestBody AccessTokenRequest accessTokenRequest) {
		String accessToken = jwtService.getAccessToken(accessTokenRequest);
		return ApiResponse.created(AccessTokenResponse.of(accessToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
		jwtService.removeToken(userDetails);
		return ApiResponse.success(null);
	}
}
