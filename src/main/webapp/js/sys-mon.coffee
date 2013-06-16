#load sysmon app
window.sysmon_load = () ->
  w1 = "ws://localhost:8080/sys-mon/echo"
  onopen = () ->
      console.log("on open")
      $(".status").html("connected")
      $(".status").removeClass("label-warning")
      $(".status").addClass("label-info")
      
  ws = new WebsocketHelper w1, onopen
  
    