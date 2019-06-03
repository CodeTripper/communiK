# CommuniK - reactive api for sending emails/SMS/chat notifications

It's a Store And Forward API built with Springboot/Reactor and MongoDb to send out notifications.

## Features: 
    
    * Send emails/sms/chats by calling a rest endpoint
    * Add your own Email/SMS providers by implementing the Notifier Interface
    * Configure blackouts - WIP
    * Send bulk notifications - WIP
    * Stores the notifications to a DB (mongodb/cassandra/Noop) before sending it
    * Register a notification template and pass dynamic data to it via Freemarker
    * One can send email attachments (html/pdf) which can be built on the fly
    * Retry the failed notifications via multiple fallback providers
    * Call webhooks on any failure/success 
    * Audit the notifications.
    * Easily extendable, so one can change any part of the system as required
    * OpenTracing (Jaegar) Integration

## Getting Started

Run the springboot application Communik

### Prerequisites

A mongoDb cluster

```
Give examples
```

### Running

Go to the root of the project directory build and test the projects 

```
$ ./gradlew 
```

To run the communik application

```
$ ./gradlew run
```


## Running the tests

```
$ ./gradlew test
```


## Deployment

TODO docker

## Built With

* [Springboot](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Gradle](https://maven.apache.org/) - Dependency Management
* [MongoDb](https://rometools.github.io/rome/) - To store the notifications
* [MapStruct](https://rometools.github.io/rome/) - To map the POJOs
* [FlyingSaucer](https://rometools.github.io/rome/) - To generate pdf attachments
* [Lombok](https://rometools.github.io/rome/) -
* [Freemarker](https://rometools.github.io/rome/) -
* [Seige](https://rometools.github.io/rome/) - To run benchmark
* [www.ascii-art-generator.org](https://www.ascii-art-generator.org/) - To create the banner



## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Code Tripper** - *Initial work* - [Code Tripper](https://github.com/CodeTripper)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc


## Benchmarking

Application Config
1. gupchup running in 500ms delay
2. Log in info mode
3. Heap size -xmx2G -xms2G

Running
1. Use siege - details in seige/howto.txt
2. JConsole to view threads and Heap
3. Run with 20 concurrent requests and repeat 10 times


Results:
    1. Transactions - 200
    2. Elapsed Time - 17.2 secs
    3. Avg Response Time - 1.63 secs  (Lower the better)
    4. Transaction Rate - 11.61 tps (Higher the better)
    5. Threads - 31 (Should be always 31)
    6. Heap - 250 MB
