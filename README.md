Steam Card Manager
==================

Description
-----------

Steam Card Manager is a Java application that allows check your steam cards inventory and know everything about it: 

- Games with cards available.
- Games with badges ready to craft.
- Games with completable badges (need some trade to complete).
- Games with a miss cards quantity to be complete (to buy or trade cards of different games). 

And some statistics: 

- Games by badge level.
- Badges that can be craft on that account, because game badge is already level 5.

Setting up
----------

**Step 1:** Install Java. 

In order to execute the .jar file you need to install a [Java JRE 9](https://docs.oracle.com/javase/9/install/overview-jdk-9-and-jre-9-installation.htm]).

**Step 2:** Execute .jar file. 

To execute via console use this instruction: 

```
$ java -jar SteamCardManager-1.0.jar
```

**Step 3:** Configure files. 

When you execute **SteamCardManager** for he first time, it generate a config file ``config.properties``:

```
  steam_user_id_64=
  steam_user_id=
  steam_api_id=
```

**steam_api_id**: A unique id generate by steam to your account, used by ask for information to steam API, you can get one [here](https://steamcommunity.com/dev/apikey).

**steam_user_id**: Is the name of your account, you can check out when enter at your profile page on the browser: http://steamcommunity.com/id/xyz your steam id is where xyz is.

**steam_user_id_64**: It is a internal steam id, the easy way to get it is using [this](https://steamidfinder.com) tool.

**Step 4:** Execute again and enjoy.

Known bugs
-----------

- For any reason, last page of steam inventory request give a ``null`` instead of json, it works after a couple of tries. 