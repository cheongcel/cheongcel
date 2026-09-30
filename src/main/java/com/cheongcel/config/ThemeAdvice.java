package com.cheongcel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * 시즌별 사이트 테마 선택.
 *
 * - 기본 테마:  application.yml 의 site.theme  (환경변수 SITE_THEME 으로도 덮어쓸 수 있음)
 * - 미리보기:   어떤 주소든 ?theme=purple-wave 처럼 붙이면 그 요청만 해당 테마로 보임
 * - 선택 가능한 목록은 site.themes. 새 테마를 만들면 여기에 이름을 추가하세요.
 *
 * 템플릿에서는 ${siteTheme} 으로 사용합니다.
 */
@ControllerAdvice
public class ThemeAdvice {

    private final List<String> available;
    private final String defaultTheme;

    public ThemeAdvice(@Value("${site.theme:badge}") String defaultTheme,
                       @Value("${site.themes:badge,purple-wave}") String themes) {
        this.available = Arrays.stream(themes.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
        this.defaultTheme = available.contains(defaultTheme) ? defaultTheme : available.get(0);
    }

    @ModelAttribute("siteTheme")
    public String siteTheme(@RequestParam(value = "theme", required = false) String preview) {
        return (preview != null && available.contains(preview)) ? preview : defaultTheme;
    }
}
