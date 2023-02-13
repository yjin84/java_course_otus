## jvm arguments
```
-Xms256m
-Xmx256m
-verbose:gc
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
```

## Before code optimizing
| Heap size | spend time         | note |
|-----------|--------------------|------|
| 256m      | msec:27767, sec:27 ||
| 2048m     | msec:18182, sec:18 ||
| 4096m     | failed             | !    |
| 3072m     | msec:19004, sec:19 | !    |
| 2560m     | msec:18242, sec:18 ||
| 1024m     | msec:18885, sec:18 ||
| 512m      | msec:21223, sec:21 ||
| 768m      | msec:20148, sec:20 ||
| 896m      | msec:18994, sec:18 ||
| 832m      | msec:19252, sec:19 ||
| 864m      | msec:19606, sec:19 ||
| 880m      | msec:19442, sec:19 || 
| 894m      | msec:18979, sec:18 ||
| 890m      | msec:18944, sec:18 | <--  |
| 888m      | msec:19858, sec:19 ||

## After code optimizing
| Heap size | spend time       | note |
|-----------|------------------|------|
| 256m      | msec:4850, sec:4 ||
| 2048m     | msec:3910, sec:3 ||
| 4096m     | msec:3386, sec:3 ||
| 1024m     | msec:4038, sec:4 ||
| 1536m     | msec:4184, sec:4 ||
| 1792m     | msec:4003, sec:4 ||
| 1920m     | msec:4211, sec:4 ||
| 1984m     | msec:4168, sec:4 ||
| 2016m     | msec:3995, sec:3 ||
| 2000m     | msec:4258, sec:4 ||
| 2008m     | msec:3982, sec:3 | <--  |
| 2004m     | msec:4103, sec:4 ||
| 2006m     | msec:4151, sec:4 ||