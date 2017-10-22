# usermapping
A TeamCity plugin to provide user mapping as parameters so other plugins can map a user to another name. For example, our [Hedwig](https://github.com/Nicologies/Hedwig) will send message to Slack/HipChat use the mapped username provided by this plugin when your GitHub/TeamCity username is different to the Slack/HipChat one.

## Install

- Download the zip from [https://github.com/Nicologies/usermapping/releases/latest] and copy to TeamCity's plugins folder

## Configuration

- Create the `usermapping` folder in `<teamcity>/system/pluginData/`
- Create a `usermapping.json` file in `<teamcity>/system/pluginData/usermapping` folder

in the `usermapping.json` file you can map a user to another name, for example:

```
[
  {
    orgNames: ["Alice", "Alice.Blue"],
    newNames: 
    {
      hipchat: "AliceBlue",
      slack: "aliceblue"
    }
  },
  {
    orgName: ["Bob", "Bob.Martin"],
    newNames: 
    {
      hipchat: "Robert",
      slack: "bob"
    }
  }
]
```

then the plugin will export the parameter `user_mapping.Alice.hipchat` with value of `AliceBlue` and the `user_mapping.Alice.slack` with value `aliceblue`
