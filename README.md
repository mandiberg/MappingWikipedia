Mapping Wikipedia
==============

This code can be used to extract all IPs and usernames from the Wikipedia revision history (stub-meta-history), geolocate them with www.DB-IP.com, and prepare them for visualization with D3.js. This code was used to produce the graphs in an article published in The Atlantic: https://www.theatlantic.com/technology/archive/2020/02/where-wikipedias-editors-are-where-they-arent-and-why/605023/

## What is each file or in each folder


* Mapping Wikipedia Data // a folder with the annual, and summary data for US and World.
* Geoname // a python script and some sql queries to parse data as well as some output examples for working with Geoname
* sql\ queries //most frequently used sql queries in parsing wiki data
* Netbeans project files
	* build.xml // Netbeans project file
	* build // Netbeans project folder
	* lib // Netbeans project folder
	* manifest.mf // Netbeans project file
	* nbproject  // Netbeans project folder
	* src //source code for Netbeans project
* mysql-connector-java-8.0.15 //dependency library
* dist // this is where the compiled jar file goes (and the dependencies: mysql connector and json-simple

## Methodology Notes

When I was making Print Wikipedia, I discovered a shadow dataset of time-stamped IP addresses. I was actually trying to make a list of the contibutors, for the Contributor Appendix, and discovered that there were a lot less username editors than the official numbers stated, and a lot more IPs. At the time of the data I worked with (March 1st 2019) there were 884 million edits to English Wikipedia, made by 41M IP addresses, and 8.6 milion username editors. I chose to only count users that had made edits to articles. 

8.6 million usernames is about 1/3 of the total number of the 35.7 million "users who have registered a username" noted in Wikipedia’s official statistics. This number is somewhat perplexing, given that it doesn’t conform to the 1% rule, and you don’t actually need an account to edit.  

Most of the IP editors represented in these maps only made a small number of edits. A very small percentage of these IP editors are active editors who edit without usernames, and do not login (in over a decade editing Wikipedia I have only encountered maybe a dozen of these active IP editors). And most importantly for this effort, this data likely includes active editors who usually login under their usernames but who have, on occasion, forgotten to log in while editing, leaving behind a trace of their location in Wikipedia’s edit logs. This is a significant assumption I have made, but I think it is a safe one. The IP data likely contains the long tail of the username editor population, but not the big head; the long tail of username editors likely matches the patterns in the IP editor data.

Working with IP data has significant limitations. IP geolocation is not a perfect science, and I have been careful to avoid some of the known pitfalls with mapping IP addresses. When the database doesn’t know the precise location of an IP, it [places the location at the geographic center of the state](https://splinternews.com/how-an-internet-mapping-glitch-turned-a-random-kansas-f-1793856052), or the country (a farm in Butler County, Kansas!) To avoid this problem I matched IPs to www.geonames.org with the www.db-ip.com database, and then filtered out the ~3% of results that were only accurate to the country or state level, leaving only county level data. In the data used, Butler County registered very low levels of Wikipedia editing activity.

I have also removed a list of 103,000 IPs blocked for being proxies, VPNs and Tor exit nodes which serve as a way of spoofing IP info; I have left in the 22,000 IPs blocked for vandalism, abuse, and other bad faith editing, though it is notable that the number of blocked IP addresses is so small!

Most of all, I was concerned about the way dynamic IPs are reassigned to new users, and thus move around, diminishing their accuracy over time. I couldn’t locate any research discussing the relative probability of reassignment, or margin of error over time, and none of my artist, tech/programmer, or professorial colleagues could offer me any guidance. The DP-IP data is from early 2019, so conceivably the older the Wikipedia data is, the more unreliable its geolocation is. If this were the case, the older data would conform less to the overall trends, and we would see more noise. But in fact, the trends in the annual data stay consistent. To be as conservative as possible I have only mapped the 2018 data, as that data is the freshest and likely most accurate, though it essentially matches the data from the full date range.


## Credits

This project was supported by the Eyebeam Center for the Future of Journalism. Danara Sarıoğlu contributed programming assistance. Frank Donnelly contributed spatial analysis assistance.
