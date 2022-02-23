package io.github.mjyoun.spring.web.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 로그인한 정보를 담는 객체
 * 
 * @author MJ Youn
 * @since 2022. 02. 21.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AbstractGrantedAuthority<U, G extends AbstractGrade> implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    /** 로그인한 유저 정보 */
    private U user;
    /** 유저가 갖는 권한 목록 정보 */
    private List<G> grades;

    /**
     * @see GrantedAuthority#getAuthority()
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    @Override
    public String getAuthority() {
        if (this.grades == null) {
            return null;
        } else {
            return this.grades.parallelStream() //
                    .map(AbstractGrade::getId) //
                    .collect(Collectors.joining(","));
        }
    }

}
