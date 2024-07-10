package aircleanprojectback.restapi.auth.filter;

import aircleanprojectback.restapi.common.AuthConstants;
import aircleanprojectback.restapi.member.dto.MemberDTO;
import aircleanprojectback.restapi.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 권한이 없더라도 접근 가능한 url
        List<String> roleLeessList = Arrays.asList(
                "/api/v1/products/\\d+",
                "/api/v1/products/\\w+",
                "/api/v1/products",
                "/api/v1/reviews/product/\\d+",
                "/api/v1/products/search?s=\\w+",
                "/auth/signup","/auth/login",
                "/api/v1/reviews",
                "/api/v1/reviews/\\d++",
                "/api/v1/reviews/(\\d+)?offset=\\d+",
                "/swagger-ui/(.*)",        //swagger 설정
                "/swagger-ui/index.html",  //swagger 설정
                "/v3/api-docs",              //swagger 설정
                "/v3/api-docs/(.*)",         //swagger 설정
                "/swagger-resources",        //swagger 설정
                "/swagger-resources/(.*)"    //swagger 설정
        );

        if(roleLeessList.stream().anyMatch(uri->roleLeessList.stream().anyMatch(pattern-> Pattern.matches(pattern,request.getRequestURI())))){
            chain.doFilter(request,response);
            return;
        }

        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        System.out.println("header가 뭔지좀 보자"+header);
        try{
            if(header != null && !header.equalsIgnoreCase("")){
                String token = TokenUtils.splitHeader(header);

                if(TokenUtils.isValidToken(token)){
                    Claims claims = TokenUtils.getClaimsFromToken(token);

                    MemberDTO authentication = new MemberDTO();
                    // 아직 안끝남
                }
            }
        }catch (Exception e){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonresponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }

    }

    private JSONObject jsonresponseWrapper(Exception e){
        String resultMsg ="";
        if(e instanceof ExpiredJwtException){

            resultMsg = "Token Expired";

        }else if(e instanceof JwtException){

            resultMsg = "Token parsing JwtException";

        }else{

            resultMsg = "other tokken error";
        }

        HashMap<String,Object> jsonMap = new HashMap<>();

        jsonMap.put("status",401);
        jsonMap.put("message",resultMsg);
        jsonMap.put("reason",e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject;
    }
}