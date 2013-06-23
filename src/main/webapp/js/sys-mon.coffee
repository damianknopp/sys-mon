#load sysmon app

class window.SysMon
  constructor: () ->
    @ws = undefined
  
  load: () =>
    w1 = "ws://localhost:8080/sys-mon/events"
   
    onclose = () =>
      console.log("closed connection..")
      disconnect = "disconnected"
      $(".activity").append(disconnect)
      $(".status").html(disconnect)
      $(".status").removeClass("label-info")
      $(".status").addClass("label-warning")
      
    onopen = () ->
      console.log("on open")
      $(".status").html("connected")
      $(".status").removeClass("label-warning")
      $(".status").addClass("label-info")
       
    onmessage = (msgs) =>
      console.log("on message")
      #console.log(msgs)
      if msgs and msgs.data
        data = JSON.parse msgs.data
        return if (_.isArray(data) and _.isEmpty(data)) #or _.isObject(data)

        _.chain(data).reverse()
          .map( (msg) -> 
              if msg and msg.eventType and msg.sysEvent
                el = msg.eventType + " - " + msg.sysEvent
              else
                el = msg
              li = $("<li/>").html(el)
              $(".activity > ul").prepend(li) 
           ).value();
           $(".activity > ul").prepend($("<hr/>"));
       else
        console.log(msgs)
      
 
    @ws = new WebsocketHelper w1, onopen, onmessage, onclose
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

      $msgs = $(".activitiy-msg");
      if rate is 0
        $msgs.html("Activity Stream [off]")
        return
        
      # set new timer
      window.timerTask = tmpTimerTask
      window.setInterval window.timerTask, rate
      $msgs.html("Activity Stream [" + rate + " ms]")
    
    clearEvents = (e) =>
      $(".activity > ul").html("");

    # set click handler for timer refresh update   
    $(".refresh-rate").on("click", updateRefresh)
    
    $(".event-clear").on("click", clearEvents)
    
    
    