# Notification project
1. Requires Lombok
        Add Lombok plugin to your IDE
2. Required to clean out directory in Idea if a mapstruct object is changed

    Template request
        {"name":"this is my test 2","type":"SMS","body":"Hi ${to}<br/>Your salary has been credited on ${timestamp}<br/>Go Niyo Team","owner":"HK"}


# Benchmarking

Application Config
1. gupchup running in 500ms delay
2. Log in info mode
3. Heap size -xmx2G -xms2G

Mongo Config

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

