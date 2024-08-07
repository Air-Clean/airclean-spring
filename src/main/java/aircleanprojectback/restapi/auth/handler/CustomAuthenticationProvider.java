package aircleanprojectback.restapi.auth.handler;

import aircleanprojectback.restapi.member.dto.MemberDTO;
import aircleanprojectback.restapi.member.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService detailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken loginToken = (UsernamePasswordAuthenticationToken) authentication;

        System.out.println("로그인 할때 여기서 토큰을 가지고 온다 "+ loginToken.getName());

        String memberId = loginToken.getName();
        String password = (String) loginToken.getCredentials();

        MemberDTO member = (MemberDTO) detailsService.loadUserByUsername(memberId);

        if(!passwordEncoder.matches(password,member.getMemberPassword())){
            throw new BadCredentialsException(password+" 는 비밀번호가 일치하지 않습니다");
        }

        return new UsernamePasswordAuthenticationToken(member,password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
