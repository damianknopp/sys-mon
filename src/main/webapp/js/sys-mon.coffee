#load sysmon app

class window.SysMon
  constructor: () ->
    @ws = undefined
  
  load: () =>
    w1 = "ws://localhost:8080/sys-mon/events"
    onopen = () ->
      console.log("on open")
      $(".status").html("connected")
      $(".status").removeClass("label-warning")
      $(".status").addClass("label-info")
       
    onmessage = (msg) =>
      console.log("on message")
      $(".activity").append(msg)
 
    @ws = new WebsocketHelper w1, onopen, onmessage
    @ws.connect w1
    @ws
      
  initRefresherView: () ->
    updateRefresh = (e) =>
      rate = Number($(e.target).attr("rate"))
      tmpTimerTask = () =>
        d = new Date()
        t = d.toLocaleTimeString()
        console.log(t)
        refreshMsg = msg: "update"
        @ws.send refreshMsg
        
      # clear old timer and set new refresh rate
      if window.timerTask
        window.clearInterval window.timerTask

      $header = $(".header");
      if rate is 0
        $header.html("Activity Stream [off]")
        return
        
      # set new timer
      window.timerTask = tmpTimerTask
      window.setInterval window.timerTask, rate
      $header.html("Activity Stream [" + rate + " ms]")
    
    # set click handler for timer refresh update   
    $(".refresh-rate").on("click", updateRefresh)
    
    
    