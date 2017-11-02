# Chat-backend
A chat backend API that allows sending messages and retrieving messages between users. Its use mysql as data store, Drop wizard and Jetty server. The entire application runs in docker container. Also includes Swagger for easier API testing.



## Project Setup:

 To get the project up and running:

1. Install Docker https://docs.docker.com/engine/installation/
2. In a terminal, go to the directory chatAPI
3. docker-compose.yml file is configured to run Java backend project.
4. To start, run the following:
1. docker-compose up backend
2. Test that it’s running http://localhost:18000/test

## Notes: 
1. The DB script after creating the schema populates the tables with 10000 messages between valid users randomly. This is handy for testing.
2. A swagger UI is also included for easy UI based testing.
http://localhost:18000/swagger


### To restart the project

    docker-compose down
    docker-compose up backend

To see schema changes, remove the old db volume by adding -v when stopping

    docker-compose down -v

To see code changes, rebuild by adding --build when starting

    docker-compose up --build backend
If you run into issues connecting to the db on startup, try restarting (without the -v flag).

## Schema Design:

The database for the chat application consists of three main tables:
  1.	Table USERS stores User data.
  2.	Table USERS_MESSAGE_COUNT stores the total message count between two users.
  3.	Table MESSAGES stores each message sent between two users.


The MESSAGE_KEY identifies messages between any two users. It will always be  same for two users and is generated by the system using the USERNAME of the two users.
The MESSAGE table has a composite index on MESSAGE_KEY and CREATE_DATE.
To retrieve messages, only these two columns are used, so retrieval is very fast.

The table USERS_MESSAGE_COUNT is a handy table that gives the total number of messages between any two users. This total count of messages is very helpful with Pagination and Cache validation. Anytime a new message between two users is stored, the count is also updated. Instead of relying on MESSAGE table to get this count( which can get very expensive as the table continues to grow ), total message count retrieval  from this table is very quick.


## API:

1.**GET /test**

    CURL:   curl -X GET --header 'Accept: application/json' 'http://localhost:18000/test'
    Swagger: http://localhost:18000/swagger#!/test/get
   
 Checks that the application is up.
 
2.**GET /users**
   
    CURL: curl -X GET --header 'Accept: application/json' 'http://localhost:18000/users'
    Swagger:  http://localhost:18000/swagger#!/users/getAllUsers

   Retrieves all Users from the system. 

3. **POST /users**

       CURL:	curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "username": "user4567", "passwd": "w23wer4dd"}' 'http://localhost:18000/users'

       Swagger:  http://localhost:18000/swagger#!/users/addUser

Adds a new User to the system.

4.**GET /message**

    CURL: curl -X GET --header 'Accept: application/json' 'http://localhost:18000/message?user1=eric&user2=tom'

    curl -X GET --header 'Accept: application/json' 'http://localhost:18000/message?user1=eric&user2=tom&pagenum=2&pagesize=5'

    Swagger: http://localhost:18000/swagger#!/message/getMessage

Takes two required params user1 and user2, which should be valid usernames and returns messages sorted by date.
Optional params pagenum and pagesize provide pagination.
By default returns the first 1000 records if no pagination params present.

This endpoint uses smart caching to store messages. If the messages are found in cache for a given request, it compares the total count of messages from cache and compares it against the total count for these two users from the USERS_MESSAGE_COUNT tables. If the count is same that means content can be served from cache. If count is not same then, makes DB call and updates cache.

4.**POST /message**

    CURL:curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"sender": "eric","receiver": "tom","message_type": "img","message_value": "Hi there" }' 'http://localhost:18000/message'

    Swagger:http://localhost:18000/swagger#!/message/addMessage

Inserts a message between two valid users. Message_type needs to be either ‘text’ or ‘img’ or ‘video’ .
For img and video, meta_data is stored in JSON format in table. This gives the benefit of adding metadata in an Ad hoc fashion not worrying about the table structure.
For every message inserted in MESSAGE table, the count is incremented in USERS_MESSAGE_COUNT table for these two users. These two operations are transactional to maintain data consistency.


## CONCLUSION:

The reads on the API are optimized using caching, reducing load on Database.
The writes efficiency will be decided by the underlying DB system. Even under peak load, it will not cause any end user impact since the clients will be posting data asynchronously. I was able to test the system for avg 13000 writes per second.
If the application needs to scale to handle 100K writes per second or more, then we can consider introducing a message queue or making DB optimizations. However, it will makes the application a little more complicated with all the data consistency checks. 









