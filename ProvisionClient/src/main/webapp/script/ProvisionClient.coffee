window.ProvisionClient = new Object if not window.ProvisionClient?
ProvisionClient = window.ProvisionClient

ProvisionClient.Image = new Object
Backbone.ModelAndView.generate
  namespace : ProvisionClient.Image
  url: "/Provision"
  name : "image"
  schema:
    id: "id"
  toString:->
    "#{@get("id")} : #{@get("name")}"
      
ProvisionClient.Firmware = new Object
Backbone.ModelAndView.generate
  namespace : ProvisionClient.Firmware
  url: "/Provision"
  name : "firmware"
  schema:
    id: "id"
  toString:->
    "#{@get("id")} : #{@get("name")}"
    
    
ProvisionClient.Firmware.NewView = Backbone.View.extend
  initialize: ->
    @form = new Backbone.Form
      model: new ProvisionClient.Firmware.Model
      schema:
        name:
          type:"Text"
          
      events:{
            "submit form": "onClick"
        }
  render: ->
    holder = $("<div>")
    holder.html @form.render().el
    holder.append "<form id='fileupload' action='http://localhost:8080/ProvisionClient/#firmware' method='POST' enctype='multipart/form-data'><input type='file' name='files'><input type='text' name='name'><input type='submit' id='button1' value='Upload'></form>" 
    form = @form
    window.dialog holder,
      title:"Create a new Firmware"
      width:1000

 
ProvisionClient.Strategy = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.Strategy
  url: "/Provision"
  name: "strategy"
  schema:
    id: "name"  
  toString:->
    "#{@get("name")} : #{@get("description")}"    


ProvisionClient.PhysicalStrategy = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.PhysicalStrategy
  url: "/Provision"
  name: "strategy/physical"
  schema:
    id: "name"  
  toString:->
    "#{@get("name")} : #{@get("description")}"    

ProvisionClient.VirtualStrategy = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.VirtualStrategy
  url: "/Provision"
  name: "strategy/virtual"
  schema:
    id: "name"  
  toString:->
    "#{@get("name")} : #{@get("description")}"    


ProvisionClient.Capability = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.Capability
  url: "/Provision"
  name: "capability"
  schema:
    id:"id"
  formSchema:
    name:
      type:"Text"
    description:
      type:"Text"
    uri:
      type:"Text"
      title:"URI"
  toString:->
    @get("name")

ProvisionClient.User = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.User
  url: "/Provision"
  name: "user"
  schema:
    id:"id"
  formSchema:
    name:
      type:"Text"
  toString:->
    @get("name")
    
    
ProvisionClient.Project = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.Project
  url: "/Provision"
  name: "project"
  schema:
    id:"id"
  formSchema:
    name:
      type:"Text"
    endPointURL:
       type:"Text"
       title:"EndPointURL"
    accessKey:
       type:"Text"
    secretKey:
       type:"Text"
  toString:->
     @get "name"

ProvisionClient.Certificate = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.Certificate
  url: "/Provision"
  name: "certificate"
  schema:
    id:"id"
  model:
    bind: [
      {
        "change": ->
          if @get("signer")? and typeof @get("signer") is "string" and @get("signer").length > 2
            me = this
            ProvisionClient.Certificate.Model.find @get("signer"), (signer)->
              me.set
                signer: signer
      }
    ]
  formSchema:
    countryName:
      type:"Text"
      defaultValue:"US"
    state:
      type:"Text"
      defaultValue:"CA"
    locality:
      type:"Text"
      defaultValue:"El Segundo"
    organization:
      type:"Text"
      defaultValue:"ClearPath Networks"
    organizationalUnit:
      type:"Text"
      defaultValue:"VSP"
    commonName:
      type:"Text"
    emailAddress:
      type:"Text"
      defaultValue:"certs@clearpathnet.com"
    subjectAltName:
      type:"Text"
      defaultValue:"email:copy"
    daysValidFor:
      type:"Number"
      defaultValue:7300
    CA:
      type:"Select"
      title:"CA"
      options: [{val:true, label:"Yes"}, {val:false, label:"No"}]
    role:
      type:"Select"
      title:"Role"
      options: [{val:0, label:"None"}, {val:1, label:"Virtual Cloud Snap Signer"}, {val:2, label:"Management Server"}, {val:4, label:"Logging Server"}, {val:8, label:"Mobile Config Signer"}, {val:16, label:"SSL Reverse Proxy Client"}, {val:32, label:"SSL Reverse Proxy Server"}]
    pathlen:
      type:"Number"
      title:"CA Path Length"
      defaultValue:-1
    signer: 
      type:"Select"
      options: (callback) ->
        ProvisionClient.Certificate.Collection.find (collection)->
          col = new ProvisionClient.Certificate.Collection
          selfSign = new ProvisionClient.Certificate.Model
          selfSign.set
            id:-1
            subject:
              CN: "Self Signed"
          col.add selfSign
          collection.each (item)->
            col.add item
          callback col
  toString:->
    @get("subject").CN


