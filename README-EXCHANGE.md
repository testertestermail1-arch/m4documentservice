**Overview**

This asset is a component of MuleSoft Accelerator for new APIs developed within Assurant.

The solution includes pre-built properties,error handler and configurations. This template can be extended to meet your API's unique needs. The following are the minimal changes required to create a valid API.

## Project Naming

 - After creating new API from the template, refactor and rename the project appropriately reflect the business purpose. Example: vehicle-api

## POM changes

 - Replace the group id with com.aiz.mule
 - Replace artifact id with appropriate api name. Example: vehicle-api
 - Replace version number with 1.0.0-SNAPSHOT
 - Replace name with api name. Example: vehicle-api
 - uncomment properties, plugins and dependencies as needed
 - uncomment scm tags and replace tag values for url,connection,developerConnection and tag fields
 - uncomment distributionManagement section
 
## Property File changes

 - Add project specific properties
 - For database properties, replace the property name with appropriate db name and db type. For example db.dbName can be renamed as db.vehicle
 - For Salesforce properties, uncomment existing properties and replace the values with appropriate user name and password
 
## Configuration Files(src/main/mule folder)

 - Create separate configuration for each flow with flow name representing the process.
 - Add flows under appropriate packages depending upon the API layer. For example, any flows interacting with the systems like database, applications, external services etc should be add under api.system package
 - Add/Remove connector configurations from global-config.xml as required for the API.(Do not keep unnecessary configurations because the related dependencies will make the archive heavy)

## Resource Files(src/main/mule folder)

 - Remove beans.xml file and its reference in `mule-artifact.json`, if data source configuration or spring beans are not used.
 - Place RAML files under api package
 - Place example RAMLs,files under api.examples package
 - Place data type RAMLs under api.types package
 - Place regular dwl files under dwl.transformations package
 - Place reusable dwl files under dwl.modules package. The code within these files can be reused in other dwl files using the construct `import dwl::modules::dwl_module_filename
 - Note that the keystore files for twg server keystores are under keystores.twg package
 - Client keystore files should be placed under appropriate key store package. For Example: client speiciic keystore for client `abc` should fo in to keystore.abc package.