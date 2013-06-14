<!DOCTYPE html>
<html>
  <head>
    <title>Sys monitor</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
  </head>
  <body>
    <h2>System monitor</h2>
    <br/>
    <div class="sysmon">
       <span class="query"></span>
       <hr/>
       <span class="status"></span>
    </div>
    
    <hr/>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/dmk-websocket.js" type="text/javascript"></script>
  </body>
</html>