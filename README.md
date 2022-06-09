# Methods and technologies of parallel programming

Repository with laboratory works on the subject "Methods and technologies of parallel programming"
___

## [LAB-01] - Tools for creating and managing threads in parallel multithreaded programs

**Task:**
Implement sequential and parallel multithreaded processing of independent tasks (for example, parameter sweep -
instances of one task for different parameter values are solved). Implement examples of three types of tasks:

1. **CPU-bound** - complex calculations with a small amount of data;
2. **Memory-bound** - work with data stored in memory;
3. **IO-bound** - work with data on disk.
   
Measure the dependence of execution time on the number of threads.

#### Detailed results: [CPU-bound](./results/lab01/cpu-bound/results.md) | [Memory-bound](./results/lab01/memory-bound/results.md) | [IO-bound](./results/lab01/io-bound/results.md)

### Conclusion:
From the results of the research we can conclude that the performance result of parallel execution of independent tasks
can differ significantly depending on the type of these tasks. For example, tasks, that involve a large amount of computation
and a small amount of data, can be accelerated very well using parallel programming methods. On the other hand,
tasks involving the use of external shared resources (RAM or I/O devices) may not be accelerated by this approach
(they can even be slowed down by overhead). Thus, the method of parallelization of independent tasks is a powerful tool,
but its use always involves a detailed analysis of the tasks to be performed.
Confirmation of this can also be seen in the charts of the dependence of the acceleration and efficiency coefficients
for different types of tasks depending on the number of threads.

![Acceleration coefficient](results/lab01/charts/accelerationCoefficient.png)

![Efficiency coefficient](results/lab01/charts/efficiencyCoefficient.png)
___

## [LAB-02] - Means of interaction and synchronization of threads in parallel multithreaded programs

**Task:**
Implement following task using multi-threaded programming tools. Two matrices (not necessarily square) of a given (numerical) type are specified.
Multiply these matrices, then find the minimum element of the resulting matrix (according to a given comparison criterion),
return its value and indices in the matrix.

Measure the dependence of execution time on the number of threads and the amount of input data.

#### Detailed results: [link](./results/lab02/results.md)

### Conclusion:
In scope of this laboratory work serial and parallel strategies for multiplication and finding the minimal element were created.
Research has shown that program execution time is significantly reduced depending on the number of threads used. 
At the same time, no significant effect of the size of the input data on the acceleration factor was found 
(except for slightly better results on small volumes, which is most likely due to the possibility of efficient use of CPU cache).
Confirmation of this can also be seen in the charts of the dependence of the average acceleration coefficients
depending on the number of threads and matrix size.

![Acceleration coefficient by Threads QTY](results/lab02/charts/accelerationByThreads.png)

![Acceleration coefficient by Matrix size](results/lab02/charts/accelerationBySize.png)
___

## [LAB-03] - Training project on parallel multithreaded programming. Brownian motion modeling

**Task:**
The Brownian motion of particles (impurities) in a one-dimensional crystal consisting of N cells is simulated.
The movement of each of the K particles is simulated independently, on a separate thread.
The motion of particles is determined by the following rule: at each moment of time (at each iteration) the particle
moves either to the right (with probability p) or to the left (with probability 1-p). When the crystal limit is reached,
the particle is reflected from it (ie does not go beyond the crystal). Initially, all impurities are in the first (left) cell of the crystal.
Particle movements are monitored in the interface (graphical or console).
Simulation is performed in two modes: with a time limit and the number of iterations. In the first mode,
the program execution time and the delay between iterations are set; the program runs for a specified time.
In the second mode, each particle makes a given number of movements (iterations); the program ends when all particles have made all the movements.

#### Detailed results: [link](./results/lab03/results.md)

### Conclusion:
In scope of this laboratory work timed and limited Brownian motion simulation strategies were created.
The basis of the implementation is the representation of each elementary particle as a separate thread.
The crystal is presented in the form of a two-dimensional matrix of AtomicInteger elements,
which allowed secure access of different threads to the shared memory area.
In addition, phaser was used to properly synchronize the operation of threads during timed simulation.