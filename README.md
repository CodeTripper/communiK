# CommuniK - reactive api for sending emails/SMS/chat notifications

[![Build Status](https://travis-ci.org/CodeTripper/communiK.svg?branch=master)](https://travis-ci.org/CodeTripper/communiK)



It's a Store And Forward API built with Springboot/Webflux and MongoDb to send out notifications.

## Features: 
    * Fully reactive
    * Centralizes your notification requirements
    * Send emails/sms/chats by calling a rest endpoint
    * Add your own Email/SMS providers by implementing the Notifier Interface
    * Configure blackouts - WIP
    * Send bulk notifications 
    * Can store the notifications to a DB (mongodb/cassandra) before sending it
    * Register a notification template and pass dynamic data to it via Freemarker
    * Locale aware
    * Generate and send pdf attachments(via FlyingSaucer/Openpdf) 
    * Generate and send html attachments (via Freemarker) 
    * Download from a url and send attachments 
    * Retry the failed notifications via multiple fallback providers
    * Call webhooks on any failure/success 
    * Audit the notifications.
    * Easily extendable, so one can change any part of the system as required
    * OpenTracing (Jaegar) integrated
    * Prometheus metrics

## Why Communik?

I can easily send all my notifications via Sendgrid/Mailgun, why do I need Communik?
Well some reasons:

    * Take control of your templates. For example, if I need to move to sendgrid from mailgun, I don't need to transfer the templates
    * If I need to retry with a different provider, I dont need to worry about replicating the templates
    * One API for all your notification needs across your systems. 

## Getting Started

Run the springboot application Communik

### Optional

If you want to store the messages, a mongoDb cluster or cassandra is required

```

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

* [Springboot](https://github.com/spring-projects/spring-boot) - The microservices framework 
* [Reactor](https://github.com/reactor/reactor) - The reactive framework used
* [Gradle](https://github.com/gradle/gradle) - Dependency Management
* [MongoDb](https://rometools.github.io/rome/) - To store the notifications
* [Mapstruct](https://github.com/mapstruct/mapstruct) - To map the POJOs
* [Springfox](https://github.com/springfox/springfox/) - To documentation
* [FlyingSaucer](https://rometools.github.io/rome/) - To generate pdf attachments
* [Lombok](https://github.com/rzwitserloot/lombok) - To do some magic!
* [Freemarker](https://github.com/apache/freemarker) - Template Engine
* [OpenTracing](https://github.com/opentracing) - Distributed Tracing Specs
* [Jaegar](https://github.com/jaegertracing/jaeger) - Distributed Tracing Impl
* [Seige](https://github.com/JoeDog/siege) - To run benchmark
* [Wiremock](https://github.com/tomakehurst/wiremock) - To run mock servers
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
1. dummyEmailer running in 200ms delay
2. Log in INFO mode
3. Heap size -xmx2G -xms2G

Running
1. Use siege - details in seige/howto.txt
2. JConsole to view threads and Heap
3. Run with 20 concurrent requests and repeat 10 times


Results:

    1. Transactions - 200
    2. Elapsed Time - 17.2 secs
    3. (Avg) Response Time - 1.63 secs  (Lower the better)
    4. Transaction Rate - 11.61 tps (Higher the better)
    5. Threads - 31 (Should be always 31)
    6. Heap - 250 MB
