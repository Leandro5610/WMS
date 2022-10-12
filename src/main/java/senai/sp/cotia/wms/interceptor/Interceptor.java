package senai.sp.cotia.wms.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.annotation.Publico;
import senai.sp.cotia.wms.rest.AlunoRestController;
import senai.sp.cotia.wms.rest.ProfessorRestController;


@Component
public class Interceptor implements HandlerInterceptor {

	public boolean handle(Object handler ,HttpServletResponse response, HttpServletRequest request) throws IOException {
		String uri = request.getRequestURI();

		System.out.println(uri);

		if (handler instanceof HandlerMethod) {
			if (uri.equals("/")) {
				return true;
			}
			if(uri.endsWith("/error")) {
				return true;
			}
			HandlerMethod chamado = (HandlerMethod) handler;
			
			if(uri.startsWith("/api/aluno")) {
				String token = null;
				
				if (chamado.getMethodAnnotation(Privado.class) != null) {
					try {
					// obtem o token da request
					token = request.getHeader("Authorization");
					// algoritmo para descriptografar
					Algorithm algoritmo = Algorithm.HMAC256(AlunoRestController.SECRET);
					// objeto para verificar o token
					JWTVerifier verifier = JWT.require(algoritmo).withIssuer(AlunoRestController.EMISSOR).build();
					DecodedJWT jwt = verifier.verify(token);
					// extrair os dados do payload
					Map<String, Claim> payloadMap = jwt.getClaims();
					return true;
					}catch (Exception e) {
						e.printStackTrace();
						if(token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value()); 
							e.getMessage();
						}else {
							response.sendError(HttpStatus.FORBIDDEN.value());
							e.getMessage();
						}
						return true; 
					}
				}
			}else if(uri.equals("api/professor")) {
				String token = null;
				if (chamado.getMethodAnnotation(Privado.class) != null) {
					try {
					// obtem o token da request
					token = request.getHeader("Authorization");
					// algoritmo para descriptografar
					Algorithm algoritmo = Algorithm.HMAC256(ProfessorRestController.SECRET);
					// objeto para verificar o token
					JWTVerifier verifier = JWT.require(algoritmo).withIssuer(ProfessorRestController.EMISSOR).build();
					DecodedJWT jwt = verifier.verify(token);
					// extrair os dados do payload
					Map<String, Claim> payloadMap = jwt.getClaims();
					return true;
					}catch (Exception e) {
						e.printStackTrace();
						if(token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value()); 
							e.getMessage();
						}else {
							response.sendError(HttpStatus.FORBIDDEN.value());
							e.getMessage();
						}
						return true; 
					}
				}
				
			}else {
				// se o metodo for publico, libera
				if (chamado.getMethodAnnotation(Publico.class) != null) {
					return true;
				}

				// verificar se existe um usuario logado
				if (request.getSession().getAttribute("usuarioLogado") != null) {
					return true;

				} else {

					// redireciona para a pagina inicial
					response.sendRedirect("/");
					return false;
				}
			}
			
			
		}
		return true;

	}
}
