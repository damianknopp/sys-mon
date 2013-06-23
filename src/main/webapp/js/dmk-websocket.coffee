#>>lang=cf

class window.WebsocketHelper
  constructor: (@location = "ws://localhost:8080/sys-mon/echo", @onopen, @onmessage, @onclose) ->
    @ws = undefined

  connect: (@location) ->
    target = @location
    @ws  = new WebSocket(target)
    console.log @ws
    
    if !@onmessage
      @ws.onmessage = (event) ->
        console.log event
        json = JSON.parse event.data
        console.log json
    else
      @ws.onmessage = @onmessage
      
    if !@onopen  
      @ws.onopen = () =>
        console.log "opened connection"
        msg = msg: "hello"
        this.send msg
    else
      @ws.onopen = @onopen
    
    if !@onclose
      @ws.onclose = () ->
        console.log "closed connection"
    else
      @ws.onclose = @onclose
    @ws

  # if not connected, connects to websocket endpoint
  # turns object passed into JSON
  # sends JSON to currently connected websocket endpoint
  send: (obj) ->
    if not @ws?
      console.log "websocket is not initialized..."
      @ws = this.connect(@location)
      console.log @ws
    else
      #console.log "reusing websocket"
      tmp = JSON.stringify obj
      @ws.send tmp
          
  close: () ->
    @ws.close() and @ws = undefined if @ws?