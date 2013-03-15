$(document).ready ->
  window.ProvisionClient = new Object if not window.ProvisionClient?
  ProvisionClient = window.ProvisionClient
  ProvisionClient.modelList = (namespace, el, index) ->
    ->
      throw "Bad namespace" if not namespace?
      throw "Can't find the element #{el.selector} to put the list view in." if el.size() < 1
      throw "Can't find the Collection for #{el.selector}. Did you forget your ModelAndView?" if not namespace.Collection?
      window.tabs.tabs "select", index if window.tabs?
      if namespace.Collection.Cache?
        return namespace.Collection.Cache.trigger "reset"
      namespace.Collection.find (collection)->
        listView = new namespace.ListView
          model: collection
          el: el
        listView.render()
        
  ProvisionClient.AppRouter = Backbone.Router.extend
    routes:
      "product": "productList"
      "resource": "resourceList"
      "server": "serverList"
      "flavor": "flavorList"
      "volume": "volumeList"
      "certificate": "certificateList"
      "certificateProvider": "certificateProviderList"
      "region": "regionList"
      "user": "userList"
      "capability": "capabilityList"
      "firmware": "firmwareList"
      "project": "projectList"
      "*path": "productList"
      
    productList: ProvisionClient.modelList ProvisionClient.Product, $("#product-list"), 0
    capabilityList: ProvisionClient.modelList ProvisionClient.Capability, $("#capability-list"), 1
    resourceList: ProvisionClient.modelList ProvisionClient.Resource, $("#resource-list"), 2
    serverList: ProvisionClient.modelList ProvisionClient.Server, $("#server-list"), 3
    volumeList: ProvisionClient.modelList ProvisionClient.Volume, $("#volume-list"), 4
    certificateList: ProvisionClient.modelList ProvisionClient.Certificate, $("#certificate-list"), 5
    certificateProviderList: ProvisionClient.modelList ProvisionClient.CertificateProvider, $("#certificate-provider-list"), 6
    regionList: ProvisionClient.modelList ProvisionClient.Region, $("#region-list"), 7
    flavorList: ProvisionClient.modelList ProvisionClient.Server.Flavor, $("#instance-type-list"), 8
    userList: ProvisionClient.modelList ProvisionClient.User, $("#user-list"), 9
    firmwareList: ProvisionClient.modelList ProvisionClient.Firmware, $("#firmware-list"), 10
    projectList: ProvisionClient.modelList ProvisionClient.Project, $("#project-list"), 11
    
  window.app = new ProvisionClient.AppRouter
  Backbone.origsync = Backbone.sync
  Backbone.sync = (method, model, options)->
    options = _.extend({username:"clearpathadmin", password:"DdS9LfZewybFcsntxTnUjb6tBVWGaw8PX8hWawER9LK8c6NRr2Zbqc6aMTCLwuySMFPzM6xW"}, options);
    Backbone.origsync(method, model, options);
  
  Backbone.history.start()
  window.tabs = $("#menu").tabs
    select: (event, ui) ->
      if ui.tab 
        location.href = ui.tab
      true
  $("#menu").fadeIn("slow", "linear");
  
  $('.ui-widget-overlay').live 'click', ->
     window.currentDialog.dialog("close") if window.currentDialog?
  
        
  window.viewButtons = [
    {el: "#addNewVirtualProductButton",                                   view: ProvisionClient.Product.VirtualNewView,       icons: {secondary: "ui-icon-newwin"}}
    {el: "#addNewPhysicalProductButton",                                  view: ProvisionClient.Product.PhysicalNewView,      icons: {secondary: "ui-icon-newwin"}}
    
    {el: ".inspectProductButton",  model: ProvisionClient.Product.Model,  view: ProvisionClient.Product.View,          icons: {secondary: "ui-icon-zoomin"}}
    {el: ".launchVirtualProductButton",   model: ProvisionClient.Product.Model,  view: ProvisionClient.VirtualResourceRequest.View,  icons: {secondary: "ui-icon-play"}}
    {el: ".launchPhysicalProductButton",  model: ProvisionClient.Product.Model,  view: ProvisionClient.PhysicalResourceRequest.View,  icons: {secondary: "ui-icon-play"}}
    {el: ".deleteProductButton",   model: ProvisionClient.Product.Model,  view: ProvisionClient.Product.DeleteView,    icons: {secondary: "ui-icon-closethick"}}
   
   
    {el: "#createNewCapabilityButton", view: ProvisionClient.Capability.NewView, icons: {secondary: "ui-icon-newwin"}}                                 
    {el: ".inspectCapabilityButton", model: ProvisionClient.Capability.Model, view: ProvisionClient.Capability.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteCapabilityButton",     model: ProvisionClient.Capability.Model, view: ProvisionClient.Capability.DeleteView, icons: {secondary: "ui-icon-closethick"}}
   
   
    {el: ".inspectResourceButton", model: ProvisionClient.Resource.Model, view: ProvisionClient.Resource.View,         icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteResourceButton",  model: ProvisionClient.Resource.Model, view: ProvisionClient.Resource.DeleteView,   icons: {secondary: "ui-icon-closethick"}}
    
    {el: ".inspectServerButton",  model: ProvisionClient.Server.Model,  view: ProvisionClient.Server.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".rebootServerButton",  model: ProvisionClient.Server.Model,  view: ProvisionClient.Server.RebootView,  icons: {secondary: "ui-icon-power"}}
    {el: ".deleteServerButton",   model: ProvisionClient.Server.Model,  view: ProvisionClient.Server.DeleteView, icons: {secondary: "ui-icon-closethick"}}
    
    {el: ".inspectVolumeButton",  model: ProvisionClient.Volume.Model,  view: ProvisionClient.Volume.View,             icons: {secondary: "ui-icon-zoomin"}}
    {el: ".detachVolumeButton",  model: ProvisionClient.Volume.Model,  view: ProvisionClient.Volume.DetachView,        icons: {secondary: "ui-icon-eject"}}
    {el: ".deleteVolumeButton",   model: ProvisionClient.Volume.Model,  view: ProvisionClient.Volume.DeleteView,       icons: {secondary: "ui-icon-closethick"}}
  
    
    {el: "#createNewCertificateButton", view: ProvisionClient.Certificate.NewView, icons: {secondary: "ui-icon-newwin"}}                                 
    {el: ".inspectCertificateButton", model: ProvisionClient.Certificate.Model, view: ProvisionClient.Certificate.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteCertificateButton",     model: ProvisionClient.Certificate.Model, view: ProvisionClient.Certificate.DeleteView, icons: {secondary: "ui-icon-closethick"}}
    
    
    {el: "#createNewCertificateProviderButton", view: ProvisionClient.CertificateProvider.NewView, icons: {secondary: "ui-icon-newwin"}}
    {el: ".deleteCertificateProviderButton",   model: ProvisionClient.CertificateProvider.Model,  view: ProvisionClient.CertificateProvider.DeleteView, icons: {secondary: "ui-icon-closethick"}}
   
   
   
    {el: "#createNewUserButton", view: ProvisionClient.User.NewView, icons: {secondary: "ui-icon-newwin"}}
    {el: ".inspectUserButton", model: ProvisionClient.User.Model, view: ProvisionClient.User.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteUserButton",     model: ProvisionClient.User.Model, view: ProvisionClient.User.DeleteView, icons: {secondary: "ui-icon-closethick"}}
    
    
    {el: "#createNewProjectButton", view: ProvisionClient.Project.NewView, icons: {secondary: "ui-icon-newwin"}}
    {el: ".inspectProjectButton", model: ProvisionClient.Project.Model, view: ProvisionClient.Project.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteProjectButton",     model: ProvisionClient.Project.Model, view: ProvisionClient.Project.DeleteView, icons: {secondary: "ui-icon-closethick"}}
                                     
     
    {el: "#createNewFirmwareButton", view: ProvisionClient.Firmware.NewView, icons: {secondary: "ui-icon-newwin"}}
    {el: ".inspectFirmwareButton", model: ProvisionClient.Firmware.Model, view: ProvisionClient.Firmware.View,       icons: {secondary: "ui-icon-zoomin"}}
    {el: ".deleteFirmwareButton",     model: ProvisionClient.Firmware.Model, view: ProvisionClient.Firmware.DeleteView, icons: {secondary: "ui-icon-closethick"}}
   
  ]
 
  window.renderButtons = ->
    for viewButton in window.viewButtons
      do(viewButton) ->
        options = {}
        throw "Can't find the view for #{viewButton.el}" if not viewButton.view?
        options.icons = viewButton.icons if viewButton.icons?
        options.title = viewButton.title if viewButton.title?
        $(viewButton.el).button(options).die().live "click", ->
          id = $(this).attr("objectId")
          if id? and (id > 0 || id.length > 0) and viewButton.model?
            viewButton.model.find id, (model) ->
              view = new viewButton.view
                model:model
              view.render.call view
          else
            view = new viewButton.view
            view.render.call view
  
  window.renderButtons()
  try
    $('#ThemeRoller').themeswitcher()
  

