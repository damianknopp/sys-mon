<!DOCTYPE html>
<html>
	<head>
		<title>sys monitor</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="css/sys-mon.css" rel="stylesheet" media="screen">
	</head>
	<body>
		<h2>System monitor</h2>
		<hr/>
		<br/>
		<div class="sysmon">
		  <span class="header span1">Activity</span><span class="status span3 label label-warning">not connected</span>
		  <span class="activity"></span>
		</div>
		<script src="http://code.jquery.com/jquery.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/dmk-websocket.js" type="text/javascript"></script>
    <script src="js/sys-mon.js" type="text/javascript"></script>
		
  <script type="text/javascript">
//<![CDATA[
	$(document).ready(function() {
		  var ws = sysmon_load();
	});
//]]>
  </script>
	</body>
</html>