ProvisionClient.CertificateProvider = new Object
Backbone.ModelAndView.generate
  namespace: ProvisionClient.CertificateProvider
  url: "/Provision"
  name: "certificate/provider"
  schema:
    id: "hostName"
  formSchema:
    hostName:
      type:"Text"
    userName:
      type:"Text"
    password:
      type:"Password"
  toString:->
    @get "hostName"
  
ProvisionClient.Server = new Object
Backbone.ModelAndView.generate
  namespace: window.ProvisionClient.Server
  url: "/Provision"
  name: "server"
  schema:
    id: "id"
  toString:->
    "#{@get("id")} : #{@get("name")}"
  model:
    bind:
      destroy: ->
        @set
          "status": "Terminating..."
  


ProvisionClient.Server.RebootView = Backbone.View.extend
  template: _.template($("#instance-reboot-view-template").html())
  render: ->
    if not @template
      throw "Coundn't find the reboot view teplate for Instances."
    if not @model?
      throw "No model to reboot"
    d=$(@template(@model.toJSON()))
    model = @model
    window.dialog d,
      title: "Confirm reboot of #{model}"
      buttons: [
        {
          text: "Cancel"
          icons: { secondary: 'ui-icon-circle-close' }
          click: ->
            $(this).dialog "close"
        }
        {
          text:"Reboot"
          icons:{ secondary: 'ui-icon-power'}
          click: ->
            $(this).dialog "close"
            model.set 
              instanceState: "Rebooting..."
              model.save
                error: (model, err) ->
                  div= $("<div />")
                  div.html err.responseText
                  window.dialog $(div)
        }
      ]
ProvisionClient.Server.DeleteView = Backbone.View.extend
  template: _.template($("#instance-delete-view-template").html())
  render: ->
    if not @template?
      throw "Couldn't find the delete view template for #{options.name}"
    if not @model?
      throw "No model for the #{options.name} delete view"
    d = $(@template(@model.toJSON()))
    model = @model
    window.dialog d,
      title: "Confirm removal of #{model}"
      buttons: [
        {
          text: "Cancel"
          icons: { secondary: 'ui-icon-circle-close' }
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Terminate"
          icons: { secondary: 'ui-icon-alert' }
          click: ->
            $(this).dialog "close"
            model.set
              instanceState: "Shutting Down..."
            model.destroy
              error: (model, err)->
                div = $("<div />")
                div.html err.responseText
                window.dialog $(div)
        }
      ]


ProvisionClient.Server.Flavor= new Object
Backbone.ModelAndView.generate
  namespace: window.ProvisionClient.Server.Flavor
  url: "/Provision"
  name: "server/flavor"
  schema:
    id:"id"
    name:name
  toString:->
    "#{@get("id")} : #{@get("name")}"

ProvisionClient.Resource = new Object
Backbone.ModelAndView.generate
  namespace : ProvisionClient.Resource
  url: "/Provision"
  name : "resource"
  toString:->
      "Resource ##{@get("id")} : #{@get("hostName")}"


ProvisionClient.Volume = new Object
Backbone.ModelAndView.generate
  namespace : ProvisionClient.Volume
  url: "/Provision"
  name : "volume"
  schema:
    id: "volumeId"
  toString:->
      "Product ##{@get("volumeId")} : #{@get("name")}"

ProvisionClient.Volume.DetachView = Backbone.View.extend
  template: _.template($("#volume-detach-view-template").html())
  render: ->
    if not @template?
      throw "Couldn't find the Detach Volume View template"
    if not @model?
      throw "No model found for Detach Volume View template"
    d = $(@template(@model.toJSON()))
    model = @model
    window.dialog d,
      title: "Confirm removal of #{model}"
      buttons: [
        {
          text: "Cancel"
          icons: { secondary: 'ui-icon-circle-close' }
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Detach"
          icons: { secondary: 'ui-icon-eject' }
          click: ->
            $(this).dialog "close"
            model.set
              "volumeAttachments": []
            model.save null, 
              error: (model, err)->
                div = $("<div />")
                div.html err.responseText
                window.dialog $(div)
        }
      ]

