public class Benchmark {
    public int benchmarkCount;
    private Runnable benchmarkFunc;

    public Benchmark(Runnable runnable, int benchmarkCount) {
        this.benchmarkCount = benchmarkCount;
        this.benchmarkFunc = runnable;

        // Warm up function
//        this.benchmarkFunc.run();
    }

    public long Start() {
        long totalTime = 0;
        long startTime;

        for (int i = 0; i < benchmarkCount; i++) {
            startTime = System.nanoTime();
            benchmarkFunc.run();
            totalTime += System.nanoTime() - startTime;
        }

        return totalTime / this.benchmarkCount;
    }
}
