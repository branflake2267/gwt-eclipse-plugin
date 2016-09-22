#GWT Plugin for Eclipse
The GWT Plugin for Eclipse repository for version 3. 
This plugin does not contain the cloud tools as they have been split into 
[Google Plugin for Eclipse](https://github.com/GoogleCloudPlatform/google-plugin-for-eclipse);  

## Reference

* [GPE Fork V2 GWT Eclipse Plugin](https://github.com/gwt-plugins/gwt-eclipse-plugin/tree/gpe-fork)

## Repository
The Eclipse repositories for this plugin. 

### Snapshot

* [http://storage.googleapis.com/gwt-eclipse-plugin/v2/snapshot](http://storage.googleapis.com/gwt-eclipse-plugin/v2/snapshot)

### Production

* [http://storage.googleapis.com/gwt-eclipse-plugin/v2/release](http://storage.googleapis.com/gwt-eclipse-plugin/v2/release)


### Build
Sencha has provided an internal build agent to build. 
[Sencha Eclipse Build](https://teamcity.sencha.com/viewType.html?buildTypeId=Gxt3_Gwt_GwtEclipsePlugin)

* `mvn clean package`

### Deploy
Google storage write permissions are needed to deploy. 

* `sh ./build-deploy-release.sh` - deploy production version
* `sh ./build-deploy-snapshot.sh` - deploy snapshot version

