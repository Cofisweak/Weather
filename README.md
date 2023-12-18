# Weather

### About project
Web application for viewing current weather. The user can register and add one or more locations (cities, villages, other points) to the collection, after which the main page of the application begins to display a list of locations with their current weather.

### Used technologies
1. Web-server: Tomcat 10
2. Database: PostgreSQL, Hibernate
3. Front-end: Thymeleaf, Bootstrap 5

### Deployment
- Install PostgreSQL database
- Init database scheme (init_db.sql)
- Install Tomcat 10
- Configure hibernate.cfg.xml
    - set url
    - set username
    - set password
- Install .war artifact

### Project functionality 
#### Common
The user can log in, register, log out of his account. 
For an authorized user, the ability to track the weather in saved locations is available. 

#### Sessions
Working with sessions is done entirely manually. Existing sessions are stored in a database. With each user request, the relevance of the session is checked.
The unique session identifier is stored by the user in cookies.

#### Weather service
The OpenWeather service is used to search for weather - https://openweathermap.org/.

#### Follow locations
Locations saved by the user are stored in the database. The database stores information about the location's geoposition and other metadata.
The user can delete a saved location.
