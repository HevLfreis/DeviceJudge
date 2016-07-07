# Devicejudge
Devicejudge is a platfrom of monitoring user behaviors in net traffic by java and django    
Location:http://dj.seeleit.com/       
Account name: test Password: 123456       
Better browsing performance with desktop browsers    

***

Devicejudge(DJ) is a basic monitoring platform which focus on the user behaviors, like devices and apps people usage, in net traffic.  DJ comprises two parts: traffic scanner and web dashboard. The traffic scanner captures the info of devices and apps through HTTP UserAgent and app fingerprints from net traffic. The web dashboard is used to manage the info we have collected.  

The scanner is based on [jNetPcap](http://www.jnetpcap.com) which is a Java wrapper for nearly all libpcap library native calls. You can redirect any net traffic to a server and run the scanner on it.     

We collect fingerprints of some popular apps in FpConstants.java manually. Maybe you can introduce pattern-recognization or machine-learning methods.   

The web dashboard provides stats and management of captured info, fingerprints as well as logs. 

## How to use

1. scanner part  
	1. DynamicTrafficScanner is the main scanner class, after it has been called, it will create three handlers(threads):  
		- SpeedHandler    
		- LogHandler     
		- DatabaseHandler      
	2. Each handler will take care of the corresponding info and sync it to database.    
	3. Start the web part to manage the info we have collected.

	
2. web part  
	1. Change the database settings in settings.py.

	2. Then 
		```
		python manage.py makemigrations
		python manage.py migrate
		```
	3. We also provide the database of traffic info we have collected under /database, you can import the .sql to your own database.
	4. For deployment, you can check [here](https://github.com/HevLfreis/server) for more infomation. Hope it helps.
	

> If you have any problem, please contact hevlhayt@foxmail.com (ﾉﾟ▽ﾟ)ﾉ







