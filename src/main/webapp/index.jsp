<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Number Guess Game</title>
  <style>
    body { font-family: system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial, sans-serif; padding: 2rem; }
    .card { max-width: 500px; margin: auto; padding: 1.5rem; border-radius: 16px; box-shadow: 0 10px 30px rgba(0,0,0,.08); }
    input[type=number]{ padding:.6rem; font-size:1rem; width:100%; box-sizing:border-box; border:1px solid #ccc; border-radius:12px; }
    button{ margin-top: .75rem; padding:.7rem 1rem; font-size:1rem; border:0; border-radius:12px; cursor:pointer; }
  </style>
</head>
<body>
<div class="card">
  <h1>🎯 Number Guess Game</h1>
  <p>I picked a number from <b>1</b> to <b>10</b>. Can you guess it?</p>

  <form action="guess" method="post">
    <label for="guess">Your guess:</label>
    <input id="guess" name="guess" type="number" min="1" max="10" required />
    <button type="submit">Submit</button>
  </form>

  <p style="margin-top:1rem;color:#333;">
    <%= request.getAttribute("message") == null ? "" : request.getAttribute("message") %>
  </p>
</div>
</body>
</html>