ProvisionClient.Volume.DeleteView = Backbone.View.extend
  template: _.template($("#volume-delete-view-template").html())
  render: ->
    if not @template?
      throw "Couldn't find the delete view template for #{options.name}"
    if not @model?
      throw "No model for the Volume delete view"
    d = $(@template(@model.toJSON()))
    model = @model
    window.dialog d,
      title: "Confirm removal of #{model}"
      buttons: [
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
            model.url = ->
              "/Provision/volume/#{model.get("volumeId")}"
            model.destroy
              error: (model, err)->
                div = $("<div />")
                div.html err.responseText
                window.dialog $(div)
        }
        {
          text: "Force Delete"
          icons: { secondary: 'ui-icon-alert' }
          click: ->
            $(this).dialog "close"
            model.url = ->
              "/Provision/volume/#{model.get("volumeId")}/force"
            model.destroy
              error: (model, err)->
                model.url = ->
                  "/Provision/volume/#{model.get("volumeId")}"
                div = $("<div />")
                div.html err.responseText
                window.dialog $(div)
        }
      ]         
          
ProvisionClient.Product = new Object
Backbone.ModelAndView.generate
  namespace : ProvisionClient.Product
  url: "/Provision"
  name : "product"
  model:
    bind:
      change:->
        if @get("capabilities") and @get("capabilities").length > 0
          @set
            capabilities: _.map( @get("capabilities"), (item)->
              if typeof item == "string"
                return ProvisionClient.Capability.Collection.Cache.get item
              else
                return item
            )
  toString:->
      "Product ##{@get("id")} : #{@get("name")}"

ProvisionClient.Product.PhysicalNewView = Backbone.View.extend
  initialize: ->
    @form = new Backbone.Form
      model: new ProvisionClient.Product.Model
      schema:
        name:
          type:"Text"
        description:
          type:"Text"
        capabilities:
          type:"Select"
          options: (callback) ->
            ProvisionClient.Capability.Collection.find (collection)->
              callback collection
              $("select#capabilities").multiselect("refresh")
        locale:
          type:"Select"
          options: [{val:"en_US", label:"English"}, {val:"de_DE", label:"German"}]
        provisioningStrategy: 
          type:"Select"
          options: (callback) ->
            ProvisionClient.PhysicalStrategy.Collection.find (collection)->
              callback collection
        firmware:
          type:"Select"
          options: (callback) ->
            ProvisionClient.Firmware.Collection.find (collection) ->
              callback collection
        productLine:
          type:"Text"
  render: ->
    holder = $("<div>")
    holder.html @form.render().el
    form = @form
    window.dialog holder, 
      title:"Create a new Physical Product"
      width:1000
      buttons: [
        {
          text: "Cancel"
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Create New Physical Product"
          icon: 
            secondary:'ui-icon-arrowthick-1-e'
          click: ->
            $(this).dialog "disable"
            dialog = $(this)
            form.commit()
            form.model.save null, 
              success: ->
                dialog.dialog "close"
                return if not ProvisionClient.Product.Collection.Cache?
                ProvisionClient.Product.Collection.Cache.add form.model
                ProvisionClient.Product.Collection.Cache.trigger "reset"
        }
      ]

    $("select#capabilities").attr("multiple", "multiple").multiselect(
      position: 
        my: 'center'
        at: 'center'
   
    ).multiselect("uncheckAll")     
         
ProvisionClient.Product.VirtualNewView = Backbone.View.extend
  initialize: ->
    @form = new Backbone.Form
      model: new ProvisionClient.Product.Model
      schema:
        name:
          type:"Text"
        description:
          type:"Text"
        capabilities:
          type:"Select"
          options: (callback) ->
            ProvisionClient.Capability.Collection.find (collection)->
              callback collection
              $("select#capabilities").multiselect("refresh")
        project: 
          type:"Select"
          options: (callback) ->
            ProvisionClient.Project.Collection.find (collection)->
              callback collection
        locale:
          type:"Select"
          options: [{val:"en_US", label:"English"}, {val:"de_DE", label:"German"}]
        image: 
          type:"Select"
          options: (callback) ->
            ProvisionClient.Image.Collection.find (collection)->
              callback collection
        flavor:
          type: "Select"
          options: (callback) ->
            ProvisionClient.Server.Flavor.Collection.find (collection)->
              callback collection
        provisioningStrategy: 
          type:"Select"
          options: (callback) ->
            ProvisionClient.VirtualStrategy.Collection.find (collection)->
              callback collection
        volumeSize:
          type:"Number"
          defaultValue:1
        startupScript:
          type:"TextArea"

          
            
  render: ->
    holder = $("<div>")
    holder.html @form.render().el
    form = @form
    window.dialog holder, 
      title:"Create a new Product"
      width:1000
      buttons: [
        {
          text: "Cancel"
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Create New Product"
          icon: 
            secondary:'ui-icon-arrowthick-1-e'
          click: ->
            $(this).dialog "disable"
            dialog = $(this)
            form.commit()
            form.model.save null, 
              success: ->
                dialog.dialog "close"
                return if not ProvisionClient.Product.Collection.Cache?
                ProvisionClient.Product.Collection.Cache.add form.model
                ProvisionClient.Product.Collection.Cache.trigger "reset"
        }
      ]

    $("select#capabilities").attr("multiple", "multiple").multiselect(
      position: 
        my: 'center'
        at: 'center'
   
    ).multiselect("uncheckAll")


