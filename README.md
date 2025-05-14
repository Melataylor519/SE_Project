# SE_Project
Systems Diagram:

![Systems Diagram Image](https://github.com/melataylor519/SE_Project/blob/main/IMG_0020.png?raw=true)


Computation:

Largest Prime Factor - The system will find the largest prime factor of the input number, for example, an input of '13195' would have an output of '5, 7, 13, 29'.


Multi-threaded Implementation of NetworkAPI: 

The upper bound for the number of threads is set to 10. This ensures efficient parallel execution without overwhelming system resources.
The thread pool is managed using a fixed thread pool from ExecutorService, which means at most 10 requests can be processed concurrently.

Set up producer/consumer multithreading to parallelize I/O as well as CPU multithreading to handle Prime Factorization:

Sequential Flow:
Input: [13195, 12345, 98765]
Sequential Processing:
  - 13195 → largest prime factor = 29
  - 12345 → largest prime factor = 823  
  - 98765 → largest prime factor = 197
Output: max(29, 823, 197) = 823

Parallel Flow:
Input: [13195, 12345, 98765]
Parallel Processing (4 threads):
  - Thread 1: 13195 → 29
  - Thread 2: 12345 → 823
  - Thread 3: 98765 → 197
  - Thread 4: (idle)
Output: max(29, 823, 197) = 823

## Benchmark Report

Check out the performance optimization results:  
👉 [Benchmark Results](./benchmark.md)
