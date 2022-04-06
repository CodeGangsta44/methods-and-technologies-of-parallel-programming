As an example of a CPU-bound problem,
the [Leibniz convergent series](https://en.wikipedia.org/wiki/Leibniz_formula_for_%CF%80) method for calculating the Pi
number was implemented. During the testing 4-core 8-threads processor was used.

Result table ([raw data](results.csv)):

| Qty of threads / tasks | Serial duration (ns) | Serial duration      | Parallel duration (ns) | Parallel duration      | Acceleration coefficient | Efficiency coefficient |
| :--------------------: | :------------------: | :------------------: | :--------------------: | :--------------------: | :----------------------: | :-------------------:  |
| 1                      | 59518718             | 0s 59ms              | 58691598               | 0s 58ms                | 1,014                    | 1,014                  |
| 2                      | 108323462            | 0s 108ms             | 61301746               | 0s 61ms                | 1,767                    | 0,884                  |
| 4                      | 221271240            | 0s 221ms             | 75411094               | 0s 75ms                | 2,934                    | 0,734                  |
| 8                      | 441732055            | 0s 441ms             | 91226831               | 0s 91ms                | 4,842                    | 0,605                  |
| 16                     | 948931644            | 0s 948ms             | 200021289              | 0s 200ms               | 4,744                    | 0,297                  |
| 32                     | 1853231459           | 1s 853ms             | 400187088              | 0s 400ms               | 4,631                    | 0,145                  |
| 64                     | 3763016666           | 3s 763ms             | 1070433205             | 1s 70ms                | 3,515                    | 0,055                  |
| 128                    | 8156422043           | 8s 156ms             | 2562013121             | 2s 562ms               | 3,184                    | 0,025                  |

**Visualization of data:**

![Execution duration](charts/executionDuration.png)

![Coefficients](charts/coefficients.png)