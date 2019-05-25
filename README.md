# CommuniK - reactive api for sending emails/SMS/chat notifications

It's a Store And Forward API built with Springboot/Reactor and MongoDb to send out notifications.

## Features: 
    
    1. Send emails/sms/chats by calling a rest endpoint
    2. Stores the notifications to a DB (mongodb) before sending it
    3. Register a notification template and pass dynamic data to it via Freemarker
    4. One can send email attachments (html/pdf) which can be build on the fly
    5. Retry the failed notifications via multiple fallback providers
    6. Audit the notifications.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Springboot](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Gradle](https://maven.apache.org/) - Dependency Management
* [MongoDb](https://rometools.github.io/rome/) - To store the notifications
* [MapStruct](https://rometools.github.io/rome/) - To map the POJOs
* [FlyingSaucer](https://rometools.github.io/rome/) - To generate pdf attachments
* [Lombok](https://rometools.github.io/rome/) -
* [Freemarker](https://rometools.github.io/rome/) -

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
3. Run with 200 concurrent requests and repeat 10 times
4.

Results:
    1. Transactions - 200
    2. Elapsed Time - 17.2 secs
    3. Avg Response Time - 1.63 secs  (Lower the better)
    4. Transaction Rate - 11.61 tps (Higher the better)
    5. Threads - 31 (Should be always 31)
    6. Heap - 250 MB
