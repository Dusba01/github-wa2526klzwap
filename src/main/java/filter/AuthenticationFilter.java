package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

/**
 * Filter that enforces authentication on every protected resource of the
 * application.
 *
 * <p>Before this filter existed, each servlet and JSP had to repeat the same
 * boilerplate: read the {@code user} attribute from the {@link HttpSession},
 * check whether it was {@code null}, and either redirect to the login page or
 * answer with {@code 401}. Centralising that logic in a single filter removes
 * the duplication and guarantees that no protected resource can ever be reached
 * without a valid session, even if a new servlet forgets to perform the
 * check.</p>
 *
 * <p>The filter is mapped to {@code /*} and lets a small whitelist of public
 * resources through (login, registration, logout and static assets). For every
 * other request it requires a logged-in {@link User} in the session:</p>
 * <ul>
 *   <li>REST requests (paths starting with {@code /rest/}) receive a
 *       {@code 401 Unauthorized} JSON response, so the front-end JavaScript can
 *       react without parsing an HTML login page;</li>
 *   <li>all other (page) requests are redirected to the login page.</li>
 * </ul>
 */
public class AuthenticationFilter implements Filter {

    /** Session attribute holding the authenticated {@link User}. */
    private static final String USER_ATTRIBUTE = "user";

    /** Path (relative to the context) the unauthenticated user is sent to. */
    private static final String LOGIN_PATH = "/login";

    /** Prefix that identifies REST resources, which must answer with JSON. */
    private static final String REST_PREFIX = "/rest/";

    /**
     * Resources reachable without an authenticated session. Exact, context
     * relative paths are matched as-is; the directory prefixes cover the static
     * assets (CSS, JavaScript, images).
     */
    private static final String[] PUBLIC_EXACT_PATHS = {
            "/", "/login", "/register", "/logout",
            "/jsp/login.jsp", "/jsp/register.jsp", "/favicon.ico"
    };

    private static final String[] PUBLIC_PREFIX_PATHS = {
            "/css/", "/js/", "/images/"
    };

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialisation parameters are required.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        // Path of the requested resource, relative to the web application
        // context (e.g. "/profile", "/rest/notes/search").
        final String path = req.getRequestURI().substring(req.getContextPath().length());

        // Public resources are always allowed through, untouched.
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpSession session = req.getSession(false);
        final User user = (session != null) ? (User) session.getAttribute(USER_ATTRIBUTE) : null;

        if (user != null) {
            // Authenticated: let the request reach the target servlet/JSP.
            chain.doFilter(request, response);
            return;
        }

        // Not authenticated: deny access in the way the caller expects.
        if (path.startsWith(REST_PREFIX)) {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"User not authenticated\"}");
        } else {
            res.sendRedirect(req.getContextPath() + LOGIN_PATH);
        }
    }

    @Override
    public void destroy() {
        // No resources to release.
    }

    /**
     * Tells whether a context-relative path points to a resource that can be
     * accessed without an authenticated session.
     *
     * @param path the context-relative request path
     * @return {@code true} if the resource is public, {@code false} otherwise
     */
    private boolean isPublicResource(String path) {
        for (String exact : PUBLIC_EXACT_PATHS) {
            if (exact.equals(path)) {
                return true;
            }
        }
        for (String prefix : PUBLIC_PREFIX_PATHS) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
