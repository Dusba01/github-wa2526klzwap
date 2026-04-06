<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Feed</title>
    <style>
        :root {
            --bg: #f3efe5;
            --panel: #fffaf2;
            --ink: #1f1d1a;
            --muted: #6f665c;
            --accent: #d45b2c;
            --accent-dark: #9f3f19;
            --line: #e2d7c7;
            --card-a: #f7e6d2;
            --card-b: #d8ebe3;
            --card-c: #f1d7dd;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Georgia", "Times New Roman", serif;
            color: var(--ink);
            background:
                    radial-gradient(circle at top left, rgba(212, 91, 44, 0.12), transparent 28%),
                    linear-gradient(180deg, #f8f3ea 0%, #efe5d5 100%);
        }

        .page-shell {
            max-width: 1120px;
            margin: 0 auto;
            padding: 24px 20px 40px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 24px;
            padding: 18px 22px;
            background: rgba(255, 250, 242, 0.92);
            border: 1px solid var(--line);
            border-radius: 22px;
            backdrop-filter: blur(8px);
        }

        .topbar h1 {
            margin: 0;
            font-size: 1.8rem;
            letter-spacing: 0.02em;
        }

        .topbar p {
            margin: 4px 0 0;
            color: var(--muted);
        }

        .logout-form {
            margin: 0;
        }

        .logout-button {
            border: none;
            border-radius: 999px;
            padding: 12px 18px;
            font: inherit;
            color: #fff;
            background: linear-gradient(135deg, var(--accent), var(--accent-dark));
            cursor: pointer;
        }

        .content-grid {
            display: grid;
            grid-template-columns: minmax(0, 280px) minmax(0, 1fr);
            gap: 22px;
        }

        .sidebar,
        .feed {
            min-height: 70vh;
        }

        .panel {
            background: var(--panel);
            border: 1px solid var(--line);
            border-radius: 22px;
            padding: 20px;
            box-shadow: 0 10px 30px rgba(76, 52, 35, 0.06);
        }

        .sidebar .panel + .panel {
            margin-top: 18px;
        }

        .sidebar h2,
        .feed h2 {
            margin-top: 0;
            font-size: 1.1rem;
        }

        .meta-list,
        .trend-list {
            margin: 0;
            padding-left: 18px;
            color: var(--muted);
        }

        .trend-list li + li,
        .meta-list li + li {
            margin-top: 10px;
        }

        .feed-stream {
            display: grid;
            gap: 18px;
            max-height: 72vh;
            overflow-y: auto;
            padding-right: 6px;
        }

        .feed-card {
            border-radius: 22px;
            padding: 22px;
            border: 1px solid rgba(0, 0, 0, 0.06);
        }

        .feed-card:nth-child(3n + 1) {
            background: var(--card-a);
        }

        .feed-card:nth-child(3n + 2) {
            background: var(--card-b);
        }

        .feed-card:nth-child(3n) {
            background: var(--card-c);
        }

        .feed-card header {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            align-items: baseline;
            margin-bottom: 10px;
        }

        .feed-card h3 {
            margin: 0;
            font-size: 1.1rem;
        }

        .feed-card span {
            color: var(--muted);
            font-size: 0.95rem;
        }

        .feed-card p {
            margin: 0;
            line-height: 1.6;
        }

        @media (max-width: 860px) {
            .content-grid {
                grid-template-columns: 1fr;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }

            .feed-stream {
                max-height: none;
            }
        }
    </style>
</head>
<body>
<%
    Object authenticatedUser = session.getAttribute("authenticatedUser");
    if (authenticatedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<div class="page-shell">
    <div class="topbar">
        <div>
            <h1>Study Feed</h1>
            <p>Welcome back, <strong><%= authenticatedUser %></strong>. Your activity stream is ready.</p>
        </div>
        <form class="logout-form" action="<%= request.getContextPath() %>/logout" method="post">
            <button class="logout-button" type="submit">Log out</button>
        </form>
    </div>

    <div class="content-grid">
        <aside class="sidebar">
            <div class="panel">
                <h2>Profile Snapshot</h2>
                <ul class="meta-list">
                    <li>Signed in as: <strong><%= authenticatedUser %></strong></li>
                    <li>Workspace: Lecture Notes App</li>
                    <li>Status: Active session</li>
                </ul>
            </div>

            <div class="panel">
                <h2>Trending Topics</h2>
                <ul class="trend-list">
                    <li>Database normalization for study notes</li>
                    <li>Servlet routing with Jakarta EE 10</li>
                    <li>PostgreSQL indexing for username and email</li>
                    <li>Designing cleaner login and signup flows</li>
                </ul>
            </div>
        </aside>

        <main class="feed">
            <div class="panel">
                <h2>Recent Feed</h2>
                <div class="feed-stream">
                    <article class="feed-card">
                        <header>
                            <h3>New database schema review</h3>
                            <span>2 min ago</span>
                        </header>
                        <p>The latest schema revision keeps the <code>users</code> table compact and lets PostgreSQL generate <code>created_at</code> automatically.</p>
                    </article>

                    <article class="feed-card">
                        <header>
                            <h3>Authentication flow updated</h3>
                            <span>12 min ago</span>
                        </header>
                        <p>The login process now routes through dedicated servlet methods for sign-in and sign-up, keeping the control flow explicit.</p>
                    </article>

                    <article class="feed-card">
                        <header>
                            <h3>Frontend registration page connected</h3>
                            <span>25 min ago</span>
                        </header>
                        <p>The register link now opens a dedicated form page, and successful registration redirects the user back to login.</p>
                    </article>

                    <article class="feed-card">
                        <header>
                            <h3>Suggested next task</h3>
                            <span>1 hour ago</span>
                        </header>
                        <p>Add a proper controller for the home page and replace static feed cards with real note or activity data from the database.</p>
                    </article>

                    <article class="feed-card">
                        <header>
                            <h3>Session tip</h3>
                            <span>Today</span>
                        </header>
                        <p>Use the logout button when switching users so the current session does not keep the previous identity in memory.</p>
                    </article>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
