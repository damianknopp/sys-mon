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
	<hr />
	<br />
	<div class="sysmon">
		<span class="header span2">Activity Stream</span> <span class="span3">
			<div class="btn-group">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					Set refresh rate <span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a class="refresh-rate" rate="0">off</a></li>
					<li><a class="refresh-rate" rate="500">500 ms</a></li>
					<li><a class="refresh-rate" rate="1000">1 s</a></li>
					<li><a class="refresh-rate" rate="3000">3 s</a></li>
					<li><a class="refresh-rate" rate="5000">5 s</a></li>
				</ul>
			</div>
		</span> <span class="status span5 label label-warning">not connected</span> <span
			class="activity"></span>
	</div>
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dmk-websocket.js" type="text/javascript"></script>
	<script src="js/sys-mon.js" type="text/javascript"></script>

	<script type="text/javascript">
//<![CDATA[
	$(document).ready(function() {
		  var sysMon = new SysMon();
		  var ws = sysMon.load();
		  console.log(ws);
		  sysMon.initRefresherView();
	});
//]]>
  </script>
</body>
</html>