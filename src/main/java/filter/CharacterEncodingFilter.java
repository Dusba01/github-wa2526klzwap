package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

/**
 * Filter that forces a consistent character encoding on every request and
 * response handled by the application.
 *
 * <p>Browsers do not always specify the charset of the data they submit. When
 * the container falls back to its platform default (often ISO-8859-1), any
 * non-ASCII character typed by the user (accented letters, symbols, etc.) is
 * decoded incorrectly. Setting the request encoding to UTF-8 <em>before</em>
 * the request parameters are read guarantees that the form data is interpreted
 * the same way it was encoded by the page, which is also UTF-8.</p>
 *
 * <p>The encoding is configurable through the {@code encoding} init parameter
 * and defaults to {@code UTF-8}. As the very first filter in the chain it also
 * sets the response encoding, so servlets and JSPs do not have to repeat
 * {@code res.setCharacterEncoding("UTF-8")} themselves.</p>
 */
public class CharacterEncodingFilter implements Filter {

    /** Encoding used when no {@code encoding} init parameter is supplied. */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /** The character encoding actually applied to requests and responses. */
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        final String configured = filterConfig.getInitParameter("encoding");
        this.encoding = (configured != null && !configured.isBlank()) ? configured : DEFAULT_ENCODING;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Only override the request encoding when the client did not declare one,
        // so an explicit charset sent by the client is always respected.
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        response.setCharacterEncoding(encoding);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No resources to release.
    }
}
