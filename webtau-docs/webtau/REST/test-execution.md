# Serial execution

The default mode for running tests is serially; in other words, scenario files are executed one after the other.

# Parallel execution

Webtau supports executing tests in parallel.  In this mode, **scenario files** are executed in parallel.  **Individual 
scenarios** are still executed sequentially.

For large test suites, it is therefore advisable to create many small focused scenario files instead of few large files.

To enable parallel execution, specify the `numberOfThreads` configuration property either through the configuration file
or as a CLI parameter.  This property dictates the maximum number of threads on which to run tests.

Note: scenario file execution order is not guaranteed.
