(->
  eq = (a, b, stack) ->
    return a isnt 0 or 1 / a is 1 / b  if a is b
    return a is b  if not a? or not b?
    a = a._wrapped  if a._chain
    b = b._wrapped  if b._chain
    return a.isEqual(b)  if a.isEqual and _.isFunction(a.isEqual)
    return b.isEqual(a)  if b.isEqual and _.isFunction(b.isEqual)
    className = toString.call(a)
    return false  unless className is toString.call(b)
    switch className
      when "[object String]"
        return a is String(b)
      when "[object Number]"
        return (if a isnt +a then b isnt +b else (if a is 0 then 1 / a is 1 / b else a is +b))
      when "[object Date]", "[object Boolean]"
        return +a is +b
      when "[object RegExp]"
        return a.source is b.source and a.global is b.global and a.multiline is b.multiline and a.ignoreCase is b.ignoreCase
    return false  if typeof a isnt "object" or typeof b isnt "object"
    length = stack.length
    return true  if stack[length] is a  while length--
    stack.push a
    size = 0
    result = true
    if className is "[object Array]"
      size = a.length
      result = size is b.length
      break  unless result = size of a is size of b and eq(a[size], b[size], stack)  while size--  if result
    else
      return false  if "constructor" of a isnt "constructor" of b or a.constructor isnt b.constructor
      for key of a
        if hasOwnProperty.call(a, key)
          size++
          break  unless result = hasOwnProperty.call(b, key) and eq(a[key], b[key], stack)
      if result
        for key of b
          break  if hasOwnProperty.call(b, key) and not (size--)
        result = not size
    stack.pop()
    result
  root = this
  previousUnderscore = root._
  breaker = {}
  ArrayProto = Array::
  ObjProto = Object::
  FuncProto = Function::
  slice = ArrayProto.slice
  concat = ArrayProto.concat
  unshift = ArrayProto.unshift
  toString = ObjProto.toString
  hasOwnProperty = ObjProto.hasOwnProperty
  nativeForEach = ArrayProto.forEach
  nativeMap = ArrayProto.map
  nativeReduce = ArrayProto.reduce
  nativeReduceRight = ArrayProto.reduceRight
  nativeFilter = ArrayProto.filter
  nativeEvery = ArrayProto.every
  nativeSome = ArrayProto.some
  nativeIndexOf = ArrayProto.indexOf
  nativeLastIndexOf = ArrayProto.lastIndexOf
  nativeIsArray = Array.isArray
  nativeKeys = Object.keys
  nativeBind = FuncProto.bind
  _ = (obj) ->
    new wrapper(obj)

  if typeof exports isnt "undefined"
    exports = module.exports = _  if typeof module isnt "undefined" and module.exports
    exports._ = _
  else if typeof define is "function" and define.amd
    define "underscore", ->
      _
  else
    root["_"] = _
  _.VERSION = "1.2.3"
  each = _.each = _.forEach = (obj, iterator, context) ->
    return  unless obj?
    if nativeForEach and obj.forEach is nativeForEach
      obj.forEach iterator, context
    else if obj.length is +obj.length
      i = 0
      l = obj.length

      while i < l
        return  if i of obj and iterator.call(context, obj[i], i, obj) is breaker
        i++
    else
      for key of obj
        return  if iterator.call(context, obj[key], key, obj) is breaker  if hasOwnProperty.call(obj, key)

  _.map = (obj, iterator, context) ->
    results = []
    return results  unless obj?
    return obj.map(iterator, context)  if nativeMap and obj.map is nativeMap
    each obj, (value, index, list) ->
      results[results.length] = iterator.call(context, value, index, list)

    results

  _.reduce = _.foldl = _.inject = (obj, iterator, memo, context) ->
    initial = arguments.length > 2
    obj = []  unless obj?
    if nativeReduce and obj.reduce is nativeReduce
      iterator = _.bind(iterator, context)  if context
      return (if initial then obj.reduce(iterator, memo) else obj.reduce(iterator))
    each obj, (value, index, list) ->
      unless initial
        memo = value
        initial = true
      else
        memo = iterator.call(context, memo, value, index, list)

    throw new TypeError("Reduce of empty array with no initial value")  unless initial
    memo

  _.reduceRight = _.foldr = (obj, iterator, memo, context) ->
    initial = arguments.length > 2
    obj = []  unless obj?
    if nativeReduceRight and obj.reduceRight is nativeReduceRight
      iterator = _.bind(iterator, context)  if context
      return (if initial then obj.reduceRight(iterator, memo) else obj.reduceRight(iterator))
    reversed = _.toArray(obj).reverse()
    iterator = _.bind(iterator, context)  if context and not initial
    (if initial then _.reduce(reversed, iterator, memo, context) else _.reduce(reversed, iterator))

  _.find = _.detect = (obj, iterator, context) ->
    result = undefined
    any obj, (value, index, list) ->
      if iterator.call(context, value, index, list)
        result = value
        true

    result

  _.filter = _.select = (obj, iterator, context) ->
    results = []
    return results  unless obj?
    return obj.filter(iterator, context)  if nativeFilter and obj.filter is nativeFilter
    each obj, (value, index, list) ->
      results[results.length] = value  if iterator.call(context, value, index, list)

    results

  _.reject = (obj, iterator, context) ->
    results = []
    return results  unless obj?
    each obj, (value, index, list) ->
      results[results.length] = value  unless iterator.call(context, value, index, list)

    results

  _.every = _.all = (obj, iterator, context) ->
    result = true
    return result  unless obj?
    return obj.every(iterator, context)  if nativeEvery and obj.every is nativeEvery
    each obj, (value, index, list) ->
      breaker  unless result = result and iterator.call(context, value, index, list)

    result

  any = _.some = _.any = (obj, iterator, context) ->
    iterator or (iterator = _.identity)
    result = false
    return result  unless obj?
    return obj.some(iterator, context)  if nativeSome and obj.some is nativeSome
    each obj, (value, index, list) ->
      breaker  if result or (result = iterator.call(context, value, index, list))

    !!result

  _.include = _.contains = (obj, target) ->
    found = false
    return found  unless obj?
    return obj.indexOf(target) isnt -1  if nativeIndexOf and obj.indexOf is nativeIndexOf
    found = any(obj, (value) ->
      value is target
    )
    found

  _.invoke = (obj, method) ->
    args = slice.call(arguments, 2)
    _.map obj, (value) ->
      (if method.call then method or value else value[method]).apply value, args

  _.pluck = (obj, key) ->
    _.map obj, (value) ->
      value[key]

  _.max = (obj, iterator, context) ->
    return Math.max.apply(Math, obj)  if not iterator and _.isArray(obj)
    return -Infinity  if not iterator and _.isEmpty(obj)
    result = computed: -Infinity
    each obj, (value, index, list) ->
      computed = (if iterator then iterator.call(context, value, index, list) else value)
      computed >= result.computed and (result =
        value: value
        computed: computed
      )

    result.value

  _.min = (obj, iterator, context) ->
    return Math.min.apply(Math, obj)  if not iterator and _.isArray(obj)
    return Infinity  if not iterator and _.isEmpty(obj)
    result = computed: Infinity
    each obj, (value, index, list) ->
      computed = (if iterator then iterator.call(context, value, index, list) else value)
      computed < result.computed and (result =
        value: value
        computed: computed
      )

    result.value

  _.shuffle = (obj) ->
    shuffled = []
    rand = undefined
    each obj, (value, index, list) ->
      if index is 0
        shuffled[0] = value
      else
        rand = Math.floor(Math.random() * (index + 1))
        shuffled[index] = shuffled[rand]
        shuffled[rand] = value

    shuffled

  _.sortBy = (obj, iterator, context) ->
    _.pluck _.map(obj, (value, index, list) ->
      value: value
      criteria: iterator.call(context, value, index, list)
    ).sort((left, right) ->
      a = left.criteria
      b = right.criteria
      (if a < b then -1 else (if a > b then 1 else 0))
    ), "value"

  _.groupBy = (obj, val) ->
    result = {}
    iterator = (if _.isFunction(val) then val else (obj) ->
      obj[val]
    )
    each obj, (value, index) ->
      key = iterator(value, index)
      (result[key] or (result[key] = [])).push value

    result

  _.sortedIndex = (array, obj, iterator) ->
    iterator or (iterator = _.identity)
    low = 0
    high = array.length
    while low < high
      mid = (low + high) >> 1
      (if iterator(array[mid]) < iterator(obj) then low = mid + 1 else high = mid)
    low

  _.toArray = (iterable) ->
    return []  unless iterable
    return iterable.toArray()  if iterable.toArray
    return slice.call(iterable)  if _.isArray(iterable)
    return slice.call(iterable)  if _.isArguments(iterable)
    _.values iterable

  _.size = (obj) ->
    _.toArray(obj).length

  _.first = _.head = (array, n, guard) ->
    (if (n?) and not guard then slice.call(array, 0, n) else array[0])

  _.initial = (array, n, guard) ->
    slice.call array, 0, array.length - (if (not (n?)) or guard then 1 else n)

  _.last = (array, n, guard) ->
    if (n?) and not guard
      slice.call array, Math.max(array.length - n, 0)
    else
      array[array.length - 1]

  _.rest = _.tail = (array, index, guard) ->
    slice.call array, (if (not (index?)) or guard then 1 else index)

  _.compact = (array) ->
    _.filter array, (value) ->
      !!value

  _.flatten = (array, shallow) ->
    _.reduce array, ((memo, value) ->
      return memo.concat((if shallow then value else _.flatten(value)))  if _.isArray(value)
      memo[memo.length] = value
      memo
    ), []

  _.without = (array) ->
    _.difference array, slice.call(arguments, 1)

  _.uniq = _.unique = (array, isSorted, iterator) ->
    initial = (if iterator then _.map(array, iterator) else array)
    result = []
    _.reduce initial, ((memo, el, i) ->
      if 0 is i or (if isSorted is true then _.last(memo) isnt el else not _.include(memo, el))
        memo[memo.length] = el
        result[result.length] = array[i]
      memo
    ), []
    result

  _.union = ->
    _.uniq _.flatten(arguments, true)

  _.intersection = _.intersect = (array) ->
    rest = slice.call(arguments, 1)
    _.filter _.uniq(array), (item) ->
      _.every rest, (other) ->
        _.indexOf(other, item) >= 0

  _.difference = (array) ->
    rest = _.flatten(slice.call(arguments, 1))
    _.filter array, (value) ->
      not _.include(rest, value)

  _.zip = ->
    args = slice.call(arguments)
    length = _.max(_.pluck(args, "length"))
    results = new Array(length)
    i = 0

    while i < length
      results[i] = _.pluck(args, "" + i)
      i++
    results

  _.indexOf = (array, item, isSorted) ->
    return -1  unless array?
    i = undefined
    l = undefined
    if isSorted
      i = _.sortedIndex(array, item)
      return (if array[i] is item then i else -1)
    return array.indexOf(item)  if nativeIndexOf and array.indexOf is nativeIndexOf
    i = 0
    l = array.length

    while i < l
      return i  if i of array and array[i] is item
      i++
    -1

  _.lastIndexOf = (array, item) ->
    return -1  unless array?
    return array.lastIndexOf(item)  if nativeLastIndexOf and array.lastIndexOf is nativeLastIndexOf
    i = array.length
    return i  if i of array and array[i] is item  while i--
    -1

  _.range = (start, stop, step) ->
    if arguments.length <= 1
      stop = start or 0
      start = 0
    step = arguments[2] or 1
    len = Math.max(Math.ceil((stop - start) / step), 0)
    idx = 0
    range = new Array(len)
    while idx < len
      range[idx++] = start
      start += step
    range

  ctor = ->

  _.bind = bind = (func, context) ->
    bound = undefined
    args = undefined
    return nativeBind.apply(func, slice.call(arguments, 1))  if func.bind is nativeBind and nativeBind
    throw new TypeError  unless _.isFunction(func)
    args = slice.call(arguments, 2)
    bound = ->
      return func.apply(context, args.concat(slice.call(arguments)))  unless this instanceof bound
      ctor:: = func::
      self = new ctor
      result = func.apply(self, args.concat(slice.call(arguments)))
      return result  if Object(result) is result
      self

  _.bindAll = (obj) ->
    funcs = slice.call(arguments, 1)
    funcs = _.functions(obj)  if funcs.length is 0
    each funcs, (f) ->
      obj[f] = _.bind(obj[f], obj)

    obj

  _.memoize = (func, hasher) ->
    memo = {}
    hasher or (hasher = _.identity)
    ->
      key = hasher.apply(this, arguments)
      (if hasOwnProperty.call(memo, key) then memo[key] else (memo[key] = func.apply(this, arguments)))

  _.delay = (func, wait) ->
    args = slice.call(arguments, 2)
    setTimeout (->
      func.apply func, args
    ), wait

  _.defer = (func) ->
    _.delay.apply _, [ func, 1 ].concat(slice.call(arguments, 1))

  _.throttle = (func, wait) ->
    context = undefined
    args = undefined
    timeout = undefined
    throttling = undefined
    more = undefined
    whenDone = _.debounce(->
      more = throttling = false
    , wait)
    ->
      context = this
      args = arguments
      later = ->
        timeout = null
        func.apply context, args  if more
        whenDone()

      timeout = setTimeout(later, wait)  unless timeout
      if throttling
        more = true
      else
        func.apply context, args
      whenDone()
      throttling = true

  _.debounce = (func, wait) ->
    timeout = undefined
    ->
      context = this
      args = arguments
      later = ->
        timeout = null
        func.apply context, args

      clearTimeout timeout
      timeout = setTimeout(later, wait)

  _.once = (func) ->
    ran = false
    memo = undefined
    ->
      return memo  if ran
      ran = true
      memo = func.apply(this, arguments)

  _.wrap = (func, wrapper) ->
    ->
      args = concat.apply([ func ], arguments)
      wrapper.apply this, args

  _.compose = ->
    funcs = arguments
    ->
      args = arguments
      i = funcs.length - 1

      while i >= 0
        args = [ funcs[i].apply(this, args) ]
        i--
      args[0]

  _.after = (times, func) ->
    return func()  if times <= 0
    ->
      func.apply this, arguments  if --times < 1

  _.keys = nativeKeys or (obj) ->
    throw new TypeError("Invalid object")  if obj isnt Object(obj)
    keys = []
    for key of obj
      keys[keys.length] = key  if hasOwnProperty.call(obj, key)
    keys

  _.values = (obj) ->
    _.map obj, _.identity

  _.functions = _.methods = (obj) ->
    names = []
    for key of obj
      names.push key  if _.isFunction(obj[key])
    names.sort()

  _.extend = (obj) ->
    each slice.call(arguments, 1), (source) ->
      for prop of source
        obj[prop] = source[prop]  if source[prop] isnt undefined

    obj

  _.defaults = (obj) ->
    each slice.call(arguments, 1), (source) ->
      for prop of source
        obj[prop] = source[prop]  unless obj[prop]?

    obj

  _.clone = (obj) ->
    return obj  unless _.isObject(obj)
    (if _.isArray(obj) then obj.slice() else _.extend({}, obj))

  _.tap = (obj, interceptor) ->
    interceptor obj
    obj

  _.isEqual = (a, b) ->
    eq a, b, []

  _.isEmpty = (obj) ->
    return obj.length is 0  if _.isArray(obj) or _.isString(obj)
    for key of obj
      return false  if hasOwnProperty.call(obj, key)
    true

  _.isElement = (obj) ->
    !!(obj and obj.nodeType is 1)

  _.isArray = nativeIsArray or (obj) ->
    toString.call(obj) is "[object Array]"

  _.isObject = (obj) ->
    obj is Object(obj)

  _.isArguments = (obj) ->
    toString.call(obj) is "[object Arguments]"

  unless _.isArguments(arguments)
    _.isArguments = (obj) ->
      !!(obj and hasOwnProperty.call(obj, "callee"))
  _.isFunction = (obj) ->
    toString.call(obj) is "[object Function]"

  _.isString = (obj) ->
    toString.call(obj) is "[object String]"

  _.isNumber = (obj) ->
    toString.call(obj) is "[object Number]"

  _.isNaN = (obj) ->
    obj isnt obj

  _.isBoolean = (obj) ->
    obj is true or obj is false or toString.call(obj) is "[object Boolean]"

  _.isDate = (obj) ->
    toString.call(obj) is "[object Date]"

  _.isRegExp = (obj) ->
    toString.call(obj) is "[object RegExp]"

  _.isNull = (obj) ->
    obj is null

  _.isUndefined = (obj) ->
    obj is undefined

  _.noConflict = ->
    root._ = previousUnderscore
    this

  _.identity = (value) ->
    value

  _.times = (n, iterator, context) ->
    i = 0

    while i < n
      iterator.call context, i
      i++

  _.escape = (string) ->
    ("" + string).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#x27;").replace /\//g, "&#x2F;"

  _.mixin = (obj) ->
    each _.functions(obj), (name) ->
      addToWrapper name, _[name] = obj[name]

  idCounter = 0
  _.uniqueId = (prefix) ->
    id = idCounter++
    (if prefix then prefix + id else id)

  _.templateSettings =
    evaluate: /<%([\s\S]+?)%>/g
    interpolate: /<%=([\s\S]+?)%>/g
    escape: /<%-([\s\S]+?)%>/g

  _.template = (str, data) ->
    c = _.templateSettings
    tmpl = "var __p=[],print=function(){__p.push.apply(__p,arguments);};" + "with(obj||{}){__p.push('" + str.replace(/\\/g, "\\\\").replace(/'/g, "\\'").replace(c.escape, (match, code) ->
      "',_.escape(" + code.replace(/\\'/g, "'") + "),'"
    ).replace(c.interpolate, (match, code) ->
      "'," + code.replace(/\\'/g, "'") + ",'"
    ).replace(c.evaluate or null, (match, code) ->
      "');" + code.replace(/\\'/g, "'").replace(/[\r\n\t]/g, " ") + ";__p.push('"
    ).replace(/\r/g, "\\r").replace(/\n/g, "\\n").replace(/\t/g, "\\t") + "');}return __p.join('');"
    func = new Function("obj", "_", tmpl)
    return func(data, _)  if data
    (data) ->
      func.call this, data, _

  wrapper = (obj) ->
    @_wrapped = obj

  _:: = wrapper::
  result = (obj, chain) ->
    (if chain then _(obj).chain() else obj)

  addToWrapper = (name, func) ->
    wrapper::[name] = ->
      args = slice.call(arguments)
      unshift.call args, @_wrapped
      result func.apply(_, args), @_chain

  _.mixin _
  each [ "pop", "push", "reverse", "shift", "sort", "splice", "unshift" ], (name) ->
    method = ArrayProto[name]
    wrapper::[name] = ->
      method.apply @_wrapped, arguments
      result @_wrapped, @_chain

  each [ "concat", "join", "slice" ], (name) ->
    method = ArrayProto[name]
    wrapper::[name] = ->
      result method.apply(@_wrapped, arguments), @_chain

  wrapper::chain = ->
    @_chain = true
    this

  wrapper::value = ->
    @_wrapped
).call this