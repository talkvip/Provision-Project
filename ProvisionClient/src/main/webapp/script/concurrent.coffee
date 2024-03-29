class ThreadBarrier
  constructor: (@parties, @block) ->
    
  join: ->
    --@parties
    if @parties < 1
      @block()
    

class Semaphore
  constructor: ->
    @waiting = []
    @inUse = false

  acquire: (block) ->
    if @inUse
      @waiting.push block
    else
      @inUse = true
      block()
      
  release: ->
    if @waiting.length > 0
      @waiting.shift()()
    else
      @inUse = false

exports = window if window?
exports.ThreadBarrier = ThreadBarrier
exports.Semaphore = Semaphore