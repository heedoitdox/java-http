package org.apache.coyote.http11;

import org.apache.coyote.support.ResourceFinder;
import org.apache.coyote.support.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceFinderTest {

    @Nested
    @DisplayName("httpPath 로 파일 내용 조회")
    class FindContent {

        @Test
        @DisplayName("httpPath 와 일치하는 static resource 의 파일 내용을 반환한다")
        void success_getDefaultContext() throws IOException {
            final URL resource = ResourceFinder.class.getClassLoader().getResource("static/index.html");

            final String actual = ResourceFinder.findContent(resource);

            final URL expectedResource = getClass().getClassLoader().getResource("static/index.html");
            var expected = new String(Files.readAllBytes(new File(expectedResource.getFile()).toPath()));

            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("httpPath 로 파일 조회")
    class FindFile {

        @Test
        @DisplayName("파일이 존재한다")
        void success() {
            String httpPath = "/index.html";

            final File actual = ResourceFinder.findFile(httpPath);

            final URL resource = getClass().getClassLoader().getResource("static/index.html");
            var expected = new File(resource.getFile());

            assertThat(actual.toString()).isEqualTo(expected.toString());
        }

        @Test
        @DisplayName("파일이 존재하지 않는다")
        void fail() {
            String httpPath = "/heedoitdox.html";

            assertThrows(ResourceNotFoundException.class,
                    () -> ResourceFinder.findFile(httpPath));
        }

        @Test
        @DisplayName("로그인 페이지에 해당하는 파일을 조회한다")
        void success_login() {
            String httpPath = "/login";

            final File actual = ResourceFinder.findFile(httpPath);

            final URL resource = getClass().getClassLoader().getResource("static/login.html");
            var expected = new File(resource.getFile());

            assertThat(actual.toString()).isEqualTo(expected.toString());
        }
    }

}