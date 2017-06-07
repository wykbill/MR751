# Map Reduce Framework for single shared memory system

## Files
Code under package `mapreduce` is the framework.
`Mapper.java` defines the interface for the `map` operation.
I use the `pu.RedLib.Reduction` for the reduction operation.
`Pair.java` defines the key value pair class.
`Job.java` defines the `map reduce` job. It has a method `execute`
to run the job.

## Usage
To use the framework is very simple. Define the `map` operation by 
extending `Mapper` interface, the `reduce` operation by extending `Reduction`
interface, create the input, specify the maximum number of thread and create
the `Job` class, then invoke its method `execute` to run the job.

`WordCountDemo.java` is a demo that shows the usage of this framework by implementing
the classic word count problem.


