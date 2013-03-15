Backbone.ModelAndView = new Object
Backbone.ModelAndView.generate = (options) ->
  if not options.templateName?
    options.templateName = options.name.replace /\//g,"-"
  viewTemplate = null
  viewTemplate = _.template($("##{options.templateName}-item-template").html()) if $("##{options.templateName}-item-template").size() > 0
  listViewTemplate = null
  listViewTemplate = _.template($("##{options.templateName}-list-item-template").html()) if $("##{options.templateName}-list-item-template").size() > 0
  deleteViewTemplate = null
  deleteViewTemplate = _.template($("##{options.templateName}-delete-view-template").html()) if $("##{options.templateName}-delete-view-template").size() > 0
  options = _.extend
    viewTemplate: viewTemplate
    listViewTemplate: listViewTemplate
    deleteViewTemplate: deleteViewTemplate
    schema:
      id:"id"
    relations:[]
    toString:->
      this
  , options
  
  namespace = options.namespace
  
  namespace.Model = Backbone.Model.extend
    url: ->
      return "#{options.url}/#{options.name}" if not @get(options.schema.id)?
      "#{options.url}/#{options.name}/#{@get(options.schema.id)}"
    schema: options.schema
    isNew:->
      return not this.get(options.schema.id)?
    
    toString: options.toString
    initialize: ->
      me = this
      if options?.model?.bind?
        for k, v of options.model.bind
          do (k,v) ->
            me.bind k, v, me
      @bind "change", ->
        return if not namespace.Collection.Cache?
        namespace.Collection.Cache.trigger("reset")
      @bind "destroy", ->
        return if not namespace.Collection.Cache?
        namespace.Collection.Cache.remove this
        namespace.Collection.Cache.trigger("reset")

  namespace.Model.find = (id, callback, reload=false) ->
    if not (id > 0 || id.length > 0)
      throw "I'm not going to be able to find #{options.name} ##{id}"
    idObj = {}
    idObj[options.schema.id] = id
    result = null
    if namespace.Collection.Cache?
      result = namespace.Collection.Cache.find (obj) ->
        return "#{id}" is "#{obj.get(options.schema.id)}"
      return callback(result) if result?
    result = new namespace.Model idObj
    result.fetch
      success:->
        callback result
      error:(error)->
        throw error
   
  namespace.Collection = Backbone.Collection.extend
    model: namespace.Model
    url: "#{options.url}/#{options.name}"
    initialize: ->
      @bind "change", ->
        namespace.Collection.Cache = this
      @bind "destroy", ->
        namespace.Collection.Cache = null
  
  namespace.Collection.find = (callback) ->
    return callback(namespace.Collection.Cache) if namespace.Collection.Cache?
    result = new namespace.Collection
    result.fetch
      success:->
        namespace.Collection.Cache = result
        callback result
    
  namespace.ListView = Backbone.View.extend
    initialize: ->
      @model.bind "reset", @render, this
    render: (eventName) ->
      @el.empty()
      _.each @model.models, ((item) ->
        $(@el).append new namespace.ListItemView(model: item).render().el
      ), this
      window.renderButtons()
      this
  
  namespace.ListItemView = Backbone.View.extend
    template: options.listViewTemplate
    render: (eventName) ->
      if not @template?
        throw "Couldn't find the list item view template for #{options.name}"
      @el= @template(@model.toJSON())
      this
  
  namespace.View = Backbone.View.extend
    template: options.viewTemplate
    render: (eventName) ->
      if not @template?
        throw "Couldn't find the view template for #{options.name}"
      if not @model?
        throw "No model for the #{options.name} view"
      model = @model
      $(@el).html @template(model.toJSON())
      dialog $(@el),
        title: model.toString()
        buttons: [
          {
            text: "Ok"
            click: ->
              $(this).dialog "close"     
          }
        ]
      this
  
  window.dialog = (el, options = {}) ->
    options = _.extend 
      width: 800
      show: "fade"
      hide: "fade"
      modal:true
      closeOnEscape: true
    , options
    origCreate = options.create
    options.create = ->
      origCreate() if origCreate?
      options.buttons ||= {}
      if options?.buttons[0]?.icons?
        for x in [0..options.buttons.length-1]
          $($(this).parent().find('.ui-dialog-buttonpane button')[x]).button
            icons: { primary: options.buttons[x].icons.primary, secondary:options.buttons[x].icons.secondary}
      else
        if $(this).parent().find('.ui-dialog-buttonpane button').size() == 1
          $(this).parent().find('.ui-dialog-buttonpane button:first-child').button
            icons: { secondary: 'ui-icon-circle-check' }
          return   
        $(this).parent().find('.ui-dialog-buttonpane button:first-child').button
          icons: { secondary: 'ui-icon-circle-close' }
        $(this).parent().find('.ui-dialog-buttonpane button:first-child').next().button
          icons: { secondary: 'ui-icon-circle-check' }
    origOpen = options.open
    options.open = ->
      origOpen() if origOpen?
      $('.ui-widget-overlay').hide().fadeIn()
    origClose = options.beforeClose
    options.beforeClose = ->
      $('.ui-widget-overlay').remove()
      $("<div />", 
        'class':'ui-widget-overlay'
      ).css(
        height: $("body").outerHeight()
        width: $("body").outerWidth()
        zIndex: 1001
      ).appendTo("body").fadeOut ->
        $(this).remove();
      origClose() if origClose?
    window.currentDialog = el.dialog options    
      
  namespace.DeleteView = Backbone.View.extend
    template: options.deleteViewTemplate
    render: ->
      if not @template?
        throw "Couldn't find the delete view template for #{options.name}"
      if not @model?
        throw "No model for the #{options.name} delete view"
      d = $(@template(@model.toJSON()))
      model = @model
      window.dialog d,
        title: "Confirm removal of #{model}"
        buttons: if options.deleteViewButtons? then options.deleteViewButtons else [
          {
            text: "Cancel"
            icons: { secondary: 'ui-icon-circle-close' }
            click: ->
              $(this).dialog "close"
          }
          {
            text: "Delete"
            icons: { secondary: 'ui-icon-alert' }
            click: ->
              $(this).dialog "close"
              model.destroy
                error: (model, err)->
                  div = $("<div />")
                  div.html err.responseText
                  window.dialog $(div)
          }
        ]

  keyToTitle = (str) ->
    str = str.replace /([A-Z])/g, ' $1'
    str = str.replace /\/(.+)/g, ' $1'
    str = str.replace /\s(.?)/g, (str)->
      str.toUpperCase()
    str = str.replace /^./, (str) ->
      str.toUpperCase()
    str
  window.keyToTitle = keyToTitle
  
  namespace.NewView = Backbone.View.extend
    initialize: ->
      @form = new Backbone.Form
        model: new namespace.Model
        schema: options.formSchema     
                
    render: ->
      throw "Can't find the schema for #{options.name}" if not @form.schema?
      holder = $("<div>")
      holder.html @form.render().el
      form = @form
      window.dialog holder, 
        title:"Create New #{keyToTitle options.name}"
        width:1000
        buttons: [
          {
            text: "Cancel"
            click: ->
              $(this).dialog "close"
          }
          {
            text: "Create New #{keyToTitle options.name}"
            icon: 
              secondary:'ui-icon-arrowthick-1-e'
            click: ->
              $(this).dialog "disable"
              dialog = $(this)
              form.commit()
              form.model.save null, 
                success: ->
                  dialog.dialog "close"
                  return if not namespace.Collection.Cache?
                  namespace.Collection.Cache.add form.model
                  namespace.Collection.Cache.trigger "reset"
          }
        ]    


