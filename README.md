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

### Results

___

###### CPU-bound:

As an example of a CPU-bound problem,
the [Leibniz convergent series](https://en.wikipedia.org/wiki/Leibniz_formula_for_%CF%80) method for calculating the Pi
number was implemented. During the testing 4-core 8-threads processor was used.

Result table ([raw data](./results/lab01/cpu-bound/results.csv)):

| Qty of threads / tasks | Serial duration (ns) | Parallel duration (ns) | Acceleration coefficient | Efficiency coefficient |
| :--------------------: | :------------------: | :--------------------: | :----------------------: | :-------------------:  |
| 1                      | 73836696             | 54741797               | 1.348817541              | 1.348817541            |
| 2                      | 104923688            | 54139244               | 1.93803386               | 0.9690169298           |
| 4                      | 208360282            | 65962089               | 3.158788406              | 0.7896971016           |
| 8                      | 415474386            | 92813951               | 4.476421718              | 0.5595527148           |
| 16                     | 829011850            | 196441276              | 4.220151013              | 0.2637594383           |
| 32                     | 1659078038           | 390972170              | 4.243468373              | 0.1326083866           |
| 64                     | 3345193375           | 1135259744             | 2.946632603              | 0.04604113443          |
| 128                    | 6789882610           | 2271873787             | 2.988670695              | 0.0233489898           |

**Visualization of data:**

![Execution duration](results/lab01/cpu-bound/charts/executionDuration.png)

![Coefficients](results/lab01/cpu-bound/charts/coefficients.png)