ProvisionClient.VirtualResourceRequest = new Object
ProvisionClient.VirtualResourceRequest.LaunchView = Backbone.View.extend
  initialize: ->
    @template = _.template $("#resourceLaunchDialog").html()
  render: ->
    @el = $(@template(@model))
    @dialog = window.dialog @el,
      title: "Your Resource is launching..."
      buttons: [
        {
          text: "Ok"
          click: ->
            $(this).dialog "close"
        },
      ]
  success: (resource) ->
    @el.html _.template $("#resourceLaunchSuccess").html(), resource
  failure: (failureReason)->
    debugger
   
    @el.html _.template $("#resourceLaunchFailure").html(), failureReason
    
ProvisionClient.VirtualResourceRequest.View = Backbone.View.extend
  initialize: ->
    
  render: ->
    holder = $("<div>")
    holder.html "Launch?"
    model = @model
    window.dialog holder,
      title:"Launching a new #{@model.toString()}"
      buttons: [
        {
          text: "Cancel"
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Launch!"
          click: ->
            me = this
            $(this).parent().fadeOut()
            $('.ui-widget-overlay').remove()
            $("<div />", 
              'class':'ui-widget-overlay'
            ).css(
              height: $("body").outerHeight()
              width: $("body").outerWidth()
              zIndex: 1001
            ).appendTo("body").fadeOut ->
              $(this).remove();
              $(me).parent().remove();
              confirmation = new ProvisionClient.VirtualResourceRequest.LaunchView
                model:model
              confirmation.render.call confirmation
              Backbone.sync "create", model, 
                url:"/Provision/resource"
                error:(err)->
                  confirmation.failure.call confirmation, err
                success:(data)->
                  return if not ProvisionClient.Resource.Collection.Cache?
                  ProvisionClient.Resource.Collection.Cache.add new ProvisionClient.Resource.Model data
                  ProvisionClient.Resource.Collection.Cache.trigger "reset"
                  confirmation.success.call confirmation, data
                  
        }
      ]

ProvisionClient.PhysicalResourceRequest = new Object
ProvisionClient.PhysicalResourceRequest.LaunchView = Backbone.View.extend
  initialize: ->
    @template = _.template $("#resourceLaunchDialog").html()
  render: ->
    @el = $(@template(@model))
    @dialog = window.dialog @el,
      title: "Your physical product is being provisioned..."
      buttons: [
        {
          text: "Ok"
          click: ->
            $(this).dialog "close"
        },
      ]
  success: (resource) ->
    @el.html _.template $("#resourceLaunchSuccess").html(), resource
  failure: (failureReason)->
    debugger
   
    @el.html _.template $("#resourceLaunchFailure").html(), failureReason
    
ProvisionClient.PhysicalResourceRequest.View = Backbone.View.extend
  initialize: ->
    @form = new Backbone.Form
      model: @model
      schema:
        serial:
          type:"Text"
        hostname:
          type:"Text"
          
            
  render: ->
    holder = $("<div>")
    holder.html @form.render().el
    form = @form
    model = @model
    window.dialog holder,
      title:"Launching a new #{@model.toString()}"
      buttons: [
        {
          text: "Cancel"
          click: ->
            $(this).dialog "close"
        }
        {
          text: "Launch!"
          click: ->
            form.commit()
            me = this
            $(this).parent().fadeOut()
            $('.ui-widget-overlay').remove()
            $("<div />", 
              'class':'ui-widget-overlay'
            ).css(
              height: $("body").outerHeight()
              width: $("body").outerWidth()
              zIndex: 1001
            ).appendTo("body").fadeOut ->
              $(this).remove();
              $(me).parent().remove();
              confirmation = new ProvisionClient.PhysicalResourceRequest.LaunchView
                model:model
              confirmation.render.call confirmation
              Backbone.sync "create", model, 
                url:"/Provision/resource"
                error:(err)->
                  confirmation.failure.call confirmation, err
                success:(data)->
                  return if not ProvisionClient.Resource.Collection.Cache?
                  ProvisionClient.Resource.Collection.Cache.add new ProvisionClient.Resource.Model data
                  ProvisionClient.Resource.Collection.Cache.trigger "reset"
                  confirmation.success.call confirmation, data
                  
        }
      ]